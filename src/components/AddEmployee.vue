<template>
    <div class="add-employee-container">
        <h2 class="title">添加新员工</h2>
        <form @submit.prevent="addEmployee" class="employee-form">
            <div class="form-group">
                <label for="name">姓名:</label>
                <input type="text" v-model="employee.name" required class="form-input" />
            </div>
            <div>
                <label for="idCard">身份证号</label>
                <input type="text" v-model="employee.idCard" required class="form-input">
            </div>
            <div class="form-group">
                <label for="gender">性别</label>
                <select v-model="employee.gender" required class="form-input">
                    <option value="" disabled>请选择性别</option>
                    <option value="男">男</option>
                    <option value="女">女</option>
                </select>
            </div>

            <div class="form-group">
                <label for="address">身份证地址</label><br>
                <el-cascader size="large" :options="options" v-model="selectedOptions" @change="handleAddressChange"
                    class="form-input" :props="{ checkStrictly: true }">
                </el-cascader>
            </div>
            <div class="form-group">
                <label for="department">部门:</label>
                <select v-model="employee.department" required class="form-input">
                    <option value="" disabled>请选择部门</option>
                    <option value="生产部">生产部</option>
                    <option value="研发部">研发部</option>
                    <option value="行政部">行政部</option>
                    <option value="人事部">人事部</option>
                    <option value="市场部">市场部</option>
                    <option value="产品部">产品部</option>
                    <option value="销售部">销售部</option>
                    <option value="采购部">采购部</option>
                </select>
            </div>
            <div class="form-group">
                <label for="position">岗位:</label>
                <input type="text" v-model="employee.position" required class="form-input" />
            </div>
            <div class="form-group">
                <label for="employeeId">工号:</label>
                <input type="text" v-model="employee.employeeId" required class="form-input" />
            </div>
            <div class="form-group">
                <label for="birthDate">出生日期:</label>
                <input type="date" v-model="employee.birthDate" required class="form-input" />
            </div>
            <div class="form-group">
                <label for="hireDate">入职日期:</label>
                <input type="date" v-model="employee.hireDate" required class="form-input" />
            </div>
            <div class="form-group">
                <label for="phone">联系电话:</label>
                <input type="text" v-model="employee.phone" required class="form-input" />
            </div>
            <div class="form-group">
                <label for="status">状态:</label>
                <select v-model="employee.status" required class="form-input">
                    <option value="在职">在职</option>
                </select>
            </div>

            <div class="button-group">
                <button type="submit" class="submit-button">添加员工</button>
            </div>
        </form>
    </div>
</template>

<script>
import axios from 'axios';
import { regionData } from 'element-china-area-data';

export default {
    name: 'AddEmployee',
    data() {
        return {
            employee: {
                name: '',
                department: '',
                employeeId: '',
                position: '',
                birthDate: '',
                hireDate: '',
                phone: '',
                status: '',
                idCard: '',
                gender: '',
                address: ''
            },
            options: regionData,
            selectedOptions: []
        };
    },
    methods: {
        async addEmployee() {
            try {
                const response = await axios.post('http://localhost:8080/api/employees', this.employee);
                alert('员工添加成功！');
                // 清空表单  
                this.employee = {
                    name: '',
                    department: '',
                    position: '',
                    employeesId: '',
                    birthDate: '',
                    hireDate: '',
                    phone: '',
                    status: '',
                    idCard: '',
                    gender: '',
                    address: ''
                };
                this.selectedOptions = [];
                this.$emit('close'); // 关闭模态框  
            } catch (error) {
                console.error('添加员工失败:', error);
                alert('添加员工失败，请检查后重试。');
            }
        },
        handleAddressChange(value) {
            this.selectedOptions = value;
            this.employee.address = this.getAddressText(value);
        },
        getAddressText(value) {
            let area = '';
            if (value.length === 1) {
                area = this.options.find(item => item.value === value[0]).label;
            } else if (value.length === 2) {
                const province = this.options.find(item => item.value === value[0]);
                const city = province.children.find(item => item.value === value[1]);
                area = `${province.label}/${city.label}`;
            } else if (value.length === 3) {
                const province = this.options.find(item => item.value === value[0]);
                const city = province.children.find(item => item.value === value[1]);
                const district = city.children.find(item => item.value === value[2]);
                area = `${province.label}/${city.label}/${district.label}`;
            }
            return area;
        }
    }
};  
</script>

<style scoped>
.add-employee-container {
    padding: 0px 100px;
    background-color: #fefefe;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.title {
    color: #333;
    margin-bottom: 20px;
}

.employee-form {
    display: flex;
    flex-direction: column;
}

.form-group {
    margin-bottom: 15px;
}

label {
    font-weight: bold;
    margin-bottom: 5px;
}

.form-input {
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    width: 100%;
    box-sizing: border-box;
}

.button-group {
    display: flex;
    justify-content: space-between;
}

.submit-button {
    padding: 10px 15px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 9px;
    cursor: pointer;
}

.submit-button:hover {
    background-color: #0056b3;
}

.cancel-button:hover {
    background-color: #aaa;
}
</style>