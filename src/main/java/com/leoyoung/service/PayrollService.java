package com.leoyoung.service;

import com.leoyoung.model.Payroll;
import com.leoyoung.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {
    @Autowired
    private PayrollRepository payrollRepository;

    // 获取工资单信息
    public Payroll getPayroll(int employeeId) {
        return payrollRepository.getPayrollByEmployeeId(employeeId);
    }
}