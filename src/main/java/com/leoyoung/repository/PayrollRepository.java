package com.leoyoung.repository;

import com.leoyoung.model.Payroll;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PayrollRepository {
    //获取PDF文件，依据员工ID和薪资日期
    @Select("SELECT e.name, e.department, e.position, s.base_salary, s.performance_salary, " +
            "s.allowance, s.bonus, s.deduction, s.overtime_pay, s.salary_date, s.tax, " +
            "(s.base_salary + s.performance_salary + s.allowance + s.bonus + s.overtime_pay - s.deduction - s.tax) AS total_payroll " +
            "FROM employees e JOIN salaries s ON e.employee_id = s.employee_id " +
            "WHERE e.employee_id = #{employeeId} AND s.salary_date = #{salaryDate}")
    Payroll getPayrollByEmployeeIdAndDate(@Param("employeeId") String employeeId, @Param("salaryDate") String salaryDate);
}