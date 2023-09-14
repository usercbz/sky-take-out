package com.sky.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.*;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;


import static com.sky.constant.MessageConstant.*;

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = getOne(Wrappers.lambdaQuery(Employee.class).eq(Employee::getUsername, username));
        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(PASSWORD_ERROR);
        }

        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    @Override
    public PageResult queryEmployeePage(EmployeePageQueryDTO employeePageQueryDTO) {

        log.info(employeePageQueryDTO.toString());
        String name = employeePageQueryDTO.getName();
        int page = employeePageQueryDTO.getPage();
        int pageSize = employeePageQueryDTO.getPageSize();

        LambdaQueryWrapper<Employee> wrapper = null;
        //判断是否传有员工姓名
        if (name != null) {
            wrapper = Wrappers.lambdaQuery(Employee.class).eq(Employee::getName, name);
        }

        Page<Employee> employeePage = page(new Page<>(page, pageSize), wrapper);

        return new PageResult(employeePage.getTotal(), employeePage.getRecords());
    }

    @Override
    public void saveEmployee(EmployeeDTO employeeDTO) {

        log.info(employeeDTO.toString());

        //构建员工数据
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);

        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        //保存到数据库
        if (!save(employee)) {
            //失败
            throw new SaveFailedException(ADD_FAIL);
        }

    }

    @Override
    public void updateEmployeeStatusById(Long id, Integer status) {

        Employee employee = new Employee();
        employee.setId(id);
        employee.setStatus(status);

        if (!updateById(employee)) {
            throw new EditFailedException(EDIT_FAIL);
        }
    }

    @Override
    public Employee queryEmployeeById(Long id) {
        return getById(id);
    }

    @Override
    public void editEmployeeInfo(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        //修改
        if (!updateById(employee)) {
            //失败
            throw new EditFailedException(EDIT_FAIL);
        }
    }

    @Override
    public void updatePassword(PasswordEditDTO passwordEditDTO) {
        Long empId = passwordEditDTO.getEmpId();
        if (empId == null) {
            empId = BaseContext.getCurrentId();
        }
        String oldPassword = DigestUtils.md5DigestAsHex(passwordEditDTO.getOldPassword().getBytes());
        //校验密码
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getId, empId).eq(Employee::getPassword, oldPassword);

        Employee employee = getOne(wrapper);

        if (employee == null) {
            //密码错误
            throw new PasswordErrorException(PASSWORD_ERROR);
        }

        //旧密码正确，修改密码
        String newPassword = DigestUtils.md5DigestAsHex(passwordEditDTO.getNewPassword().getBytes());
        employee.setPassword(newPassword);

        if (!updateById(employee)) {
            //修改失败
            throw new PasswordEditFailedException(PASSWORD_EDIT_FAILED);
        }

    }

}
