package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;

public interface EmployeeService extends IService<Employee> {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    Result<PageResult> queryEmployeePage(EmployeePageQueryDTO employeePageQueryDTO);

    Result<Object> saveEmployee(EmployeeDTO employeeDTO);

    Result<String> updateEmployeeStatusById(Long id, Integer status);

    Result<Employee> queryEmployeeById(Long id);

    Result<String> editEmployeeInfo(EmployeeDTO employeeDTO);

    Result<String> updatePassword(PasswordEditDTO passwordEditDTO);
}
