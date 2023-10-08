package com.example.minio.util;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.minio.entity.Account;
import com.example.minio.service.AccountService;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author LiTeng
 * @Date 2023/10/5 13:20
 * Version 1.0
 * @Description jwt工具类
 */
@Component
public class JwtUtil {

    private static AccountService accountStaticService;
    @Resource
    private  AccountService accountService;

    @PostConstruct
    public void setAccountService(){
        accountStaticService = accountService;
    }
    public static String getToken(String userId,String sign){
        return JWT.create().withAudience(userId)  // 将 user id 保存到 token 里面
                .withExpiresAt(DateUtil.offsetHour(new Date(),24)) //设置过期时间,24小时后过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
    }

    /**
     * 获取当前登录的用户信息
     *
     */
    public static Account getCurrentUser(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {

                String userId = JWT.decode(token).getAudience().get(0);
                Account account = accountStaticService.getById(Integer.valueOf(userId));
                return account;

            }
        }catch (Exception e){
            return null;
        }
        return null;
    }


}
