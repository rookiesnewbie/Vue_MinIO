package com.example.minio.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.minio.entity.Employee;
import com.example.minio.entity.vo.ImportExcelEmployeesResp;
import com.example.minio.entity.vo.account.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 员工信息表 服务类
 * </p>
 *
 * @author liteng
 * @since 2023-10-28
 */
public interface EmployeeService extends IService<Employee> {

    void addEmployee(AddEmployeeReq req);

    Map<String, Object> getEmployeeList(GetEmployeeListReq req);

    void deleteEmployee(Integer id);


    void updateEmpStatus(UpdateEmpStatusReq req);

    List<Employee> getEnableEmpList();

    void exportUser(ExportEmployeesReq req, HttpServletResponse response) throws IOException;

    void importTemplate(HttpServletResponse response) throws IOException;

    ImportExcelEmployeesResp importEmployees(MultipartFile file, Boolean updateSupport) throws IOException;

    void updateEmployee(UpdateEmployeeReq req);
}
