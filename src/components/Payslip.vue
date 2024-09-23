<template>  
    <div class="container">  
        <div class="header">  
            <h2>员工工资</h2>  
            <div class="underline">  
                <div class="highlight"></div>  
            </div>  
        </div>  
        <div class="form">  
            <div class="form-grid">  
                <div class="form-item">  
                    <label class="label"><span class="required">*</span>工号</label>  
                    <input type="text" v-model="employeeId" placeholder="输入工号" class="input">  
                </div>  
                <div class="form-item">  
                    <label class="label"><span class="required">*</span>薪资归属日期</label>  
                    <input type="date" v-model="salaryDate" placeholder="选择日期" class="input">  
                </div>  
                <div class="form-item">  
                    <label class="label"><span class="required">*</span>基本工资</label>  
                    <input type="number" v-model.number="baseSalary" placeholder="输入基本工资" class="input" step="0.01" @input="calculateTotalPayroll">  
                </div>  
                <div class="form-item">  
                    <label class="label">绩效</label>  
                    <input type="number" v-model.number="performanceSalary" placeholder="输入绩效" class="input" step="0.01" @input="calculateTotalPayroll">  
                </div>  
                <div class="form-item">  
                    <label class="label">津贴</label>  
                    <input type="number" v-model.number="allowance" placeholder="输入津贴" class="input" step="0.01" @input="calculateTotalPayroll">  
                </div>  
                <div class="form-item">  
                    <label class="label">奖金</label>  
                    <input type="number" v-model.number="bonus" placeholder="输入奖金" class="input" step="0.01" @input="calculateTotalPayroll">  
                </div>  
                <div class="form-item">  
                    <label class="label">扣除</label>  
                    <input type="number" v-model.number="deduction" placeholder="输入扣除" class="input" step="0.01" @input="calculateTotalPayroll">  
                </div>  
                <div class="form-item">  
                    <label class="label">加班费</label>  
                    <input type="number" v-model.number="overtimePay" placeholder="输入加班费" class="input" step="0.01" @input="calculateTotalPayroll">  
                </div>  
                <div class="form-item">  
                    <label class="label">应交税额</label>  
                    <input type="text" :value="calculateTax()" readonly class="input">  
                </div>  
                <div class="form-item">  
                    <label class="label">实发薪资</label>  
                    <input type="number" v-model.number="totalPayroll" readonly class="input">  
                </div>  
            </div>  
            <div class="form-item">  
                <button @click="submitSalary" class="submit-button">提交</button>  
            </div>  
        </div>  
    </div>  
</template>  

<script>  
import axios from 'axios';  
export default {  
    name: 'EmployeeForm',  
    data() {  
        return {  
            salaryDate: '',  
            employeeId: '',  
            baseSalary: '',  
            performanceSalary: 0.00,  
            allowance: 0.00,  
            bonus: 0.00,  
            deduction: 0.00,  
            overtimePay: 0.00,  
            tax: 0.00,  
            totalPayroll: 0  
        };  
    },  
    methods: {  
        calculateTax() {  
            const totalIncome = (this.baseSalary || 0) + (this.performanceSalary || 0) +  
                (this.allowance || 0) + (this.bonus || 0) +  
                (this.overtimePay || 0) - (this.deduction || 0);  
            let tax = 0;  
            if (totalIncome <= 3000) {  
                tax = 0;  
            } else if (totalIncome <= 12000) {  
                tax = (totalIncome - 3000) * 0.03;  
            } else if (totalIncome <= 25000) {  
                tax = (totalIncome - 12000) * 0.1 + 270;  
            } else if (totalIncome <= 35000) {  
                tax = (totalIncome - 25000) * 0.2 + 970;  
            } else if (totalIncome <= 55000) {  
                tax = (totalIncome - 35000) * 0.25 + 1970;  
            } else if (totalIncome <= 80000) {  
                tax = (totalIncome - 55000) * 0.3 + 4220;  
            } else {  
                tax = (totalIncome - 80000) * 0.35 + 7220;  
            }  
            return tax.toFixed(2);  
        },  
        calculateTotalPayroll() {  
            const totalIncome = (this.baseSalary || 0) + (this.performanceSalary || 0) +  
                (this.allowance || 0) + (this.bonus || 0) +  
                (this.overtimePay || 0) - (this.deduction || 0);  
            const taxAmount = this.calculateTax();  
            this.totalPayroll = (totalIncome - taxAmount).toFixed(2);  
        },  
        async submitSalary() {  
            const confirmed = confirm("请仔细核对，确定提交吗？");  
            if (!confirmed) {  
                return;  
            }  

            try {  
                const existsResponse = await axios.get(`http://localhost:8080/api/employees/${this.employeeId}`);  
                if (!existsResponse.data.exists) {  
                    alert("该工号不存在，请检查后再试。");  
                    return;  
                }  

                const salaryExistsResponse = await axios.get(`http://localhost:8080/api/salaries/check?employeeId=${this.employeeId}&salaryDate=${this.salaryDate}`);  
                if (salaryExistsResponse.data.exists) {  
                    alert("该员工在此日期的工资记录已存在，请选择其他日期。");  
                    return;  
                }  

                const taxAmount = this.calculateTax();  
                this.tax = taxAmount;  
                const payload = {  
                    employeeId: this.employeeId,  
                    salaryDate: this.salaryDate,  
                    baseSalary: this.baseSalary,  
                    performanceSalary: this.performanceSalary,  
                    allowance: this.allowance,  
                    bonus: this.bonus,  
                    deduction: this.deduction,  
                    overtimePay: this.overtimePay,  
                    tax: this.tax,  
                    totalPayroll: this.totalPayroll  
                };  

                console.log("即将发送的数据:", payload);  
                const response = await axios.post('http://localhost:8080/api/salaries/salaries', payload);  
                console.log("提交成功:", response.data);  
                this.resetForm();  
            } catch (error) {  
                console.error("提交失败:", error);  
                if (error.response) {  
                    console.error("服务器返回的错误:", error.response.data);  
                    alert(`提交失败: ${error.response.data.message || error.response.data}`);  
                } else {  
                    alert("提交失败: 网络错误或服务器未响应");  
                }  
            }  
        },  
        resetForm() {  
            this.salaryDate = '';  
            this.employeeId = '';  
            this.baseSalary = '';  
            this.performanceSalary = '';  
            this.allowance = '';  
            this.bonus = '';  
            this.deduction = '';  
            this.overtimePay = '';  
            this.totalPayroll = 0;  
        }  
    }  
}  
</script>

