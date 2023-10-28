package com.example.minio.entity.vo.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

/**
 * @Author LiTeng
 * @Date 2023/10/28 13:28
 * Version 1.0
 * @Description
 */
@Data
public class UpdateEmployeeReq {

    /**
     * 员工id
     */
    private Long id;

    /**
     * 员工姓名
     */
    @NotEmpty(message = "员工姓名不能为空")
    private String name;

    /**
     * 员工性别
     */
    private Integer sex;

    /**
     * 员工电话
     */
    private String phone;

    /**
     * 员工邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 员工生日
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date birthday;

    /**
     * 入职日期
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date entryDate;



    /**
     * 备注
     */
    private String remark;

}
