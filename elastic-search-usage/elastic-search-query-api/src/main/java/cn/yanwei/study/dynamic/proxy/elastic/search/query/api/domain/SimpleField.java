package cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 16:06
 */
public class SimpleField implements Field {

    private final String name;

    public SimpleField(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}