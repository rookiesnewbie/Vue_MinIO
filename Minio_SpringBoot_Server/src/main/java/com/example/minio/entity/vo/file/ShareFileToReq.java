package com.example.minio.entity.vo.file;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author LiTeng
 * @Date 2023/10/6 13:26
 * Version 1.0
 * @Description
 */
@Data
public class ShareFileToReq {

    /**
     * 文件ID
     */
    @NotNull(message = "文件ID不能为空")
    private Long fileId;

    /**
     * 文件挂载目录
     */
    @NotNull(message = "文件挂载目录不能为空")
    private Long mountFolderId;

}
