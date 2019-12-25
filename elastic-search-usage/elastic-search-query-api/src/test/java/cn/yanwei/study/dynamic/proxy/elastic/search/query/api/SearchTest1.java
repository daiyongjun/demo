//package cn.yanwei.study.dynamic.proxy.elastic.search.query.api;
//
//import org.elasticsearch.index.query.BoolQueryBuilder;
//import org.elasticsearch.index.query.QueryBuilder;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.aggregations.AggregationBuilders;
//import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.elasticsearch.search.sort.ScoreSortBuilder;
//import org.elasticsearch.search.sort.SortOrder;
//import org.junit.jupiter.api.Test;
//
//import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
//import static org.elasticsearch.index.query.QueryBuilders.termQuery;
//
//class SearchTest1 {
//    @Test
//    void testMessageQueue() {
//        //增加过滤-去除默认参数
//        String[] filters = new String[]{"\"operator\":\"OR\",", "\"fuzziness\":\"AUTO\",", "\"prefix_length\":3,", "\"max_expansions\":10,", "\"fuzzy_transpositions\":true,", "\"lenient\":false,", "\"zero_terms_query\":\"NONE\",", "\"auto_generate_synonyms_phrase_query\":true,", ",\"boost\":1.0"};
//        //检索所有
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
//        String search = searchSourceBuilder.toString();
////        for (String filter : filters) {
////            search = search.replaceAll(filter, "");
////        }
//        System.out.println(search);
//
//        //term进行检索
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.termQuery("user", "kimchy"));
//        sourceBuilder.from(0);
//        sourceBuilder.size(5);
//        search = sourceBuilder.toString();
//        System.out.println(search);
//
//        //match进行检索
//        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("user", "kimchy");
//        sourceBuilder.query(matchQueryBuilder);
//        sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
////        sourceBuilder.sort(new FieldSortBuilder("_id").order(SortOrder.ASC));
//        String[] includeFields = new String[]{"title", "innerObject.*"};
//        String[] excludeFields = new String[]{"user"};
//        sourceBuilder.fetchSource(includeFields, excludeFields);
//        search = sourceBuilder.toString();
//        System.out.println(search);
//
//        //增加bool条件
//        BoolQueryBuilder boolQueryBuilder = boolQuery().should(termQuery("user", "kimchy"));
//        boolQueryBuilder.should(termQuery("user", "kimchy1"));
//        sourceBuilder.query(boolQueryBuilder);
//        search = sourceBuilder.toString();
//        System.out.println(search);
//
//        //聚合
//        TermsAggregationBuilder aggregation = AggregationBuilders.terms("by_company")
//                .field("company.keyword");
//        aggregation.subAggregation(AggregationBuilders.avg("average_age")
//                .field("age"));
//        sourceBuilder.aggregation(aggregation);
//        search = sourceBuilder.toString();
//
//
//
//        System.out.println(search);
//
//    }
//
//}
