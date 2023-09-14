package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtProperties jwtProperties;

    public EmployeeController(EmployeeService employeeService, JwtProperties jwtProperties) {
        this.employeeService = employeeService;
        this.jwtProperties = jwtProperties;
    }

    /**
     * 登录接口
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("登录功能")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 查询员工信息分页
     *
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("page")
    @ApiOperation("员工信息分页")
    public Result<PageResult> queryPage(EmployeePageQueryDTO employeePageQueryDTO) {
        PageResult employeePage = employeeService.queryEmployeePage(employeePageQueryDTO);
        return Result.success(employeePage);

    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增员工")
    public Result<Object> addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.saveEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 启用/禁用账号
     *
     * @param id
     * @param status
     * @return
     */
    @PostMapping("status/{status}")
    @ApiOperation("修改账号状态")
    public Result<String> editStatusById(Long id, @PathVariable Integer status) {
        employeeService.updateEmployeeStatusById(id, status);
        return Result.success();
    }


    @GetMapping("{id}")
    @ApiOperation("根据员工id查询")
    public Result<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.queryEmployeeById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     *
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改员工信息")
    public Result<String> editEmployeeInfo(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.editEmployeeInfo(employeeDTO);
        return Result.success();
    }

    /**
     * 修改账号密码
     *
     * @param passwordEditDTO
     * @return
     */
    @PutMapping("editPassword")
    @ApiOperation("修改账号密码")
    public Result<String> editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        employeeService.updatePassword(passwordEditDTO);
        return Result.success();
    }
}
