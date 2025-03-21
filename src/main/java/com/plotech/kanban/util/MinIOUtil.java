package com.plotech.kanban.util;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONObject;
import com.plotech.kanban.config.MinIOConfig;
import com.plotech.kanban.exception.CommonBaseErrorCode;
import com.plotech.kanban.exception.CommonBaseException;
import com.plotech.kanban.pojo.entity.FileMetadata;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class MinIOUtil {
    private final MinIOConfig prop;
    private final MinioClient minioClient;

    /**
     * 判断bucket是否存在
     *
     * @param bucketName bucket名称
     * @return true：存在；false：不存在
     */
    public Boolean bucketExists(String bucketName) {
        boolean exists = false;
        try {
            exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            log.info("判断MinIO中bucket【{}】是否存在时，返回结果为：{}", bucketName, exists);
        } catch (Exception e) {
            log.error("判断MinIO中bucket【{}】是否存在时出现错误，错误信息：{}", bucketName, e.getMessage());
            throw new CommonBaseException(CommonBaseErrorCode.BUCKET_NOT_EXIST);
        }
        return exists;
    }

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     * @return true：创建成功；false：创建失败
     */
    public Boolean makeBucket(String bucketName) {
        boolean flag = false;
        try {
            if (!bucketExists(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                String policyJson = "{\n" +
//                        "\t\"Version\": \"" + new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()) + "\",\n" +
                        "\t\"Version\": \"2012-10-17\",\n" +
                        "\t\"Statement\": [{\n" +
                        "\t\t\"Effect\": \"Allow\",\n" +
                        "\t\t\"Principal\": {\n" +
                        "\t\t\t\"AWS\": [\"*\"]\n" +
                        "\t\t},\n" +
                        "\t\t\"Action\": [\"s3:GetBucketLocation\", \"s3:ListBucket\", \"s3:ListBucketMultipartUploads\"],\n" +
                        "\t\t\"Resource\": [\"arn:aws:s3:::" + bucketName + "\"]\n" +
                        "\t}, {\n" +
                        "\t\t\"Effect\": \"Allow\",\n" +
                        "\t\t\"Principal\": {\n" +
                        "\t\t\t\"AWS\": [\"*\"]\n" +
                        "\t\t},\n" +
                        "\t\t\"Action\": [\"s3:AbortMultipartUpload\", \"s3:DeleteObject\", \"s3:GetObject\", \"s3:ListMultipartUploadParts\", \"s3:PutObject\"],\n" +
                        "\t\t\"Resource\": [\"arn:aws:s3:::" + bucketName + "/*\"]\n" +
                        "\t}]\n" +
                        "}\n";
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policyJson).build());
                log.info("buckets:【{}】，创建【readwrite】策略成功！", bucketName);
                flag = true;
            } else {
                log.info("buckets:【{}】已存在！", bucketName);
            }
            log.info("创建MinIO中bucket【{}】成功", bucketName);
        } catch (Exception e) {
            log.error("创建MinIO中bucket【{}】时出现错误，错误信息：{}", bucketName, e.getMessage());
            throw new CommonBaseException(CommonBaseErrorCode.BUCKET_CREATE_FAIL);
        }
        return flag;
    }

    /**
     * 删除bucket
     *
     * @param bucketName bucket名称
     * @return true：删除成功；false：删除失败
     */
    public Boolean removeBucket(String bucketName) {
        boolean flag = false;
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            flag = true;
            log.info("删除MinIO中bucket【{}】成功", bucketName);
        } catch (Exception e) {
            log.error("删除MinIO中bucket【{}】时出现错误，错误信息：{}", bucketName, e.getMessage());
            throw new CommonBaseException(CommonBaseErrorCode.BUCKET_DELETE_FAIL);
        }
        return flag;
    }

    /**
     * 获取全部bucket
     *
     * @return bucket列表
     */
    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error("获取MinIO中bucket列表时出现错误，错误信息：{}", e.getMessage());
            throw new CommonBaseException(CommonBaseErrorCode.BUCKET_NOT_EXIST);
        }
    }

    /**
     * 上传文件
     *
     * @param file MultipartFile对象，要上传的文件
     * @return FileMetadata对象
     */
    public FileMetadata uploadFile(MultipartFile file, String uploader) {
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setUploader(uploader);
        String originalFileName = file.getOriginalFilename();
        fileMetadata.setOriginalFileName(originalFileName);
        if (!StringUtils.hasText(originalFileName)) {
            throw new CommonBaseException(CommonBaseErrorCode.FILE_IS_BLANK);
        }
        int fileTypeIndex = originalFileName.lastIndexOf(".");
        String fileType = originalFileName.substring(fileTypeIndex);
        fileMetadata.setFileType(fileType);
        String fileName = originalFileName.substring(fileTypeIndex - 1);
        String md5FileName = DigestUtils.md5DigestAsHex(fileName.getBytes());
        fileMetadata.setId(md5FileName);
        LocalDateTime now = LocalDateTime.now();
        fileMetadata.setBuildDate(now);
        fileMetadata.setLastModifyDate(now);
        String objectName = now.format(DateTimeFormatter.ofPattern("yyyy-MM/dd")) + "/" + md5FileName + fileType;
        fileMetadata.setMinIOUrl(prop.getEndpoint() + "/" + prop.getBucketName() + "/" + objectName);
        try {
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(prop.getBucketName())
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build();
            minioClient.putObject(objectArgs); // 文件名称相同时，会覆盖
            log.info("上传文件【{}】到MinIO成功，在MinIO中的名称可能是【{}】", originalFileName, objectName);
        } catch (Exception e) {
            log.error("上传文件【{}】到MinIO时出现错误，错误信息：{}", originalFileName, e.getMessage());
            throw new CommonBaseException(CommonBaseErrorCode.FILE_UPLOAD_FAIL);
        }
        return fileMetadata;
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @param res      HttpServletResponse对象
     */
    public void downloadFile(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(prop.getBucketName())
                .object(fileName)
                .build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            byte[] buffer = new byte[1024];
            int len;
            try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
                while ((len = response.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
                os.flush();
                byte[] bytes = os.toByteArray();
                res.setCharacterEncoding("UTF-8");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                try (ServletOutputStream stream = res.getOutputStream()) {
                    stream.write(bytes);
                    stream.flush();
                }
            }
        } catch (Exception e) {
            log.error("下载文件【{}】时出现错误，错误信息：{}", fileName, e.getMessage());
        }
    }

    /**
     * 删除文件
     *
     * @param url 文件名
     * @return true：删除成功；false：删除失败
     */
    public Boolean removeFile(String url) {
        JSONObject jsonObject = JSONObject.parseObject(url);
        url = (String) jsonObject.get("url");
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(prop.getBucketName())
                    .object(url)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("删除文件【{}】时出现错误，错误信息：{}", url, e.getMessage());
            return false;
        }
    }

    /**
     * 预览图片
     *
     * @param fileName 文件名
     * @return 文件地址
     */
    public String previewImg(String fileName) {
        // 文件地址
        GetPresignedObjectUrlArgs build = new GetPresignedObjectUrlArgs.Builder()
                .bucket(prop.getBucketName())
                .object(fileName)
                .method(Method.GET)
                .build();
        try {
            return minioClient.getPresignedObjectUrl(build);
        } catch (Exception e) {
            log.error("预览图片【{}】时出现错误，错误信息：{}", fileName, e.getMessage());
            return null;
        }
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    public List<Item> listObjects() {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(prop.getBucketName()).build());
        List<Item> items = new ArrayList<>();
        try {
            for (Result<Item> result : results) {
                items.add(result.get());
            }
        } catch (Exception e) {
            log.error("获取存储在bucket内的文件对象信息出错，错误信息：{}", e.getMessage());
            return null;
        }
        return items;
    }
}
