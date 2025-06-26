package com.plotech.itsrvsys.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plotech.itsrvsys.mapper.FileMetadataMapper;
import com.plotech.itsrvsys.pojo.entity.FileMetadata;
import com.plotech.itsrvsys.service.FileMetadataService;
import com.plotech.itsrvsys.util.MinIOUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author OF00047
 * @description 针对表【FileMatadata】的数据库操作Service实现
 * @createDate 2025-03-20 14:51:09
 */
@DS("mssql")
@Service
@AllArgsConstructor
public class FileMetadataServiceImpl extends ServiceImpl<FileMetadataMapper, FileMetadata>
        implements FileMetadataService {

    private final MinIOUtil minIOUtil;
    private final FileMetadataMapper fileMetadataMapper;

    @Override
    public FileMetadata saveOrUpdate(MultipartFile file, String uploader) {
        FileMetadata fileMetadata = minIOUtil.uploadFile(file, uploader);
        FileMetadata data = fileMetadataMapper.getById(fileMetadata.getId());
        if (data == null) {
            fileMetadataMapper.insert(fileMetadata);
        } else {
            fileMetadataMapper.updateLastModifyDateById(fileMetadata.getId());
        }
        return fileMetadata;
    }
}




