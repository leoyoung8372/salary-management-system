package com.leoyoung.controller;

import com.leoyoung.model.AttendanceRecord;
import com.leoyoung.service.AttRecService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/attendance")
public class AttRecController {

    private final AttRecService attRecService;

    @Autowired
    public AttRecController(AttRecService attRecService) {
        this.attRecService = attRecService;
    }

    //插入打卡记录
    @PostMapping("/checkin")
    public ResponseEntity<String> checkIn(@RequestBody AttendanceRecord attendanceRecord) {
        attRecService.insertCheckInRecord(
                attendanceRecord.getEmployeeId(),
                attendanceRecord.getCheckInTime(),
                attendanceRecord.getStatus(),
                attendanceRecord.getDate(),
                attendanceRecord.getSalaryCycle(),
                attendanceRecord.getNotes(),
                attendanceRecord.getName(),
                attendanceRecord.getDepartment(),
                attendanceRecord.getPosition()
        );
        System.out.print(attendanceRecord.getEmployeeId());
        System.out.println("打卡成功");
        return ResponseEntity.noContent().build();
    }

    //获取所有信息
    @GetMapping("/AllRecords")
    public List<AttendanceRecord> getAllRecords() {
        return attRecService.getAttendanceRecords();
    }

    //根据员工ID和签到时间查询是否存在时间段内的打卡记录
    @GetMapping("/can-checkin")
    public ResponseEntity<String> canCheckIn(@RequestParam String employeeId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date checkInTime) {
        // 检查是否可以打卡
        boolean canCheckIn = attRecService.canCheckIn(employeeId, checkInTime);
        if (canCheckIn) {
            return ResponseEntity.ok("可以打卡！");
        } else {
            return ResponseEntity.status(400).body("打卡失败：该时间段内已存在打卡记录。");
        }
    }

    //根据员工ID和日期查询考勤记录
    @GetMapping("/records")
    public ResponseEntity<List<AttendanceRecord>> getAttendanceRecords(
            @RequestParam String employeeId,
            @RequestParam Date date) {
        List<AttendanceRecord> records = attRecService.getAttRecByEmployeeIdAndDate(employeeId, date);
        return ResponseEntity.ok(records);
    }
}