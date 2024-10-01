<template>  
    <div class="container">  
        <div class="header">  
            <h2 class="title">员工档案</h2>  
            <!-- 新增员工按钮，点击后显示添加员工的表单模态框 -->  
            <button class="add-employee-button" @click="showAddEmployeeModal = true">新增员工</button>  
        </div>  
        <!-- 新增员工模态框 -->  
        <div v-if="showAddEmployeeModal" class="modal-overlay">  
            <div class="modal-content">  
                <!-- 新增员工表单 -->  
                <div class="add-employee-container">  
                    <h2 class="title">添加新员工</h2>  
                    <form @submit.prevent="addEmployee" class="employee-form">  
                        <!-- 姓名输入 -->  
                        <div class="form-group">  
                            <label for="name">姓名:</label>  
                            <input type="text" v-model="employee.name" required class="form-input" />  
                        </div>  
                        <!-- 身份证号输入 -->  
                        <div class="form-group">  
                            <label for="idCard">身份证号:</label>  
                            <input type="text" v-model="employee.idCard" required class="form-input" />  
                        </div>  
                        <!-- 性别选择 -->  
                        <div class="form-group">  
                            <label for="gender">性别:</label>  
                            <select v-model="employee.gender" required class="form-input">  
                                <option value="" disabled>请选择性别</option>  
                                <option value="男">男</option>  
                                <option value="女">女</option>  
                            </select>  
                        </div>  
                        <!-- 身份证地址选择 -->  
                        <div class="form-group">  
                            <label for="address">身份证地址:</label>  
                            <br>  
                            <el-cascader  
                                size="large"  
                                :options="options"  
                                v-model="selectedOptions"  
                                @change="handleAddressChange"  
                                class="form-input"  
                                :props="{ checkStrictly: true }"  
                            ></el-cascader>  
                        </div>  
                        <!-- 部门选择 -->  
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
                        <!-- 岗位选择 -->  
                        <div class="form-group">  
                            <label for="position">岗位:</label>  
                            <select v-model="employee.position" required class="form-input">  
                                <option value="" disabled>请选择岗位</option>  
                                <option v-for="position in positions" :key="position" :value="position">{{ position }}</option>  
                            </select>  
                        </div>  
                        <!-- 工号输入 -->  
                        <div class="form-group">  
                            <label for="employeeId">工号:</label>  
                            <input  
                                type="text"  
                                v-model="employee.employeeId"  
                                required  
                                class="form-input"  
                                @blur="checkEmployeeExists"  
                            />  
                            <!-- 工号存在性提示 -->  
                            <span v-if="employeeExists" class="text-danger">工号已存在</span>  
                            <span v-else-if="employeeExists === false" class="text-success">工号可用</span>  
                        </div>  
                        <!-- 出生日期输入 -->  
                        <div class="form-group">  
                            <label for="birthDate">出生日期:</label>  
                            <input type="date" v-model="employee.birthDate" required class="form-input" />  
                        </div>  
                        <!-- 入职日期输入 -->  
                        <div class="form-group">  
                            <label for="hireDate">入职日期:</label>  
                            <input type="date" v-model="employee.hireDate" required class="form-input" />  
                        </div>  
                        <!-- 联系电话输入 -->  
                        <div class="form-group">  
                            <label for="phone">联系电话:</label>  
                            <input type="text" v-model="employee.phone" required class="form-input" />  
                        </div>  
                        <!-- 状态选择 -->  
                        <div class="form-group">  
                            <label for="status">状态:</label>  
                            <select v-model="employee.status" required class="form-input">  
                                <option value="正式工">正式工</option>  
                                <option value="试用期">试用期</option>  
                                <option value="临时工">临时工</option>  
                            </select>  
                        </div>  
                        <!-- 表单操作按钮 -->  
                        <div class="button-group">  
                            <button type="submit" class="submit-button">添加员工</button>  
                            <button type="button" class="cancel-button" @click="closeAddEmployeeModal">取消</button>  
                        </div>  
                    </form>  
                </div>  
            </div>  
        </div>  
        <!-- 员工表格 -->  
        <table class="employee-table">  
            <thead>  
                <tr>  
                    <th>姓名</th>  
                    <th>身份证号</th>  
                    <th>性别</th>  
                    <th>身份证地址</th>  
                    <th>部门</th>  
                    <th>岗位</th>  
                    <th>工号</th>  
                    <th>出生日期</th>  
                    <th>入职日期</th>  
                    <th>联系电话</th>  
                    <th>状态</th>  
                </tr>  
            </thead>  
            <tbody>  
                <!-- 遍历员工数据 -->  
                <tr v-for="employee in employees" :key="employee.employeeId">  
                    <td>{{ employee.name }}</td>  
                    <td>{{ employee.idCard }}</td>  
                    <td>{{ employee.gender }}</td>  
                    <td>{{ employee.address }}</td>  
                    <td>{{ employee.department }}</td>  
                    <td>{{ employee.position }}</td>  
                    <td>{{ employee.employeeId }}</td>  
                    <td>{{ employee.birthDate }}</td>  
                    <td>{{ employee.hireDate }}</td>  
                    <td>{{ employee.phone }}</td>  
                    <td>{{ employee.status }}</td>  
                </tr>  
            </tbody>  
        </table>  
    </div>  
