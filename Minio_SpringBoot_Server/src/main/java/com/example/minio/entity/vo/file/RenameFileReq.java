package com.example.minio.entity.vo.file;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author LiTeng
 * @Date 2023/10/5 22:35
 * Version 1.0
 * @Description
 */
@Data
public class RenameFileReq {
    @NotNull(message = "文件id不能为空")
    private Long id;

    @NotBlank(message = "文件名称不能为空")
    private String filename;
}
