<template>
    <div class="container">
        <div class="header">
            <img src="../../assets/background.png" alt="">
            <h2>薪资看板</h2>
        </div>
        <div class="payroll">
            
        </div>
    </div>
</template>

<script>

</script>

<style scoped>
.container {
    width: 100%;
    height: 100vh;
    background-color: #f4f6fc;
}

.header {
    display: inline-block;
    /* 防止外边距合并 */
    position: relative;
    margin-left: 20px;
    margin-top: 20px;
}

.header img {
    width: 98%;
    height: 100px;
    border-radius: 15px;
}

.header h2 {
    position: absolute;
    top: 19px;
    /* 垂直居中 */
    left: 520px;
    /* 水平居中 */
    color: white;
    font-size: 50px;
    letter-spacing: 12px;
    /* 设置字母间距 */
}
</style>