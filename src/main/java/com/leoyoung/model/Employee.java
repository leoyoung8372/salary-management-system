package com.leoyoung.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Employee {
    private Integer id;
    private String name;
    private String department;
    private String position;
    private String birthDate; // 使用 String 类型，方便 MyBatis 处理
    private String hireDate;  // 使用 String 类型，方便 MyBatis 处理
    private String phone;
    private String employeeId;
    private String status;
    private String idCard;
    private String gender;
    private String address;
}