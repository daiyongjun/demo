package cn.yanwei.study.elastic.search.query.sql;

import cn.yanwei.study.elastic.search.query.sql.domain.Search;
import cn.yanwei.study.elastic.search.query.sql.executors.CsvResult;
import cn.yanwei.study.elastic.search.query.sql.executors.CsvResultsExtractor;
import cn.yanwei.study.elastic.search.query.sql.parse.AnswerParser;
import cn.yanwei.study.elastic.search.query.sql.query.EsActionFactory;
import com.alibaba.fastjson.JSON;
import com.qiniu.pandora.util.Auth;
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
        String accessKey = "xxx";
        String secretKey = "xxx";
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
        CsvResult csvResult = null;
        if (searchResponse.getAggregations() != null) {
            csvResult = new CsvResultsExtractor().extractResults(searchResponse.getAggregations(), false, "\t");
            System.out.println("结果：\t" + JSON.toJSON(csvResult.getHeaders()));
            System.out.println("结果：\t" + JSON.toJSON(csvResult.getLines()));
        } else {
            csvResult = new CsvResultsExtractor().extractResults(searchResponse.getHits(), false, "\t");
            System.out.println("结果：\t" + JSON.toJSON(csvResult.getHeaders()));
            System.out.println("结果：\t" + JSON.toJSON(csvResult.getLines()));
        }
    }

    @Test
    public void testSelectStar() {
        sql = "select * from weibo_202001 LIMIT 1000";
    }

    @Test
    public void testSelectWhere() {
        sql = "select news_title,news_posttime  from app1_retention where news_title = '疫情' AND news_posttime >= '2020-03-09' LIMIT 20";
    }

    @Test
    public void testSelectGourp() {
//        sql = "select a,case when c='1' then 'haha' when c='2' then 'book' else 'hbhb' end as gg from tbl_a group by "; // order by a asc,c desc,d asc limit 8,12";
        sql = "select a, floor(num) my_b, case when os = 'iOS' then 'hehe' else 'haha' end as my_os from t_zhongshu_test";
    }

    @Test
    public void testSelectMethod() {
//        sql = "select a,case when c='1' then 'haha' when c='2' then 'book' else 'hbhb' end as gg from tbl_a group by "; // order by a asc,c desc,d asc limit 8,12";
        sql = "select sum(news_level) d,avg(news_level) s from web2_retention";
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
        repos = String.format("{\"index\":[\"%s\"]}\n", repos);
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
        url = "https://nb-insight.qiniuapi.com/v5/logdbkibana/msearch?start_time=" + startStamp + "&end_time=" + endStamp;
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
