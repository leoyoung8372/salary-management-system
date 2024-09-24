import { createApp } from "vue";  
import App from './App.vue';  
import ElementPlus from 'element-plus';  
import 'element-plus/dist/index.css'; // 引入样式  
// 引入路由器  
import router from "./router/router";  

// 创建一个应用  
const app = createApp(App);  

// 使用 Element Plus  
app.use(ElementPlus); 

// 使用路由器  
app.use(router);  

// 挂载整个应用到 app 容器  
app.mount('#app');