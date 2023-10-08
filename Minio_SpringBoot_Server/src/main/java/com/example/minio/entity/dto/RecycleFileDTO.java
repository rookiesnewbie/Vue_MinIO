package com.example.minio.entity.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author LiTeng
 * @Date 2023/10/6 13:49
 * Version 1.0
 * @Description
 */
@Data
public class RecycleFileDTO {
    /**
     * 回收站文件id
     */
    private Long id;

    /**
     * 删除时间
     */
    private Date deletedTime;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 文件大小
     */
    private String size;

}
