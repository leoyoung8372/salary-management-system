<template>
    <div class="container">
        <div class="header">
            <img src="../../assets/background.png" alt="">
            <h2>薪资看板</h2>
        </div>
        <div class="payroll">
            <div>
                <div>
                    <!--从获取到所有人的“基本工资”+“绩效”+“奖金”+“津贴”+“加班费”累加，然后减去“扣除额”-->
                    当月累计应发薪资
                </div>
                <!--从获取到所有人的“基本工资”+“绩效”+“奖金”+“津贴”+“加班费”并累加-->
                当月累计实发薪资
            </div>
            <div>
                平均工资
            </div>
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
    height: 120px;
    border-radius: 20px;
}

.header h2 {
    position: absolute;
    top: 30px;
    /* 垂直居中 */
    left: 520px;
    /* 水平居中 */
    color: white;
    font-size: 50px;
    letter-spacing: 12px;
    /* 设置字母间距 */
}
</style>