package com.leoyoung.repository;

import com.leoyoung.model.Salary;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SalaryRepository {

    // 获取所有的薪资信息
    @Select("SELECT * FROM salaries")
    List<Salary> getAllSalaries();

    // 插入薪资记录
    @Insert("INSERT INTO salaries(employee_id, base_salary, performance_salary, allowance, bonus, overtime_pay, deduction, salary_date, tax, total_payroll) " +
            "VALUES(#{employeeId}, #{baseSalary}, #{performanceSalary}, #{allowance}, #{bonus}, #{overtimePay}, #{deduction}, #{salaryDate}, #{tax}, #{totalPayroll})")
    void insertSalary(Salary salary);

    // 根据员工 ID 获取薪资详情
    @Select("SELECT base_salary, performance_salary, allowance, bonus, overtime_pay, deduction, salary_date " +
            "FROM salaries WHERE employee_id = #{employeeId}")
    Salary getSalaryDetailsByEmployeeId(String employeeId);

    // 判断同一员工是否存在同年同月的薪资记录
    @Select("SELECT COUNT(*) > 0 FROM salaries WHERE employee_id = #{employeeId} AND YEAR(salary_date) = #{year} AND MONTH(salary_date) = #{month}")
    boolean existsByEmployeeIdAndYearAndMonth(@Param("employeeId") String employeeId, @Param("year") int year, @Param("month") int month);

    // 获取各项薪资合计
    @Select("SELECT SUM(base_salary) FROM salaries")
    BigDecimal sumBaseSalary();

    @Select("SELECT SUM(performance_salary) FROM salaries")
    BigDecimal sumPerformanceSalary();

    @Select("SELECT SUM(allowance) FROM salaries")
    BigDecimal sumAllowance();

    @Select("SELECT SUM(bonus) FROM salaries")
    BigDecimal sumBonus();

    @Select("SELECT SUM(deduction) FROM salaries")
    BigDecimal sumDeduction();

    @Select("SELECT SUM(overtime_pay) FROM salaries")
    BigDecimal sumOvertimePay();

    @Select("SELECT SUM(tax) FROM salaries")
    BigDecimal sumTax();

    @Select("SELECT SUM(total_payroll) FROM salaries")
    BigDecimal sumTotalPayroll();
}