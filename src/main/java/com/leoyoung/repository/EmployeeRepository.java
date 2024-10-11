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

    //检查数据库中是否存在具有特定员工 ID 的记录，并返回一个布尔值表示结果。
    @Select("SELECT COUNT(*) > 0 FROM employees WHERE employee_id = #{employeeId}")
    boolean existsByEmployeeId(@Param("employeeId") String employeeId); // 修改为 String 类型

    // 获取所有员工id和名字
    @Select("SELECT employee_id, name FROM employees")
    List<Employee> getAllEmployeeIdsAndNames();

    // 根据员工 ID 更新员工信息的 SQL 语句
    @Update("UPDATE employees SET name=#{name}, department=#{department}, position=#{position}, birth_date=#{birthDate}, hire_date=#{hireDate}, phone=#{phone}, status=#{status}, id_card=#{idCard}, gender=#{gender}, address=#{address} WHERE employee_id=#{employeeId}")
    void updateEmployee(Employee employee);

    //根据员工ID获取该员工信息
    @Select("SELECT * FROM employees WHERE employee_id = #{employee_id}")
    List<Employee> getEmployeeByEmployeeId(@Param("employee_id") String employeeId);

    //根据员工ID删除员工信息
    @Delete("DELETE FROM employees WHERE employee_id = #{employeeId}")
    void deleteByEmployeeId(@Param("employeeId") String employeeId);
}