</template>  

<script>  
import axios from 'axios';  
import { regionData } from 'element-china-area-data';  

export default {  
    name: 'EmployeeRecords',  
    data() {  
        return {  
            // 员工列表  
            employees: [],  
            // 控制新增员工模态框显示  
            showAddEmployeeModal: false,  
            // 新增员工表单数据  
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
            },  
            // 地区选择数据  
            options: regionData,  
            // 选中的地区选项  
            selectedOptions: [],  
            // 岗位选项  
            positions: [],  
            // 部门对应的工号前缀  
            departmentPrefixes: {  
                '人事部': 'HR-',  
                '生产部': 'PRD-',  
                '研发部': 'R&D-',  
                '行政部': 'ADM-',  
                '市场部': 'MKT-',  
                '产品部': 'PDT-',  
                '销售部': 'SAL-',  
                '采购部': 'PUR-'  
            },  
            // 工号是否存在的标志  
            employeeExists: null,  
        };  
    },  
    async mounted() {  
        // 加载员工数据  
        await this.fetchEmployees();  
    },  
    methods: {  
        // 获取员工数据  
        async fetchEmployees() {  
            try {  
                const response = await axios.get('http://localhost:8080/api/employees'); // 替换为实际的API地址  
                console.log(response.data);  
                this.employees = response.data; // 设置员工列表数据  
            } catch (error) {  
                console.error('获取员工数据失败:', error);  
                alert('获取员工数据失败，请检查后重试。');  
            }  
        },  
        // 关闭新增员工模态框  
        closeAddEmployeeModal() {  
            this.showAddEmployeeModal = false;  
            this.resetEmployeeForm();  
        },  
        // 重置新增员工表单  
        resetEmployeeForm() {  
            this.employee = {  
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
            };  
            this.selectedOptions = [];  
            this.positions = [];  
            this.employeeExists = null;  
        },  
        // 添加员工  
        async addEmployee() {  
            // 检查工号是否已存在  
            if (this.employeeExists) {  
                alert('该工号已存在，请使用其他工号。');  
                return;  
            }  
            try {  
                // 发送添加员工的请求  
                const response = await axios.post('http://localhost:8080/api/employees', this.employee);  
                alert('员工添加成功！');  
                // 关闭模态框并重置表单  
                this.closeAddEmployeeModal();  
                // 重新获取员工列表  
                await this.fetchEmployees();  
            } catch (error) {  
                console.error('添加员工失败:', error);  
                alert('添加员工失败，请检查后重试。');  
            }  
        },  
        // 处理地址选择变化  
        handleAddressChange(value) {  
            this.selectedOptions = value;  
            this.employee.address = this.getAddressText(value);  
        },  
        // 获取选中的地址文本  
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
        // 检查工号是否存在  
        async checkEmployeeExists() {  
            try {  
                const response = await axios.get(`http://localhost:8080/api/employees/${this.employee.employeeId}`);  
                this.employeeExists = response.data.exists; // 设置工号存在性标志  
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
/* 容器样式 */  
.container {  
    padding: 20px;  
}  

/* 标题样式 */  
.title {  
    color: #333;  
    margin-bottom: 20px;  
}  

/* 新增员工按钮样式 */  
.add-employee-button {  
    padding: 10px 15px;  
    background-color: #28a745;  
    color: white;  
    border: none;  
    border-radius: 9px;  
    cursor: pointer;  
    margin-bottom: 20px;  
}  

.add-employee-button:hover {  
    background-color: #218838;  
}  

/* 表格样式 */  
.employee-table {  
    width: 100%;  
    border-collapse: collapse;  
}  

.employee-table th,  
.employee-table td {  
    border: 1px solid #ddd;  
    padding: 8px;  
}  

.employee-table th {  
    background-color: #f2f2f2;  
}  

/* 新增员工模态框样式 */  
.modal-overlay {  
    position: fixed;  
    top: 0;  
    left: 0;  
    right: 0;  
    bottom: 0;  
    background-color: rgba(0, 0, 0, 0.5);  
    display: flex;  
    justify-content: center;  
    align-items: center;  
    z-index: 999;  
}  

.modal-content {  
    background-color: #fff;  
    padding: 20px;  
    border-radius: 8px;  
    width: 80%;  
    max-width: 800px;  
}  

/* 新增员工表单样式 */  
.add-employee-container {  
    padding: 20px;  
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
    justify-content: flex-end;  
    margin-top: 5px;  
}  

.submit-button,  
.cancel-button {  
    padding: 10px 15px;  
    border: none;  
    border-radius: 9px;  
    cursor: pointer;  
    margin-left: 10px;  
}  

.submit-button {  
    background-color: #007bff;  
    color: white;  
}  

.submit-button:hover {  
    background-color: #0056b3;  
}  

.cancel-button {  
    background-color: #6c757d;  
    color: white;  
}  

.cancel-button:hover {  
    background-color: #5a6268;  
}  

/* 工号存在性提示样式 */  
.text-danger {  
    color: red;  
}  

.text-success {  
    color: green;  
}  
</style>  