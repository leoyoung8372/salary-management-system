<template>  
  <div class="check-in-container">  
    <div class="flex" >  
      <!-- 考勤表 -->  
      <div class="check-in-form" >  
        <h2>考勤</h2>  
        <form @submit.prevent="handleCheckIn" class="signature">  
          <div class="form-group">  
            <label for="employeeId"><span style="color: red;">*</span>员工ID: </label>  
            <input type="text" v-model="attendanceRecord.employeeId" @blur="fetchEmployeeInfo" required />  
          </div>  
          <div class="form-group">  
            <label>员工姓名 </label>  
            <input type="text" v-model="employeeInfo.name" readonly />  
          </div>  
          <div class="form-group">  
            <label>所属部门 </label>  
            <input type="text" v-model="employeeInfo.department" readonly />  
          </div>  
          <div class="form-group">  
            <label>所在岗位 </label>  
            <input type="text" v-model="employeeInfo.position" readonly />  
          </div>  
          <div class="form-group">  
            <label for="checkInTime">签到时间:</label>  
            <input type="datetime-local" v-model="attendanceRecord.checkInTime" @change="updateStatus" required />  
          </div>  
          <div class="form-group">  
            <label for="date">日期:</label>  
            <input type="date" v-model="attendanceRecord.date" readonly />  
          </div>  
          <div class="form-group">  
            <label for="salaryCycle">归属薪资周期:</label>  
            <input type="month" v-model="attendanceRecord.salaryCycle" readonly />  
          </div>  
          <div class="form-group">  
            <label for="notes">备注<span style="color: gray; font-weight: 100;">（非必填）</span>:</label>  
            <input type="text" maxlength="20" v-model="attendanceRecord.notes" placeholder="特殊原因可填写，最长20字符" />  
          </div>  
          <button type="submit">打卡</button>  
        </form>  
      </div>  

      <!-- 考勤记录 -->  
      <div class="attendance-records">  
        <h3>考勤记录</h3>  
        <ul class="attendance-list">  
          <li v-for="record in attendanceRecords" :key="record.checkInTime" class="attRecItem">  
            <p>员工ID: {{ record.employeeId }}</p>  
            <p>姓名: {{ record.name }}</p>  
            <p>签到时间: {{ record.checkInTime }}</p>  
            <p>状态: {{ record.status }}</p>  
            <p>备注: {{ record.notes }}</p>  
          </li>  
        </ul>  
      </div>  
    </div>  

    <div v-if="message" :class="messageClass" class="status-message">{{ message }}</div>  
  </div>  
</template>   

<script setup>  
import { ref } from 'vue';  
import axios from 'axios';  
import '../../assets/css/CheckIn.css';  

// 定义考勤记录的响应式数据  
const attendanceRecord = ref({  
  employeeId: '',  
  checkInTime: '',  
  status: '', // 保留状态字段  
  date: '', // 日期字段  
  salaryCycle: '', // 归属薪资周期字段  
  notes:'',//备注字段  
  name:'',  
  department:'',  
  position:''  
});  

// 定义员工信息的响应式数据  
const employeeInfo = ref({  
  name: '',  
  department: '',  
  position: ''  
});  

// 定义消息的响应式数据  
const message = ref('');  
const messageClass = ref(''); // 用于动态设置消息的样式  

// 定义考勤记录列表  
const attendanceRecords = ref([]);  

// 根据员工ID获取员工信息的函数  
const fetchEmployeeInfo = async () => {  
  if (!attendanceRecord.value.employeeId) return; // 如果员工ID为空，不执行请求  

  try {  
    const response = await axios.get(`http://localhost:8080/api/employees/info/${attendanceRecord.value.employeeId}`);  
    const employee = response.data;  
    // 填充员工信息  
    employeeInfo.value.name = employee.name;  
    employeeInfo.value.department = employee.department;  
    employeeInfo.value.position = employee.position;  

    // 同时填充到考勤记录中  
    attendanceRecord.value.name = employee.name;  
    attendanceRecord.value.department = employee.department;  
    attendanceRecord.value.position = employee.position;  
  } catch (error) {  
    // 处理错误，例如员工ID不存在  
    employeeInfo.value.name = '';  
    employeeInfo.value.department = '';  
    employeeInfo.value.position = '';  
    message.value = '该员工不存在';  
    messageClass.value = 'error';  
    setTimeout(() => {  
      message.value = '';  
    }, 2000);  
  }  
};  

