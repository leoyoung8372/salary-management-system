package com.leoyoung.service;

import com.leoyoung.model.AttendanceRecord;
import com.leoyoung.repository.AttRecRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //根据员工ID和签到时间查询
    public boolean hasCheckedIn(String employeeId, String checkInTime) {
        int count = attRecRepository.countAttendanceRecords(employeeId, checkInTime);
        return count > 0; // 如果记录数量大于0，表示已打卡
    }

    //根据员工ID和日期查询考勤记录
    public List<AttendanceRecord> getAttRecByEmployeeIdAndDate(String employeeId, Date date){
        return attRecRepository.getAttRecByEmployeeIdAndDate(employeeId,date);
    }

}
