package com.plotech.kanban.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName FileMetadata
 */
@TableName(value ="FileMetadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileMetadata implements Serializable {
    /**
     * 原始文件名的MD5Hex码
     */
    @TableId
    private String id;

    /**
     * 原始文件名
     */
    private String originalFileName;

    /**
     * MinIO访问路径
     */
    private String minIOUrl;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 上传者
     */
    private String uploader;

    /**
     * 上传时间
     */
    private LocalDateTime buildDate;

    /**
     * 最后修改时间
     */
    private LocalDateTime lastModifyDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}