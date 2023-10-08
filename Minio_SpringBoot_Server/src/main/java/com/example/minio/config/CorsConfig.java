package com.example.minio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
    //当前跨域请求最大的有效时长，这里默认是1天
    private static final long MAX_AGE = 24* 60 * 60;

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedHeader("*"); // 允许任何头
        config.addAllowedOrigin("*");   // 允许任何域名
        config.addAllowedMethod("*");  // 允许任何方法
        config.setMaxAge(MAX_AGE); // 预检请求的有效期，单位为秒。
        source.registerCorsConfiguration("/**", config);// 对接口配置跨域设置
        return new CorsFilter(source);
    }
}
