package com.example.minio.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.minio.entity.Employee;
import com.example.minio.entity.dto.AccountDto;
import com.example.minio.entity.Account;
import com.example.minio.entity.vo.file.pageQuery;
import com.example.minio.excption.MyException;
import com.example.minio.mapper.AccountMapper;
import com.example.minio.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.minio.service.EmployeeService;
import com.example.minio.util.JwtUtil;
import com.example.minio.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author liteng
 * @since 2023-10-05
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    private EmployeeService employeeService;
    @Override
    public AccountDto login(Account account) {
        String email = account.getEmail();
        String password = account.getPassword();
        if (email == null || password == null){
            throw new MyException(500,"邮箱或密码不能为空");
        }
        String newPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getEmail,email);
        Employee employee = employeeService.getOne(wrapper);
        account.setPassword(newPassword);
        Account accountInfo = getAccount(account);
        if (null == accountInfo){
            throw new MyException(500,"该账户不存在");
        }
        if (accountInfo.getEmail().equals(email) && accountInfo.getPassword().equals(newPassword)){
            AccountDto accountDto = new AccountDto();
            BeanUtils.copyProperties(accountInfo,accountDto);
            //设置token,以用户密码作为签名
            String token = JwtUtil.getToken(accountInfo.getId().toString(), accountInfo.getPassword());
            accountDto.setToken(token);
            accountDto.setSex(employee.getSex());
            return accountDto;
        }else {
            throw new MyException(500,"邮箱或密码不正确");
        }

    }

    @Override
    public  Map<String, Object> getAccountInfoList(pageQuery pageQuery) {
        Map<String, Object> map = new HashMap<>();
        IPage<Account> accountPage = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());

        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(!pageQuery.getKeyWord().isEmpty(),Account::getNickname,pageQuery.getKeyWord());

        IPage<Account> accountIPage = baseMapper.selectPage(accountPage, wrapper);

        List<Account> accountList = accountIPage.getRecords();
        long total = accountPage.getTotal();

        map.put("total",total);
        map.put("accountList",accountList);

        return map;
    }

    @Override
    public Long getAccountTotal() {
        return baseMapper.selectCount(new QueryWrapper<>());
    }

    @Override
    public void addAccount(Account account) {
        String password = account.getPassword();
        String newPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        account.setPassword(newPassword);
        baseMapper.insert(account);
    }

    @Override
    public Boolean verifyPassword(String password, String s) {
        return password.equals(s);
    }

    /**
     * 修改账户信息
     * @param account
     * @return
     */
    @Override
    public String updateAccount(Account account) {
        // 如果修改了密码
        if(account.getPassword() != null && account.getPassword().length() >= 6){
            account.setPassword(PasswordUtil.encryptPassword(account.getPassword().trim()));
            baseMapper.updateById(account);
            return JwtUtil.getToken(account.getId().toString(),account.getPassword());
        }
        return null;
    }

    private Account getAccount(Account account){
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", account.getEmail());

        queryWrapper.eq("password",account.getPassword());
        Account accountInfo;
        try{
            accountInfo = baseMapper.selectOne(queryWrapper);   //只查询一个用户，如果数据库中存在多个相同的用户就会报错，此时就要抛出异
        }catch (Exception e){
            throw new MyException(500,"抱歉，系统出错了！！！");
        }
        return accountInfo;
    }
}
