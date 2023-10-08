package com.example.minio.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author LiTeng
 * @Date 2023/10/5 22:26
 * Version 1.0
 * @Description
 */
@Data
public class CreateFolderReq {
    @NotBlank(message = "文件夹名称不能为空")
    private String filename;

    /**
     * 员工id
     */
    @NotNull
    private Long empId;

    /**
     * 当前文件目录id
     */
    @NotNull
    private Long parentId;

    /**
     * 创建共享文件夹
     */
    private Integer isShared;
}
