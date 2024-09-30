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
                <select v-model="employee.department" @change="updatePositions" required class="form-input">
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
                <select v-model="employee.position" required class="form-input">
                    <option value="" disabled>请选择岗位</option>
                    <option v-for="position in positions" :key="position" :value="position">{{ position }}</option>
                </select>
            </div>
            <div class="form-group">
                <label for="employeeId">工号:</label>
                <input type="text" v-model="employee.employeeId" required class="form-input"
                    @blur="checkEmployeeExists" />
                <span v-if="employeeExists" class="text-success">工号已存在</span>
                <span v-if="employeeExists === false" class="text-danger">工号不存在</span>
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
                    <option value="试用期">试用期</option>
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
                address: '',
                employeeExists: null, 
            },
            options: regionData,
            selectedOptions: [],
            positions: [], // 用于存储岗位选项  
            departmentPrefixes: {
                '人事部': 'HR-',
                '生产部': 'PRD-',
                '研发部': 'R&D-',
                '行政部': 'ADM-',
                '市场部': 'MKT-',
                '产品部': 'PDT-',
                '销售部': 'SAL-',
                '采购部': 'PUR-'
            }
        };
    },
    methods: {
        async addEmployee() {  
    // 检查工号是否存在  
    if (this.employeeExists === true) {  
        alert('该工号已存在，请使用其他工号。');  
        return; // 阻止提交  
    }  

    try {  
        const response = await axios.post('http://localhost:8080/api/employees', this.employee);  
        alert('员工添加成功！');  
        // 清空表单  
        this.employee = {  
            name: '',  
            department: '',  
            position: '',  
            employeeId: '',  
            birthDate: '',  
            hireDate: '',  
            phone: '',  
            status: '',  
            idCard: '',  
            gender: '',  
            address: ''  
        };  
        this.selectedOptions = [];  
        this.positions = []; // 清空岗位选项  
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
        },
        async checkEmployeeExists() {
            try {
                const response = await axios.get(`http://localhost:8080/api/employees/${this.employee.employeeId}`);
                this.employeeExists = response.data.exists; // 根据返回结果设置状态  
            } catch (error) {
                console.error('检查工号存在性失败:', error);
                this.employeeExists = false; // 处理错误情况  
            }
        },
        // 更新岗位选项和工号前缀  
        updatePositions() {
            switch (this.employee.department) {
                case '销售部':
                    this.positions = ['销售经理', '销售代表', '客户经理'];
                    break;
                case '产品部':
                    this.positions = ['产品经理', '产品专员', '用户体验设计师'];
                    break;
                case '研发部':
                    this.positions = ['研发经理', '软件工程师', '测试工程师'];
                    break;
                case '生产部':
                    this.positions = ['生产经理', '生产工人', '质量检验员'];
                    break;
                case '行政部':
                    this.positions = ['行政经理', '行政助理', '前台接待'];
                    break;
                case '人事部':
                    this.positions = ['人事经理', '招聘专员', '培训专员'];
                    break;
                case '市场部':
                    this.positions = ['市场经理', '市场专员', '品牌经理'];
                    break;
                case '采购部':
                    this.positions = ['采购经理', '采购专员', '供应链管理专员'];
                    break;
                default:
                    this.positions = [];
            }

            // 设置工号前缀  
            this.employee.employeeId = this.departmentPrefixes[this.employee.department] || '';
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