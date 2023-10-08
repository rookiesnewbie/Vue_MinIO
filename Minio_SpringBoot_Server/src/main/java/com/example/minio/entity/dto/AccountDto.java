package com.example.minio.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author LiTeng
 * @Date 2023/10/5 13:33
 * Version 1.0
 * @Description
 */
@Data
public class AccountDto {
    @ApiModelProperty(value = "员工id")
    private Integer id;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "账户昵称")
    private String nickname;

    @ApiModelProperty(value = "token")
    private String token;
}
