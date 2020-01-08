## 创建对象
> [csdn博客：创建对象的5种方式](https://blog.csdn.net/m0_38016268/article/details/82957536Java "创建对象的5种方式")

> [菜鸟教程：Java教程-序列化](https://www.runoob.com/java/java-serialization.html "Java教程-序列化")


- new对象
- 使用反射类中Class.newInstance()创建对象
- 使用反射类中Class类中的Constructor的newInstance()方法创建对象，如:Object.class.getConstructor.newInstance()
- 使用对象的克隆方法object.clone()方法，前提object类必须实现Cloneable类，让object类具有clone功能
- Java序列化，java提供一种将对象序列化机制，实质就是将一个对象转换成字节序列，字节序列依然保存该对象的数据，及对象类型信息和对象属性类型等内容。同时提供反序列化机制。我们通过反序列化的ObjectInputStream中的readObject方法创建对象。前提创建对象需要实现Serializable[ˈsɪˌriəˌlaɪzəbl]接口。

##### 创建pojo类，需要实现Cloneable和Serializablejie接口上

 ```
package cn.yanwei.study.instance.modules;

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
 ```
##### 测试对象创建
 ```
 package cn.yanwei.study.instance;

import Employee;
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
            System.out.println("反序列化：" + employee4);
        }
    }
}

 ```