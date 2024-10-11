package com.leoyoung.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AttendanceRecord {
    private Integer id; // 主键，自动递增的考勤记录唯一标识符
    private String employeeId; // 员工ID，外键，不能为空
    private Date checkInTime; // 签到时间，不能为空
    private Date checkOutTime; // 签退时间，可以为空
    private String status; // 考勤状态，非空，取值范围为预定义的状态
    private Date date; // 考勤日期，不能为空
    private Date createdAt; // 创建时间，默认值为当前时间
    private Date updatedAt; // 更新时间，默认值为当前时间
    private String salaryCycle;//薪资周期
    private String notes;//备注
    private String name;
    private String department;
    private String position;
}