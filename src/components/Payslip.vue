<template>
    <div class="container">
        <div class="header">
            <h2 style="font-weight: bolder;">员工工资</h2>
            <div class="underline">
                <div class="highlight"></div>
            </div>
        </div>

        <!-- 警告提示 -->
        <div v-if="warningMessage" class="warning-message" :style="{ opacity: warningOpacity }">
            {{ warningMessage }}
        </div>

        <div class="left-right">
            <div class="form-left">
                <div class="form-grid">
                    <!-- 选择员工下拉框 -->
                    <div class="form-item">
                        <label class="label"><span class="required">*</span>选择员工</label>
                        <select v-model="selectedEmployee" @change="updateEmployeeId" class="input">
                            <option v-for="employee in employees" :key="employee.employeeId"
                                :value="employee.employeeId">
                                {{ employee.name }}
                            </option>
                        </select>
                    </div>
                    <!-- 工号输入框，绑定到 employeeId -->
                    <div class="form-item">
                        <label class="label"><span class="required">*</span>工号</label>
                        <input type="text" v-model="employeeId" placeholder="自动填充" class="input" readonly>
                    </div>
                    <div class="form-item">
                        <label class="label"><span class="required">*</span>薪资发放日期</label>
                        <input type="date" v-model="salaryDate" placeholder="选择日期" class="input">
                    </div>
                    <div class="form-item">
                        <label class="label"><span class="required">*</span>基本工资</label>
                        <input type="number" min="0" max="50000" v-model.number="baseSalary" placeholder="输入基本工资"
                            class="input" step="0.01" @input="calculateTotalPayroll" readonly>
                    </div>
                    <div class="form-item">
                        <label class="label">绩效</label>
                        <input type="number" min="0" max="50000" v-model.number="performanceSalary" placeholder="输入绩效"
                            class="input" step="0.01" @input="calculateTotalPayroll" readonly>
                    </div>
                    <div class="form-item">
                        <label class="label">津贴</label>
                        <input type="number" min="0" max="50000" v-model.number="allowance" placeholder="输入津贴"
                            class="input" step="0.01" @input="calculateTotalPayroll" readonly>
                    </div>
                    <div class="form-item">
                        <label class="label">奖金</label>
                        <input type="number" min="0" max="50000" v-model.number="bonus" placeholder="输入奖金" class="input"
                            step="0.01" @input="calculateTotalPayroll" readonly>
                    </div>
                    <div class="form-item">
                        <label class="label">扣除</label>
                        <input type="number" min="0" max="50000" v-model.number="deduction" placeholder="输入扣除"
                            class="input" step="0.01" @input="calculateTotalPayroll" readonly>
                    </div>
                    <div class="form-item">
                        <label class="label">加班费</label>
                        <input type="number" min="0" max="50000" v-model.number="overtimePay" placeholder="输入加班费"
                            class="input" step="0.01" @input="calculateTotalPayroll" readonly>
                    </div>
                    <div class="form-item">
                        <label class="label">应交税额</label>
                        <input type="text" :value="calculateTax()" readonly class="input">
                    </div>
                    <div class="form-item" style="margin-bottom: 10px;">
                        <label class="label">实发薪资</label>
                        <input type="number" v-model.number="totalPayroll" readonly class="input">
                    </div>
                </div>
                <div class="form-item">
                    <button @click="submitSalary" class="submit-button" >提交</button>
                </div>
            </div>

            <!-- 右侧 -->
            <div class="form-right">
                <div class="searchSalary">
                    <label>薪资归属日期</label>
                    <input type="month" v-model="searchSalaryDate" required>
                    <label>员工工号</label>
                    <input type="text" v-model="searchEmployeeId" required>
                    <button @click="getAttendanceSummary" class="submit-button">查询</button>
                </div>
                <div class="show">
                    <p>考勤情况: </p>
                    <p>上班天数: <span>{{ attendanceSummary.workDays }}</span></p>
                    <p>加班天数: <span>{{ attendanceSummary.overtimeDays }}</span></p>
                    <p>迟到次数: <span>{{ attendanceSummary.lateCount }}</span></p>
                </div>
                <div class="explain">
                    <p>说明: </p>
                    <p>月基本工资 = 160元/天 × 22天 = 3520元</p>
                    <p>绩效 = 当加班天数 ≥ 5天且上班天数 ≥ 15天时，绩效奖200元;</p>
                    <p>奖金 = 当上班天数 ≥ 22天时，奖金220元；小于22天则无奖金。</p>
                    <p>扣除 = 迟到次数 × 5元;</p>
                    <p>加班费 = 60元/天 × 加班天数;</p>
                    <p>津贴 = 300元/月;</p>
                </div>
                <div class="explain">
                    <p>提示: </p>
                    <p>查询后会自动填充薪资到输入框中，不使用查询会导致无法填充基本工资，导致提交失败</p>
                </div>
            </div>

        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import eventBus from '@/utils/eventBus'; // 导入事件总线  

