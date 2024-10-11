<template>
    <div :class="['container', $attrs.class]">
        <!-- 标题 -->
        <div class="header">
            <h2>员工档案管理</h2>
        </div>

        <div class="tools">
            <!-- 新增员工按钮 -->
            <button class="employee-button add" @click="showAddEmployeeModal = true">新增员工</button>
            <button class="employee-button edit" @click="handleEditEmployee">修改员工</button>
            <button class="employee-button delete" @click="deleteEmployee">删除员工</button>
            <!-- 搜索框 -->
            <input class="searchBox" type="search" placeholder="请输入搜索内容..." v-model="searchQuery" />
            <!-- 搜索按钮 -->
            <button @click="searchEmployees" class="searchBotton">搜索</button>
        </div>

        <!-- 新增员工模态框 -->
        <div v-if="showAddEmployeeModal" class="modal-overlay">
            <div class="modal-content">
                <div class="add-employee-container">
                    <h2 style="padding-bottom: 16px;">添加员工</h2>
                    <form class="employee-form" @submit.prevent="addEmployee">
                        <!-- 姓名输入框 -->
                        <div class="form-group">
                            <label for="name">姓名:</label>
                            <input type="text" v-model="employee.name" required class="form-input" />
                        </div>
                        <!-- 身份证号输入框 -->
                        <div class="form-group">
                            <label for="idCard">身份证号:</label>
                            <input type="text" v-model="employee.idCard" @input="handleIdCardInput" required
                                class="form-input" />
                            <!-- 身份证号长度提示 -->
                            <span :class="idCardExceeded ? 'text-danger' : 'text-lightgray'">{{ idCardLength
                                }}/18</span>
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
                            <el-cascader size="large" :options="options" v-model="selectedOptions"
                                @change="handleAddressChange" class="form-input"
                                :props="{ checkStrictly: true }"></el-cascader>
                        </div>
                        <!-- 部门选择 -->
                        <div class="form-group">
                            <label for="department">部门:</label>
                            <select v-model="employee.department"
                                @change="updatePositions(employee.department, employee)" required class="form-input">
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
                                <option v-for="position in positions" :key="position" :value="position">{{ position }}
                                </option>
                            </select>
                        </div>
                        <!-- 工号输入框 -->
                        <div class="form-group">
                            <label for="employeeId">工号:</label>
                            <span style="color: gray; font-size: small; font-weight: 100;">格式示例：SAL-001</span>
                            <input type="text" v-model="employee.employeeId" required class="form-input"
                                @blur="checkEmployeeExists" />
                            <!-- 工号存在性提示 -->
                            <span v-if="employeeExists" class="text-danger">工号已存在</span>
                            <span v-else-if="employeeExists === false" class="text-success">工号可用</span>
                        </div>
                        <!-- 出生日期输入框 -->
                        <div class="form-group">
                            <label for="birthDate">出生日期:</label><br>
                            <input type="date" v-model="employee.birthDate" required class="form-input" />
                        </div>
                        <!-- 入职日期输入框 -->
                        <div class="form-group">
                            <label for="hireDate">入职日期:</label>
                            <input type="date" v-model="employee.hireDate" required class="form-input" />
                        </div>
                        <!-- 联系电话输入框 -->
                        <div class="form-group">
                            <label for="phone">联系电话:</label>
                            <input type="number" v-model="employee.phone" required class="form-input" />
                        </div>
                        <!-- 员工状态选择 -->
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

        <!-- 员工列表表格 -->
        <table class="employee-table animate-in">
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
                    <th>选择</th>
                </tr>
            </thead>
            <tbody>
                <!-- 迭代显示员工信息 -->
                <tr v-for="employee in filteredEmployees" :key="employee.employeeId">
                    <td>{{ employee.name }}</td>
                    <td>{{ employee.idCard }}</td>
                    <td>{{ employee.gender }}</td>
                    <td>{{ truncateText(employee.address, 12) }}</td>
                    <td>{{ employee.department }}</td>
                    <td>{{ employee.position }}</td>
                    <td>{{ employee.employeeId }}</td>
                    <td>{{ employee.birthDate }}</td>
                    <td>{{ employee.hireDate }}</td>
                    <td>{{ employee.phone }}</td>
                    <td>{{ employee.status }}</td>
                    <td style="text-align: center;">
                        <!-- 选择员工的复选框 -->
                        <input type="checkbox" :checked="employee.checked" @change="toggleEmployeeSelection(employee)"
                            style="accent-color: #ff0000; transform: scale(1.5);" />
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <!-- 编辑员工模态框 -->
    <div v-if="showEditEmployeeModal" class="modal-overlay">
        <div class="modal-content">
            <div class="edit-employee-container">
                <h2 style="padding-bottom: 16px;">编辑员工信息</h2>
                <form class="employee-form" @submit.prevent="updateEmployee">
                    <div v-for="(value, key) in editingEmployee" :key="key" class="form-group">
                        <!-- address 字段 被排除-->
                        <template v-if="key !== 'address'">
                            <label :for="key">
                                <!-- 检查是否为工号字段 -->
                                <span v-if="key === 'employeeId'"><span style="color: red;">*</span>确认工号:<span
                                        style="color: gray; font-weight: 100;font-size: small;">错误的工号会导致更新失败</span></span>
                                <span v-else>{{ getFieldLabel(key) }}:</span>
                            </label>

                            <template
                                v-if="key !== 'department' && key !== 'position' && key !== 'gender' && key !== 'status'">
                                <input :type="getInputType(key)" v-model="editingEmployee[key]" :id="key"
                                    class="form-input" />
                            </template>

                            <template v-else-if="key === 'gender'">
                                <select v-model="editingEmployee[key]" :id="key" class="form-input">
                                    <option value="男">男</option>
                                    <option value="女">女</option>
                                </select>
                            </template>

                            <template v-else-if="key === 'department'">
                                <select v-model="editingEmployee[key]" :id="key" class="form-input"
                                    @change="updatePositions(editingEmployee[key], editingEmployee)">
                                    <option v-for="dept in departments" :key="dept" :value="dept">{{ dept }}</option>
                                </select>
                            </template>

                            <template v-else-if="key === 'position'">
                                <select v-model="editingEmployee[key]" :id="key" class="form-input">
                                    <option v-for="pos in positions" :key="pos" :value="pos">{{ pos }}</option>
                                </select>
                            </template>

                            <template v-else-if="key === 'status'">
                                <select v-model="editingEmployee[key]" :id="key" class="form-input">
                                    <option value="正式工">正式工</option>
                                    <option value="试用期">试用期</option>
                                    <option value="临时工">临时工</option>
                                </select>
                            </template>
                        </template>
                    </div>
                    <div class="button-group">
                        <button type="submit" class="submit-button">保存修改</button>

                        <button type="button" class="cancel-button" @click="closeEditEmployeeModal">取消</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- 删除的模态框 -->
    <div v-if="showDelectModal" class="delectModal">
        <div class="modal-content">
            <h3>确认删除</h3>
            <p>确定要删除这名员工吗?</p>
            <button @click="confirmDelete" class="delectConfirm">确认</button>
            <button @click="cancelDelete" class="delectCancel">取消</button>
        </div>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import axios from 'axios';
