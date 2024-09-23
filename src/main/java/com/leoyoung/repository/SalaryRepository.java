package com.leoyoung.repository;

import com.leoyoung.model.Salary;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SalaryRepository {
    @Select("SELECT * FROM salaries")
    List<Salary> getAllSalaries();

    //插入薪资记录
    @Insert("INSERT INTO salaries(employee_id, base_salary, performance_salary, allowance, bonus,overtime_pay,deduction,salary_date,tax,total_payroll) " +
            "VALUES(#{employeeId}, #{baseSalary}, #{performanceSalary}, #{allowance}, #{bonus}, #{overtimePay},#{deduction},#{salaryDate},#{tax},#{totalPayroll})")
    void insertSalary(Salary salary);

    //用于根据员工 ID 获取薪资详情
    @Select("SELECT base_salary , performance_salary, allowance, bonus, overtime_pay, deduction, salary_date " +
            "FROM salaries WHERE employee_id = #{employeeId}")
    Salary getSalaryDetailsByEmployeeId(String employeeId);

    @Select("SELECT COUNT(*) > 0 FROM salaries WHERE employee_id = #{employeeId} AND salary_date = #{salaryDate}")
    boolean existsByEmployeeIdAndSalaryDate(@Param("employeeId") String employeeId, @Param("salaryDate") String salaryDate);
}