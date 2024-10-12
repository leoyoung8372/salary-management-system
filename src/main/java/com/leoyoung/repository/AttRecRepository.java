package com.leoyoung.repository;

import com.leoyoung.model.AttendanceRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface AttRecRepository {

    // 插入打卡记录（
    @Insert("INSERT INTO attendance_record (employee_id, check_in_time,date,status,salary_cycle,notes,name,department,position) " +
            "VALUES (#{employeeId}, #{checkInTime}, #{date}, #{status},#{salaryCycle},#{notes},#{name},#{department},#{position})")
    void insertCheckInRecord(@Param("employeeId") String employeeId,
                             @Param("checkInTime") Date checkInTime,
                             @Param("status") String status,
                             @Param("date") Date date,
                             @Param("salaryCycle") String salaryCycle,
                             @Param("notes") String notes,
                             @Param("name") String name,
                             @Param("department") String department,
                             @Param("position") String position
    );

    //获取所有记录
    @Select("SELECT * FROM attendance_record ")
    List<AttendanceRecord> findAll();

    //根据员工ID和签到时间查询
    @Select("SELECT COUNT(*) FROM attendance_record " +
            "WHERE employee_id = #{employeeId} " +
            "AND DATE(check_in_time) = CURDATE() " +
            "AND ( " +
            "    (check_in_time BETWEEN '07:30:00' AND '08:10:00') OR " +
            "    (check_in_time BETWEEN '12:00:00' AND '12:30:00') OR " +
            "    (check_in_time BETWEEN '12:31:00' AND '13:10:00') OR " +
            "    (check_in_time BETWEEN '17:00:00' AND '17:30:00') OR " +
            "    (check_in_time BETWEEN '17:31:00' AND '18:00:00') OR " +
            "    (check_in_time BETWEEN '20:00:00' AND '20:30:00') " +
            ")")
    int countAttendanceRecords(@Param("employeeId") String employeeId, @Param("checkInTime") String checkInTime);


    // 查询考勤记录(根据员工ID和日期）
    @Select("SELECT * FROM attendance_record WHERE employee_id = #{employeeId} AND date = #{date}")
    List<AttendanceRecord> getAttRecByEmployeeIdAndDate(
            @Param("employeeId") String employeeId,
            @Param("date") Date date);
}
