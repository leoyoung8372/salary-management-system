package com.leoyoung.controller;

import com.leoyoung.model.Salary;
import com.leoyoung.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {

    // 注入薪资数据访问层
    @Autowired
    private SalaryService salaryService;

    @GetMapping
    public List<Salary> getAllSalaries() {
        return salaryService.getAllSalaries();
    }

    @PostMapping
    public void createSalary(@RequestBody Salary salary) {
        salaryService.createSalary(salary);
    }

    // 根据employeeId获取薪资详情
    @GetMapping("/{employeeId}")
    public Salary getSalaryDetailsByEmployeeId(@PathVariable Long employeeId) {
        return salaryService.getSalaryDetailsByEmployeeId(employeeId);
    }


}