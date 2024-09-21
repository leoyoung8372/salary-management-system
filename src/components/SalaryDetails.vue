<template>
    <div class="container mx-auto p-4">
        <h1 class="text-2xl font-bold mb-4">员工薪资查询</h1>
        <input v-model="employeeId" type="number" placeholder="请输入员工ID" class="border p-2 mb-4" />
        <button @click="fetchSalaryDetails" class="bg-blue-500 text-white p-2 rounded">
            查询薪资
        </button>

        <div v-if="salary" class="mt-4">
            <h2 class="text-xl font-semibold">薪资详情</h2>
            <ul class="list-disc pl-5">
                <li><strong>基本薪资:</strong> {{ salary.baseSalary }}</li>
                <li><strong>绩效薪资:</strong> {{ salary.performanceSalary }}</li>
                <li><strong>津贴:</strong> {{ salary.allowance }}</li>
                <li><strong>奖金:</strong> {{ salary.bonus }}</li>
                <li><strong>加班费:</strong> {{ salary.overtimePay }}</li>
                <li><strong>扣费:</strong>{{ salary.deduction }}</li>
            </ul>
            <h3 class="text-lg font-semibold mt-4">总薪资: {{ totalSalary }}</h3>
        </div>

        <div v-if="errorMessage" class="text-red-500 mt-4">
            {{ errorMessage }}
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            employeeId: '',
            salary: null,
            errorMessage: null,
        };
    },
    computed: {
        totalSalary() {
            if (this.salary) {
                return (this.salary.baseSalary || 0) +
                    (this.salary.performanceSalary || 0) +
                    (this.salary.allowance || 0) +
                    (this.salary.bonus || 0) +
                    (this.salary.overtimePay || 0) -
                    (this.salary.deduction || 0);
            }
            return 0;
        }
    },
    methods: {
        async fetchSalaryDetails() {
            this.errorMessage = null; // 清空错误信息  
            try {
                const response = await axios.get(`http://localhost:8080/api/salaries/${this.employeeId}`);
                this.salary = response.data; // 获取薪资详情  
            } catch (error) {
                this.salary = null; // 清空薪资信息  
                this.errorMessage = "未找到该员工的薪资信息"; // 设置错误信息  
                console.error("Error fetching salary details:", error);
            }
        },
    },
};  
</script>

<style scoped>
.container {
    max-width: 600px;
}
</style>