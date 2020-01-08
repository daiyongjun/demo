package cn.yanwei.study.dynamic.proxy.junit.operation.base;

import cn.yanwei.study.dynamic.proxy.junit.operation.modules.EmpBusinessLogic;
import cn.yanwei.study.dynamic.proxy.junit.operation.modules.EmployeeDetails;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * JUnit - 编写测试
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/12 16:52
 */
class TestEmployeeDetails {
    private EmpBusinessLogic empBusinessLogic = new EmpBusinessLogic();
    private EmployeeDetails employee = new EmployeeDetails();

    //test to check appraisal
    @Test
    void testCalculateAppriasal() {
        employee.setName("Rajeev");
        employee.setAge(25);
        employee.setMonthlySalary(8000);
        double appraisal = empBusinessLogic.calculateAppraisal(employee);
        assertEquals(500, appraisal, 0.0);
    }

    // test to check yearly salary
    @Test
    void testCalculateYearlySalary() {
        employee.setName("Rajeev");
        employee.setAge(25);
        employee.setMonthlySalary(8000);
        double salary = empBusinessLogic.calculateYearlySalary(employee);
        assertEquals(96000, salary, 0.0);
    }
}
