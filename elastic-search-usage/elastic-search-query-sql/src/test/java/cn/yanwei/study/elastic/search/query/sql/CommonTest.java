package cn.yanwei.study.elastic.search.query.sql;

import cn.yanwei.study.elastic.search.query.sql.domain.Search;
import cn.yanwei.study.elastic.search.query.sql.executors.CsvResult;
import cn.yanwei.study.elastic.search.query.sql.executors.CsvResultsExtractor;
import cn.yanwei.study.elastic.search.query.sql.parse.AnswerParser;
import cn.yanwei.study.elastic.search.query.sql.query.EsActionFactory;
import com.qiniu.pandora.util.Auth;
import com.qiniu.pandora.util.Json;
import com.squareup.okhttp.*;
import org.elasticsearch.action.search.SearchResponse;
import org.junit.After;
import org.junit.Test;
import cn.yanwei.study.elastic.search.query.sql.query.QueryAction;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CommonTest {
    private static String sql = null;
    private static Auth auth;
    private static OkHttpClient httpClient;

    static {
        String accessKey = "xxxx";
        String secretKey = "xxxx";
        auth = Auth.create(accessKey, secretKey);
        httpClient = new OkHttpClient();
        httpClient.setRetryOnConnectionFailure(true);
        httpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        httpClient.setReadTimeout(300, TimeUnit.SECONDS);
        httpClient.setWriteTimeout(300, TimeUnit.SECONDS);
    }

    @After
    public void execute() throws Exception {
        System.out.println("sql=\n" + sql);
        QueryAction qa = EsActionFactory.create(sql);
        Search search = qa.explain();
        System.out.println(search.getQueryString());
        Response response = cstQueryds(search.getIndex(), search.getQueryString(), search.getStartStamp(), search.getEndStamp());
        SearchResponse searchResponse = AnswerParser.parseEntity(response, SearchResponse::fromXContent);
        CsvResult csvResult;
        if (searchResponse.getAggregations() != null) {
            csvResult = new CsvResultsExtractor().extractResults(searchResponse.getAggregations(), true, "\t");
            System.out.println("结果：\t" + Json.encode(csvResult));
        } else {
            csvResult = new CsvResultsExtractor().extractResults(searchResponse.getHits(), true, "\t");
            System.out.println("结果：\t" + Json.encode(csvResult));
        }
    }

    @Test
    public void testSelectStar() {
        sql = "select * from app1_retention";
    }

    @Test
    public void testSelectWhere() {
//        news_title,news_posttime
//        sql = "select count(*)  from app1_retention where news_title = '疫情' AND news_posttime >= '2020-03-09' LIMIT 20";
//        sql = "select news_posttime,news_title from app where (news_title = '疫情' OR news_content = '疫情') and news_posttime >= '2020-03-07 00:00:00'";
//        sql = "select news_posttime,news_title from app where news_title = TERMS(疫情) AND news_posttime >= '2020-03-09 00:00:00'";
//        sql = "select platform from app,web group by  platform";
//        sql = "select count(*) as 数量 from app,web group by range(news_read_count,20,25,30,35,40) order by 数量";
    }

    @Test
    public void testSelectGourp() {
//        sql = "select a,case when c='1' then 'haha' when c='2' then 'book' else 'hbhb' end as gg from tbl_a group by "; // order by a asc,c desc,d asc limit 8,12";
        sql = "select a, floor(num) my_b, case when os = 'iOS' then 'hehe' else 'haha' end as my_os from t_zhongshu_test";
    }

    @Test
    public void testSelectMethod() {
//        sql = "select a,case when c='1' then 'haha' when c='2' then 'book' else 'hbhb' end as gg from tbl_a group by "; // order by a asc,c desc,d asc limit 8,12";
//        sql = "select count(*) from web2_retention";
        sql = "select count(*)  as 数量 from app,web group by range(news_read_count,20,25,30,35,40) ";
        sql = "select news_posttime from app,web group by date_histogram(field='news_posttime','interval'='1.0h','format'='yyyy-MM-dd','min_doc_count'=5)";
//        sql ="select count(*) as 数量 from app,web group by platform,news_emotion order by 数量 LIMIT 1";
//        group by news_level
//        sql = "select * d from web2_retention group by news_level,news_level";
//        sql = "select news_positive,case when news_positive > 0.45 then '消极' else '积极' end as positive from weibo_202001 LIMIT 1";
    }

    /**
     * 使用七牛云的Es
     *
     * @param repos      索引
     * @param statement  语句
     * @param startStamp 时间戳
     * @param endStamp   时间戳
     * @return Reponse
     * @throws Exception 异常信息
     */
    private Response cstQueryds(String repos, String statement, Long startStamp, Long endStamp) throws Exception {
        repos = String.format("{\"index\":[\"%s\"],\"typed_keys\":true}\n", repos);
        statement = repos + statement;
        Map<String, Object> headers = new HashMap<>(16);
        headers.put("Content-Type", "text/plain");
        String serverTime = QiniuUtil.getServerTime();
        headers.put("Date", serverTime);
        String token = null;
        String url = "https://logdb.qiniu.com/v5/logdbkibana/msearch";
        try {
            token = auth.signRequest(url, "POST", headers, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaType type = MediaType.parse("Content-Type: text/plain");
        RequestBody requestBody = RequestBody.create(type, statement);
        url = "https://logdb.qiniu.com/v5/logdbkibana/msearch?typed_keys=true&start_time=" + startStamp + "&end_time=" + endStamp;
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", token)
                .header("Date", serverTime)
                .header("Content-Type", "text/plain")
                .post(requestBody)
                .build();
        Call call = httpClient.newCall(request);
        return call.execute();
    }
}
