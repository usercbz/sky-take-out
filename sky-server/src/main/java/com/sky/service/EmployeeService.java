package com.sky.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService extends IService<Employee> {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO 登录数据，用户名、密码
     * @return 用户实体
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 查询员工分页信息
     *
     * @param employeePageQueryDTO 分页条件、配置
     * @return 分页信息
     */
    PageResult queryEmployeePage(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 新增员工
     *
     * @param employeeDTO 员工信息
     */
    void saveEmployee(EmployeeDTO employeeDTO);

    /**
     * 员工账号的启用、禁用
     *
     * @param id     员工id
     * @param status 状态 1-- 启用 0-- 禁用
     */
    void updateEmployeeStatusById(Long id, Integer status);

    /**
     * 根据id查询员工
     *
     * @param id 员工id
     * @return 员工实体
     */
    Employee queryEmployeeById(Long id);

    /**
     * 修改员工信息
     *
     * @param employeeDTO 员工修改后信息
     */
    void editEmployeeInfo(EmployeeDTO employeeDTO);

    /**
     * 修改密码
     *
     * @param passwordEditDTO 密码数据
     */
    void updatePassword(PasswordEditDTO passwordEditDTO);
}
