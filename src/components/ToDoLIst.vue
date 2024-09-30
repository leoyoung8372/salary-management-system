<template>  
    <div>  
      <input v-model="employeeId" type="text" placeholder="请输入员工ID" />  
      <button @click="generatePayrollPdf">生成工资单PDF</button>  
    </div>  
  </template>  
  
  <script>  
  import axios from 'axios';  
  
  export default {  
    data() {  
      return {  
        employeeId: null, // 初始化员工ID  
      };  
    },  
    methods: {  
      async generatePayrollPdf() {  
        if (!this.employeeId) {  
          alert('请先输入员工ID');  
          return;  
        }  
        
        // 使用用户输入的员工ID  
        const response = await axios.get(`http://localhost:8080/api/payroll/${this.employeeId}`);  
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