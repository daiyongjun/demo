package cn.yanwei.study.elastic.search.rest.client.init;

import cn.yanwei.study.elastic.search.rest.client.config.Configuration;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 初始胡RestClient
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/10/19 14:46
 */
@Data
public class ElasticSearchLowTemplate {
    /**
     * restClient实例
     */
    private RestClient restClient;

    /**
     * ElasticSearchTemplate构造方法
     *
     * @param configuration 配置文件信息
     */
    public ElasticSearchLowTemplate(Configuration configuration) {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(configuration.getUsername(), configuration.getPassword());
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        RestClientBuilder restClientBuilder = RestClient.builder(setHttpHosts(configuration.getHost()));
        restClientBuilder = restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider).setMaxConnTotal(10));
        //Set the default headers
//        Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
//        restClientBuilder.setDefaultHeaders(defaultHeaders);
        // The default value is 30 seconds, same as the default socket timeout
        //同一请求进行多次尝试时应遵守的超时时间
//        restClientBuilder.setMaxRetryTimeoutMillis(10000);
//        //设置socket timeout的时间
//        restClientBuilder.setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(10000));
        //设置proxy 回调
//        restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setProxy(new HttpHost("proxy", 9000, "http")));
        this.restClient = restClientBuilder.build();
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
     * @param url   查询url
     * @return String
     */
    public String query(String query, String url, String type) {
        // 配置请求参数
        Map<String, String> paramMap = new HashMap<>(10);
        paramMap.put("pretty", "true");
        HttpEntity entity = new NStringEntity(query, ContentType.APPLICATION_JSON);
        Request request = new Request("GET", url);
        for (Map.Entry<String, String> stringStringEntry : paramMap.entrySet()) {
            request.addParameter(stringStringEntry.getKey(), stringStringEntry.getValue());
        }
        request.setEntity(entity);
        String rest = "";
        if (type != null) {
            ResponseListener responseListener = new ResponseListener() {
                @Override
                public void onSuccess(Response response) {
                    try {
                        String rest = EntityUtils.toString(response.getEntity());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Exception exception) {

                }
            };
            restClient.performRequestAsync(request, responseListener);
        } else {
            Response indexResponse;
            try {
                indexResponse = restClient.performRequest(request);
                rest = EntityUtils.toString(indexResponse.getEntity());
            } catch (Exception e) {
                System.err.println("client重置处理:\t" + e.getMessage());
                rest = "";
            }
        }
        return rest;
    }

    /**
     * 手动关闭restClient
     */
    public void destroy() {
        if (this.restClient != null) {
            try {
                this.restClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
