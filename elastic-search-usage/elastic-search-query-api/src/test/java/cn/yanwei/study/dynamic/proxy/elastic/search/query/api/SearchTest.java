//package cn.yanwei.study.dynamic.proxy.elastic.search.query.api;
//
//import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.domain.ElasticBaseSearch;
//import cn.yanwei.study.dynamic.proxy.elastic.search.query.api.model.EsModel;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.common.unit.Fuzziness;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.junit.jupiter.api.Test;
//
//class SearchTest {
//    @Test
//    void testMessageQueue() throws IllegalAccessException {
//        EsModel es = new EsModel();
//        es.setId(1);
//        es.setName("name");
//        es.setPhone("1233221344");
//        System.out.println("查询参数," + es.toString());
//        QueryBuilder qb = ElasticBaseSearch.getInstance().getQueryBuilder(es);
//        System.out.println("拼装的查询参数是:" + qb.toString());
//
//        es = new EsModel();
//        es.setId(12);
//        es.setName("name2");
//        System.out.println("查询参数," + es.toString());
//        qb = ElasticBaseSearch.getInstance().getQueryBuilder(es);
//        System.out.println("拼装的查询参数是:" + qb.toString());
//
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_company")
//                .field("company.keyword");
//        aggregation.subAggregation(AggregationBuilders.avg("average_age")
//                .field("age"));
//        System.out.println(aggregation.toString());
//
//        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("user", "kimchy")
//                .fuzziness(Fuzziness.AUTO)
//                .prefixLength(3)
//                .maxExpansions(10);
//        searchSourceBuilder.query(matchQueryBuilder);
//        searchSourceBuilder.aggregation(aggregation);
//        SearchRequest searchRequest = new SearchRequest();
//        searchRequest.source(searchSourceBuilder);
//        System.out.println("最终结果:\t" + searchSourceBuilder.toString());
////		SearchResponse searchResponse = ElasticBaseSearch.getInstance().getIndexAndType(null);
//
//    }
//
//}