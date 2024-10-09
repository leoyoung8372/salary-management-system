package com.leoyoung.controller;

import com.leoyoung.model.AttendanceRecord;
import com.leoyoung.service.AttRecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
                attendanceRecord.getDate()
        );
        return ResponseEntity.ok("打卡记录已成功插入");
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