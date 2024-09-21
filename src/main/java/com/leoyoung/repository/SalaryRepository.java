package com.leoyoung.repository;

import com.leoyoung.model.Salary;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SalaryRepository {
    @Select("SELECT * FROM salaries")
    List<Salary> getAllSalaries();

    @Insert("INSERT INTO salaries(employee_id, base_salary, performance_salary, allowance, bonus,overtime_pay,deduction) " +
            "VALUES(#{employeeId}, #{baseSalary}, #{performanceSalary}, #{allowance}, #{bonus}, #{overtimePay},#{deduction})")
    void insertSalary(Salary salary);

    //用于根据员工 ID 获取薪资详情
    @Select("SELECT base_salary , performance_salary, allowance, bonus, overtime_pay, deduction " +
            "FROM salaries WHERE employee_id = #{employeeId}")
    Salary getSalaryDetailsByEmployeeId(Long employeeId);
}