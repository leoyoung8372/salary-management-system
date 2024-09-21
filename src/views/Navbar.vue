<template>
    <div class="container">
        <!-- 导航栏 -->
        <nav class="navbar">
            <router-link to="#" @click.prevent="showAddEmployeeModal">新增员工</router-link>
            <router-link to="/employeeRecords" class="navbarItem">员工档案</router-link>
            <router-link to="/salaryDashboard" class="navbarItem">薪资看板</router-link>
            <router-link to="/salaryDetails" class="navbarItem">工资明细</router-link>
            <router-link to="/salaryItems" class="navbarItem">薪资项</router-link>
        </nav>

        <!-- 模态框 -->
        <div v-if="isModalVisible" class="modal">
            <div class="modal-content">
                <span class="close" @click="closeModal">&times;</span>
                <AddEmployee @close="closeModal" /> <!-- 只在模态框中显示 AddEmployee -->
            </div>
        </div>
    </div>
</template>

<script>
import AddEmployee from '@/components/AddEmployee.vue'; // 确保引入在这里  

export default {
    name: 'Navbar',
    components: {
        AddEmployee // 注册 AddEmployee 组件  
    },
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
    background-color: #aba6a6;
}

.navbarItem {
    display: block;
    font-size: 22px;
    text-decoration: none;
}

/* 让正在显示的路由高亮 */
.router-link-active {
    /* 高亮背景色 */
    background-color: #007bff;
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