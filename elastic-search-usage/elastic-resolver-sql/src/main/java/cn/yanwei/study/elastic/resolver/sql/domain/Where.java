package cn.yanwei.study.elastic.resolver.sql.domain;

/**
 * where描述类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/2/17 9:07
 */
class Where {
    /**
     * where条件的逻辑关系
     */
    public enum Conn {
        AND, OR;

        public Conn negative() {
            return this == AND ? OR : AND;
        }
    }



}
