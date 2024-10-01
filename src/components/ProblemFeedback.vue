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

// 定义格式化日期的函数  
const formatDate = (date) => {
    if (!date) return '';
    const d = new Date(date);
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
};

// 下载工资单PDF的方法  
const downloadPayrollPdf = () => {
    console.log('employeeId:', employeeId.value);
    console.log('salaryDate:', salaryDate.value);

    // 使用 formatDate 函数格式化日期  
    const formattedDate = formatDate(salaryDate.value);

    axios({
        method: 'get',
        url: `http://localhost:8080/api/payroll/pdf?employeeId=${employeeId.value}&salaryDate=${formattedDate}`,
        responseType: 'blob',
        validateStatus: function (status) {
            // 接受所有状态码，以便在 then 中处理  
            return true;
        }
    })
        .then(response => {
            if (response.status === 200) {
                // 成功响应，处理 PDF 下载  
                const url = URL.createObjectURL(response.data);

                // 从响应头中获取文件名  
                const contentDisposition = response.headers['content-disposition'];
                let fileName = 'payroll.pdf';
                if (contentDisposition) {
                    const fileNameMatch = contentDisposition.match(/filename\*=UTF-8''(.+)|filename="(.+)"|filename=(.+)/);
                    if (fileNameMatch) {
                        fileName = decodeURIComponent(fileNameMatch[1] || fileNameMatch[2] || fileNameMatch[3]);
                    }
                }

                const link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', fileName);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            } else if (response.status === 404) {
                // 处理 404 错误，提示用户  
                alert('不存在该薪资记录。');
            } else {
                // 处理其他错误  
                alert('下载 PDF 时发生错误。');
            }
        })
        .catch(error => {
            console.error(error);
            alert('发生了网络错误或未知错误。');
        });
};

</script>

<style>
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