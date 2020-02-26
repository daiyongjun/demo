package cn.yanwei.study.elastic.resolver.sql.domain;

import lombok.Data;

/**
 * from描述类 主要包含查询的索引信息和类型
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/2/17 9:06
 */
@Data
class From {
    private String index;
    private String type;
    private String alias;
}
