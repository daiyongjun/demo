package cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 15:58
 */
public interface Field {
    /**
     * Get the name of the field used in schema.xml of elasticsearch server
     *
     * @return
     */
    String getName();
}
