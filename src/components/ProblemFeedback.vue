<template>  
    <div class="container">  
        <!-- 头部 -->  
        <div class="header">  
            <h2>考勤签到打卡</h2>
        </div>  
        <!-- 主体 -->  
        <div class="main">  
            <!-- 主体左侧 -->  
            <div class="aside-left">  
                <form @submit.prevent="handleCheckIn">  
                    <div class="form-group">  
                        <label for="checkInTime"><span style="color: red;">*</span>签到时间</label>  
                        <input type="datetime-local" v-model="attendanceRecord.checkInTime" required  
                            @change="checkInTimeChanged" />  
                    </div>  
                    <div class="form-group">  
                        <label for="employeeId"><span style="color: red;">*</span>员工ID</label>  
                        <input type="text" v-model="attendanceRecord.employeeId" required   
                            @blur="fetchEmployeeInfo" />  
                    </div>  
                    <div class="form-group">  
                        <label for="employeeName">员工姓名</label>  
                        <input type="text" v-model="attendanceRecord.name" />  
                    </div>  
                    <div class="form-group">  
                        <label for="department">所属部门</label>  
                        <input type="text" v-model="attendanceRecord.department" />  
                    </div>  
                    <div class="form-group">  
                        <label for="position">所在岗位</label>  
                        <input type="text" v-model="attendanceRecord.position" />  
                    </div>  
                    <!-- 隐藏的日期字段 -->  
                    <input type="hidden" v-model="attendanceRecord.date" />  
                    <div class="form-group">  
                        <label for="salaryCycle">归属薪资日期</label>  
                        <input type="month" v-model="attendanceRecord.salaryCycle" />  
                    </div>  
                    <div class="form-group">  
                        <label for="status">状态</label>  
                        <input type="text" v-model="attendanceRecord.status" readonly />  
                    </div>  
                    <div class="form-group">  
                        <label for="notes">备注<span style="color: gray; font-size: smaller;">(非必填)</span></label>  
                        <input type="text" v-model="attendanceRecord.notes" maxlength="12" placeholder="迟到需要备注原因,最长12个字"/>  
                    </div>  
                    <div class="btn">  
                        <button type="submit" class="check-in-btn" :disabled="isDisabled">打卡</button>  
                        <button type="button" class="check-in-clean" @click="clearForm">清空</button>  
                    </div>  
                </form>  
            </div>  
            <!-- 主体右侧 -->  
            <div class="aside-right">  
                <!-- 打卡记录列表 -->  
                <table>  
                    <thead>  
                        <tr>  
                            <th style="width: 180px;">签到时间</th>  
                            <th style="width: 100px;">员工ID</th>  
                            <th style="width: 88px;">员工姓名</th>  
                            <th style="width: 88px;">所属部门</th>  
                            <th style="width: 88px;">所在岗位</th>  
                            <!-- <th style="width: 100px;">日期</th>   -->
                            <th style="width: 110px;">归属薪资日期</th>
                            <th style="width: 90px;">状态</th>
                            <th>备注</th>
                        </tr>  
                    </thead>  
                    <tbody>  
                        <tr v-for="record in attendanceRecords.slice(0,25)" :key="record.id">  
                            <td>{{ formatDate(record.checkInTime) }}</td>  
                            <td>{{ record.employeeId }}</td>  
                            <td>{{ record.name }}</td>  
                            <td>{{ record.department }}</td>  
                            <td>{{ record.position }}</td>  
                            <!-- <td>{{ formatDateOnly(record.date) }}</td>    -->
                            <td>{{ record.salaryCycle }}</td>  
                            <td>{{ record.status }}</td>
                            <td style="font-size: 15px;">{{ record.notes }}</td>  

                        </tr>                        
                    </tbody>  
                </table>  
            </div>  
        </div>  
    </div>  
</template>  

<script setup>  
import { ref, onMounted, watch } from 'vue';  

// 存储表单输入的数据  
const attendanceRecord = ref({  
    checkInTime: '',  
    employeeId: '',  
    name: '',  
    department: '',  
    position: '',  
    date: '', // 仍然保留该字段以便后端接收  
    salaryCycle: '',  
    status: '',  
    notes: ''  
});  

// 存储打卡记录  
const attendanceRecords = ref([]);  

// 用于禁用打卡按钮  
const isDisabled = ref(false);  

