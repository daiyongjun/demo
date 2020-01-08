package cn.yanwei.study.mockito.usage.modules;

import lombok.Data;

/**
 * POJO类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/25 9:54
 */
@Data
public class Person {
    private final int id;
    private final String name;
    public Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
