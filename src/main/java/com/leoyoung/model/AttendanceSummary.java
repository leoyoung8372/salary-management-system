package com.leoyoung.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AttendanceSummary {
    private Integer id; // 主键，自动递增的考勤总结唯一标识符
    private String employeeId; // 员工ID，外键，不能为空
    private Date date; // 考勤日期，不能为空
    private Integer totalWorkingHours; // 总工作时间，以分钟为单位
    private String status; // 考勤总结状态，非空，取值范围为预定义的状态
    private Date createdAt; // 创建时间，默认值为当前时间
    private Date updatedAt; // 更新时间，默认值为当前时间
}