<template>
    <div class="p-4">
        <h2 class="text-lg font-bold mb-2">Salary Statistics</h2>
        <div v-if="loading">Loading...</div>
        <div v-if="error" class="text-red-500">{{ error }}</div>
        <div v-if="!loading && statistics">
            <ul>
                <li>基本工资总额: {{ statistics.totalBaseSalary.toFixed(2) }}</li>
                <li>绩效工资总额: {{ statistics.totalPerformanceSalary.toFixed(2) }}</li>
                <li>津贴总额: {{ statistics.totalAllowance.toFixed(2) }}</li>
                <li>奖金总额: {{ statistics.totalBonus.toFixed(2) }}</li>
                <li>扣款总额: {{ statistics.totalDeduction.toFixed(2) }}</li>
                <li>加班费总额: {{ statistics.totalOvertimePay.toFixed(2) }}</li>
                <li>税费总额: {{ statistics.totalTax.toFixed(2) }}</li>
                <li>总薪资: {{ statistics.totalPayroll.toFixed(2) }}</li>
            </ul>
        </div>
    </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import axios from 'axios';

export default {
    name: 'SalaryStatistics',
    setup() {
        const statistics = ref({});
        const loading = ref(true);
        const error = ref(null);

        const fetchStatistics = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/salaries/statistics');
                statistics.value = response.data;
            } catch (err) {
                error.value = 'Failed to fetch statistics';
            } finally {
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
/* Add any additional styles here */
</style>