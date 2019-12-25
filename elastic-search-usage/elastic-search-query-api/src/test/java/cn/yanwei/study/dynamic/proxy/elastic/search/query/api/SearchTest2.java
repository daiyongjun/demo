package cn.yanwei.study.dynamic.proxy.elastic.search.query.api;

import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain.Criteria;
import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain.CriteriaQueryProcessor;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.jupiter.api.Test;


/**
 * 基本测试类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 16:20
 */
public class SearchTest2 {
    @Test
    void testMessageQueue() {
        Criteria criteria = new Criteria("hello").is("test AND dd");
        System.out.println(criteria);
        CriteriaQueryProcessor processor = new CriteriaQueryProcessor();
        QueryBuilder queryBuilder = processor.createQueryFromCriteria(criteria);
        System.out.println(queryBuilder.toString());
    }
    @Test
    void testMessageQueue1() {

    }
}
