package com.plotech.kanban.mapper;

import com.plotech.kanban.pojo.entity.FileMetadata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author OF00047
* @description 针对表【FileMatadata】的数据库操作Mapper
* @createDate 2025-03-20 14:51:09
* @Entity com.plotech.kanban.pojo.entity.FileMetadata
*/
@Mapper
public interface FileMetadataMapper extends BaseMapper<FileMetadata> {
    FileMetadata getById(String id);
    void updateLastModifyDateById(String id);
}




