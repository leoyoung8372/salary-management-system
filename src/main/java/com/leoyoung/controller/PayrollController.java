package com.leoyoung.controller;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.leoyoung.model.Payroll;
import com.leoyoung.service.PayrollService;
import com.leoyoung.utils.PdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {
    @Autowired
    private PayrollService payrollService;

    @Autowired
    private PdfGenerator pdfGenerator;

    //生成PDF文件
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> getPayrollPdf(@RequestParam("employeeId") String employeeId, @RequestParam("salaryDate") String salaryDate) {

        // 根据员工ID和工资日期获取工资单信息
        Payroll payroll = payrollService.getPayrollByEmployeeIdAndDate(employeeId, salaryDate);

        // 创建一个字节数组输出流用于存储PDF文件
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 使用PDF生成器生成PDF文件并写入输出流
            pdfGenerator.generatePdf(payroll, outputStream);
        } catch (IOException | java.io.IOException e) {
            // 如果发生异常,打印堆栈信息并返回内部服务器错误响应
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // 从输出流中获取PDF文件的字节数组
        byte[] pdfBytes = outputStream.toByteArray();

        // 根据员工ID和工资日期动态生成PDF文件名
        String fileName = "payroll_" + employeeId + "_" + salaryDate + ".pdf";

        // 设置HTTP响应头,指定内容类型为PDF,并设置文件名
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", fileName);
        System.out.printf(fileName);

        // 返回包含PDF文件字节数组的HTTP响应
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

}