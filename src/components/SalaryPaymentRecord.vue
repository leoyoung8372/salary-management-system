<template>
    <div class="container">
        <h2>员工薪资记录</h2>
        <div class="table-container">
            <table class="salary-table">
                <thead>
                    <tr>
                        <th>员工ID</th>
                        <th>员工姓名</th>
                        <th>手机号</th>
                        <th>基础工资
                            <!-- 添加排序按钮 -->
                            <button @click="sortBy('baseSalary')" class="sort-btn">
                                <span v-if="sortColumn === 'baseSalary' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'baseSalary' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>绩效工资
                            <!-- 添加排序按钮 -->
                            <button @click="sortBy('performanceSalary')" class="sort-btn">
                                <span v-if="sortColumn === 'performanceSalary' && sortDirection === 'asc'">▲</span>
                                <span
                                    v-else-if="sortColumn === 'performanceSalary' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>津贴
                            <!-- 添加排序按钮 -->
                            <button @click="sortBy('allowance')" class="sort-btn">
                                <span v-if="sortColumn === 'allowance' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'allowance' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>奖金
                            <!-- 添加排序按钮 -->
                            <button @click="sortBy('bonus')" class="sort-btn">
                                <span v-if="sortColumn === 'bonus' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'bonus' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>扣除
                            <!-- 添加排序按钮 -->
                            <button @click="sortBy('deduction')" class="sort-btn">
                                <span v-if="sortColumn === 'deduction' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'deduction' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>加班费
                            <!-- 添加排序按钮 -->
                            <button @click="sortBy('overtimePay')" class="sort-btn">
                                <span v-if="sortColumn === 'overtimePay' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'overtimePay' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>工资日期</th>
                        <th>税费
                            <!-- 添加排序按钮 -->
                            <button @click="sortBy('tax')" class="sort-btn">
                                <span v-if="sortColumn === 'tax' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'tax' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>总工资
                            <!-- 添加排序按钮 -->
                            <button @click="sortBy('totalPayroll')" class="sort-btn">
                                <span v-if="sortColumn === 'totalPayroll' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'totalPayroll' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(record, index) in salaryRecords" :key="record.employee_id"
                        :class="{ 'bg-white': index % 2 === 0, 'bg-gray-200': index % 2 !== 0 }">
                        <td>{{ record.employeeId }}</td>
                        <td>{{ record.name }}</td>
                        <td>{{ record.phone }}</td>
                        <td>{{ record.baseSalary }}</td>
                        <td>{{ record.performanceSalary }}</td>
                        <td>{{ record.allowance }}</td>
                        <td>{{ record.bonus }}</td>
                        <td>{{ record.deduction }}</td>
                        <td>{{ record.overtimePay }}</td>
                        <td>{{ formatDate(record.salaryDate) }}</td>
                        <td>{{ record.tax }}</td>
                        <td>{{ record.totalPayroll }}</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- 显示加载状态 -->
        <div v-if="loading" class="mt-4">加载中...</div>
        <!-- 显示错误信息 -->
        <div v-if="error" class="mt-4 text-red-600">错误: {{ error }}</div>
    </div>
</template>

<script>
import axios from 'axios';
import { ref, onMounted } from 'vue';

export default {
    name: 'EmployeeSalaryRecords',
    setup() {
        // 定义响应式数据  
        const salaryRecords = ref([]);
        const loading = ref(false);
        const error = ref(null);
        const sortColumn = ref(null);
        const sortDirection = ref(null);

        // 获取员工薪资记录的方法  
        const fetchSalaryRecords = async () => {
            loading.value = true;
            error.value = null;
            try {
                const response = await axios.get('http://localhost:8080/api/salaries/records');
                salaryRecords.value = response.data;
            } catch (err) {
                error.value = err.message || '无法获取数据';
            } finally {
                loading.value = false;
            }
        };

        // 格式化日期的方法  
        const formatDate = (dateString) => {
            const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
            const date = new Date(dateString);
            return date.toLocaleDateString('zh-CN', options);
        };

        // 排序方法  
        const sortBy = (column) => {
            // 如果当前列已经是排序列,则切换排序方向  
            if (sortColumn.value === column) {
                sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc';
            } else {
                // 否则设置新的排序列和排序方向  
                sortColumn.value = column;
                sortDirection.value = 'asc';
            }
            // 根据排序列和排序方向对数据进行排序  
            salaryRecords.value.sort((a, b) => {
                const valueA = a[column];
                const valueB = b[column];
                if (valueA < valueB) return sortDirection.value === 'asc' ? -1 : 1;
                if (valueA > valueB) return sortDirection.value === 'asc' ? 1 : -1;
                return 0;
            });
        };

        // 在组件挂载时获取员工薪资记录  
        onMounted(fetchSalaryRecords);

        // 返回需要在模板中使用的数据和方法  
        return {
            salaryRecords,
            loading,
            error,
            formatDate,
            sortColumn,
            sortDirection,
            sortBy,
        };
    },
};
</script>

<style scoped>
/* 容器样式 */
.container {
    padding: 20px;
}

/* 表格容器样式 */
.table-container {
    max-height: 860px;
    overflow-y: auto;
    border-radius: 4px;
}

/* 薪资表格样式 */
.salary-table {
    width: 100%;
    border-collapse: collapse;
}

/* 表格单元格样式 */
.salary-table th,
.salary-table td {
    padding: 15px;
    text-align: left;
    border: 1px solid #ededed;
}

/* 表头样式 */
.salary-table th {
    background-color: #e3e3e3;
    position: relative;
}

/* 排序按钮样式 */
.sort-btn {
    position: absolute;
    top: 50%;
    right: 10px;
    transform: translateY(-50%);
    background-color: transparent;
    border: none;
    cursor: pointer;
    font-size: 16px;
    color: #333;
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