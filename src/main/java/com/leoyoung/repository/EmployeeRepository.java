package com.leoyoung.repository;

// 导入所需的类
import com.leoyoung.model.Employee;
import org.apache.ibatis.annotations.*;
import java.util.List;

// 标记该接口为 MyBatis 的 Mapper 接口
@Mapper
public interface EmployeeRepository {

    // 查询所有员工信息的 SQL 语句
    @Select("SELECT * FROM employees")
    List<Employee> getAllEmployees();

    // 插入新员工信息的 SQL 语句
    @Insert("INSERT INTO employees(name, department, position, employee_id, birth_date, hire_date, phone, status, id_card, gender, address) " +
            "VALUES(#{name}, #{department}, #{position}, #{employeeId}, #{birthDate}, #{hireDate}, #{phone}, #{status}, #{idCard}, #{gender}, #{address})")
    void insertEmployee(Employee employee);

    @Select("SELECT COUNT(*) > 0 FROM employees WHERE employee_id = #{employeeId}")
    boolean existsByEmployeeId(@Param("employeeId") String employeeId); // 修改为 String 类型

}