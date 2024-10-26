//引入createRouter
import { createRouter, createWebHistory } from "vue-router";
//引入一个一个可能要呈现的组件
import EmployeeRecords from "@/components/EmployeeRecords.vue";
import SalaryDashboard from "@/components/SalaryDashboard.vue";
import SalaryPaymentRecord from "@/components/SalaryPaymentRecord.vue";
import SalaryItems from "@/components/SalaryItems.vue";
import Payslip from "@/components/Payslip.vue"
import ToDoLIst from "@/components/ToDoLIst.vue";
import ProblemFeedback from "@/components/ProblemFeedback.vue";
import CheckIn from "@/components/CheckIn.vue";
//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {  
            path: '/',  
            redirect: '/CheckIn' // 添加默认路由 (默认显示) 
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
            path:'/salaryPaymentRecord',
            component:SalaryPaymentRecord
        },
        {
            path:'/salaryItems',
            component:SalaryItems
        },
        {
            path:'/payslip',
            component:Payslip
        },
        {
            path:'/toDoLIst',
            component:ToDoLIst
        },
        {
            path:'/problemFeedback',
            component:ProblemFeedback
        },
        {
            path:'/checkIn',
            component:CheckIn
        }
    ]
})
export default router