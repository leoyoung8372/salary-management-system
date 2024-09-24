<template>
    <div class="container">
        <h2>薪资发放记录</h2>
        <div class="table-container">
            <table class="salary-table">
                <thead>
                    <tr>
                        <th>发放日期</th>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>基本工资</th>
                        <th>绩效</th>
                        <th>津贴</th>
                        <th>奖金</th>
                        <th>加班费</th>
                        <th>扣除</th>
                        <th>税费</th>
                        <th>实发薪资</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="record in salaryRecords" :key="record.salaryDate">
                        <!-- 使用格式化日期的方法 -->
                        <td>{{ formatDate(record.salaryDate) }}</td>
                        <td>{{ record.name}}</td>
                        <td>{{ record.employeeId }}</td>
                        <td>{{ record.baseSalary }}</td>
                        <td>{{ record.performanceSalary }}</td>
                        <td>{{ record.allowance }}</td>
                        <td>{{ record.bonus }}</td>
                        <td>{{ record.overtimePay }}</td>
                        <td>{{ record.deduction }}</td>
                        <td>{{ record.tax }}</td>
                        <td>{{ record.totalPayroll }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</template>

<script>
import axios from 'axios'; // 导入 axios  
import eventBus from '@/utils/eventBus'; // 导入事件总线  

export default {
    name: 'SalaryPaymentRecord',
    data() {
        return {
            salaryRecords: [],
            employeeName: '' // 用于存储接收到的员工姓名
        };
    },
    created() {
        // 监听薪资提交事件  
        eventBus.on('salarySubmitted', (salaryData) => {
            console.log("接收到薪资数据:", salaryData); // 输出接收到的数据
            this.salaryRecords.push(salaryData); // 将新记录添加到数组中  
            console.log("当前薪资记录:", this.salaryRecords); // 输出当前薪资记录
        });
        this.fetchSalaryRecords(); // 加载已有的薪资记录  
    },
    beforeDestroy() {
        // 清理事件监听  
        eventBus.off('salarySubmitted');
    },
    mounted() {
        // 监听事件  
        eventBus.on('employeeNameUpdated', (name) => {  
            this.employeeName = name; // 将接收到的姓名存储在组件的状态中  
        });
    },
    methods: {
        
        async fetchSalaryRecords() {
            try {
                const response = await axios.get('http://localhost:8080/api/salaries');
                this.salaryRecords = response.data; // 将返回的数据赋值给 salaryRecords  
            } catch (error) {
                console.error("获取薪资记录失败:", error);
            }
        },
        formatDate(dateString) {
            const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
            const date = new Date(dateString);
            return date.toLocaleDateString('zh-CN', options); // 格式化为 YY-MM-DD  
        }
    }
}  
</script>

<style scoped>
.container {
    padding: 20px;
}

.table-container {
    max-height: 860px;
    overflow-y: auto;
    /* border: 1px solid #ddd;   */
    border-radius: 4px;
}

.salary-table {
    width: 100%;
    border-collapse: collapse;
}

.salary-table th,
.salary-table td {
    padding: 15px;
    /* 增加填充，增加列之间的空间 */
    text-align: left;
    border: 1px solid #ededed;
}

.salary-table th {
    background-color: #e3e3e3;
}

/* 交替行颜色 */
.salary-table tr:nth-child(even) {
    background-color: #f2f2f2;
    /* 浅灰色，用于偶数行 */
}

.salary-table tr:nth-child(odd) {
    background-color: #ffffff;
    /* 白色代表奇数行 */
}
</style>