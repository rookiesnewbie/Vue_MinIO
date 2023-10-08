package com.example.minio.entity.vo;

import lombok.Data;

/**
 * @Author LiTeng
 * @Date 2023/10/5 15:14
 * Version 1.0
 * @Description 分页查询条件
 */

@Data
public class pageQuery {

    //查询条
    private String keyWord;

    //当前页
    private Integer pageNum;

    //每页记录数
    private Integer pageSize;

}
