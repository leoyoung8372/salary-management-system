package com.leoyoung.service;

import com.leoyoung.model.Salary;
import com.leoyoung.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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

    // 根据 employeeId 获取薪资详情
    public Salary getSalaryDetailsByEmployeeId(String employeeId) {
        return salaryRepository.getSalaryDetailsByEmployeeId(employeeId);
    }

    // 用于判断同一员工是否存在同年同月的薪资记录
    public boolean checkSalaryExists(String employeeId, String salaryDate) {
        try {
            // 解析日期字符串，获取年份和月份
            LocalDate date = LocalDate.parse(salaryDate);
            int year = date.getYear();
            int month = date.getMonthValue();
            return salaryRepository.existsByEmployeeIdAndYearAndMonth(employeeId, year, month);
        } catch (DateTimeParseException e) {
            // 处理日期解析异常
            throw new IllegalArgumentException("Invalid date format: " + salaryDate);
        }
    }

    // 获取各项薪资合计
    public BigDecimal getTotalBaseSalary() {
        return salaryRepository.sumBaseSalary();
    }

    public BigDecimal getTotalPerformanceSalary() {
        return salaryRepository.sumPerformanceSalary();
    }

    public BigDecimal getTotalAllowance() {
        return salaryRepository.sumAllowance();
    }

    public BigDecimal getTotalBonus() {
        return salaryRepository.sumBonus();
    }

    public BigDecimal getTotalDeduction() {
        return salaryRepository.sumDeduction();
    }

    public BigDecimal getTotalOvertimePay() {
        return salaryRepository.sumOvertimePay();
    }

    public BigDecimal getTotalTax() {
        return salaryRepository.sumTax();
    }

    public BigDecimal getTotalPayroll() {
        return salaryRepository.sumTotalPayroll();
    }
}