package com.example.minio.entity.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author LiTeng
 * @Date 2023/10/6 13:41
 * Version 1.0
 * @Description
 */
@Data
public class PageParam implements Serializable {

    private static final Integer PAGE_NO = 1; //起始页
    private static final Integer PAGE_SIZE = 10;//页面大小

    /**
     * 页码，从 1 开始
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    private Integer pageNo = PAGE_NO;

    /**
     * 每页条数，最大值为 100
     */
    @NotNull(message = "每页条数不能为空")
    @Min(value = 1, message = "页码最小值为 1")
    @Max(value = 100, message = "页码最大值为 100")
    private Integer pageSize = PAGE_SIZE;

}
