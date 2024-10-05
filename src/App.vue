<template>
  <div class="app-container">
    <Navbar class="navbar" />
    <div class="content">
      <div class="scrollable-content">
        <Showcase />
        <!-- <router-view />   -->
      </div>
    </div>
  </div>
</template>

<script>
import Navbar from './views/Navbar.vue'
import Showcase from './views/Showcase.vue';

export default {
  name: 'App',
  components: {
    Navbar,
    Showcase
  }
}  
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  /* 使元素的宽高包括内边距和边框 */
}

.app-container {
  display: flex;
  /* 使用 Flexbox 布局 */
  height: 100vh;
  /* 设置高度为视口高度 */
}

.navbar {
  width: 333px;
  height: 100%;
  background-color: #ffffff;
  position: fixed;
  /* 固定左侧导航栏 */
}

.content {
  flex: 1;
  /* 使 router-view 占据剩余空间 */
  margin-left: 333px;
  /* 留出导航栏的宽度 */
  background-color: #ffffff;
  /* 可选：设置背景颜色 */
}

.scrollable-content {
  /* 减去一些边距 */
  /* height: calc(100vh - 20px); */
  overflow-y: auto;
  /* 允许内容区垂直滚动 */
  padding: 10px;
  /* 可选：设置内边距 */
}
</style>