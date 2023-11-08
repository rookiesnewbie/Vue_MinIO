package com.example.minio.entity.dto;

import io.gitee.chemors.secure.ext.annotations.DesensitizationProp;
import io.gitee.chemors.secure.ext.enums.SensitiveTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @DesensitizationProp(SensitiveTypeEnum.EMAIL)
    private String email;

    @ApiModelProperty(value = "账户昵称")
    @DesensitizationProp(value = SensitiveTypeEnum.CUSTOM,preLength = 1,sufLength = 2)
    private String nickname;

    @ApiModelProperty(value = "token")
    private String token;

    @ApiModelProperty(value = "sex")
    private Integer sex;
}
