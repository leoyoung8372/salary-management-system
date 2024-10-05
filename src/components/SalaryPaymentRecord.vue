<template>
    <div class="container">
        <!-- 提示信息组件，用于显示操作成功或错误的信息 -->
        <transition name="fade">
            <div v-if="showNotification" class="notification">
                {{ notificationMessage }}
            </div>
        </transition>

        <!-- 页面标题 -->
        <h2>员工薪资记录</h2>

        <!-- PDF 导出和输入区域 -->
        <div class="pdf">
            <div class="flex items-center mb-4">
                <!-- 员工ID输入框 -->
                <div class="rubric">
                    <label for="employeeId">员工ID：</label>
                    <input id="employeeId" v-model="employeeId" type="text" class="border rounded px-2 py-1 mr-4" />
                </div>

                <!-- 薪资日期输入框 -->
                <div class="rubric">
                    <label for="salaryDate">薪资日期：</label>
                    <input id="salaryDate" v-model="salaryDate" type="text" placeholder="YYYY-MM-DD"
                        class="border rounded px-2 py-1 mr-4" />
                </div>

                <!-- 清空输入内容按钮 -->
                <button @click="clearInputs" class="textClean">清空输入内容</button>

                <!-- 导出PDF按钮 -->
                <button @click="downloadPayrollPdf" class="pdfButton">导出PDF文件</button>
            </div>
        </div>

        <!-- 薪资记录表格 -->
        <div class="table-container">
            <table class="salary-table">
                <thead>
                    <tr>
                        <th>员工ID</th>
                        <th>员工姓名</th>
                        <th>手机号</th>
                        <!-- 表头带有排序功能的列 -->
                        <th>
                            基础工资
                            <!-- 排序按钮 -->
                            <button @click="sortBy('baseSalary')" class="sort-btn">
                                <span v-if="sortColumn === 'baseSalary' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'baseSalary' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <!-- 其他可排序的列，同上 -->
                        <th>
                            绩效工资
                            <button @click="sortBy('performanceSalary')" class="sort-btn">
                                <span v-if="sortColumn === 'performanceSalary' && sortDirection === 'asc'">▲</span>
                                <span
                                    v-else-if="sortColumn === 'performanceSalary' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>
                            津贴
                            <button @click="sortBy('allowance')" class="sort-btn">
                                <span v-if="sortColumn === 'allowance' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'allowance' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>
                            奖金
                            <button @click="sortBy('bonus')" class="sort-btn">
                                <span v-if="sortColumn === 'bonus' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'bonus' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>
                            扣除
                            <button @click="sortBy('deduction')" class="sort-btn">
                                <span v-if="sortColumn === 'deduction' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'deduction' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>
                            加班费
                            <button @click="sortBy('overtimePay')" class="sort-btn">
                                <span v-if="sortColumn === 'overtimePay' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'overtimePay' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>
                            工资日期
                            <button @click="sortBy('salaryDate')" class="sort-btn">
                                <span v-if="sortColumn === 'salaryDate' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'salaryDate' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>
                            税费
                            <button @click="sortBy('tax')" class="sort-btn">
                                <span v-if="sortColumn === 'tax' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'tax' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                        <th>
                            总工资
                            <button @click="sortBy('totalPayroll')" class="sort-btn">
                                <span v-if="sortColumn === 'totalPayroll' && sortDirection === 'asc'">▲</span>
                                <span v-else-if="sortColumn === 'totalPayroll' && sortDirection === 'desc'">▼</span>
                                <span v-else>⇵</span>
                            </button>
                        </th>
                    </tr>
                </thead>
                <tbody>
                    <!-- 使用 v-for 迭代薪资记录 -->
                    <tr v-for="(record, index) in salaryRecords" :key="record.employee_id"
                        :class="{ 'bg-white': index % 2 === 0, 'bg-gray-200': index % 2 !== 0 }"
                        :style="{ animationDelay: (index * 0.033) + 's' }">
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
        // 定义响应式变量
        const salaryRecords = ref([]); // 薪资记录列表
        const loading = ref(false); // 加载状态
        const error = ref(null); // 错误信息
        const sortColumn = ref(null); // 当前排序列
        const sortDirection = ref(null); // 当前排序方向
        const employeeId = ref(''); // 员工ID输入值
        const salaryDate = ref(''); // 薪资日期输入值
        const showNotification = ref(false); // 是否显示通知
        const notificationMessage = ref(''); // 通知消息

        // 获取薪资记录数据
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

        // 格式化显示日期
        const formatDate = (dateString) => {
            const options = { year: 'numeric', month: '2-digit', day: '2-digit' };
            const date = new Date(dateString);
            return date.toLocaleDateString('zh-CN', options);
        };

        // 格式化请求中的日期参数
        const formatDateForRequest = (date) => {
            if (!date) return '';
            const d = new Date(date);
            const year = d.getFullYear();
            const month = String(d.getMonth() + 1).padStart(2, '0');
            const day = String(d.getDate()).padStart(2, '0');
            return `${year}-${month}-${day}`;
        };

        // 清空输入的员工ID和薪资日期
        const clearInputs = () => {
            employeeId.value = '';
            salaryDate.value = '';

            // 显示清除成功的通知
            notificationMessage.value = '清除成功';
            showNotification.value = true;

            // 一秒后隐藏通知
            setTimeout(() => {
                showNotification.value = false;
            }, 1000);
        };

        // 排序函数
        const sortBy = (column) => {
            // 如果已经按该列排序，则切换升降序
            if (sortColumn.value === column) {
                sortDirection.value = sortDirection.value === 'asc' ? 'desc' : 'asc';
            } else {
                // 否则设置新的排序列和升序
                sortColumn.value = column;
                sortDirection.value = 'asc';
            }

            // 对薪资记录进行排序
            salaryRecords.value.sort((a, b) => {
                let valueA = a[column];
                let valueB = b[column];

                // 如果是日期列，需转换为日期对象
                if (column === 'salaryDate') {
                    valueA = new Date(valueA);
                    valueB = new Date(valueB);
                }

                if (valueA < valueB) return sortDirection.value === 'asc' ? -1 : 1;
                if (valueA > valueB) return sortDirection.value === 'asc' ? 1 : -1;
                return 0;
            });
        };

        // 下载工资单PDF
        const downloadPayrollPdf = () => {
            console.log('employeeId:', employeeId.value);
            console.log('salaryDate:', salaryDate.value);

            const formattedDate = formatDateForRequest(salaryDate.value);

            axios({
                method: 'get',
                url: `http://localhost:8080/api/payroll/pdf?employeeId=${employeeId.value}&salaryDate=${formattedDate}`,
                responseType: 'blob',
                validateStatus: function (status) {
                    // 接受所有状态码以在后续处理
                    return true;
                },
            })
                .then((response) => {
                    if (response.status === 200) {
                        // 请求成功，处理PDF文件下载
                        const url = URL.createObjectURL(response.data);

                        // 从响应头中获取文件名
                        const contentDisposition = response.headers['content-disposition'];
                        let fileName = 'payroll.pdf';
                        if (contentDisposition) {
                            const fileNameMatch = contentDisposition.match(
                                /filename\*=UTF-8''(.+)|filename="(.+)"|filename=(.+)/
                            );
                            if (fileNameMatch) {
                                fileName = decodeURIComponent(
                                    fileNameMatch[1] || fileNameMatch[2] || fileNameMatch[3]
                                );
                            }
                        }

                        // 创建下载链接并触发下载
                        const link = document.createElement('a');
                        link.href = url;
                        link.setAttribute('download', fileName);
                        document.body.appendChild(link);
                        link.click();
                        document.body.removeChild(link);
                    } else if (response.status === 404) {
                        // 处理未找到记录的情况
                        alert('不存在该薪资记录。');
                    } else {
                        // 处理其他错误
                        alert('下载 PDF 时发生错误。');
                    }
                })
                .catch((error) => {
                    console.error(error);
                    alert('发生了网络错误或未知错误。');
                });
        };

        // 组件挂载时获取薪资记录数据
        onMounted(fetchSalaryRecords);

        return {
            salaryRecords,
            loading,
            error,
            formatDate,
            sortColumn,
            sortDirection,
            sortBy,
            employeeId,
            salaryDate,
            downloadPayrollPdf,
            clearInputs,
            showNotification,
            notificationMessage,
        };
    },
};
</script>

