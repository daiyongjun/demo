package cn.yanwei.study.elastic.search.query.sql.domain;

import lombok.Data;

/**
 * 转换后的查询类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/2/27 14:35
 */
@Data
public class Search {
    private String queryString;
    private String index;
}
