package com.leoyoung.controller;

// 导入所需的类
import com.leoyoung.model.Employee;
import com.leoyoung.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

// 标记该类为 REST 控制器
@RestController
// 定义 API 路径前缀
@RequestMapping("/api/employees")
public class EmployeeController {

    // 自动注入 EmployeeService 以便处理业务逻辑
    @Autowired
    private EmployeeService employeeService;

    // 处理 GET 请求以获取所有员工信息
    @GetMapping
    public List<Employee> getAllEmployees() {
        // 调用服务层方法获取所有员工
        return employeeService.getAllEmployees();
    }

    // 处理 POST 请求以创建新员工
    @PostMapping
    public void createEmployee(@RequestBody Employee employee) {
        // 调用服务层方法插入新员工
        employeeService.createEmployee(employee);
    }

    //检查员工ID是否存在
    @GetMapping("/{id}")
    public ResponseEntity<?> checkEmployeeExists(@PathVariable String id) { // 修改为 String 类型
        boolean exists = employeeService.existsById(id); // 确保服务层也能处理 String 类型
        return ResponseEntity.ok().body(Map.of("exists", exists)); // 返回结果
    }

    //  获取所有的 员工ID 和 名字
    @GetMapping("/employees")
    public List<Employee> getAllEmployeeIdsAndNames() {
        return employeeService.getAllEmployeeIdsAndNames();
    }

    // 更新员工信息
    @PutMapping("/{employeeId}")
    public void updateEmployee(@PathVariable String employeeId, @RequestBody Employee employee) {
        // 设置员工的 employeeId，以确保更新的是指定的员工
        employee.setEmployeeId(employeeId);
        employeeService.updateEmployee(employee);
    }
}