<style scoped>
.container {
    padding: 1.5rem;
    padding-left: 5rem;
    /* 内边距 */
    background-color: white;
    /* 背景颜色 */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
    border-radius: 0.375rem;
    /* 圆角 */
}

/* 表头样式 */
.header {
    margin-bottom: 1.5rem;
}

/* 下划线样式 */
.underline {
    border-bottom: 2px solid #C6F6D5;
    /* 绿色下划线 */
    margin-bottom: 1.5rem;
    /* 下边距 */
}

.highlight {
    width: 25%;
    /* 绿色高亮部分的宽度 */
    height: 0.25rem;
    /* 高度 */
    background-color: #48BB78;
    /* 绿色 */
}

.required {
    color: red;
    /* 红色 */
    margin-left: 0.25rem;
    /* 左边距 */
}

/* 表单网格样式 */
.form-grid {
    display: grid;
    /* 使用网格布局 */
    grid-template-columns: repeat(1, 1fr);
    /* 两列布局 */
    gap: 1.5rem;
    /* 网格项之间的间距 */
}

/* 表单项样式 */
.form-item {
    display: flex;
    flex-direction: column;
    /* 垂直排列 */
    margin-bottom: -0.44rem;
}

/* 标签样式 */
.label {
    color: #4A5568;
    margin-bottom: 0.25rem;
}

/* 输入框样式 */
.input {
    margin-top: 0.25rem;
    /* 上边距 */
    width: 50%;
    /* 宽度100% */
    border: 1px solid #D1D5DB;
    /* 边框颜色 */
    border-radius: 0.375rem;
    /* 圆角 */
    padding: 0.5rem;
    /* 内边距 */
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
    transition: border-color 0.2s;
    /* 边框颜色过渡效果 */
}

/* 输入框聚焦时的样式 */
.input:focus {
    border-color: #68D391;
    /* 聚焦时边框颜色 */
    outline: none;
    /* 去掉默认轮廓 */
    box-shadow: 0 0 0 1px rgba(104, 211, 145, 0.5);
    /* 聚焦时阴影效果 */
}

/* 提交按钮样式 */
.submit-button {
    width: 200px;
    margin-top: 35px;
    background-color: #48BB78;
    /* 按钮背景颜色 */
    color: white;
    /* 按钮文字颜色 */
    padding: 0.5rem 1rem;
    /* 内边距 */
    border: none;
    /* 无边框 */
    border-radius: 0.375rem;
    /* 圆角 */
    cursor: pointer;
    /* 鼠标指针样式 */
    transition: background-color 0.2s;
    /* 背景颜色过渡效果 */
}

/* 提交按钮悬停时的样式 */
.submit-button:hover {
    background-color: #38A169;
    /* 悬停时背景颜色 */
}
</style>