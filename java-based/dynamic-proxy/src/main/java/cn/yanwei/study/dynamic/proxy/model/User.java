package cn.yanwei.study.dynamic.proxy.model;

import lombok.Data;

/**
 * POJO类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/19 15:25
 */
@Data
public class User {
    private Integer id;
    private String name;
    private int age;

    public User(Integer id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
