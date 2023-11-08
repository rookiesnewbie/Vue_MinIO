package com.example.minio.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @Author LiTeng
 * @Date 2023/11/8 10:20
 * Version 1.0
 * @Description 密码加密工具类
 */
public class PasswordUtil {

    public static String encryptPassword(String password){
        return DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
    }

}
