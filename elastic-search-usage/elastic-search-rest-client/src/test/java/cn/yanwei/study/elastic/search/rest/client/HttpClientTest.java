package cn.yanwei.study.elastic.search.rest.client;

import cn.yanwei.study.elastic.search.rest.client.init.RestClientTemplate;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试httpClient
 * <p>
 * 参考文档： http://hc.apache.org/httpcomponents-client-ga/tutorial/html/
 * https://www.cnblogs.com/hujunzheng/p/11629198.html
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/10/27 11:55
 */
public class HttpClientTest {

    @Test
    public void execute() throws Exception {
        RestClientTemplate restClientTemplate = new RestClientTemplate();

        CloseableHttpAsyncClient restClient = restClientTemplate.getClient();
        try {
            restClient.start();
            int count = 60;
            final CountDownLatch countDownLatch = new CountDownLatch(count);
            ExecutorService executorService = Executors.newFixedThreadPool(count);
            for (int i = 0; i < count; i++) {
                executorService.submit(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t开始");
                    try {
                        restClientTemplate.query("http://www.apache.org/", countDownLatch);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
            countDownLatch.await();
        } finally {
            restClient.close();
        }
    }
}