// 定义响应式变量  
const employees = ref([]); // 员工列表  
const selectedEmployee = ref(''); // 选中的员工ID   
const salaryDate = ref(''); // 薪资归属日期  
const employeeId = ref(''); // 工号  
const baseSalary = ref(''); // 基本工资  
const performanceSalary = ref(0.00); // 绩效  
const allowance = ref(0.00); // 津贴  
const bonus = ref(0.00); // 奖金  
const deduction = ref(0.00); // 扣除  
const overtimePay = ref(0.00); // 加班费  
const tax = ref(0)   //税费
const totalPayroll = ref(0); // 实发薪资   
const warningMessage = ref(''); // 警告信息  
const warningOpacity = ref(1); // 警告透明度   

// 新增变量，用于考勤统计  
const searchSalaryDate = ref(''); // 用于查询的薪资日期  
const searchEmployeeId = ref(''); // 用于查询的员工工号  
const attendanceSummary = ref({ workDays: 0, overtimeDays: 0, lateCount: 0 }); // 考勤统计信息  

// 组件加载时获取员工列表  
onMounted(fetchEmployees);

// 获取员工列表  
async function fetchEmployees() {
    try {
        const response = await axios.get('http://localhost:8080/api/employees/employees');
        employees.value = response.data; // 将获取的员工数据存储到 employees 数组中  
    } catch (error) {
        console.error("获取员工列表失败:", error); // 错误处理  
    }
}

// 更新工号  
function updateEmployeeId() {
    employeeId.value = selectedEmployee.value; // 根据选择的员工更新工号  
}

// 计算税额  
function calculateTax() {
    const totalIncome = (baseSalary.value || 0) + (performanceSalary.value || 0) +
        (allowance.value || 0) + (bonus.value || 0) +
        (overtimePay.value || 0) - (deduction.value || 0);
    let taxAmount = 0;
    if (totalIncome <= 3000) {
        taxAmount = 0;
    } else if (totalIncome <= 12000) {
        taxAmount = (totalIncome - 3000) * 0.03;
    } else if (totalIncome <= 25000) {
        taxAmount = (totalIncome - 12000) * 0.1 + 270;
    } else if (totalIncome <= 35000) {
        taxAmount = (totalIncome - 25000) * 0.2 + 970;
    } else if (totalIncome <= 55000) {
        taxAmount = (totalIncome - 35000) * 0.25 + 1970;
    } else if (totalIncome <= 80000) {
        taxAmount = (totalIncome - 55000) * 0.3 + 4220;
    } else {
        taxAmount = (totalIncome - 80000) * 0.35 + 7220;
    }
    return taxAmount.toFixed(2); // 返回格式化的税额  
}

// 计算总薪资  
function calculateTotalPayroll() {
    const totalIncome = (baseSalary.value || 0) + (performanceSalary.value || 0) +
        (allowance.value || 0) + (bonus.value || 0) +
        (overtimePay.value || 0) - (deduction.value || 0);
    const taxAmount = calculateTax();
    totalPayroll.value = (totalIncome - taxAmount).toFixed(2); // 计算实发薪资  
}

// 显示警告信息  
function showWarning(message) {
    warningMessage.value = message;
    warningOpacity.value = 1; // 重置透明度为1  
    setTimeout(() => {
        const fadeOutInterval = setInterval(() => {
            if (warningOpacity.value > 0) {
                warningOpacity.value -= 0.05; // 每次减少透明度  
            } else {
                clearInterval(fadeOutInterval);
                warningMessage.value = ''; // 隐藏警告  
            }
        }, 100); // 每100毫秒减少透明度  
    }, 1314); // 1秒后开始淡出  
}

