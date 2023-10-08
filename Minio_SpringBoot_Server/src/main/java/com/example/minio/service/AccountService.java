package com.example.minio.service;

import com.example.minio.entity.dto.AccountDto;
import com.example.minio.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.minio.entity.vo.pageQuery;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
public interface AccountService extends IService<Account> {

    //登录
    AccountDto login(Account account);

    //带条件的分页查询
    Map<String, Object> getAccountInfoList(pageQuery pageQuery);

    Long getAccountTotal();
}
