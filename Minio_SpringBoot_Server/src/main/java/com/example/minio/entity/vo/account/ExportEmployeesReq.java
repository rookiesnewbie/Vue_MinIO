package com.example.minio.entity.vo.account;

import com.example.minio.constant.ProjectConstant;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author LiTeng
 * @Date 2023/10/28 13:28
 * Version 1.0
 * @Description
 */

@Data
public class ExportEmployeesReq{
    /**
     * 员工姓名
     */
    private String name;

    /**
     * 员工手机号码
     */
    private String phone;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 入职日期 范围 - 开始
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY)
    private Date beginTime;

    /**
     * 入职日期 范围 - 结束
     */
    @DateTimeFormat(pattern = ProjectConstant.FORMAT_YEAR_MONTH_DAY)
    private Date endTime;


}
