package com.example.minio.config;

import com.example.minio.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author LiTeng
 * @Date 2023/10/8 13:19
 * Version 1.0
 * @Description 拦截器配置
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor handlerInterceptor(){
        return new JwtInterceptor();
    }

    @Resource
    private JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor())

                .addPathPatterns("/**") //拦截所有的请求
                .excludePathPatterns("/account/login","/account/logout");//排除登录、注册、退出接口放行
    }


}
