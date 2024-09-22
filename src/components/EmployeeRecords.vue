<template>
    <div class="container">
        <div class="header">
            <h2 class="title">员工档案</h2>
        </div>
        <table class="employee-table">
            <thead>
                <tr>
                    <th>姓名</th>
                    <th>身份证号</th>
                    <th>性别</th>
                    <th>身份证地址</th>
                    <th>部门</th>
                    <th>岗位</th>
                    <th>工号</th>
                    <th>出生日期</th>
                    <th>入职日期</th>
                    <th>联系电话</th>
                    <th>状态</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="employee in employees" :key="employee.employeesId">
                    <td>{{ employee.name }}</td>
                    <td>{{ employee.idCard }}</td>
                    <td>{{ employee.gender }}</td>
                    <td>{{ employee.address }}</td>
                    <td>{{ employee.department }}</td>
                    <td>{{ employee.position }}</td>
                    <td>{{ employee.employeeId }}</td>
                    <td>{{ employee.birthDate }}</td>
                    <td>{{ employee.hireDate }}</td>
                    <td>{{ employee.phone }}</td>
                    <td>{{ employee.status }}</td>
                </tr>
            </tbody>
        </table>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    name: 'EmployeeRecords',
    data() {
        return {
            employees: [] // 存储员工数据  
        };
    },
    async mounted() {
        await this.fetchEmployees(); // 组件加载时获取员工数据  
    },
    methods: {
        async fetchEmployees() {
            try {
                const response = await axios.get('http://localhost:8080/api/employees'); // 替换为您的 API 地址 
                console.log(response.data);
                this.employees = response.data; // 将获取的数据赋值给 employees  
            } catch (error) {
                console.error('获取员工数据失败:', error);
                alert('获取员工数据失败，请检查后重试。');
            }
        }
    }
};  
</script>

<style lang="scss" scoped>
.container {
    height: 100vh;
    /* background-color: aqua; */
}
.employee-table {
    width: 100%;
    border-collapse: collapse;
}


.employee-table th,
.employee-table td {
    border: 1px solid #ddd;
    padding: 8px;
}

.employee-table th {
    background-color: #f2f2f2;
}
</style>