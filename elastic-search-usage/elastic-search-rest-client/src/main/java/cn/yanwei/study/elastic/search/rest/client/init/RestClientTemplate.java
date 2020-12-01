package cn.yanwei.study.elastic.search.rest.client.init;

import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.nio.reactor.IOReactorException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * rest-client的客户端
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/11/3 10:18
 */
@Data
public class RestClientTemplate {
    private CloseableHttpAsyncClient client;

    private CloseableHttpAsyncClient httpClient;


    public RestClientTemplate() {
        //配置io线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom().
                setIoThreadCount(Runtime.getRuntime().availableProcessors() * 2)
                .setSoKeepAlive(true)
                .build();
        //配置请求参数
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(50000)
                .setSocketTimeout(50000)
                .setConnectionRequestTimeout(1000)
                .build();


        //设置连接池大小
        ConnectingIOReactor ioReactor = null;
        try {
            ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
        } catch (IOReactorException e) {
            e.printStackTrace();
        }
        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(200);

        this.client = HttpAsyncClients.custom().setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig).build();


        httpClient = HttpAsyncClients.createDefault();
    }

    public void query(String url, CountDownLatch countDownLatch) throws InterruptedException {
        httpClient.start();
        final HttpGet request = new HttpGet("http://www.apache.org/");

        Future<HttpResponse> future = httpClient.execute(request,null);
        // and wait until a response is received
        HttpResponse response1 = null;
        try {
            response1 = future.get();
            System.out.println(request.getRequestLine() + "->" + response1.getStatusLine());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        httpClient.execute(request, new FutureCallback<HttpResponse>() {
            private long start = System.currentTimeMillis();

            @Override
            public void completed(final HttpResponse response) {
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + "->" + "cost is:" + (System.currentTimeMillis() - start) + "->" + request.getRequestLine() + "->" + response.getStatusLine());
            }

            @Override
            public void failed(final Exception e) {
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + "->" + "cost is:" + (System.currentTimeMillis() - start) + "->" + request.getRequestLine() + "->" + e);
            }

            @Override
            public void cancelled() {
                countDownLatch.countDown();
                System.out.println(Thread.currentThread().getName() + "->" + "cost is:" + (System.currentTimeMillis() - start) + "->" + request.getRequestLine() + " cancelled");
            }
        });
        countDownLatch.await();
    }
}
