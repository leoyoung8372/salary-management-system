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

    // 插入打卡记录
    @Insert("INSERT INTO attendance_record (employee_id, check_in_time, date,status) " +
            "VALUES (#{employeeId}, #{checkInTime}, #{date}, #{status})")
    void insertCheckInRecord(@Param("employeeId") String employeeId,
                             @Param("checkInTime") Date checkInTime,
                             @Param("status") String status,
                             @Param("date") Date date);

    // 查询考勤记录
    @Select("SELECT * FROM attendance_record WHERE employee_id = #{employeeId} AND date = #{date}")
    List<AttendanceRecord> getAttRecByEmployeeIdAndDate(
            @Param("employeeId") String employeeId,
            @Param("date") Date date);
}
