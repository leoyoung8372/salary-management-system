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
        return employeeRepository.getAllEmployees(); // 调用 repository 方法获取所有员工
    }

    // 创建新员工的方法
    public void createEmployee(Employee employee) {
        // 调用 repository 方法插入新员工
        employeeRepository.insertEmployee(employee);
    }

    //检查数据库中是否存在具有特定员工 ID 的记录
    public boolean existsById(String employeeId) {
        return employeeRepository.existsByEmployeeId(employeeId); // 确保 repository 方法也能处理 String 类型
    }

    // 获取所有员工的工号和名字
    public List<Employee> getAllEmployeeIdsAndNames() {
        return employeeRepository.getAllEmployeeIdsAndNames();
    }

    //更新员工数据
    public void updateEmployee(Employee employee) {
        employeeRepository.updateEmployee(employee);
    }

    //根据员工ID获取该员工信息
    public List<Employee> getEmployeeByEmployeeId(String employeeId) {
        return employeeRepository.getEmployeeByEmployeeId(employeeId);
    }

    //删除员工
    public void deleteByEmployeeId(String employeeId) {
        employeeRepository.deleteByEmployeeId(employeeId);
    }

}