package cn.yanwei.study.dynamic.proxy.elastic.search.client.java.client;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.NodeSelector;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;

/**
 * 构建ElasticClients类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/13 11:26
 */
public class ElasticClients {
    /**
     * 借鉴单例模式,保证clients中只有一个客户端 实现:使用懒汉的线程安全模式
     */
    private volatile static RestClient client;


    public static synchronized RestClient getRestClient() {
        if (client == null) {
            client = instanceRestClient();
        }
        return client;
    }


    private static RestClient instanceRestClient() {
        /**
         * 
         */
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http"),
                new HttpHost("localhost", 9201, "http")).build();

        RestClientBuilder builder = RestClient.builder(
                new HttpHost("localhost", 9200, "http"));
        Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
        builder.setDefaultHeaders(defaultHeaders);


        RestClientBuilder builder1 = RestClient.builder(
                new HttpHost("localhost", 9200, "http"));
        builder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {

            }
        });

        RestClientBuilder builder2 = RestClient.builder(
                new HttpHost("localhost", 9200, "http"));
        builder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);

        RestClientBuilder builder3 = RestClient.builder(
                new HttpHost("localhost", 9200, "http"));
        builder.setRequestConfigCallback(
                new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(
                            RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder.setSocketTimeout(10000);
                    }
                });

        RestClientBuilder builder4 = RestClient.builder(
                new HttpHost("localhost", 9200, "http"));
        builder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(
                    HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setProxy(
                        new HttpHost("proxy", 9000, "http"));
            }
        });
        return null;
    }

    public void closeClient() {
        try {
            if (client != null) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * document API 主要是些简单的增删改查操作
     */
    public void documentApi() {
        //...
    }

    /**
     * Search API 主要是些复杂查询操作
     */
    public void searchApi() {
        //...
    }
}
