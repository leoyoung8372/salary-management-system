//引入createRouter
import { createRouter, createWebHistory } from "vue-router";
//引入一个一个可能要呈现的组件
import AddEmployee from "@/components/AddEmployee.vue";
import EmployeeRecords from "@/components/EmployeeRecords.vue";
import SalaryDashboard from "@/components/SalaryDashboard.vue";
import SalaryDetails from "@/components/SalaryDetails.vue";
import SalaryItems from "@/components/SalaryItems.vue";
import Payslip from "@/components/Payslip.vue"
//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {  
            path: '/',  
            redirect: '/SalaryItems' // 添加默认路由 (默认显示) 
        }, 
        {
            path:'/addEmployee',
            component: AddEmployee
        },
        {
            path: '/employeeRecords',
            component: EmployeeRecords
        },
        {
            path:'/salaryDashboard',
            component:SalaryDashboard
        },
        {
            path:'/salaryDetails',
            component:SalaryDetails
        },
        {
            path:'/salaryItems',
            component:SalaryItems
        },
        {
            path:'/payslip',
            component:Payslip
        }
    ]
})
export default router