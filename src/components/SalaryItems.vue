<template>
    <div class="container">
        <h2 class="title">Salary Statistics</h2>
        <div v-if="loading">Loading...</div>
        <div v-if="error" class="error">{{ error }}</div>
        <div v-if="!loading && statistics" class="grid">
            <div class="card card-background">
                <div class="card-title">基本工资总额</div>
                <div class="card-value">{{ statistics.totalBaseSalary.toFixed(2) }}元</div>
            </div>
            <div class="card card-background">
                <div class="card-title">绩效工资总额</div>
                <div class="card-value">{{ statistics.totalPerformanceSalary.toFixed(2) }}元</div>
            </div>
            <div class="card card-background">
                <div class="card-title">津贴总额</div>
                <div class="card-value">{{ statistics.totalAllowance.toFixed(2) }}元</div>
            </div>
            <div class="card card-background">
                <div class="card-title">奖金总额</div>
                <div class="card-value">{{ statistics.totalBonus.toFixed(2) }}元</div>
            </div>
            <div class="card card-background">
                <div class="card-title">扣款总额</div>
                <div class="card-value">{{ statistics.totalDeduction.toFixed(2) }}元</div>
            </div>
            <div class="card card-background">
                <div class="card-title">加班费总额</div>
                <div class="card-value">{{ statistics.totalOvertimePay.toFixed(2) }}元</div>
            </div>
            <div class="card card-background">
                <div class="card-title">税费总额</div>
                <div class="card-value">{{ statistics.totalTax.toFixed(2) }}元</div>
            </div>
            <div class="card card-background">
                <div class="card-title">累计发薪</div>
                <div class="card-value">{{ statistics.totalPayroll.toFixed(2) }}元</div>
            </div>
        </div>
    </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
    name: 'SalaryDashboard',
    setup() {
        const statistics = ref({});
        const loading = ref(true);
        const error = ref(null);

        const fetchStatistics = async () => {
            try {
                // 发送 GET 请求以获取薪资统计数据  
                const response = await axios.get('http://localhost:8080/api/salaries/statistics');

                // 将获取到的数据赋值给 statistics 变量  
                statistics.value = response.data;
            } catch (err) {
                // 如果请求失败，设置 error 变量以显示错误信息  
                error.value = 'Failed to fetch statistics';
            } finally {
                // 无论请求成功与否，最终都将 loading 设置为 false  
                loading.value = false;
            }
        };

        onMounted(fetchStatistics);

        return {
            statistics,
            loading,
            error,
        };
    },
};  
</script>

<style scoped>
.container {
    padding: 16px;
}

.title {
    font-size: 1.25rem;
    font-weight: bold;
    margin-bottom: 8px;
}

.error {
    color: red;
}

.grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 16px;
}

.card {
    background-size: cover;
    background-position: center;
    color: white;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    position: relative;
}

.card-title {
    font-size: 1rem;
    position: relative;
    z-index: 1;
    /* 确保文字在图片上方 */
}

.card-value {
    font-size: 1.5rem;
    font-weight: bold;
    position: relative;
    z-index: 1;
    /* 确保文字在图片上方 */
}

/* 公共背景图片类 */
.card-background {
    background-image: url('../../assets/Venice.jpg');
    /* 替换为实际的图片URL */
}
</style>