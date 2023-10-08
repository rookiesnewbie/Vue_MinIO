package com.example.minio.interceptor;


import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.minio.entity.Account;
import com.example.minio.excption.MyException;
import com.example.minio.service.AccountService;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author LiTeng
 * @Date 2023/10/8 13:21
 * Version 1.0
 * @Description 拦截器
 */

@Component
public class JwtInterceptor implements HandlerInterceptor {


    @Resource
    private AccountService accountService;




    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader(
                "Access-Control-Allow-Headers", "Content-Type, Content-Length, token");
        // 明确允许通过的方法，不建议使用 *
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Expose-Headers", "*");

        //获取每个请求中header携带的token
        String token = request.getHeader("token");

        //如果不是映射到方法直接放行
        if (!(handler instanceof HandlerMethod)){
            return true;
        }

        //认证token
        if (StrUtil.isBlank(token)){
            throw new MyException(500,"请先登录");
        }

        //解析token获取用户id
        Integer accountId;

        try {
            accountId = Integer.valueOf(JWT.decode(token).getAudience().get(0));
        } catch (Exception e) {
            throw new MyException(501,"登录失败，请重新登录");
        }

        //根据用户id获取用户信息
        Account account = accountService.getById(accountId);
        if (null == account){
            throw new MyException(404,"用户不存在，请重新登录");
        }

        if (null != account.getPassword()){
            //通过id获取用户信息，再次生成token，与header中的 token比较是否一直
            JWTVerifier verification = JWT.require(Algorithm.HMAC256(account.getPassword())).build();

            //校验token
            try {
                verification.verify(token);
            } catch (JWTVerificationException e) {
                throw new MyException(403,"登录过期,请重新登录");
            }

        }
        return true;

    }
}
