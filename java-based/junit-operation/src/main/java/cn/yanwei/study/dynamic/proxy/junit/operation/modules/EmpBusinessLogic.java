package cn.yanwei.study.dynamic.proxy.junit.operation.modules;

/**
 * business logic 类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/12 16:41
 */
public class EmpBusinessLogic {
    /**
     * Calculate the yearly salary of employee
     *
     * @param employeeDetails employee details
     * @return yearly salary
     */
    public double calculateYearlySalary(EmployeeDetails employeeDetails) {
        double yearlySalary = 0;
        yearlySalary = employeeDetails.getMonthlySalary() * 12;
        return yearlySalary;
    }

    /**
     * Calculate the appraisal amount of employee
     *
     * @param employeeDetails employee details
     * @return appraisal amount
     */
    public double calculateAppraisal(EmployeeDetails employeeDetails) {
        double appraisal = 0;
        double threshold = 10000;
        if (employeeDetails.getMonthlySalary() < threshold) {
            appraisal = 500;
        } else {
            appraisal = 1000;
        }
        return appraisal;
    }

}