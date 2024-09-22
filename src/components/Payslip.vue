<template>
    <div class="container">
        <!-- 表单头部 -->
        <div class="header">
            <h2>员工工资</h2>
            <!-- 分隔线 -->
            <div class="underline">
                <div class="highlight"></div>
            </div>
        </div>
        <!-- 表单内容 -->
        <div class="form">
            <div class="form-grid">

                <!-- 员工ID输入框 -->
                <div class="form-item">
                    <label class="label">员工ID</label>
                    <input type="text" v-model="employeeId" placeholder="输入员工ID" class="input">
                </div>
                <!-- 薪资归属日期输入框 -->
                <div class="form-item">
                    <label class="label">薪资归属日期</label>
                    <input type="date" v-model="salaryDate" placeholder="选择日期" class="input">
                </div>
                <!-- 基本工资输入框 -->
                <div class="form-item">
                    <label class="label">基本工资</label>
                    <input type="number" v-model.number="baseSalary" placeholder="输入基本工资" class="input" step="0.01">
                </div>
                <!-- 绩效输入框 -->
                <div class="form-item">
                    <label class="label">绩效</label>
                    <input type="number" v-model.number="performanceSalary" placeholder="输入绩效" class="input"
                        step="0.01">
                </div>
                <!-- 津贴输入框 -->
                <div class="form-item">
                    <label class="label">津贴</label>
                    <input type="number" v-model.number="allowance" placeholder="输入津贴" class="input" step="0.01">
                </div>
                <!-- 奖金输入框 -->
                <div class="form-item">
                    <label class="label">奖金</label>
                    <input type="number" v-model.number="bonus" placeholder="输入奖金" class="input" step="0.01">
                </div>
                <!-- 扣除输入框 -->
                <div class="form-item">
                    <label class="label">扣除</label>
                    <input type="number" v-model.number="deduction" placeholder="输入扣除" class="input" step="0.01">
                </div>
                <!-- 加班费输入框 -->
                <div class="form-item">
                    <label class="label">加班费</label>
                    <input type="number" v-model.number="overtimePay" placeholder="输入加班费" class="input" step="0.01">
                </div>
                <!-- 税额计算结果 -->
                <div class="form-item">
                    <label class="label">应交税额</label>
                    <input type="text" :value="calculateTax()" readonly class="input">
                </div>
            </div>
            <!-- 提交按钮 -->
            <div class="form-item">
                <button @click="submitSalary" class="submit-button">提交</button>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    name: 'EmployeeForm', // 组件名称  
    data() {
        return {
            salaryDate: '', // 存储薪资归属日期  
            employeeId: '', // 存储员工ID  
            baseSalary: '', // 存储基本工资  
            performanceSalary: '', // 存储绩效  
            allowance: '', // 存储津贴  
            bonus: '', // 存储奖金  
            deduction: '', // 存储扣除  
            overtimePay: '', // 存储加班费
            tax: ''
        };
    },
    methods: {
        // 计算应交税额  
        calculateTax() {
            // 计算应纳税所得额  
            const totalIncome = (this.baseSalary || 0) + (this.performanceSalary || 0) +
                (this.allowance || 0) + (this.bonus || 0) +
                (this.overtimePay || 0) - (this.deduction || 0);

            // 超额累进税率计算  
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
            return tax.toFixed(2); // 返回格式化后的税额  
        },
        async submitSalary() {
            try {
                const taxAmount = this.calculateTax();
                this.tax = taxAmount; // 更新 tax 属性
                const payload = {
                    employeeId: this.employeeId,
                    salaryDate: this.salaryDate,
                    baseSalary: this.baseSalary,
                    performanceSalary: this.performanceSalary,
                    allowance: this.allowance,
                    bonus: this.bonus,
                    deduction: this.deduction,
                    overtimePay: this.overtimePay,
                    tax: this.tax
                };


                console.log("即将发送的数据:", payload); // 打印即将发送的数据  

                const response = await axios.post('http://localhost:8080/api/salaries/salaries', payload);
                console.log("提交成功:", response.data);
                this.resetForm();
            } catch (error) {
                console.error("提交失败:", error);
                if (error.response) {
                    console.error("服务器返回的错误:", error.response.data); // 打印服务器返回的错误信息  
                }
            }
        },
        // 重置表单  
        resetForm() {
            this.salaryDate = '';
            this.employeeId = '';
            this.baseSalary = '';
            this.performanceSalary = '';
            this.allowance = '';
            this.bonus = '';
            this.deduction = '';
            this.overtimePay = '';
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