// 根据签到时间更新状态的函数  
const updateStatus = () => {  
  const checkInTime = new Date(attendanceRecord.value.checkInTime);  
  const hours = checkInTime.getHours();  
  const minutes = checkInTime.getMinutes();  

  // 提取日期并格式化为 YYYY-MM-DD  
  attendanceRecord.value.date = checkInTime.toISOString().split('T')[0];  

  // 提取年月并格式化为 YYYY-MM  
  const year = checkInTime.getFullYear();  
  const month = String(checkInTime.getMonth() + 1).padStart(2, '0');  
  attendanceRecord.value.salaryCycle = `${year}-${month}`;  

  // 检查时间段  
  if (hours >= 22 || hours < 7) {  
    message.value = '22:00-7:30该时间段不可以打卡';  
    messageClass.value = 'error'; // 设置错误消息的样式  
    attendanceRecord.value.status = null; // 确保状态为null  
    return;  
  }  
  
  // 更新状态逻辑  
  if (hours === 7 && minutes >= 30) {  
    attendanceRecord.value.status = '签到';  
  } else if (hours === 8 && minutes <= 30) {  
    attendanceRecord.value.status = '迟到';  
  } else if (hours === 8 && minutes > 30 && hours < 12) {  
    attendanceRecord.value.status = '早退';  
  } else if (hours === 12 && minutes <= 30) {  
    attendanceRecord.value.status = '签退';  
  } else if (hours === 12 && minutes > 30 && hours < 13) {  
    attendanceRecord.value.status = '签到';  
  } else if (hours === 13 && minutes <= 30) {  
    attendanceRecord.value.status = '迟到';  
  } else if (hours === 13 && minutes > 30 && hours < 17) {  
    attendanceRecord.value.status = '早退';  
  } else if (hours === 17 && minutes <= 30) {  
    attendanceRecord.value.status = '签退';  
  } else if (hours === 18 && minutes <= 30) {  
    attendanceRecord.value.status = '签到';  
  } else if (hours >= 9 && hours < 12) {  
    attendanceRecord.value.status = '早退';  
  } else if (hours >= 14 && hours < 17) {  
    attendanceRecord.value.status = '早退';  
  } else {  
    attendanceRecord.value.status = null; // 确保状态为null  
  }  
};  

// 处理签到的函数  
const handleCheckIn = async () => {  
  if (message.value === '22:00-7:30该时间段不可以打卡') return; // 如果在禁止时间段，不执行打卡  

  try {  
    // 发送签到数据到后端  
    await axios.post('http://localhost:8080/api/attendance/checkin', attendanceRecord.value);  
    message.value = `打卡成功，你已${attendanceRecord.value.status}`;  
    setMessageClass(); // 设置消息的背景颜色  

    // 获取所有考勤记录并更新前端显示  
    await fetchAllAttendanceRecords();  

    // 将新记录添加到考勤记录列表的开头  
    attendanceRecords.value.unshift({ ...attendanceRecord.value });  

    setTimeout(() => {  
      message.value = ''; // 2秒后清空消息  
    }, 3000);  
  } catch (error) {  
    message.value = '打卡失败，请重试';  
    messageClass.value = 'error'; // 设置错误消息的样式  
    setTimeout(() => {  
      message.value = ''; // 2秒后清空消息  
    }, 2000);  
  }  
}; 

// 获取所有考勤记录的函数  
const fetchAllAttendanceRecords = async () => {  
  try {  
    const response = await axios.get('http://localhost:8080/api/attendance/AllRecords');  
    attendanceRecords.value = response.data; // 更新考勤记录列表  
  } catch (error) {  
    console.error('获取考勤记录失败:', error);  
  }  
};  

// 根据状态设置消息的背景颜色  
const setMessageClass = () => {  
  switch (attendanceRecord.value.status) {  
    case '签到':  
    case '签退':  
      messageClass.value = 'success'; // 绿色背景  
      break;  
    case '迟到':  
    case '早退':  
      messageClass.value = 'warning'; // 黄色背景  
      break;  
    default:  
      messageClass.value = 'error'; // 红色背景  
  }  
};  
</script>

