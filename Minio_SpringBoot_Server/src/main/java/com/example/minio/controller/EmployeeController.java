package com.example.minio.controller;


import com.example.minio.common.Result;
import com.example.minio.entity.Employee;
import com.example.minio.entity.vo.account.*;
import com.example.minio.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工信息表 前端控制器
 * </p>
 *
 * @author liteng
 * @since 2023-10-28
 */


@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    //添加用户
    @PostMapping
    public Result addEmployee(@RequestBody final AddEmployeeReq req){
        employeeService.addEmployee(req);
        return Result.success().message("添加成功");
    }

    //获取用户列表(分页显示)
    @GetMapping
    public Result getEmployeeList(@Valid final GetEmployeeListReq req){
        Map<String, Object> employeeMap = employeeService.getEmployeeList(req);
        return Result.success(employeeMap);
    }

    //修改用户
    @PutMapping
    public Result updateEmployee(@Valid @RequestBody final UpdateEmployeeReq req){
        employeeService.updateEmployee(req);
        return Result.success().message("修改成功");
    }

    //删除用户
    @DeleteMapping("/{id}")
    public Result deleteEmployeeById(@PathVariable("id") Integer id){
        employeeService.deleteEmployee(id);
        return Result.success().message("删除成功");
    }


    //获取启用用户列表  用于下拉框选择
    @GetMapping("listEnableEmp")
    public Result getEnableEmpList(){
        List<Employee> employeeList = employeeService.getEnableEmpList();
        return Result.success(employeeList);
    }

    //获取用户详情
    @GetMapping("/get")
    public Result getEmpDetail(@RequestParam final Long id){
        return Result.success(employeeService.getById(id));
    }

    //更改用户状态
    @PutMapping("updateStatus")
    public Result updateEmpStatus(@Valid @RequestBody final UpdateEmpStatusReq req){
        employeeService.updateEmpStatus(req);
        return Result.success().message("更改成功");
    }

    //导出用户
    @GetMapping("export")
    public Result exportUser(@Valid ExportEmployeesReq req, HttpServletResponse response) throws IOException {
        employeeService.exportUser(req,response);
        return Result.success().message("导出成功");
    }

    //下载导入用户数据的excel模板
    @GetMapping("getImportTemplate")
    public Result importTemplate(HttpServletResponse response) throws IOException {
        employeeService.importTemplate(response);
        return Result.success().message("下载成功");
    }
    @ApiOperation(value = "导入员工信息")
    @PostMapping("/import")
    public Result importExcelEmployees(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport)
            throws Exception {
        return Result.success(employeeService.importEmployees(file, updateSupport));
    }

}