import { regionData } from 'element-china-area-data';
import { ElMessage, ElMessageBox } from 'element-plus'

// 员工列表
const employees = ref([]);
const searchQuery = ref('');
const filteredEmployees = ref([]);

const showDelectModal = ref(false);

// 控制"新增员工"对话框的显示
const showAddEmployeeModal = ref(false);

// 新员工表单的数据
const employee = reactive({
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
});

// 地区选择的数据
const options = ref(regionData);

// 选中的地区选项
const selectedOptions = ref([]);

// 根据部门动态更新的岗位列表
const positions = ref([]);

// 部门对应的工号前缀
const departmentPrefixes = {
    人事部: 'HRD-',
    生产部: 'PRD-',
    研发部: 'R&D-',
    行政部: 'ADM-',
    市场部: 'MKT-',
    产品部: 'PDT-',
    销售部: 'SAL-',
    采购部: 'PUR-',
};

// 标志工号是否已存在
const employeeExists = ref(null);

// 身份证号的长度
const idCardLength = ref(0);

// 身份证号是否超过限制
const idCardExceeded = ref(false);

// 当前选中的员工
const selectedEmployee = ref(null);

//创建一个新的 ref 来存储当前选中的员工信息
const selectedEmployeeInfo = ref(null);

//控制编辑员工模态框的显示状态
const showEditEmployeeModal = ref(false);

// 存储当前正在编辑的员工信息，初始值为null
const editingEmployee = ref(null);

// 包含多个部门名称的数组
const departments = ['销售部', '产品部', '研发部', '生产部', '行政部', '人事部', '市场部', '采购部'];

