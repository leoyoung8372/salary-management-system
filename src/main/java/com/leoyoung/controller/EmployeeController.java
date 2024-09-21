package com.leoyoung.controller;

// 导入所需的类
import com.leoyoung.model.Employee;
import com.leoyoung.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
}