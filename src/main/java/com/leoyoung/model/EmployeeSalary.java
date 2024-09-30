package com.leoyoung.model;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
public class EmployeeSalary {
    private String  employeeId;
    private String name;
    private String department;
    private String position;
    private Date birthDate;
    private Date hireDate;
    private String phone;
    private String status;
    private String idCard;
    private String gender;
    private String address;

    private BigDecimal baseSalary;
    private BigDecimal performanceSalary;
    private BigDecimal allowance;
    private BigDecimal bonus;
    private BigDecimal deduction;
    private BigDecimal overtimePay;
    private Date salaryDate;
    private BigDecimal tax;
    private BigDecimal totalPayroll;
}
