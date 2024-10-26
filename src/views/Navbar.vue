<template>
    <div class="container">
        <!-- 导航栏 -->
        <nav class="navbar">

            <router-link to="/checkIn" class="navbarItem">
                <img src="../../assets/salaryItem.png" alt="">
                考勤打卡签到
            </router-link>
            <!-- <router-link to="/toDoList" class="navbarItem">
                <img src="../../assets/backlog.png"class="icon" />
                待办事项清单
            </router-link> -->
            <router-link to="/salaryDashboard" class="navbarItem">
                <img src="../../assets/SalaryIcon.png"class="icon" />
                企业薪资看板
            </router-link>
            <router-link to="/employeeRecords" class="navbarItem">
                <img src="../../assets/EmployeeFile.png"class="icon" />
                员工档案管理
            </router-link>
            
            <!-- <router-link to="/salaryItems" class="navbarItem">
                <img src="../../assets/salaryItem.png"class="icon" />
                薪资项
            </router-link> -->
            <router-link to="/Payslip" class="navbarItem">
                <img src="../../assets/salarypay.png"class="icon" />
                员工薪资发放
            </router-link>
            <router-link to="/salaryPaymentRecord" class="navbarItem">
                <img src="../../assets/record.png">
                薪资发放记录
            </router-link>
            <!-- <router-link to="/problemFeedback" class="navbarItem">
                <img src="../../assets/feedback.png"class="icon" />
                问题反馈表单
            </router-link> -->
        </nav>

        <!-- 模态框 -->
        <div v-if="isModalVisible" class="modal">
            <div class="modal-content">
                <span class="close" @click="closeModal">&times;</span>
                <!-- 只在模态框中显示 AddEmployee -->
            </div>
        </div>
    </div>
</template>

<script>

export default {
    name: 'Navbar',
    
    data() {
        return {
            isModalVisible: false // 控制模态框的显示  
        };
    },
    methods: {
        showAddEmployeeModal() {
            this.isModalVisible = true; // 显示模态框  
        },
        closeModal() {
            this.isModalVisible = false; // 关闭模态框  
        }
    }
}
</script>

<style>
.navbar {
    width: 333px;
    height: 100%;
    background-color: #d7d7d7;

}

.navbar img {
    width: 25px;
    height: 25px;
    margin: 5px 10px; 
}

.navbarItem {  
    display: flex; /* 使用 Flexbox */  
    align-items: center; /* 垂直居中对齐 */  
    font-size: 22px;  
    text-decoration: none;  
    border-radius: 6px;  
    color: #000;  
    padding: 10px; /* 添加一些内边距以增加点击区域 */  
} 

/* 让正在显示的路由高亮 */
.router-link-active {
    /* 高亮背景色 */
    background-color: #485f77;
    /* 高亮文字颜色 */
    color: white;
}

/* 模态框样式 */
.modal {
    display: flex;
    position: fixed;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0, 0, 0, 0.3);
}

.modal-content {
    background-color: #fefefe;
    margin: 0.3% auto;
    padding-top: 20px;
    /* 上下各0.8%的间距 */
    border: 1px solid #888;
    width: 50%;
    /* 模态框大小 */
}

.close {
    color: #aaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: black;
    text-decoration: none;
    cursor: pointer;
}
</style>