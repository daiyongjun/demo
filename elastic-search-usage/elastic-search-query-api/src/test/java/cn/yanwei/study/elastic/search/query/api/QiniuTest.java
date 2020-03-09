package cn.yanwei.study.elastic.search.query.api;

import cn.yanwei.study.elastic.search.query.api.domain.CriteriaQueryProcessor;
import cn.yanwei.study.elastic.search.query.api.models.Criteria;
import com.qiniu.pandora.common.PandoraClient;
import com.qiniu.pandora.common.PandoraClientImpl;
import com.qiniu.pandora.logdb.LogDBClient;
import com.qiniu.pandora.logdb.search.MultiSearchService;
import com.qiniu.pandora.util.Auth;
import com.qiniu.pandora.util.Json;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.Strings;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试七牛最新api
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/3/8 16:07
 */
public class QiniuTest {
    private CriteriaQueryProcessor processor = new CriteriaQueryProcessor();
    private MultiSearchService multiSearchService;

    @Before
    public void setUp() {
        String ak = "vWJHmggrexK0fTCIbxEcQ8RKQAMI1euJ1897pMZv";
        String sk = "PB4sniup05vNjSWA32emGU8BY_mAjxnpC1AWMODG";
        if (Strings.isNullOrEmpty(ak)){
            ak = System.getenv("QINIU_ACCESS_KEY");
            sk = System.getenv("QINIU_SECRETY_KEY");
        }
        Auth auth = Auth.create(ak,sk);
        PandoraClient client = new PandoraClientImpl(auth);
        LogDBClient logDBClient = new LogDBClient(client);
        this.multiSearchService = logDBClient.NewMultiSearchService();
    }
    @Test
    public void qiniu() throws Exception{
        MultiSearchRequest request = new MultiSearchRequest();
        SearchRequest firstSearchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = condition("app","合肥");
        firstSearchRequest.source(searchSourceBuilder);
        firstSearchRequest.indices("app1_retention");
        System.out.println(searchSourceBuilder.toString());
        request.add(firstSearchRequest);
        MultiSearchResponse searchResult = multiSearchService.multiSearch(request);
        for(MultiSearchResponse.Item searchs: searchResult.getResponses()){
            SearchResponse searchResponse = searchs.getResponse();
            System.out.println(Json.encode(searchResponse.getHits()));
            System.out.println(Json.encode(searchResponse.getAggregations()));
        }
    }

    private SearchSourceBuilder condition(String repo, String city){
        Criteria criteria = new Criteria("news_title").phrase(city);
        if ("toutiao".equals(repo)) {
            criteria = criteria.and("app_uuid").is("11");
        } else if ("app".equals(repo)) {
            criteria = criteria.not("app_uuid").is("11");
        } else {
            criteria = criteria.and("news_media").is(repo);
        }
        criteria = criteria.and("news_posttime").between("2019-01-01T00:00:00", "2019-12-31T00:00:00");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(processor.createQueryFromCriteria(criteria));
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(1);
//        RangeAggregationBuilder rangeAggregationBuilder = AggregationBuilders.range("news_positive").field("news_positive");
//        rangeAggregationBuilder.addUnboundedTo("负面", 0.45);
//        rangeAggregationBuilder.addRange("正常", 0.45, 0.75);
//        rangeAggregationBuilder.addUnboundedFrom("消极", 0.75);
//        searchSourceBuilder.aggregation(rangeAggregationBuilder);
        return searchSourceBuilder;
    }
}
