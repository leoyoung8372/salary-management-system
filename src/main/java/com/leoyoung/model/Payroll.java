package com.leoyoung.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Payroll {
    private String name;                // 员工姓名
    private String department;          // 部门
    private String position;            // 职位
    private double baseSalary;          // 基本工资
    private double performanceSalary;   // 绩效工资
    private double allowance;           // 津贴
    private double bonus;               // 奖金
    private double deduction;           // 扣款
    private double overtimePay;         // 加班费
    private String salaryDate;          // 薪资日期
    private double tax;                 // 税金
    private double totalPayroll;        // 总薪资

    @Override
    public String toString() {
        return "Payroll{" +
                "name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", baseSalary=" + baseSalary +
                ", performanceSalary=" + performanceSalary +
                ", allowance=" + allowance +
                ", bonus=" + bonus +
                ", deduction=" + deduction +
                ", overtimePay=" + overtimePay +
                ", salaryDate='" + salaryDate + '\'' +
                ", tax=" + tax +
                ", totalPayroll=" + totalPayroll +
                '}';
    }
}