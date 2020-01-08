## 设计模式（Design pattern）
> [菜鸟教程：设计模式](https://www.runoob.com/design-pattern/design-pattern-tutorial.html "Java教程-设计模式")

#### 单例模式
##### 概述
**单例模式**是 **Java设计模式** 中最简单的模式之一，这种设计模式类型属于创建型模式，它提供一种创建对象的最佳方式。它主要涉及一个单一的类，该类负责创建自身的对象，同时确保只有一个对象被创建。这个类提供了一个访问该唯一对象途径，可以直接访问，无需实例化该类的对象。
##### 介绍
- 主要解决：一个全局使用的类频繁地创建与销毁。
- 如何解决：判断系统是否已经有这个单例，如果有则返回，如果没有则创建。
- 关键代码：构造函数是私有的。单例模式提供一个无需实例化调用方法，该方法负责返回该类创建自身的对象。
- 优点：
    1. 在内存里只有一个实例，减少了内存的开销，尤其是频繁的创建和销毁实例（比如管理学院首页页面缓存）。
    2. 避免对资源的多重占用（比如写文件操作）
- 缺点：没有接口，不能继承，与单一职责原则冲突，一个类应该只关心内部逻辑，而不关心外面怎么样来实例化。
- 使用场景：
    1. 要求生产唯一序列号。
    2. WEB 中的计数器，不用每次刷新都在数据库里加一次，用单例先缓存起来。
    3. 创建的一个对象需要消耗的资源过多，比如 I/O 与数据库的连接等。

##### 实现

- 设计核心创建一个私有化构造方法。该类负责创建自己对象，无需他人创建。
- 提供访问唯一对象的方法，直接访问，无需实例化。
- 创建一个静态属性来存储创建自己的对象,保证对象只会被创建一次。
- 增加额外打印功能。
- 增加单元测试-单例模式
 ```
 package cn.yanwei.study.design.pattern.singleton;

/**
 * 单例模式：单例模式是java设计模式中最简单的模式之一，这种类型的设计模式属于创建型模式，它提供创建对象的最佳方式。
 * 这种模式设计一个单个类，该类负责创建自己的对象，同时保证对象只能被创建一次。这个类提供访问唯一对象的途径，直接创建，无需实例化对象。
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/13 15:59
 */
@SuppressWarnings("unused")
class Singleton {
    /**
     * 3、创建一个静态属性来存储创建自己的对象,保证对象只会被创建一次
     */
    private static Singleton instance = new Singleton();

    /**
     * 1、设计核心创建一个私有化构造方法。该类负责创建自己对象，无需他人创建
     */
    private Singleton() {

    }

    /**
     * 2、提供访问唯一对象的方法，直接访问，无需实例化
     */
    static Singleton getInstance() {
        return instance;
    }

    /**
     * 4、增加额外打印功能
     */
    void showMessage() {
        System.out.println("Hello World!");
    }
}
 ```
 ```
 package cn.yanwei.study.design.pattern.singleton;

import org.junit.jupiter.api.Test;

/**
 * 5、增加单元测试-单例模式
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/13 16:15
 */
@SuppressWarnings("unused")
class TestSingleton {
    @Test
    void run() {
        Singleton singleton = Singleton.getInstance();
        singleton.showMessage();
    }
}
 ```
##### 单例模式的几种实现方式
- 懒汉式，线程不安全。
- 懒汉式，线程安全。
- 饿汉式。
- 双检锁/双重校验锁
- 登记式/静态内部类