// 提交薪资信息  
async function submitSalary() {
    const confirmed = confirm("请仔细核对，确定提交吗？");
    if (!confirmed) {
        return; // 用户取消提交  
    }

    try {
        // 检查工号是否存在  
        const existsResponse = await axios.get(`http://localhost:8080/api/employees/${employeeId.value}`);
        if (!existsResponse.data.exists) {
            showWarning("该工号不存在，请检查后再试。");
            return;
        }

        // 检查该员工在该月的工资记录是否已存在  
        const salaryExistsResponse = await axios.get(`http://localhost:8080/api/salaries/check?employeeId=${employeeId.value}&salaryDate=${salaryDate.value}`);
        if (salaryExistsResponse.data.exists) {
            showWarning("该员工在该月的工资记录已存在，请选择其他日期。");
            return;
        }

        const taxAmount = calculateTax();
        // 更新税额  
        tax.value = taxAmount;
        // 获取选中员工的姓名  
        const selectedEmployeeName = employees.value.find(emp => emp.employeeId === selectedEmployee.value)?.name || '';
        console.log("选中员工姓名:", selectedEmployeeName);
        const payload = {
            employeeId: employeeId.value,
            name: selectedEmployeeName,
            salaryDate: salaryDate.value,
            baseSalary: baseSalary.value,
            performanceSalary: performanceSalary.value,
            allowance: allowance.value,
            bonus: bonus.value,
            deduction: deduction.value,
            overtimePay: overtimePay.value,
            tax: tax.value,
            totalPayroll: totalPayroll.value
        };

        console.log("即将发送的数据:", payload);
        const response = await axios.post('http://localhost:8080/api/salaries/salaries', payload);
        console.log("提交成功:", response.data);
        resetForm(); // 提交成功后重置表单  

        // 提示用户提交成功  
        showWarning("提交成功");

        // 发送事件，传递薪资信息  
        eventBus.emit('salarySubmitted', payload);
        console.log("事件已发送，薪资数据:", payload);

    } catch (error) {
        console.error("提交失败:", error);
        if (error.response) {
            console.error("服务器返回的错误:", error.response.data);
            showWarning(`提交失败，注意检查“薪资发放日期”: ${error.response.data.message || error.response.data}`);
        } else {
            showWarning("提交失败: 网络错误或服务器未响应");
        }
    }
}

// 获取考勤统计信息  
async function getAttendanceSummary() {  
    const yearMonth = searchSalaryDate.value; // 薪资归属日期  
    const empId = searchEmployeeId.value; // 员工工号  

    if (!yearMonth || !empId) {  
        showWarning("请填写员工工号和薪资归属日期！");  
        return;  
    }  

    try {  
        const response = await axios.get(`http://localhost:8080/api/attendance/summary?employeeId=${empId}&salaryDate=${yearMonth}`);  
        attendanceSummary.value = response.data; // 更新考勤统计信息   

        // 获取考勤信息  
        const { workDays, overtimeDays, lateCount } = attendanceSummary.value;  

        // 计算各项薪资  
        const dailyWage = 160; // 每天薪资  
        const baseSalaryCalculated = workDays * dailyWage; // 基本工资 = 上班天数 × 每天薪资  
        const performanceCalculated = (overtimeDays >= 5 && workDays >= 15) ? 200 : 0; // 绩效奖  
        const allowanceCalculated = 300; // 津贴  
        const bonusCalculated = (workDays >= 22) ? 220 : 0; // 奖金  
        const deductionCalculated = lateCount * 5; // 扣除 = 迟到次数 × 5 元  

        // 填充到form-left类的输入框中  
        baseSalary.value = baseSalaryCalculated;  
        performanceSalary.value = performanceCalculated;  
        allowance.value = allowanceCalculated;  
        bonus.value = bonusCalculated;  
        deduction.value = deductionCalculated;  

        // 计算总的薪资  
        calculateTotalPayroll(); // 重新计算实发薪资  

    } catch (error) {  
        console.error("获取考勤统计信息失败:", error);  
        showWarning("获取考勤统计信息失败，请检查工号或日期。");  
    }  
}

// 重置表单  
function resetForm() {
    salaryDate.value = '';
    employeeId.value = '';
    baseSalary.value = '';
    performanceSalary.value = 0.00;
    allowance.value = 0.00;
    bonus.value = 0.00;
    deduction.value = 0.00;
    overtimePay.value = 0.00;
    totalPayroll.value = 0;
    selectedEmployee.value = ''; // 重置选中的员工  
}  
</script>

<style scoped>
.container {
    padding: 1.5rem;
    padding-left: 5rem;
    /* 内边距 */
    background-color: #ffffff;
    /* 背景颜色 */
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
    border-radius: 0.375rem;
    /* 圆角 */
}

/* 下划线样式 */
.underline {
    border-bottom: 2px solid #c7f7d7;
    /* 绿色下划线 */
    margin-bottom: 1.5rem;
    /* 下边距 */
}

