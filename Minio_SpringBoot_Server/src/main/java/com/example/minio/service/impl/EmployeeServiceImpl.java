package com.example.minio.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.minio.entity.Account;
import com.example.minio.entity.Employee;
import com.example.minio.entity.Enum.AccountStatusEnum;
import com.example.minio.entity.dto.EmployeeExportExcelDTO;
import com.example.minio.entity.dto.EmployeeImportExcelDTO;
import com.example.minio.entity.vo.ImportExcelEmployeesResp;
import com.example.minio.entity.vo.account.*;
import com.example.minio.excption.MyException;
import com.example.minio.mapper.EmployeeMapper;
import com.example.minio.service.AccountService;
import com.example.minio.service.EmployeeService;
import com.example.minio.util.JwtUtil;
import io.gitee.chemors.secure.ext.annotations.Desensitization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 员工信息表 服务实现类
 * </p>
 *
 * @author liteng
 * @since 2023-10-28
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    private AccountService accountService;


    //添加用户
    @Override
    @Transactional
    public void addEmployee(AddEmployeeReq req) {
        List<Employee> list = this.list();

        List<String> name = list.stream().map(e -> e.getName()).collect(Collectors.toList());
        List<String> email = list.stream().map(e -> e.getEmail()).collect(Collectors.toList());

        if (name.contains(req.getName())){
            throw new MyException(500,"该名称已经存在");
        }

        if (email.contains(req.getEmail())){
            throw new MyException(500,"该邮箱已经存在");
        }



        Employee employee = new Employee();
        BeanUtils.copyProperties(req,employee);
        employee.setStatus(AccountStatusEnum.ENABLE.getStatus());
        employee.setCreator(JwtUtil.getCurrentUser().getNickname());

        baseMapper.insert(employee);

        List<Account> accountList = accountService.list();
        List<String> nickName = accountList.stream().map(account -> account.getNickname()).collect(Collectors.toList());
        //创建帐号
        Account account = new Account();
        BeanUtils.copyProperties(req, account);

        if (nickName.contains(account.getNickname())){
            throw new MyException(500,"该昵称已经存在");
        }

        accountService.addAccount(account);


    }

    @Override
    public void updateEmployee(UpdateEmployeeReq req) {
        List<Employee> list = this.list();

        List<String> name = list.stream().filter(e-> !e.getName().equals(req.getName())).map(e -> e.getName()).collect(Collectors.toList());
        List<String> email = list.stream().filter(e -> !e.getEmail().equals(req.getEmail())).map(e -> e.getEmail()).collect(Collectors.toList());
        List<String> phone = list.stream().filter(e-> !e.getPhone().equals(req.getPhone())).map(e -> e.getPhone()).collect(Collectors.toList());

        log.info("过滤掉自己后的name：{}",name);
        log.info("过滤掉自己后的email：{}",email);
        log.info("过滤掉自己后的phone：{}",phone);

        if (name.contains(req.getName())){
            throw new MyException(500,"该名称已经存在");
        }

        if (email.contains(req.getEmail())){
            throw new MyException(500,"该邮箱已经存在");
        }

        if (phone.contains(req.getPhone())){
            throw new MyException(500,"该手机号已经存在");
        }



        Employee employee = new Employee();
        BeanUtils.copyProperties(req,employee);
        employee.setStatus(AccountStatusEnum.ENABLE.getStatus());
        employee.setCreator(JwtUtil.getCurrentUser().getNickname());

        baseMapper.updateById(employee);

    }

    //分页查询
    @Override
    @Desensitization
    public Map<String, Object> getEmployeeList(GetEmployeeListReq req) {
        Map<String, Object> map = new HashMap<>();
        IPage<Employee> employeePage = new Page<>(req.getPageNo(), req.getPageSize());
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(null != req.getName(),Employee::getName,req.getName());
        wrapper.like(null != req.getPhone(),Employee::getPhone,req.getPhone());

        IPage<Employee> employeeIPage = baseMapper.selectPage(employeePage, wrapper);

        map.put("list",employeeIPage.getRecords());
        map.put("total",employeeIPage.getTotal());

        return map;
    }

    @Override
    @Transactional
    public void deleteEmployee(Integer id) {
        Employee employee = baseMapper.selectById(id);
        if (null == employee){
            throw new MyException(500,"用户不存在");
        }
        String email = employee.getEmail();
        LambdaQueryWrapper<Account> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Account::getEmail,email);

        accountService.remove(wrapper);

        baseMapper.deleteById(id);


    }

    @Override
    public void updateEmpStatus(UpdateEmpStatusReq req) {
        Employee employee = baseMapper.selectById(req.getId());
        employee.setStatus(req.getStatus());
        baseMapper.updateById(employee);
    }

    @Override
    public List<Employee> getEnableEmpList() {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getStatus,AccountStatusEnum.ENABLE.getStatus());
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void exportUser(ExportEmployeesReq req, HttpServletResponse response) throws IOException {
        List<Employee> employeeList = baseMapper.selectList(new LambdaQueryWrapper<Employee>().like(null != req.getName(), Employee::getName, req.getName())
                .like(null != req.getPhone(), Employee::getPhone, req.getPhone()));

        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), EmployeeExportExcelDTO.class)
                // 不要自动关闭，交给 Servlet 自己处理
                .autoCloseStream(false)
                // 基于 column 长度，自动适配。最大 255 宽度
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("用户表").doWrite(employeeList);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户数据.xlsx", "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");

    }

    @Override
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 手动创建 demo
        List<EmployeeImportExcelDTO> importExcelEmployees = Collections.singletonList(
                EmployeeImportExcelDTO.builder()
                        .name("root")
                        .nickname("root")
                        .password("123456")
                        .sex(2)
                        .phone("18985800659")
                        .email("2049448867@qq.com")
                        .birthday(new Date())
                        .entryDate(new Date())
                        .remark("备注")
                        .status(0)
                        .build()
        );
        // 输出
        EasyExcel.write(response.getOutputStream(), EmployeeImportExcelDTO.class)
                // 不要自动关闭，交给 Servlet 自己处理
                .autoCloseStream(false)
                // 基于 column 长度，自动适配。最大 255 宽度
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("用户表").doWrite(importExcelEmployees);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户导入模板.xlsx", "UTF-8"));
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;" + "filename=" + URLEncoder.encode("用户导入模板.xlsx", "UTF-8").replace("+", "%20"));


    }

    @Override
    public ImportExcelEmployeesResp importEmployees(MultipartFile file, Boolean updateSupport) throws IOException {

        List<EmployeeImportExcelDTO> importExcelEmployees = EasyExcel.read(file.getInputStream(), EmployeeImportExcelDTO.class, null)
                // 不要自动关闭，交给 Servlet 自己处理
                .autoCloseStream(false)
                .doReadAllSync();

        if (CollUtil.isEmpty(importExcelEmployees)){
            throw new MyException(500,"导入员工数据不能为空！");
        }

        ImportExcelEmployeesResp build = ImportExcelEmployeesResp.builder()
                .createNicknames(new ArrayList<>())
                .updateNicknames(new ArrayList<>())
                .failureNicknames(new LinkedHashMap<>())
                .build();

        importExcelEmployees.forEach(importEmployees -> {
            try {
                checkCreateOrUpdate(null,null,importEmployees.getPhone(),importEmployees.getEmail());
            } catch (MyException e) {
                build.getFailureNicknames().put(importEmployees.getNickname(), e.getMsg());
                return;
            }

            //判断用户是否存在，在进行插入操作
            Employee employee = baseMapper.selectOne(new LambdaQueryWrapper<Employee>().eq(Employee::getName, importEmployees.getName()));
            if (employee == null){
                AddEmployeeReq addEmployeeReq = BeanUtil.toBean(importEmployees, AddEmployeeReq.class);
                addEmployee(addEmployeeReq);
                build.getUpdateNicknames().add(importEmployees.getNickname());
                return;
            }

            //如果存在判断是否允许更新
            if (!updateSupport){
                build.getFailureNicknames().put(importEmployees.getNickname(),"已经存在该手机号的用户");
                return;
            }

            Employee bean = BeanUtil.toBean(importEmployees, Employee.class);

            bean.setId(employee.getId());
            baseMapper.updateById(bean);
            build.getUpdateNicknames().add(importEmployees.getNickname());
        });

        return build;
    }



    private void checkCreateOrUpdate(Long id, String nickname, String phone, String email) {
        // 校验员工存在
        this.checkEmployeeExists(id);
        // 校验账户昵称唯一
        this.checkNicknameUnique(id, nickname);
        // 校验手机号唯一
        this.checkPhoneUnique(id, phone);
        // 校验邮箱唯一
        this.checkEmailUnique(id, email);
        // 校验部门处于开启状态 TODO
        // departmentService.validDepts(CollectionUtils.singleton(deptId));
        // 校验岗位处于开启状态 TODO
        // positionService.validPosts(postIds);
    }

    public void checkEmployeeExists(Long id) {
        if (id == null) {
            return;
        }
        Employee employee = baseMapper.selectById(id);
        if (null == employee){
            throw new MyException(500,"当前操作的用户不存在");
        }
    }

    public void checkNicknameUnique(Long id, String nickname) {
        if (StrUtil.isBlank(nickname)) {
            return;
        }
        Employee employee = baseMapper.selectOne(new LambdaQueryWrapper<Employee>().eq(Employee::getNickname, nickname));
        if (employee == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的员工
        if (id == null){
            throw new MyException(500,"已经存在该账户昵称的用户");
        }

        if (employee.getId().equals(id)){
            throw new MyException(500,"已经存在该账户昵称的用户");
        }
    }

    public void checkPhoneUnique(Long id, String phone) {
        if (StrUtil.isBlank(phone)) {
            return;
        }
        Employee employee = baseMapper.selectOne(new LambdaQueryWrapper<Employee>().eq(Employee::getPhone, phone));
        if (employee == null) {
            return;
        }
        if (id == null){
            throw new MyException(500,"已经存在该手机号的用户");
        }

        if (employee.getId().equals(id)){
            throw new MyException(500,"已经存在该手机号的用户");
        }
    }

    public void checkEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        Employee employee = baseMapper.selectOne(new LambdaQueryWrapper<Employee>().eq(Employee::getEmail, email));
        if (employee == null) {
            return;
        }
        if (id == null){
            throw new MyException(500,"已经存在该邮箱的用户");
        }

        if (employee.getId().equals(id)){
            throw new MyException(500,"已经存在该邮箱的用户");
        }
    }
}
