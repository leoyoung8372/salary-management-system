package com.leoyoung.utils;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.leoyoung.model.Payroll;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class PdfGenerator {
    // 生成PDF文件的方法
    public void generatePdf(Payroll payroll, OutputStream outputStream) throws IOException {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // 使用仿宋字体
        PdfFont font = PdfFontFactory.createFont("src/main/resources/fonts/simfang.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

        // 打印接收到的 Payroll 数据
        System.out.println("接收到的 Payroll 数据: " + payroll);

        // 添加工资单信息到PDF
        document.add(new Paragraph("员工姓名: " + payroll.getName()).setFont(font));
        document.add(new Paragraph("部门: " + payroll.getDepartment()).setFont(font));
        document.add(new Paragraph("职位: " + payroll.getPosition()).setFont(font));
        document.add(new Paragraph("基本工资: " + payroll.getBaseSalary()).setFont(font));
        document.add(new Paragraph("绩效工资: " + payroll.getPerformanceSalary()).setFont(font));
        document.add(new Paragraph("津贴: " + payroll.getAllowance()).setFont(font));
        document.add(new Paragraph("奖金: " + payroll.getBonus()).setFont(font));
        document.add(new Paragraph("扣款: " + payroll.getDeduction()).setFont(font));
        document.add(new Paragraph("加班费: " + payroll.getOvertimePay()).setFont(font));
        document.add(new Paragraph("薪资日期: " + payroll.getSalaryDate()).setFont(font));
        document.add(new Paragraph("税金: " + payroll.getTax()).setFont(font));
        document.add(new Paragraph("总工资: " + payroll.getTotalPayroll()).setFont(font));

        // 关闭文档
        document.close();
    }
}