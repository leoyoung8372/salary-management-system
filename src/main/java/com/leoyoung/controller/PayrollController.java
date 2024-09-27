package com.leoyoung.controller;

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

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {
    @Autowired
    private PayrollService payrollService;

    @Autowired
    private PdfGenerator pdfGenerator;

    // 根据员工ID获取工资单
    @GetMapping("/{employeeId}")
    public Payroll getPayroll(@PathVariable int employeeId) {
        return payrollService.getPayroll(employeeId);
    }

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePayrollPdf(@RequestBody Payroll payroll) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // 打印接收到的 Payroll 数据
            System.out.println("接收到的 Payroll 数据: " + payroll);

            // 生成 PDF 文件到输出流
            pdfGenerator.generatePdf(payroll, outputStream);

            byte[] pdfBytes = outputStream.toByteArray();

            // 返回 PDF 文件作为响应
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payroll.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("PDF生成失败：" + e.getMessage()).getBytes());
        }
    }
}