// 获取所有打卡记录的函数  
const fetchAttendanceRecords = async () => {  
    try {  
        const response = await fetch('http://localhost:8080/api/attendance/AllRecords');  
        if (response.ok) {  
            // 在渲染的时就反转数组，让最新记录在上。
            attendanceRecords.value = (await response.json()).reverse();   
        } else {  
            console.error('获取打卡记录失败', response.statusText);  
        }  
    } catch (error) {  
        console.error('请求错误', error);  
    }  
};  

// 在组件挂载时获取打卡记录  
onMounted(() => {  
    fetchAttendanceRecords();  
});  

// 判断打卡状态的函数  
const determineStatus = (time) => {  
    const date = new Date(time);  
    const hours = date.getHours();  
    const minutes = date.getMinutes();  
    const totalMinutes = hours * 60 + minutes;  

    if (totalMinutes >= 450 && totalMinutes <= 480) { // 7:30 - 8:00  
        return '上午签到';  
    } else if (totalMinutes >= 481 && totalMinutes <= 490) { // 8:01 - 8:10  
        return '上午迟到';  
    } else if (totalMinutes >= 491 && totalMinutes < 720) { // 8:11 - 11:59  
        return '不可签到';  
    } else if (totalMinutes >= 720 && totalMinutes <= 750) { // 12:00 - 12:30  
        return '中午签退';  
    } else if (totalMinutes >= 751 && totalMinutes <= 780) { // 12:31 - 13:00  
        return '下午签到';  
    } else if (totalMinutes >= 781 && totalMinutes <= 790) { // 13:01 - 13:10  
        return '下午迟到';  
    } else if (totalMinutes >= 791 && totalMinutes < 1020) { // 13:11 - 16:59  
        return '不可签到';  
    } else if (totalMinutes >= 1020 && totalMinutes <= 1050) { // 17:00 - 17:30  
        return '下午签退';  
    } else if (totalMinutes >= 1051 && totalMinutes <= 1080) { // 17:31 - 18:00  
        return '加班签到';  
    } else if (totalMinutes >= 1200 && totalMinutes <= 1230) { // 20:00 - 20:30  
        return '加班签退';  
    } else if (totalMinutes >= 1231 || totalMinutes < 450) { // 20:31 - 次日7:29  
        return '不可签到';  
    }  
    return '不可签到'; // 确保所有未匹配的时间都返回不可签到  
};  


// 监听签到时间的变化  
watch(() => attendanceRecord.value.checkInTime, (newTime) => {  
    if (newTime) {  
        const status = determineStatus(newTime);  
        if (status !== '不可签到') {  
            attendanceRecord.value.status = status; // 仅在状态不是“不可签到”时更新状态  
            updateDateFields(newTime); // 更新日期和归属薪资日期  
        } else {  
            attendanceRecord.value.status = ''; // 清空状态  
        }  
    }  
});  

// 更新日期和归属薪资日期的函数  
const updateDateFields = (checkInTime) => {  
    const date = new Date(checkInTime);  
    const yyyy = date.getFullYear();  
    const mm = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始  
    const dd = String(date.getDate()).padStart(2, '0');  

    attendanceRecord.value.date = `${yyyy}-${mm}-${dd}`; // YYYY-MM-DD格式  
    attendanceRecord.value.salaryCycle = `${yyyy}-${mm}`; // YYYY-MM格式  
};  

// 检查签到时间变化的函数  
const checkInTimeChanged = () => {  
    const status = determineStatus(attendanceRecord.value.checkInTime);  
    if (status === '不可签到') {  
        alert('该时间段不可签到');  
        isDisabled.value = true; // 禁用打卡按钮  
        attendanceRecord.value.status = ''; // 清空状态  
    } else {  
        isDisabled.value = false; // 否则启用打卡按钮  
    }  
};  

//格式化时间：年月日时分秒
const formatDate = (dateString) => {  
    const date = new Date(dateString);  
    return date.toISOString().replace('T', ' ').substring(0, 19); // 格式化为 YYYY-MM-DD HH:mm:ss  
};
// 格式化时间：年月日
const formatDateOnly = (dateString) => {  
    const date = new Date(dateString);  
    return date.toISOString().substring(0, 10); // 格式化为 YYYY-MM-DD  
};

// 获取员工信息的函数  
const fetchEmployeeInfo = async () => {  
    const employeeId = attendanceRecord.value.employeeId;  
    if (!employeeId) return;  

    try {  
        const response = await fetch(`http://localhost:8080/api/employees/info/${employeeId}`);  
        if (response.ok) {  
            const data = await response.json();  
            attendanceRecord.value.name = data.name;  
            attendanceRecord.value.department = data.department;  
            attendanceRecord.value.position = data.position;  
            // 保持状态不变  
        } else {  
            alert('不存在该员工');  
            attendanceRecord.value.name = '';  
            attendanceRecord.value.department = '';  
            attendanceRecord.value.position = '';  
        }  
    } catch (error) {  
        console.error('请求错误', error);  
    }  
};  

