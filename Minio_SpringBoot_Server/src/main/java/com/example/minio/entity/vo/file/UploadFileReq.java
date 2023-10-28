package com.example.minio.entity.vo.file;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author LiTeng
 * @Date 2023/10/6 12:32
 * Version 1.0
 * @Description
 */

@Data
public class UploadFileReq {
    /**
     * 员工id
     */
    @NotNull(message = "员工id不能为空")
    private Long empId;

    /**
     * 上传操作所在目录
     */
    @NotNull(message = "上传目录不能为空")
    private Long parentId;

    /**
     * 上传共享文件
     */
    private Integer isShared;
}
