package cn.yanwei.study.dynamic.proxy.instance.modules;

import lombok.Data;

import java.io.Serializable;

/**
 * pojo实例
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/12 10:45
 */
@Data
public class Employee implements Serializable, Cloneable {
    private String name;

    public Employee() {
        System.out.println("Employee Constructor Called...");
    }

    @Override
    public Employee clone() {
        Employee clone = null;
        try {
            clone = (Employee) super.clone();
        } catch (CloneNotSupportedException ignored) {

        }
        return clone;
    }
}
