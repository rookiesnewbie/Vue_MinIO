package com.example.minio.entity.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.example.minio.entity.Enum.SexConverter;
import com.example.minio.entity.Enum.StatusConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author LiTeng
 * @Date 2023/10/28 14:51
 * Version 1.0
 * @Description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeImportExcelDTO {

    /**
     * 员工姓名
     */
    @ExcelProperty(index = 0, value = "姓名")
    private String name;

    /**
     * 账户昵称
     */
    @ExcelProperty(index = 1, value = "账户昵称")
    private String nickname;

    /**
     * 账户密码
     */
    @ExcelProperty(index = 2, value = "账户密码")
    private String password;

    /**
     * 员工性别 1女，2男
     */
    @ExcelProperty(index = 3, value = "性别", converter = SexConverter.class)
    private Integer sex;

    /**
     * 员工电话
     */
    @ExcelProperty(index = 4, value = "电话")
    private String phone;

    /**
     * 员工邮箱
     */
    @ExcelProperty(index = 5, value = "邮箱")
    private String email;

    /**
     * 员工生日
     */
    @ExcelProperty(index = 6, value = "生日")
    @DateTimeFormat(value = "yyyy年MM月dd日")
    @ColumnWidth(value = 15)
    private Date birthday;


    /**
     * 入职日期
     */
    @ExcelProperty(index = 7, value = "创建日期")
    @DateTimeFormat(value = "yyyy年MM月dd日")
    @ColumnWidth(value = 15)
    private Date entryDate;

    /**
     * 备注
     */
    @ExcelProperty(index = 8, value = "备注")
    private String remark;

    /**
     * 员工状态
     */
    @ExcelProperty(index = 9, value = "状态", converter = StatusConverter.class)
    private Integer status;

}
