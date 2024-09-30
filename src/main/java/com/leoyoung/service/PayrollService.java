package com.leoyoung.service;

import com.leoyoung.model.Payroll;
import com.leoyoung.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {
    @Autowired
    private PayrollRepository payrollRepository;

    //获取PDF文件，依据员工ID和薪资日期
    public Payroll getPayrollByEmployeeIdAndDate(String employeeId, String salaryDate) {
        return payrollRepository.getPayrollByEmployeeIdAndDate(employeeId, salaryDate);
    }
}