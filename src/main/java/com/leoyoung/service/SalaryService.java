package com.leoyoung.service;

import com.leoyoung.model.Salary;
import com.leoyoung.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SalaryService {

    // 注入薪资数据访问层
    @Autowired
    private SalaryRepository salaryRepository;

    // 获取所有薪资信息
    public List<Salary> getAllSalaries() {
        return salaryRepository.getAllSalaries();
    }

    // 创建新的薪资记录
    public void createSalary(Salary salary) {
        salaryRepository.insertSalary(salary);
    }

    // 根据employeeId获取薪资详情
    public Salary getSalaryDetailsByEmployeeId(String employeeId) {
        return salaryRepository.getSalaryDetailsByEmployeeId(employeeId);
    }

    public boolean checkSalaryExists(String employeeId, String salaryDate) {
        // 解析日期字符串，获取年份和月份
        LocalDate date = LocalDate.parse(salaryDate);
        int year = date.getYear();
        int month = date.getMonthValue();
        return salaryRepository.existsByEmployeeIdAndYearAndMonth(employeeId, year, month);
    }
}