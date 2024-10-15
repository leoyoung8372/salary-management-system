package com.leoyoung.service;

import com.leoyoung.model.AttendanceRecord;
import com.leoyoung.repository.AttRecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class AttRecService {

    //自动注入
    @Autowired
    private AttRecRepository attRecRepository;

    //插入打卡记录
    public void insertCheckInRecord(String employeeId, Date checkInTime, String status, Date date,
                                    String salaryCycle,String notes,String name,String department,String position){
        attRecRepository.insertCheckInRecord(employeeId,checkInTime,status,date,salaryCycle,notes,name,department,position);
    }

    //获取所有记录
    public List<AttendanceRecord> getAttendanceRecords(){
        return attRecRepository.findAll();
    }

    //根据员工ID和签到时间查询是否存在时间段内的打卡记录
    public boolean canCheckIn(String employeeId, Date checkInTime) {
        // 定义日期格式化对象，用于将日期转换为字符串
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 将打卡时间转换为字符串格式
        String dateStr = dateFormat.format(checkInTime);
        // 获取日期部分（yyyy-MM-dd）
        String date = dateStr.split(" ")[0];
        String startTime = null; // 初始化开始时间
        String endTime = null;   // 初始化结束时间
        try {
            // 检查打卡时间是否在早上打卡时间范围内
            if (checkInTime.compareTo(dateFormat.parse(date + " 07:30:00")) >= 0 &&
                    checkInTime.compareTo(dateFormat.parse(date + " 08:10:00")) <= 0) {
                startTime = date + " 07:30:00"; // 设置开始时间
                endTime = date + " 08:10:00";   // 设置结束时间
                // 检查打卡时间是否在午餐前打卡时间范围内
            } else if (checkInTime.compareTo(dateFormat.parse(date + " 12:00:00")) >= 0 &&
                    checkInTime.compareTo(dateFormat.parse(date + " 12:30:00")) <= 0) {
                startTime = date + " 12:00:00"; // 设置开始时间
                endTime = date + " 12:30:00";   // 设置结束时间
                // 检查打卡时间是否在午餐后打卡时间范围内
            } else if (checkInTime.compareTo(dateFormat.parse(date + " 12:31:00")) >= 0 &&
                    checkInTime.compareTo(dateFormat.parse(date + " 13:10:00")) <= 0) {
                startTime = date + " 12:31:00"; // 设置开始时间
                endTime = date + " 13:10:00";   // 设置结束时间
                // 检查打卡时间是否在下午打卡时间范围内
            } else if (checkInTime.compareTo(dateFormat.parse(date + " 17:00:00")) >= 0 &&
                    checkInTime.compareTo(dateFormat.parse(date + " 17:30:00")) <= 0) {
                startTime = date + " 17:00:00"; // 设置开始时间
                endTime = date + " 17:30:00";   // 设置结束时间
                // 检查打卡时间是否在晚间打卡时间范围内
            } else if (checkInTime.compareTo(dateFormat.parse(date + " 17:31:00")) >= 0 &&
                    checkInTime.compareTo(dateFormat.parse(date + " 18:00:00")) <= 0) {
                startTime = date + " 17:31:00"; // 设置开始时间
                endTime = date + " 18:00:00";   // 设置结束时间
                // 检查打卡时间是否在夜间打卡时间范围内
            } else if (checkInTime.compareTo(dateFormat.parse(date + " 20:00:00")) >= 0 &&
                    checkInTime.compareTo(dateFormat.parse(date + " 20:30:00")) <= 0) {
                startTime = date + " 20:00:00"; // 设置开始时间
                endTime = date + " 20:30:00";   // 设置结束时间
            }
        } catch (ParseException e) {
            // 捕获解析异常并打印堆栈跟踪
            e.printStackTrace();
            return false; // 如果解析失败，返回false
        }
        // 如果没有设置开始时间或结束时间，返回false
        if (startTime == null || endTime == null) {
            return false;
        }
        // 查询在指定时间范围内的考勤记录
        List<AttendanceRecord> records = attRecRepository.findAttendanceRecordsInRange(employeeId, startTime, endTime);
        // 如果没有找到考勤记录，返回true，表示可以打卡
        return records.isEmpty();
    }


    // 根据员工ID和薪资归属日期计算考勤统计信息
    public AttendanceRecord calculateAttendanceSummary(String employeeId, String salaryDate) {
        int workDays = attRecRepository.countAfternoonSignOff(employeeId, salaryDate);
        int overtimeDays = attRecRepository.countOvertimeSignOff(employeeId, salaryDate);
        int lateCount = attRecRepository.countLateTimes(employeeId, salaryDate);

        AttendanceRecord summary = new AttendanceRecord();
        summary.setWorkDays(workDays);
        summary.setOvertimeDays(overtimeDays);
        summary.setLateCount(lateCount);

        return summary;
    }



    //根据员工ID和日期查询考勤记录
    public List<AttendanceRecord> getAttRecByEmployeeIdAndDate(String employeeId, Date date){
        return attRecRepository.getAttRecByEmployeeIdAndDate(employeeId,date);
    }

}
