<template>  
    <div class="container">  
        <div class="header">  
            <img src="../../assets/background.png" alt="">  
            <h2>薪资看板</h2>  
        </div>  
        <div class="payroll">  
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
        <!-- 添加薪资总额柱形图 -->  
        <div class="chart-container">  
            <h3 class="chart-title">薪资总额柱形图</h3>  
            <canvas ref="barChart"></canvas>  
            <p>此图表展示了各类薪资的总额，有助于更好地理解薪资结构。</p>  
        </div>  
    </div>  
</template>  

<script>  
import { ref, onMounted, onBeforeUnmount } from 'vue';  
import axios from 'axios';  
import { Chart, registerables } from 'chart.js';  

Chart.register(...registerables);  

export default {  
    name: 'SalaryDashboard',  
    setup() {  
        const statistics = ref({});  
        const loading = ref(true);  
        const error = ref(null);  
        const barChart = ref(null);  

        const fetchStatistics = async () => {  
            try {  
                const response = await axios.get('http://localhost:8080/api/salaries/statistics');  
                statistics.value = response.data;  
                console.log(statistics.value); // 调试信息  
                createChart(); // 创建图表  
            } catch (err) {  
                error.value = 'Failed to fetch statistics';  
                console.error(err); // 打印错误信息  
            } finally {  
                loading.value = false;  
            }  
        };  

        const createChart = () => {  
            const ctx = barChart.value.getContext('2d');  
            if (barChart.value) {  
                // 创建图表并将实例赋值给 barChart.value  
                barChart.value = new Chart(ctx, {  
                    type: 'bar',  
                    data: {  
                        labels: ['基本工资', '绩效工资', '津贴', '奖金', '扣款', '加班费', '税费'],  
                        datasets: [{  
                            label: '薪资总额',  
                            data: [  
                                statistics.value.totalBaseSalary || 0,  
                                statistics.value.totalPerformanceSalary || 0,  
                                statistics.value.totalAllowance || 0,  
                                statistics.value.totalBonus || 0,  
                                statistics.value.totalDeduction || 0,  
                                statistics.value.totalOvertimePay || 0,  
                                statistics.value.totalTax || 0,  
                            ],  
                            backgroundColor: 'rgba(75, 192, 192, 0.6)',  
                        }],  
                    },  
                    options: {  
                        responsive: true,  
                        scales: {  
                            y: {  
                                beginAtZero: true  
                            }  
                        }  
                    }  
                });  
            }  
        };  

        onMounted(fetchStatistics);  
        onBeforeUnmount(() => {  
            if (barChart.value && typeof barChart.value.destroy === 'function') {  
                barChart.value.destroy(); // 销毁图表  
            }  
        });  

        return {  
            statistics,  
            loading,  
            error,  
            barChart,  
        };  
    },  
};  
</script>  

<style scoped>  
.container {  
    width: 100%;  
    height: 100vh;  
    background-color: #f4f6fc;  
}  

.header {  
    display: inline-block;  
    position: relative;  
    margin-left: 30px;  
    margin-top: 20px;  
}  

.header img {  
    width: 98%;  
    height: 100px;  
    border-radius: 15px;  
}  

.header h2 {  
    position: absolute;  
    top: 19px;  
    left: 520px;  
    color: white;  
    font-size: 50px;  
    letter-spacing: 12px;  
}  

.title {  
    font-size: 1.25rem;  
    font-weight: bold;  
    margin-bottom: 8px;  
}   

.payroll {  
    margin: 20px 30px;  
}   

.error {  
    color: red;  
}  

.grid {  
    display: grid;  
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));   
    gap: 10px;  
}  

.card {  
    display: flex;  
    flex-direction: column;  
    justify-content: center;  
    align-items: center;  
    background-size: cover;  
    background-position: center;  
    color: white;  
    padding: 30px;  
    border-radius: 8px;  
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);  
    position: relative;  
    height: 140px;  
}   

.card-title {  
    font-size: 1.5rem;  
    position: relative;  
    z-index: 1;  
}  

.card-value {  
    font-size: 2rem;  
    font-weight: bold;  
    position: relative;  
    z-index: 1;  
}  

.card-background {  
    background-image: url('../../assets/Venice.jpg');  
}  

.chart-container {  
    margin: 20px 30px;  
    text-align: center;  
}  

.chart-title {  
    font-size: 1.5rem;  
    margin-bottom: 10px;  
}  
</style>