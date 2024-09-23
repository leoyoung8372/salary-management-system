package com.leoyoung.controller;

import com.leoyoung.model.Salary;
import com.leoyoung.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/api/salaries")
public class SalaryController {

    // 注入薪资数据访问层
    @Autowired
    private SalaryService salaryService;

    // 获取所有薪资信息
    @GetMapping
    public List<Salary> getAllSalaries() {
        return salaryService.getAllSalaries();
    }

    // 创建新的薪资记录
    @PostMapping("/salaries")
    public void createSalary(@RequestBody Salary salary) {
        salaryService.createSalary(salary);
    }

    // 根据employeeId获取薪资详情
    @GetMapping("/{employeeId}")
    public Salary getSalaryDetailsByEmployeeId(@PathVariable String employeeId) {
        return salaryService.getSalaryDetailsByEmployeeId(employeeId);
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkSalaryExists(@RequestParam String employeeId, @RequestParam String salaryDate) {
        boolean exists = salaryService.checkSalaryExists(employeeId, salaryDate);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}