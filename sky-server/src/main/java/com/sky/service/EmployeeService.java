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
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    PageResult queryEmployeePage(EmployeePageQueryDTO employeePageQueryDTO);

    void saveEmployee(EmployeeDTO employeeDTO);

    void updateEmployeeStatusById(Long id, Integer status);

    Employee queryEmployeeById(Long id);

    void editEmployeeInfo(EmployeeDTO employeeDTO);

    void updatePassword(PasswordEditDTO passwordEditDTO);
}
