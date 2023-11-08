package com.example.minio.controller;


import com.example.minio.annotation.Checking;
import com.example.minio.annotation.VerifyParameters;
import com.example.minio.common.Result;
import com.example.minio.entity.Account;
import com.example.minio.entity.dto.AccountDto;
import com.example.minio.entity.dto.UpdateAccountPasswordDto;
import com.example.minio.entity.vo.file.pageQuery;
import com.example.minio.service.AccountService;
import com.example.minio.util.JwtUtil;
import com.example.minio.util.PasswordUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping("/login")
    @Checking
    public Result login(@VerifyParameters(required = true)@RequestBody  Account account){
        AccountDto accountDto = accountService.login(account);
        return Result.success(accountDto).message("登录成功");
    }

    @GetMapping("detail")
    public Result getAccountDetail(){
        Account currentUser = JwtUtil.getCurrentUser();
        return Result.success(currentUser);
    }


    @DeleteMapping("logout")
    public Result logout(){

        return Result.success().message("退出成功");
    }


    @PostMapping("/list")
    public Result getAccountInfoList(@RequestBody pageQuery pageQuery){

        Map<String, Object> accountInfoList = accountService.getAccountInfoList(pageQuery);

        return Result.success(accountInfoList);
    }


    @PostMapping("/add")
    public Result addAccount(@RequestBody  Account account){


        accountService.addAccount(account);

        return Result.success().message("添加成功");
    }

    @PutMapping("/detail")
    public Result updateAcount(@RequestBody Account account){

        String token = accountService.updateAccount(account);

        return Result.success(token).message("修改成功");
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteAcount(@PathVariable Integer id){

        accountService.removeById(id);

        return Result.success().message("删除成功");
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatchAcount(@VerifyParameters(required = true) List<Integer> ids){

        accountService.removeBatchByIds(ids);

        return Result.success().message("删除成功");
    }

    @GetMapping("/total")
    public Result getAccountTotal(){

        Long total = accountService.getAccountTotal();

        return Result.success(total);
    }

    @PostMapping("/password")
    public Result updatePassword(@RequestBody UpdateAccountPasswordDto accountPasswordDto){
        final Account account = accountService.getById(accountPasswordDto.getId());

        final Boolean verifyData = accountService.verifyPassword(account.getPassword(), PasswordUtil.encryptPassword(accountPasswordDto.getPassword()));
        return Result.success(verifyData);
    }

}
