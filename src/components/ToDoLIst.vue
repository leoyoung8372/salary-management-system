<template>  
  <div class="demo-date-picker">  
    <div class="block">  
      <!-- 日期选择器组件 -->  
      <el-date-picker  v-model="value1" type="date" placeholder="Pick a day" size="large"/>  
    </div>  
  </div>  
</template>  

<script setup>  
import { ref } from 'vue'  

// 定义响应式变量，用于存储选择的日期  
const value1 = ref('')  
</script>  

<style scoped>  
.demo-date-picker {  
  display: flex;  /* 使用flex布局 */  
  width: 100%;    /* 容器宽度为100% */  
  padding: 0;     /* 去除内边距 */  
  flex-wrap: wrap; /* 允许换行 */  
}  

.demo-date-picker .block {  
  padding: 30px 0; /* 设置上下内边距 */  
  text-align: center; /* 文本居中对齐 */  
  border-right: solid 1px var(--el-border-color); /* 右边框 */  
  flex: 1; /* 使每个块均匀分配空间 */  
}  

.demo-date-picker .block:last-child {  
  border-right: none; /* 去除最后一个块的右边框 */  
}  

.demo-date-picker .demonstration {  
  display: block; /* 作为块元素显示 */  
  color: var(--el-text-color-secondary); /* 设置文本颜色 */  
  font-size: 14px; /* 设置字体大小 */  
  margin-bottom: 20px; /* 设置底部外边距 */  
}  
</style>