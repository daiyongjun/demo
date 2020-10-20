package cn.yanwei.study.elastic.search.rest.client;

import cn.yanwei.study.elastic.search.rest.client.config.Configuration;
import cn.yanwei.study.elastic.search.rest.client.init.ElasticSearchHighTemplate;
import cn.yanwei.study.elastic.search.rest.client.init.ElasticSearchLowTemplate;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class ElasticSearchHighRestClientApplicationTests {
    private Configuration configuration;

    {
        this.configuration = new Configuration();
        this.configuration.setUsername("elastic");
        this.configuration.setPassword("");
        this.configuration.setHost("");
    }

    @Test
    public void contextLoads() throws IOException, InterruptedException {
        ElasticSearchHighTemplate searchTemplate = new ElasticSearchHighTemplate(configuration);
        String url = "web_202010";
        //创建使用固定线程数的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "\t开始");
                long startTime = System.currentTimeMillis();
                String rest = null;
                try {
                    rest = searchTemplate.query(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                System.out.println("耗时：\t" + (endTime - startTime) + "ms\t" + Thread.currentThread().getName() + "\t" + rest);
            });
        }
        //等待3分钟
        Thread.sleep(1000 * 60 * 3);
        searchTemplate.destroy();
    }
}
