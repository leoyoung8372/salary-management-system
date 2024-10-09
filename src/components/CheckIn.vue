<template>  
    <div class="check-in-container">  
      <h2>签到</h2>  
      <form @submit.prevent="handleCheckIn">  
        <div>  
          <label for="employeeId">员工ID:</label>  
          <input type="text" v-model="attendanceRecord.employeeId" required />  
        </div>  
        <div>  
          <label for="checkInTime">签到时间:</label>  
          <input type="datetime-local" v-model="attendanceRecord.checkInTime" required />  
        </div>  
        <div>  
          <label for="status">状态:</label>  
          <select v-model="attendanceRecord.status" required>  
            <option value="正常">正常</option>  
            <option value="迟到">迟到</option>  
            <option value="缺勤">缺勤</option>  
          </select>  
        </div>  
        <div>  
          <label for="date">日期:</label>  
          <input type="date" v-model="attendanceRecord.date" required />  
        </div>  
        <button type="submit">签到</button>  
      </form>  
      <p v-if="message">{{ message }}</p>  
    </div>  
  </template>  
  
  <script setup>  
  import { ref } from 'vue';  
  import axios from 'axios';  
  
  const attendanceRecord = ref({  
    employeeId: '',  
    checkInTime: '',  
    status: '正常',  
    date: ''  
  });  
  
  const message = ref('');  
  
  const handleCheckIn = async () => {  
    try {  
      const response = await axios.post('http://localhost:8080/api/attendance/checkin', attendanceRecord.value);  
      message.value = response.data; // 显示成功消息  
    } catch (error) {  
      message.value = '签到失败: ' + (error.response?.data || error.message); // 显示错误消息  
    }  
  };  
  </script>  
  
  <style scoped>  
  .check-in-container {  
    max-width: 400px;  
    margin: auto;  
    padding: 20px;  
    border: 1px solid #ccc;  
    border-radius: 8px;  
    background-color: #f9f9f9;  
  }  
  </style>