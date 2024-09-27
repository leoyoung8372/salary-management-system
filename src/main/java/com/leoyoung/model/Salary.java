package com.leoyoung.model;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    private BigDecimal totalPayroll;
}