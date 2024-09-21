package com.leoyoung.service;

import com.leoyoung.model.Salary;
import com.leoyoung.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaryService {

    @Autowired
    private SalaryRepository salaryRepository;

    public List<Salary> getAllSalaries() {
        return salaryRepository.getAllSalaries();
    }

    public void createSalary(Salary salary) {
        salaryRepository.insertSalary(salary);
    }

    //根据employeeId获取薪资详情
    public Salary getSalaryDetailsByEmployeeId(Long employeeId) {
        return salaryRepository.getSalaryDetailsByEmployeeId(employeeId);
    }


}