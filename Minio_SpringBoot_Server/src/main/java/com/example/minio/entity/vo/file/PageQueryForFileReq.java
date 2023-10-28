package com.example.minio.entity.vo.file;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
/**
 * @Author LiTeng
 * @Date 2023/10/5 21:56
 * Version 1.0
 * @Description
 */
@EqualsAndHashCode(callSuper = true)

@Data
public class PageQueryForFileReq extends PageParam{
    /**
     * 父级id，即当前要查询的目录，为0即根目录
     */
    @NotNull(message = "当前目录不能为空")
    private Long parentId;

    /**
     * 文件名称 —— 搜索
     */
    private String name;

    /**
     * 查询共享文件
     */
    private Integer isShared;

    /**
     * 用户id —— 个人文件
     */
    private Long accountId;

    /**
     * 回收站
     */
    private Integer isDeleted;
}
