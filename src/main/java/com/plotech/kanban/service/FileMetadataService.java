package com.plotech.kanban.service;

import com.plotech.kanban.pojo.entity.FileMetadata;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;

/**
* @author OF00047
* @description 针对表【FileMatadata】的数据库操作Service
* @createDate 2025-03-20 14:51:09
*/
public interface FileMetadataService extends IService<FileMetadata> {
    FileMetadata saveOrUpdate(MultipartFile file, String uploader);
}
