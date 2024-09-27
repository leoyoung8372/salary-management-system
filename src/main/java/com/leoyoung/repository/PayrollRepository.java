package com.leoyoung.repository;

import com.leoyoung.model.Payroll;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PayrollRepository {
    // 根据员工ID获取工资单信息
    @Select("SELECT e.name, e.department, e.position, s.base_salary, s.performance_salary, " +
            "s.allowance, s.bonus, s.deduction, s.overtime_pay, s.salary_date, s.tax, " +
            "(s.base_salary + s.performance_salary + s.allowance + s.bonus + s.overtime_pay - s.deduction - s.tax) AS total_payroll " +
            "FROM employees e JOIN salaries s ON e.employee_id = s.employee_id " +
            "WHERE e.employee_id = #{employeeId}")
    Payroll getPayrollByEmployeeId(int employeeId);


}