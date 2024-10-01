
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
      <!-- 添加薪资总额柱形图和饼状图 -->
      <div class="chart-container">
        <h3 class="chart-title">薪资总额图表</h3>
        <div class="chart-wrapper">
          <!-- 柱形图 -->
          <div class="chart-item">
            <canvas ref="barChart"></canvas>
            <p style="color: gray;">柱形图展示了各类薪资的总额。</p>
          </div>
          <!-- 饼状图 -->
          <div class="chart-item">
            <canvas ref="pieChart"></canvas>
            <p style="color: gray;">饼状图展示了各类薪资的比例。</p>
          </div>
        </div>
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
      // 柱形图和饼状图的引用
      const barChart = ref(null);
      const pieChart = ref(null);
      // 图表实例
      let barChartInstance = null;
      let pieChartInstance = null;
  
      // 获取统计数据
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
  
      // 创建图表
      const createChart = () => {
        const labels = ['基本工资', '绩效工资', '津贴', '奖金', '扣款', '加班费', '税费'];
        const dataValues = [
          statistics.value.totalBaseSalary || 0,
          statistics.value.totalPerformanceSalary || 0,
          statistics.value.totalAllowance || 0,
          statistics.value.totalBonus || 0,
          statistics.value.totalDeduction || 0,
          statistics.value.totalOvertimePay || 0,
          statistics.value.totalTax || 0,
        ];
  
        // 创建柱形图
        if (barChart.value) {
          const ctxBar = barChart.value.getContext('2d');
          barChartInstance = new Chart(ctxBar, {
            type: 'bar',
            data: {
              labels: labels,
              datasets: [{
                label: '薪资总额',
                data: dataValues,
                backgroundColor: 'rgba(75, 192, 192, 0.6)',
              }],
            },
            options: {
              responsive: true,
              maintainAspectRatio: false,
              scales: {
                y: {
                  beginAtZero: true,
                },
              },
            },
          });
        }
  
        // 创建饼状图
        if (pieChart.value) {
          const ctxPie = pieChart.value.getContext('2d');
          pieChartInstance = new Chart(ctxPie, {
            type: 'pie',
            data: {
              labels: labels,
              datasets: [{
                data: dataValues,
                backgroundColor: [
                  '#FF6384',
                  '#36A2EB',
                  '#FFCE56',
                  '#4BC0C0',
                  '#9966FF',
                  '#FF9F40',
                  '#C9CBCF',
                ],
              }],
            },
            options: {
              responsive: true,
              maintainAspectRatio: false,
              plugins: {
                tooltip: {
                  callbacks: {
                    label: function(context) {
                      let label = context.label || '';
                      let value = context.parsed;
                      let data = context.chart.data.datasets[0].data;
                      let total = data.reduce((accumulator, currentValue) => accumulator + currentValue, 0);
                      let percentage = ((value / total) * 100).toFixed(2) + '%';
                      return label + ': ' + percentage;
                    }
                  }
                }
              }
            },
          });
        }
      };
  
      onMounted(fetchStatistics);
  
      onBeforeUnmount(() => {
        // 销毁柱形图实例
        if (barChartInstance) {
          barChartInstance.destroy();
        }
        // 销毁饼状图实例
        if (pieChartInstance) {
          pieChartInstance.destroy();
        }
      });
  
      return {
        statistics,
        loading,
        error,
        barChart,
        pieChart,
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
  
  /* 图表容器样式 */
  .chart-container {
    margin: 20px 30px;
  }
  
  .chart-title {
    font-size: 1.5rem;
    margin-bottom: 10px;
    text-align: center;
  }
  
  /* 图表包裹容器，使用Flex布局 */
  .chart-wrapper {
    display: flex;
    justify-content: space-around;
    align-items: center;
  }
  
  /* 单个图表项样式 */
  .chart-item {
    width: 45%;
    height: 400px;
    position: relative;
  }
  
  .chart-item canvas {
    width: 100%;
    height: 100%;
  }
  
  .chart-item p {
    text-align: center;
    color: gray;
  }
  </style>
  