<style scoped>
/* 容器样式 */
.container {
    padding: 20px;
}

/* 表格容器，设置最大高度和滚动 */
.table-container {
    max-height: 860px;
    overflow-y: auto;
    border-radius: 4px;
}

/* 标题样式 */
h2 {
    display: flex;
    height: 100px;
    margin-bottom: 20px;
    font-size: 40px;
    background-color: #4eab9d;
    color: white;
    justify-content: center;
    align-items: center;
}

/* 输入区域的样式 */
.rubric {
    display: flex;
    background-color: white;
    width: 400px;
    height: 80px;
    flex-direction: column;
    margin-right: 20px;
    padding: 0 22px;
}

/* 标签样式 */
.rubric label {
    padding: 10px;
    font-family: Roboto;
    font-weight: 300;
}

/* 输入框样式 */
.rubric input {
    border: 2px solid #e9e9e9;
    font-family: Roboto;
    font-weight: 300;
}

/* 输入框聚焦样式 */
input:focus {
    border-color: #52b0a6;
    outline: none;
}

/* 清空按钮样式 */
.textClean {
    width: 234px;
    height: 80px;
    background-color: #cf1c1c;
    font-weight: 600;
    color: white;
    border: none;
}

.textClean:hover {
    background-color: #9d1616;
}

/* 导出PDF按钮样式 */
.pdfButton {
    width: 222px;
    height: 80px;
    background-color: #52b0a6;
    font-weight: 600;
    color: white;
    border: none;
}

.pdfButton:hover {
    background-color: #3f8b83;
}

/* 表格样式 */
.salary-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 14px;
    font-family: FZXiHei;
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
}

.salary-table tr:nth-child(odd) {
    background-color: #ffffff;
}

/* 通知消息样式 */
.notification {
    position: fixed;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    background-color: #4CAF50;
    color: white;
    padding: 16px;
    border-radius: 4px;
    z-index: 1000;
}

/* 淡入淡出动画 */
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.5s;
}

.fade-enter,
.fade-leave-to {
    opacity: 0;
}


/* 表格动画效果 */
@keyframes slideIn {  
    from {  
        transform: translateX(-100%);  
        opacity: 0;  
    }  
    to {  
        transform: translateX(0);  
        opacity: 1;  
    }  
}  

.table-container {  
    overflow: hidden; /* 确保动画效果不超出容器 */  
}  

.salary-table tr {  
    opacity: 0; /* 初始透明度为0 */  
    animation: slideIn 0.3s ease forwards; /* 应用动画 */  
}
</style>