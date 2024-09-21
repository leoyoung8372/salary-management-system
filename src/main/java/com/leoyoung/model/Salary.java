package com.leoyoung.model;

import java.math.BigDecimal;

public class Salary {
    private Integer id;
    private Integer employeeId; // 外键
    private BigDecimal baseSalary;
    private BigDecimal performanceSalary;
    private BigDecimal allowance;
    private BigDecimal bonus;
    private BigDecimal overtimePay;
    private BigDecimal deduction;

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Integer employeeId) {
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
}
