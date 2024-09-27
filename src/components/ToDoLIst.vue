<template>  
    <div>  
      <button @click="generatePayrollPdf">生成工资单PDF</button>  
    </div>  
</template>  
  
<script>  
import axios from 'axios';  
  
export default {  
  methods: {  
    async generatePayrollPdf() {  
      const employeeId = 13; // 员工ID  
      // 使用正确的URL格式，直接使用变量  
      const response = await axios.get(`http://localhost:8080/api/payroll/${employeeId}`);  
      const payroll = response.data;  
  
      // 调用后端生成PDF的API  
      const pdfResponse = await axios.post('http://localhost:8080/api/payroll/generate-pdf', payroll, {  
        responseType: 'blob'  
      });  
  
      // 创建下载链接  
      const url = window.URL.createObjectURL(new Blob([pdfResponse.data]));  
      const link = document.createElement('a');  
      link.href = url;  
      link.setAttribute('download', 'payroll.pdf');  
      document.body.appendChild(link);  
      link.click();  
      console.log(payroll);  
    }  
  }  
}  
</script>