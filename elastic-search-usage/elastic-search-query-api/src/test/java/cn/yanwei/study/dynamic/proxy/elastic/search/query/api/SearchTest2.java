package cn.yanwei.study.dynamic.proxy.elastic.search.query.api;

import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain.Criteria;
import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain.CriteriaQueryProcessor;
import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain.GeoBox;
import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain.GeoPoint;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


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
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        Criteria orCriteria = new Criteria();
//        orCriteria = orCriteria.or("news_title").phrase("邮政储蓄")
//                .or("news_digest").phrase("邮政储蓄")
//                .or("news_content").phrase("邮政储蓄");
//        Criteria criteria = new Criteria().and(orCriteria);
////                .and("news_media").is("app")
////                .and("news_posttime").between("2019-05-21 00:00:00", "2019-05-22 00:00:00");
//        CriteriaQueryProcessor processor = new CriteriaQueryProcessor();
//        QueryBuilder queryBuilder = processor.createQueryFromCriteria(criteria);
        Criteria orCriteria = new Criteria()
                .or("news_title").phrase("邮政储蓄")
                .or("news_digest").phrase("邮政储蓄")
                .or("news_content").phrase("邮政储蓄");
        Criteria criteria = new Criteria.OrCriteria().or(orCriteria);
        CriteriaQueryProcessor processor = new CriteriaQueryProcessor();
        QueryBuilder queryBuilder = processor.createQueryFromCriteria(criteria);
        System.out.println(queryBuilder.toString());
    }
}
