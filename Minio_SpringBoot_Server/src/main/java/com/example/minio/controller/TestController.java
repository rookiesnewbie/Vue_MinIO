package com.example.minio.controller;

import com.example.minio.annotation.Checking;
import com.example.minio.annotation.VerifyParameters;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author LiTeng
 * @Date 2023/10/15 18:37
 * Version 1.0
 * @Description  测试自定义参数校验注解
 */

@RestController
@RequestMapping
public class TestController {

    @Checking
    @RequestMapping("/index")
    public String index(@VerifyParameters(required = true) String index){
        return index;
    }

}