//关闭编辑员工的模态框，将showEditEmployeeModal的值设为false并重置editingEmployee为null
const closeEditEmployeeModal = () => {
    showEditEmployeeModal.value = false;
    editingEmployee.value = null;
};

//根据输入的键返回对应的字段标签，如果键在预定义的标签中不存在，则返回键本身。
const getFieldLabel = (key) => {
    const labels = {
        name: '姓名',
        idCard: '身份证号',
        gender: '性别',
        // address: '身份证地址',
        department: '部门',
        position: '岗位',
        employeeId: '工号',
        birthDate: '出生日期',
        hireDate: '入职日期',
        phone: '联系电话',
        status: '状态'
    };
    return labels[key] || key;
};

//根据输入的键返回特定的输入类型。
const getInputType = (key) => {
    if (key === 'birthDate' || key === 'hireDate') {
        return 'date';
    }
    if (key === 'phone') {
        return 'tel';
    }
    return 'text';
};

// 更新员工数据
const updateEmployee = async () => {
    try {
        // 创建一个新对象，排除 address 字段  
        const updatedEmployee = Object.keys(editingEmployee.value)
            .filter(key => key !== 'address')
            .reduce((obj, key) => {
                obj[key] = editingEmployee.value[key];
                return obj;
            }, {});
        await axios.put(`http://localhost:8080/api/employees/${editingEmployee.value.employeeId}`, editingEmployee.value);
        alert('员工信息更新成功！');
        closeEditEmployeeModal();
        await fetchEmployees();
    } catch (error) {
        console.error('更新员工信息失败:', error);
        alert('更新员工信息失败，请检查后重试。');
    }
};

//判断是否选择了员工，再弹出模态框
const handleEditEmployee = () => {
    if (selectedEmployeeInfo.value) {
        editingEmployee.value = { ...selectedEmployeeInfo.value };
        // 显示编辑员工的模态窗口  
        showEditEmployeeModal.value = true;
        // 初始化岗位列表  
        // 先检查 editingEmployee.value.department 是否存在,如果存在则调用 updatePositions 函数  
        if (editingEmployee.value.department) {
            updatePositions(editingEmployee.value.department, editingEmployee.value);
        } else {
            console.error('editingEmployee.department is undefined');
        }
    } else {
        alert('请先选择要修改的员工');
    }
};
// 搜索员工的函数  
const searchEmployees = () => {
    if (searchQuery.value.trim() === '') {
        // 如果搜索框为空，显示所有员工  
        filteredEmployees.value = employees.value;
    } else {
        // 根据输入的字符过滤员工  
        filteredEmployees.value = employees.value.filter(employee => {
            return Object.values(employee).some(value =>
                String(value).includes(searchQuery.value)
            );
        });
    }
};
// 获取员工数据
const fetchEmployees = async () => {
    try {
        const response = await axios.get('http://localhost:8080/api/employees');
        employees.value = response.data.map((employee) => ({
            ...employee,
            checked: false,
        }));
        filteredEmployees.value = employees.value; // 默认显示所有员工  
    } catch (error) {
        console.error('获取员工数据失败:', error);
        alert('获取员工数据失败，请检查后重试。');
    }
};

// 关闭"新增员工"对话框并重置表单
const closeAddEmployeeModal = () => {
    showAddEmployeeModal.value = false;
    resetEmployeeForm();
};

// 文本截取方法，用于显示限定长度的文本
const truncateText = (text, length) => {
    if (!text || typeof text !== 'string') {
        return ''; // 或者返回一个默认值  
    }
    return text.length <= length ? text : text.slice(0, length) + '...';
};

// 重置新员工表单
const resetEmployeeForm = () => {
    Object.keys(employee).forEach(key => {
        employee[key] = '';
    });
    selectedOptions.value = [];
    positions.value = [];
    employeeExists.value = null;
    idCardLength.value = 0;
    idCardExceeded.value = false;
};

// 添加新员工
const addEmployee = async () => {
    if (employeeExists.value) {
        alert('该工号已存在，请使用其他工号。');
        return;
    }
    try {
        await axios.post('http://localhost:8080/api/employees', employee);
        alert('员工添加成功！');
        closeAddEmployeeModal();
        await fetchEmployees();
    } catch (error) {
        console.error('添加员工失败:', error);
        alert('添加员工失败，请检查后重试。');
    }
};