// 处理打卡的函数  
const handleCheckIn = async () => {  
    try {  
        // 调用判断是否已打卡的API  
        const checkInResponse = await fetch(`http://localhost:8080/api/attendance/ifCheckin?employeeId=${attendanceRecord.value.employeeId}&checkInTime=${attendanceRecord.value.checkInTime}`);  
        
        if (checkInResponse.ok) {  
            const isCheckedIn = await checkInResponse.text(); // 假设返回的是字符串  

            if (isCheckedIn.includes("您已在该时间段打卡")) {  
                alert('您已在该时间段打卡，请勿重复打卡。');  
                return; // 终止后续打卡逻辑  
            }  
        } else {  
            console.error('检查打卡状态失败', checkInResponse.statusText);  
            return; // 处理错误情况  
        }  

        // 如果未打卡，继续提交打卡请求  
        const response = await fetch('http://localhost:8080/api/attendance/checkin', {  
            method: 'POST',  
            headers: {  
                'Content-Type': 'application/json',  
            },  
            body: JSON.stringify(attendanceRecord.value),  
        });  

        if (response.ok) {  
            console.log('打卡成功');  
            alert('打卡成功！');  
            fetchAttendanceRecords(); // 打卡成功后重新获取打卡记录  
        } else {  
            console.error('打卡失败', response.statusText);  
        }  
    } catch (error) {  
        console.error('请求错误', error);  
    }  
}; 

// 清空表单内容的函数  
const clearForm = () => {  
    attendanceRecord.value = {  
        checkInTime: '',  
        employeeId: '',  
        name: '',  
        department: '',  
        position: '',  
        date: '', // 仍然保留该字段以便后端接收  
        salaryCycle: '',  
        status: '',  
        notes: ''  
    };  
};  

</script>

<style scoped>
/* 容器 */
.container {
    padding: 0;
    margin: 0;
    height: 100vh;
    box-sizing: border-box;
}

/* 头部 */
.header {
    background-color: #6baea6;
    display: flex; /* 使用 Flexbox */  
    justify-content: center; /* 水平居中 */  
    align-items: center; /* 垂直居中 */
    height: 10%;
}
/* 头部标题 */
.header h2{
    font-size: 40px;
    color: white;
}

/* 主体 */
.main {
    display: flex;
    background-color: brown;
    height: 90%;
}

/* 主体左侧 */
.main .aside-left {
    background-color: #efeff4;
    height: 100%;
    width: 30%;
}

/* 主体左侧的group */
.aside-left .form-group {
    width: 90%;
    height: 80px;
    background-color: rgb(255, 255, 255);
    margin: 12px 10px 0 18px;
    /* 上 右 下 左 */
}

/* 标签 */
.aside-left label {
    font-weight: 300;
    margin-left: 18px;
    padding-top: 10px;
}

/* 输入框 */
.aside-left input {
    width: 90%;
    margin-left: 18px;
    margin-top: 5px;
}

.aside-left input:hover {
    border: #52b0a6 solid;
}

/* 按钮容器 */
.btn {
    margin-top: 20px;
}

/* 签到按钮 */
.btn .check-in-btn {
    width: 70%;
    height: 60px;
    background-color: #52b0a6;
    color: white;
    font-size: 15px;
    margin-left: 20px;
    border: none;
    border-radius: 5px 0 0 5px;
}

.btn .check-in-btn:hover {
    background-color: #3f8b83;
}

/* 清空按钮 */
.btn .check-in-clean {
    width: 18%;
    height: 60px;
    font-size: 15px;
    background-color: #be3229;
    border-radius: 0 5px 5px 0;
    border: none;
    color: rgb(240, 240, 240);
}

.btn .check-in-clean:hover {
    background-color: #91261f;
}


/* 主体右侧 */
.main .aside-right {
    background-color: rgb(233, 233, 233);
    height: 100%;
    width: 70%;
}

.aside-right table {
    width: 100%;
    margin-top: 12px;
    border-collapse: collapse;
}

th,
td {
    /* 表格边框线颜色 */
    border: 1px solid rgb(169, 169, 169);
    padding: 5px;
    text-align: left;
    text-align: center; /* 水平居中 */  
    vertical-align: middle; /* 垂直居中 */ 
}
</style>