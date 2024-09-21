package com.leoyoung.service;
// 导入所需的类
import com.leoyoung.model.Employee;
import com.leoyoung.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

// 标记该类为服务层组件
@Service
public class EmployeeService {

    // 自动注入 EmployeeRepository 以便进行数据库操作
    @Autowired
    private EmployeeRepository employeeRepository;

    // 获取所有员工信息的方法
    public List<Employee> getAllEmployees() {
        // 调用 repository 方法获取所有员工
        return employeeRepository.getAllEmployees();
    }

    // 创建新员工的方法
    public void createEmployee(Employee employee) {
        // 调用 repository 方法插入新员工
        employeeRepository.insertEmployee(employee);
    }
}