<template>  
    <div>  
        <!-- 包含输入框和按钮的容器 -->  
        <div class="flex items-center mb-4">  
            <!-- 员工ID输入框 -->  
            <label for="employeeId" class="mr-2">员工ID:</label>  
            <input id="employeeId" v-model="employeeId" type="text" class="border rounded px-2 py-1 mr-4" />  

            <!-- 薪资日期输入框 -->  
            <label for="salaryDate" class="mr-2">薪资日期:</label>  
            <input id="salaryDate" v-model="salaryDate" type="text" class="border rounded px-2 py-1 mr-4" />  

            <!-- 导出PDF按钮 -->  
            <button @click="downloadPayrollPdf"  
                class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">  
                导出PDF文件  
            </button>  
        </div>  
    </div>  
</template>  

<script setup>  
import { ref } from 'vue';  
import axios from 'axios';  

// 定义员工ID和薪资日期的响应式变量  
const employeeId = ref('');  
const salaryDate = ref('');  

// 下载工资单PDF的方法  
const downloadPayrollPdf = () => {  
    console.log('employeeId:', employeeId.value);  
    console.log('salaryDate:', salaryDate.value);  

    axios({  
        method: 'get',  
        url: `http://localhost:8080/api/payroll/pdf?employeeId=${employeeId.value}&salaryDate=${salaryDate.value}`,  
        responseType: 'blob'  
    })  
    .then(response => {  
        console.log('response.data:', response.data);  

        const url = URL.createObjectURL(response.data);  
        console.log('url:', url);  

        const link = document.createElement('a');  
        link.href = url;  
        link.setAttribute('download', 'payroll.pdf');  
        console.log('link:', link);  

        document.body.appendChild(link);  
        link.click();  
        document.body.removeChild(link);  
    })  
    .catch(error => {  
        console.error(error);  
    });  
};
</script>  

<style>  
/* 样式定义 */  
.flex {  
    display: flex;  
    align-items: center;  
}  

.mb-4 {  
    margin-bottom: 1rem;  
}  

.mr-2 {  
    margin-right: 0.5rem;  
}  

.mr-4 {  
    margin-right: 1rem;  
}  

.border {  
    border: 1px solid #ccc;  
}  

.rounded {  
    border-radius: 0.25rem;  
}  

.px-2 {  
    padding-left: 0.5rem;  
    padding-right: 0.5rem;  
}  

.py-1 {  
    padding-top: 0.25rem;  
    padding-bottom: 0.25rem;  
}  

.bg-blue-500 {  
    background-color: #3b82f6;  
}  

.hover\:bg-blue-700:hover {  
    background-color: #2563eb;  
}  

.text-white {  
    color: #fff;  
}  

.font-bold {  
    font-weight: bold;  
}  

.py-2 {  
    padding-top: 0.5rem;  
    padding-bottom: 0.5rem;  
}  

.px-4 {  
    padding-left: 1rem;  
    padding-right: 1rem;  
}  

.rounded {  
    border-radius: 0.25rem;  
}  
</style>