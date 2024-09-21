<template>  
  <div class="app-container">  
    <Navbar class="navbar" />  
    <div class="content">  
      <router-view />  
    </div>  
  </div>  
</template>  

<script>  
import Navbar from './views/Navbar.vue'  

export default {  
  name: 'App',  
  components: {  
    Navbar  
  }  
}  
</script>  

<style>  
* {  
    margin: 0;  
    padding: 0;  
    box-sizing: border-box; /* 使元素的宽高包括内边距和边框 */  
}  
.app-container {  
  display: flex; /* 使用 Flexbox 布局 */  
}  



.content {  
  flex: 1; /* 使 router-view 占据剩余空间 */  
  /* padding: 20px; 可选：设置内边距   */
  background-color: #ffffff; /* 可选：设置背景颜色 */  
}  
</style>