//package cn.yanwei.study.elastic.search.rest.client;
//
//import cn.yanwei.study.elastic.search.rest.client.config.Configuration;
//import cn.yanwei.study.elastic.search.rest.client.init.ElasticSearchTemplate;
//import org.junit.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@SpringBootTest
//public class ElasticSearchTest {
//    private Configuration configuration;
//
//    {
//        this.configuration = new Configuration();
//        this.configuration.setUsername("elastic");
//        this.configuration.setPassword("Y79p*#r*soyxSgMM");
//        this.configuration.setHost("es-cn-v6419r5a30003cjkh.elasticsearch.aliyuncs.com:9200");
//    }
//
//    @Test
//    public void contextLoads() throws InterruptedException {
//        ElasticSearchTemplate searchTemplate = new ElasticSearchTemplate(configuration);
//        String url = "/web_202010/_search?ignore_unavailable=true&typed_keys=true";
//        //创建使用固定线程数的线程池
//        ExecutorService executorService = Executors.newFixedThreadPool(100);
//        for (int i = 0; i < 100; i++) {
//            executorService.submit(() -> {
//                System.out.println(Thread.currentThread().getName() + "\t开始");
//                long startTime = System.currentTimeMillis();
//                String rest = searchTemplate.query("{\"size\":0}", url, null);
//                long endTime = System.currentTimeMillis();
//                System.out.println("耗时：\t" + (endTime - startTime) + "ms\t" + Thread.currentThread().getName() + "\t" + rest.substring(0, 100));
//            });
//        }
//        //等待3分钟
//        Thread.sleep(1000 * 60 * 3);
//        searchTemplate.destroy();
//    }
//}
