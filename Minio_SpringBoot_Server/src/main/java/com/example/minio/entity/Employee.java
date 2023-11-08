package com.example.minio.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.gitee.chemors.secure.ext.annotations.DesensitizationProp;
import io.gitee.chemors.secure.ext.enums.SensitiveTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 员工信息表
 * </p>
 *
 * @author liteng
 * @since 2023-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("employee")
@ApiModel(value="Employee对象", description="员工信息表")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "员工id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "员工姓名")
    @DesensitizationProp(value = SensitiveTypeEnum.CUSTOM,preLength = 1,sufLength = 2)
    private String name;

    @ApiModelProperty(value = "账户昵称")
    private String nickname;

    @ApiModelProperty(value = "员工性别 1女，2男")
    private Integer sex;

    @DesensitizationProp(value = SensitiveTypeEnum.MOBILE_PHONE)
    @ApiModelProperty(value = "员工电话")
    private String phone;

    @DesensitizationProp(value = SensitiveTypeEnum.EMAIL)
    @ApiModelProperty(value = "员工邮箱")
    private String email;

    @ApiModelProperty(value = "员工生日")
    private Date birthday;


    @ApiModelProperty(value = "入职日期")
    private Date entryDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "帐号状态 0正常，1停用")
    private Integer status;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updater;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "逻辑删除 0未删除，1已删除")
    @TableLogic
    private Boolean isDeleted;


}