// 处理地区选择的变化
const handleAddressChange = (value) => {
    selectedOptions.value = value;
    employee.address = getAddressText(value);
};

// 获取选中的地区文本
const getAddressText = (value) => {
    let area = '';
    if (value.length === 1) {
        area = options.value.find((item) => item.value === value[0]).label;
    } else if (value.length === 2) {
        const province = options.value.find((item) => item.value === value[0]);
        const city = province.children.find((item) => item.value === value[1]);
        area = `${province.label}/${city.label}`;
    } else if (value.length === 3) {
        const province = options.value.find((item) => item.value === value[0]);
        const city = province.children.find((item) => item.value === value[1]);
        const district = city.children.find((item) => item.value === value[2]);
        area = `${province.label}/${city.label}/${district.label}`;
    }
    return area;
};

// 检查工号是否已存在
const checkEmployeeExists = async () => {
    try {
        const response = await axios.get(`http://localhost:8080/api/employees/${employee.employeeId}`);
        employeeExists.value = response.data.exists;
    } catch (error) {
        console.error('检查工号存在性失败:', error);
        employeeExists.value = false;
    }
};

// 根据部门更新岗位列表并设置工号前缀
const updatePositions = (department, employeeObject) => {
    if (department && employeeObject) {
        switch (department) {
            case '销售部':
                positions.value = ['销售经理', '销售代表', '客户经理'];
                break;
            case '产品部':
                positions.value = ['产品总监', '产品经理', '产品专员'];
                break;
            case '研发部':
                positions.value = ['研发总监', '项目经理', '技术总监'];
                break;
            case '生产部':
                positions.value = ['生产经理', '生产线长', '生产工人'];
                break;
            case '行政部':
                positions.value = ['行政经理', '行政助理', '前台接待'];
                break;
            case '人事部':
                positions.value = ['人事经理', '招聘专员', '培训专员'];
                break;
            case '市场部':
                positions.value = ['市场经理', '市场专员', '品牌经理'];
                break;
            case '采购部':
                positions.value = ['采购经理', '采购助理', '采购专员'];
                break;
            default:
                positions.value = [];
        }
        // 设置对应的工号前缀  
        employeeObject.employeeId = departmentPrefixes[department] || '';
        // 设置对应的工号前缀  
        employeeObject.employeeId = departmentPrefixes[department] || '';
    } else {
        console.error('department or employeeObject is undefined');
    }
};


// 处理身份证号输入
const handleIdCardInput = () => {
    idCardLength.value = employee.idCard.length;
    if (idCardLength.value > 18) {
        employee.idCard = employee.idCard.slice(0, 18);
        idCardExceeded.value = true;
    } else {
        idCardExceeded.value = false;
    }
};

// 切换员工选择状态  
const toggleEmployeeSelection = (employee) => {
    if (selectedEmployee.value === employee) {
        selectedEmployee.value = null;
        employee.checked = false;
        selectedEmployeeInfo.value = null;
        console.log('已取消选择员工');
    } else {
        if (selectedEmployee.value) {
            selectedEmployee.value.checked = false;
        }
        selectedEmployee.value = employee;
        employee.checked = true;

        selectedEmployeeInfo.value = {
            name: employee.name,
            idCard: employee.idCard,
            gender: employee.gender,
            address: employee.address,
            department: employee.department,
            position: employee.position,
            employeeId: employee.employeeId,
            birthDate: employee.birthDate,
            hireDate: employee.hireDate,
            phone: employee.phone,
            status: employee.status
        };
        console.log('已选择员工:', JSON.stringify(selectedEmployeeInfo.value, null, 2));

    }

    if (selectedEmployeeInfo.value) {
        editingEmployee.value = { ...selectedEmployeeInfo.value };
    }
};


// 显示删除员工的模态框
const deleteEmployee = () => {
    if (selectedEmployeeInfo.value) {
        showDelectModal.value = true; // 显示模态框  
    } else {
        alert('请先选择要删除的员工');
    }
};

//删除员工
const confirmDelete = async () => {
    try {
        await axios.delete(`http://localhost:8080/api/employees/${selectedEmployeeInfo.value.employeeId}`);
        alert('员工信息删除成功!');
        employees.value = employees.value.filter(employee => employee.employeeId !== selectedEmployeeInfo.value.employeeId);
        selectedEmployeeInfo.value = null;
        window.location.reload();
    } catch (error) {
        console.error('删除员工信息失败:', error);
        alert('删除员工信息失败,存在薪资记录，尝试');
    } finally {
        showDelectModal.value = false; // 关闭模态框  
    }
};
// 取消删除
const cancelDelete = () => {
    showDelectModal.value = false; // 关闭模态框  
};

