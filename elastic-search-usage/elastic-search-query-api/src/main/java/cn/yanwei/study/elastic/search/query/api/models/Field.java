package cn.yanwei.study.elastic.search.query.api.models;

/**
 * 设置查询列的类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 15:58
 */
public interface Field {
    /**
     * Get the name of the field used in schema.xml of elasticsearch server
     *
     * @return String
     */
    String getName();
}
