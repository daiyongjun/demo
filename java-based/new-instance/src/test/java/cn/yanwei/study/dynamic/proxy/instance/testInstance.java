package cn.yanwei.study.dynamic.proxy.instance;

import cn.yanwei.study.dynamic.proxy.instance.modules.Employee;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 测试对象的实例化
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/13 9:33
 */
class testInstance {
    private static final String FILE_NAME = "employee.obj";

    @Test
    void instance() throws Exception {
        //通过构造方法创建对象
        Employee employee = new Employee();
        employee.setName("张三");
        System.out.println(employee);

        //通过反射类class中的newInstance方法进行创建对象
        Employee employee1 = Employee.class.newInstance();
        employee1.setName("xxx1");
        System.out.println("Class类的newInstance()方法：" + employee1);

        //通过反射类class中的constructor类的newInstance方法进行创建
        Employee employee2 = Employee.class.getConstructor().newInstance();
        employee2.setName("xxx2");
        System.out.println("使用Constructor类的newInstance()：" + employee2);

        //通过对象的clone方法
        Employee employee3 = employee.clone();
        employee3.setName("xxx3");
        System.out.println("使用Constructor类的newInstance()：" + employee3);

        //通过序列化和反序列化进行创建对象
        // 使用 反序列化ObjectInputStream 的readObject()方法：类必须实现 Serializable接口
        // 序列化
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(employee);
        }
        // 反序列化
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            Employee employee4 = (Employee) ois.readObject();
            employee4.setName("xxx4");
            System.out.println("反序列化：" + employee4);
        }
    }
}
