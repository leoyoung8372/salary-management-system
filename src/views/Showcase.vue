<template>  
    <router-view v-slot="{ Component }">  
      <component :is="Component" class="routerStyle" />  
    </router-view>  
  </template>  
  
  <script setup>  
  // 如果需要的话，可以在这里添加任何必要的逻辑  
  </script>  
  
  <style scoped>  
  .routerStyle {  
    padding: 20px;  
    height: 100vh;  
    background-color: #f4f6fb;  
  }  
  </style>