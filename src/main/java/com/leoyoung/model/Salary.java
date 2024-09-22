package com.leoyoung.model;

import java.math.BigDecimal;
import java.util.Date;

public class Salary {
    private Integer id;
    private String employeeId; // 外键
    private BigDecimal baseSalary;  //表示任意精度的十进制数
    private BigDecimal performanceSalary;
    private BigDecimal allowance;
    private BigDecimal bonus;
    private BigDecimal overtimePay;
    private BigDecimal deduction;
    private Date salaryDate;
    private BigDecimal tax;

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }
    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }

    public BigDecimal getPerformanceSalary() {
        return performanceSalary;
    }
    public void setPerformanceSalary(BigDecimal performanceSalary) {
        this.performanceSalary = performanceSalary;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }
    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getBonus() {
        return bonus;
    }
    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public BigDecimal getOvertimePay() {
        return overtimePay;
    }
    public void setOvertimePay(BigDecimal overtimePay) {
        this.overtimePay = overtimePay;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }
    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public Date getSalaryDate() {
        return salaryDate;
    }
    public void setSalaryDate(Date salaryDate) {
        this.salaryDate = salaryDate;
    }

    public BigDecimal getTax() {
        return tax;
    }
    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }
}
