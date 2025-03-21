package com.plotech.kanban.controller;

import com.plotech.kanban.config.MinIOConfig;
import com.plotech.kanban.pojo.vo.R;
import com.plotech.kanban.service.FileMetadataService;
import com.plotech.kanban.util.MinIOUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/api/minio")
@AllArgsConstructor
public class MinIOController {

    private final MinIOUtil minIOUtil;
    private final MinIOConfig prop;
    private final FileMetadataService fileMetadataService;

    @GetMapping("/bucketExists")
    public R bucketExists(@RequestParam String bucketName) {
        return R.ok().put("bucketName", bucketName).put("exists", minIOUtil.bucketExists(bucketName));
    }

    @GetMapping("/makeBucket")
    public R makeBucket(@RequestParam String bucketName) {
        return R.ok().put("bucketName", bucketName).put("made", minIOUtil.makeBucket(bucketName));
    }

    @GetMapping("/removeBucket")
    public R removeBucket(@RequestParam String bucketName) {
        return R.ok().put("bucketName", bucketName).put("removed", minIOUtil.removeBucket(bucketName));
    }

    @GetMapping("/getAllBuckets")
    public R getAllBuckets() {
        return R.ok().put("buckets", minIOUtil.getAllBuckets());
    }

    @PostMapping("/upload")
    public R uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("uploader") String uploader) {
        return R.ok().put("url", fileMetadataService.saveOrUpdate(file, uploader).getMinIOUrl());
    }

    @GetMapping("/download")
    public R downloadFile(@RequestParam String fileName, HttpServletResponse res) {
        minIOUtil.downloadFile(fileName, res);
        return R.ok();
    }

    @PostMapping("/remove")
    public R removeFile(@RequestBody String url) {
        String fileName = url.substring(url.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
        return R.ok().put("fileName", fileName).put("removed", minIOUtil.removeFile(fileName));
    }

    @GetMapping("/preview")
    public R previewImg(@RequestParam String fileName) {
        return R.ok().put("fileName", fileName).put("url", minIOUtil.previewImg(fileName));
    }
}
