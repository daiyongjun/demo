package cn.yanwei.study.elastic.search.rest.client.init;

import cn.yanwei.study.elastic.search.rest.client.config.Configuration;
import lombok.Data;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 初始化高阶RestClient
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/10/19 14:46
 */
@Data
public class ElasticSearchHighTemplate {
    /**
     * 高阶RestClient实例
     */
    private RestHighLevelClient restHighLevelClient;

    /**
     * ElasticSearchTemplate构造方法
     *
     * @param configuration 配置文件信息
     */
    public ElasticSearchHighTemplate(Configuration configuration) {
        // HTTP 的身份认证
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        // 认证的用户名和密码
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(configuration.getUsername(), configuration.getPassword()));
        // 高阶的rest依赖于低阶的API

        RestClientBuilder builder = RestClient.builder(setHttpHosts(configuration.getHost()));
        builder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider).setMaxConnTotal(100));
        RestHighLevelClient client = new RestHighLevelClient(builder);
    }

    /**
     * 根据初始化hosts列表，配置httpHosts数组
     *
     * @return HttpHost[]
     */
    @SuppressWarnings("all")
    private HttpHost[] setHttpHosts(String host) {
        HttpHost[] httpHosts = null;
        if (host != null) {
            String[] hosts = host.split(",");
            int size = hosts.length;
            httpHosts = new HttpHost[size];
            for (int i = 0; i < size; i++) {
                String[] tmp = hosts[i].split(":");
                HttpHost newHttpHost = new HttpHost(tmp[0], Integer.valueOf(tmp[1]), "http");
                httpHosts[i] = newHttpHost;
            }
        }
        return httpHosts;
    }


    /**
     * 基础的查询条件
     *
     * @param query 查询条件
     * @return String
     * @throws IOException 异常信息
     */
    public String query(String query) throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        SearchRequest searchRequest = new SearchRequest(query);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.size(1);
        searchRequest = searchRequest.source(sourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest);
        TimeValue took = response.getTook();
        return took.getStringRep();
    }

    /**
     * 手动关闭restClient
     */
    public void destroy() {
        if (this.restHighLevelClient != null) {
            try {
                this.restHighLevelClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
