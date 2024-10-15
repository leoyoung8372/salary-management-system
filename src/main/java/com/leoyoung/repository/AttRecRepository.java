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

    //根据员工ID和签到时间查询是否存在时间段内的打卡记录
    @Select("SELECT * FROM attendance_record " +
            "WHERE employee_id = #{employeeId} " +
            "AND check_in_time BETWEEN #{startTime} AND #{endTime}")
    List<AttendanceRecord> findAttendanceRecordsInRange(
            @Param("employeeId") String  employeeId,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime);



    // 根据员工ID和薪资归属日期统计"下午签退"的次数
    @Select("SELECT COUNT(*) FROM attendance_record " +
            "WHERE employee_id = #{employeeId} " +
            "AND DATE_FORMAT(date, '%Y-%m') = #{salaryDate} " +
            "AND status = '下午签退'")
    int countAfternoonSignOff(
            @Param("employeeId") String employeeId,
            @Param("salaryDate") String salaryDate);
    // 根据员工ID和薪资归属日期统计"加班签退"的次数
    @Select("SELECT COUNT(*) FROM attendance_record " +
            "WHERE employee_id = #{employeeId} " +
            "AND DATE_FORMAT(date, '%Y-%m') = #{salaryDate} " +
            "AND status = '加班签退'")
    int countOvertimeSignOff(
            @Param("employeeId") String employeeId,
            @Param("salaryDate") String salaryDate);
    // 根据员工ID和薪资归属日期统计迟到的次数（包括"早上迟到"和"下午迟到"）
    @Select("SELECT COUNT(*) FROM attendance_record " +
            "WHERE employee_id = #{employeeId} " +
            "AND DATE_FORMAT(date, '%Y-%m') = #{salaryDate} " +
            "AND status LIKE '%迟到%'")
    int countLateTimes(
            @Param("employeeId") String employeeId,
            @Param("salaryDate") String salaryDate);



    // 查询考勤记录(根据员工ID和日期）
    @Select("SELECT * FROM attendance_record WHERE employee_id = #{employeeId} AND date = #{date}")
    List<AttendanceRecord> getAttRecByEmployeeIdAndDate(
            @Param("employeeId") String employeeId,
            @Param("date") Date date);
}
