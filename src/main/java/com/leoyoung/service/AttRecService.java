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
    public void insertCheckInRecord(String employeeId, Date checkInTime, String status, Date date){
        attRecRepository.insertCheckInRecord(employeeId,checkInTime,status,date);
    }

    //根据员工ID和日期查询考勤记录
    public List<AttendanceRecord> getAttRecByEmployeeIdAndDate(String employeeId, Date date){
        return attRecRepository.getAttRecByEmployeeIdAndDate(employeeId,date);
    }

}