// 初始化时获取员工数据
onMounted(async () => {
    await fetchEmployees();
});
</script>


<style scoped>
/* 容器样式 */
.container {
    padding: 20px;
}

@keyframes fadeIn {
    0% {
        opacity: 0;
        transform: translateY(20px);
    }

    100% {
        opacity: 1;
        transform: translateY(0);
    }
}

.employee-table {
    animation: fadeIn 0.5s ease-in-out;
}

/* 标题样式 */
.header h2 {
    display: flex;
    height: 100px;
    margin-bottom: 20px;
    font-size: 40px;
    background-color: #4eab9d;
    color: white;
    justify-content: center;
    align-items: center;
}

/* 员工按钮 */
.employee-button {
    padding: 10px 15px;
    color: white;
    border: none;
    border-radius: 9px;
    cursor: pointer;
    margin-bottom: 20px;
    margin-right: 10px;
}

/* 搜索框 */
.tools .searchBox {
    background-image: url('../../assets/搜索.png');
    background-size: 20px 20px;
    background-repeat: no-repeat;
    background-position: 2% 50%;
    width: 300px;
    height: 35px;
    border-radius: 8px;
    border: #d7d1d1 1px solid;
    padding-left: 30px;
    transition: border-color 0.3s;
    /* 添加过渡效果 */
    margin-left: 636px;
}

/* 悬停时的边框颜色 */
.tools .searchBox:hover {
    border: 2px solid #67a99d;
    /* 悬停时的边框颜色 */
}

/* 聚焦时的边框颜色 */
.tools .searchBox:focus {
    outline: none;
    /* 去掉默认的聚焦轮廓 */
    border-color: gray;
    /* 聚焦时的边框颜色 */
}

/* 当鼠标悬停并且输入框获得焦点时，边框颜色保持绿色 */
.tools .searchBox:hover:focus {
    border-color: #67a99d;
    /* 悬停并聚焦时的边框颜色 */
}

/* 搜索按钮 */
.tools .searchBotton {
    width: 69px;
    height: 35px;
    margin-left: 10px;
    border-radius: 5px;
    background-color: rgb(230, 230, 230);
    border: 1px solid gray;
}

.tools .searchBotton:hover {
    border: 1.4px solid #218838;
    background-color: rgb(222, 222, 222);
}

.tools .searchBotton:active {
    background-color: #e9e9ea;
}

/* 按钮的默认背景颜色 */
.employee-button.add {
    background-color: #28a745;
}

.employee-button.edit {
    background-color: #5183e6;
}

.employee-button.delete {
    background-color: #e03122;
}

/* 鼠标悬停时的背景颜色 */
.employee-button.add:hover {
    background-color: #218838;
}

.employee-button.edit:hover {
    background-color: #3a6fb0;
}

.employee-button.delete:hover {
    background-color: #c0271a;
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

/* 表格行的悬停效果 */
.employee-table tbody tr:hover {
    background-color: #98d4f0;
}

/* 对话框遮罩层样式 */
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
.employee-container {
    padding: 20px;
}

.employee-form {
    display: flex;
    flex-direction: column;
}

.form-group {
    margin-bottom: 7px;
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

/* 验证消息的文本样式 */
.text-danger {
    color: red;
}

.text-success {
    color: green;
}

.text-lightgray {
    color: lightgray;
}

/* 删除按钮的遮罩层 */
.delectModal {
    position: fixed;
    top: 0;
    left: 0;
    /* 遮罩层的宽高 */
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}

/* 删除的模态框 */
.delectModal .modal-content {
    width: 333px;
    height: 150px;
}

/* 删除确认按钮 */
.delectModal .delectConfirm {
    width: 90px;
    height: 30px;
    margin-left: 44px;
    border-radius: 8px;
    background-color: #ce4230;
    border: none;
}

.delectModal .delectConfirm:hover {
    background-color: red;
}

/* 删除的取消按钮 */
.delectModal .delectCancel {
    width: 90px;
    height: 30px;
    margin-top: 20px;
    margin-left: 20px;
    border-radius: 8px;
    background-color: #5a9cf8;
    border: none;
}

.delectModal .delectCancel:hover {
    background-color: #0073ff;
}

.delectModal p {
    margin-top: 20px;
}
</style>
