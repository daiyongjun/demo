package cn.yanwei.study.elastic.search.query.api;

import cn.yanwei.study.elastic.search.query.api.domain.CriteriaQueryProcessor;
import cn.yanwei.study.elastic.search.query.api.models.Criteria;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;


/**
 * 基本测试类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 16:20
 */
class SearchTest {
    @Test
    void testMessageQueue() {
        Criteria andCriteria = new Criteria()
                .and("news_title").phrase("九牧")
                .and("news_title").phrase("安装");
        Criteria andCriteria1 = new Criteria()
                .and("news_content").phrase("九牧")
                .and("news_content").phrase("安装");
        Criteria criteria = new Criteria().or(andCriteria);
        criteria = criteria.or(andCriteria1);
        criteria = criteria.and("news_media").is("wx");
        criteria = criteria.and("news_posttime").between("2018-03-20T00:00:00", "2018-03-23T00:00:00");
        CriteriaQueryProcessor processor = new CriteriaQueryProcessor();
        QueryBuilder queryBuilder = processor.createQueryFromCriteria(criteria);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        String source = "news_media,app_name,news_channel,news_title,news_digest,news_positive,news_posttime,news_url,content_city,content_province,news_read_count,news_comment_count,news_is_origin,media_type,origin_author_name,news_uuid,sim_hash,news_keywords_list,news_province,news_city,author_gender,news_negative,news_postdate";
        searchSourceBuilder.fetchSource(source.split(","), new String[]{});
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1);
        searchSourceBuilder.sort("news_posttime");
//        TermsAggregationBuilder aggregation = AggregationBuilders.terms("time")
//                .field("news_posttime");
        DateHistogramAggregationBuilder aggregation = AggregationBuilders.dateHistogram("time").field("news_posttime").dateHistogramInterval(DateHistogramInterval.days(1));
        searchSourceBuilder.aggregation(aggregation);
        System.out.println(searchSourceBuilder.toString());
    }

    @Test
    void testMessageQueue1() {
        Criteria orCriteria = new Criteria()
                .or("news_title").phrase("雅诗兰黛高能小棕瓶")
                .or("news_content").phrase("雅诗兰黛高能小棕瓶");
        Criteria criteria = new Criteria().and(orCriteria);
        criteria = criteria.and("news_media").is("wx");
        criteria = criteria.and("news_posttime").between("2019-06-01T00:00:36", "2019-12-21T14:02:36");
        CriteriaQueryProcessor processor = new CriteriaQueryProcessor();
        QueryBuilder queryBuilder = processor.createQueryFromCriteria(criteria);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        String source = "news_media,app_name,news_channel,news_title,news_digest,news_positive,news_posttime,news_url,content_city,content_province,news_read_count,news_comment_count,news_is_origin,media_type,origin_author_name,news_uuid,sim_hash,news_keywords_list,news_province,news_city,author_gender,news_negative,news_postdate";
        searchSourceBuilder.fetchSource(source.split(","), new String[]{});
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1);
        searchSourceBuilder.sort("news_posttime");
//        TermsAggregationBuilder aggregation = AggregationBuilders.terms("time")
//                .field("news_posttime");
        DateHistogramAggregationBuilder aggregation = AggregationBuilders.dateHistogram("time").field("news_posttime").dateHistogramInterval(DateHistogramInterval.MONTH);
        searchSourceBuilder.aggregation(aggregation);
        System.out.println(searchSourceBuilder.toString());

    }


    @Test
    void testMessageQueue2() {
        Criteria criteria = new Criteria().or(new Criteria().and("news_title").phrase("vivo").and("news_title").phrase("oppo")).or(new Criteria().and("news_content").phrase("vivo").and("news_content").phrase("oppo")).and("news_posttime").between("2019-01-01T00:00:00", "2020-01-11T00:00:00");
        CriteriaQueryProcessor processor = new CriteriaQueryProcessor();
        QueryBuilder queryBuilder = processor.createQueryFromCriteria(criteria);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
//        String source = "news_title,news_content,news_posttime";
//        searchSourceBuilder.fetchSource(source.split(","), new String[]{});
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1);
        System.out.println(searchSourceBuilder.toString());
    }
}
