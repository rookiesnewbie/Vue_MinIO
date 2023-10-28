package com.example.minio.entity.vo.account;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author LiTeng
 * @Date 2023/10/28 13:28
 * Version 1.0
 * @Description
 */
@Data
public class UpdateEmpStatusReq{

    /**
     * 员工id
     */
    @NotNull
    private Long id;

    /**
     * 状态
     */
    @NotNull
    private Integer status;

}

