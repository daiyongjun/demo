package cn.yanwei.study.dynamic.proxy.design.pattern.singleton;

/**
 * 单例模式：单例模式是java设计模式中最简单的模式之一，这种类型的设计模式属于创建型模式，它提供创建对象的最佳方式。
 * 这种模式设计一个单个类，该类负责创建自己的对象，同时保证对象只能被创建一次。这个类提供访问唯一对象的途径，直接创建，无需实例化对象。
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/13 15:59
 */
@SuppressWarnings("unused")
class Singleton1 {
    /**
     * 3、创建一个静态属性来存储创建自己的对象,保证对象只会被创建一次
     */
    private static Singleton1 instance = new Singleton1();

    /**
     * 1、设计核心创建一个私有化构造方法。该类负责创建自己对象，无需他人创建
     */
    private Singleton1() {

    }

    /**
     * 2、提供访问唯一对象的方法，直接访问，无需实例化
     */
    static Singleton1 getInstance() {
        return instance;
    }

    /**
     * 4、增加额外打印功能
     */
    void showMessage() {
        System.out.println("Hello World!");
    }
}
