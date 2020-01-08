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
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        Criteria criteria = Criteria.where("news_digest");
        GeoPoint geoPoint = new GeoPoint(10,20);
        GeoBox geoBox = new GeoBox(geoPoint,geoPoint);
        criteria = criteria.phrase("达能")
                .and("news_content").phrase("达能")
                .and("app_name").phrase("达能")
                .and("type").is("weibo")
                .and("news_posttime").between("2019-09-01 00:00:00","2019-10-01 00:00:00")
                .boundedBy(geoBox);
        CriteriaQueryProcessor processor = new CriteriaQueryProcessor();
        QueryBuilder queryBuilder = processor.createQueryFromCriteria(criteria);
        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_company")
                .field("name");
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.aggregation(aggregation);
        searchSourceBuilder.sort("news_content");
        searchSourceBuilder.fetchSource(new String[]{"news_content","app_name"},new String[]{});
        System.out.println(searchSourceBuilder.toString());
    }
}