.warning-message {
    position: fixed;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    background-color: rgb(221, 62, 62);
    color: white;
    padding: 10px 20px;
    border-radius: 5px;
    z-index: 1000;
    transition: opacity 0.5s ease;
}

.highlight {
    width: 22%;
    /* 绿色高亮部分的宽度 */
    height: 0.4rem;
    /* 高度 */
    background-color: #3b877f;
    /* 绿色 */
    border-radius: 2px 22px 2px 22px;
    margin-top: 2px;
}

.required {
    color: red;
    /* 红色 */
    margin-left: 0.25rem;
    /* 左边距 */
}

/* 盒子 */
.left-right {
    display: flex;
}

.form-left {
    width: 33%;
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
    margin-bottom: -0.88rem;
}

/* 标签样式 */
.label {
    color: #4A5568;
    font-weight: bold;
    margin-bottom: 0.25rem;
}

/* 输入框样式 */
.input {
    margin-top: 0.25rem;
    /* 上边距 */
    width: 350px;
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
    border-color: #53b69c;
    /* 聚焦时边框颜色 */
    outline: none;
    /* 去掉默认轮廓 */
    /* box-shadow: 0 0 0 1px rgba(104, 211, 145, 0.5); */
    /* 聚焦时阴影效果 */
}

/* 提交按钮样式 */
.submit-button {
    width: 200px;
    margin-top: 15px;
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


/* 右侧 */
.left-right .form-right {
    background-color: #9bc4ae;
    /* 背景颜色 */
    width: 70%;
    padding: 20px;
    /* 内边距 */
    border-radius: 8px;
    /* 圆角 */
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    /* 阴影效果 */
    display: flex;
    flex-direction: column;
    /* 垂直排列 */
}

.searchSalary {
    display: flex;
    flex-direction: column;
    /* 垂直排列 */
    margin-bottom: 20px;
    /* 底部间距 */
}

.searchSalary label {
    font-weight: bold;
    /* 加粗字体 */
    margin-bottom: 5px;
    /* 标签与输入框之间的间距 */
    color: #ffffff;
    /* 标签颜色 */
}

.searchSalary input {
    padding: 10px;
    /* 输入框内边距 */
    border: none;
    /* 去掉边框 */
    border-radius: 4px;
    /* 圆角 */
    margin-bottom: 10px;
    /* 输入框间距 */
    font-size: 16px;
    /* 字体大小 */
}

.searchSalary button {
    background-color: #ffffff;
    /* 按钮背景色 */
    color: #38A169;
    /* 按钮字体颜色 */
    padding: 10px;
    /* 按钮内边距 */
    border: none;
    /* 去掉边框 */
    border-radius: 4px;
    /* 圆角 */
    cursor: pointer;
    /* 鼠标悬停时显示为指针 */
    font-weight: bold;
    /* 加粗字体 */
    transition: background-color 0.3s;
    /* 背景色变换效果 */
}

.searchSalary button:hover {
    background-color: #bfbdd2;
    /* 鼠标悬停时变更的颜色 */
}

.show {
    background-color: #ffffff;
    /* 显示区背景色 */
    padding: 15px;
    /* 内边距 */
    border-radius: 4px;
    /* 圆角 */
    box-shadow: 0 1px 5px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
}

.show p {
    font-size: 16px;
    /* 字体大小 */
    margin: 5px 0;
    /* 段落间距 */
    color: #333;
    /* 字体颜色 */
}
.show p:first-child {  
    font-weight: bold; /* 说明引导文字加粗 */  
    font-size: 18px; /* 为说明内容使用稍大的字体 */  
    color: #38A169; /* 说明文字颜色，使用与其他元素一致的颜色 */  
} 

.show span {
    font-weight: bold;
    /* 加粗数字 */
    color: #38A169;
    /* 数字颜色 */
}
.explain {  
    background-color: #f9f9f9; /* 背景颜色，淡灰色以区分不同部分 */  
    padding: 15px; /* 内部填充 */  
    border-radius: 4px; /* 圆角 */  
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* 阴影效果，给部分深度感 */  
    margin-top: 20px; /* 顶部间隔，防止内容重叠 */  
}  

.explain p {  
    font-size: 16px; /* 字体大小 */  
    margin: 5px 0; /* 每段之间的间距 */  
    color: #333; /* 文字颜色 */  
}  

.explain p:first-child {  
    font-weight: bold; /* 说明引导文字加粗 */  
    font-size: 18px; /* 为说明内容使用稍大的字体 */  
    color: #38A169; /* 说明文字颜色，使用与其他元素一致的颜色 */  
} 
</style>