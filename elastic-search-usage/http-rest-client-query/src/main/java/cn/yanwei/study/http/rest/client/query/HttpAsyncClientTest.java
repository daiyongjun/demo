//package cn.yanwei.study.http.rest.client.query;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.concurrent.FutureCallback;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
//import org.apache.http.impl.nio.client.HttpAsyncClients;
//import org.apache.http.util.EntityUtils;
//
//import java.io.IOException;
//import java.util.concurrent.CountDownLatch;
//
///**
// * http客户端异步测试
// *
// * @author daiyongjun
// * @version 1.0
// * Created on date: 2020/11/3 12:02
// */
//public class HttpAsyncClientTest {
//    public static void main(String[] args) throws InterruptedException, IOException {
//
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpget = new HttpGet("http://www.baidu.com/");
//        CloseableHttpResponse response = httpClient.execute(httpget);
//        try {
//            HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                long len = entity.getContentLength();
//                if (len != -1 && len < 2048) {
//                    System.out.println(EntityUtils.toString(entity));
//                } else {
//                    // Stream content out
//                }
//            }
//        } finally {
//            response.close();
//        }
//
//
//        CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
//        try {
//            httpClient.start();
//            int count = 1;
//            final CountDownLatch countDownLatch = new CountDownLatch(count);
//            final HttpGet request = new HttpGet("http://www.apache.org/");
//
//
//            httpClient.execute(request, new FutureCallback<HttpResponse>() {
//
//                public void completed(final HttpResponse response) {
//                    countDownLatch.countDown();
//                    System.out.println(request.getRequestLine() + "->" + response.getStatusLine());
//                }
//
//                public void failed(final Exception e) {
//                    countDownLatch.countDown();
//                    System.out.println(request.getRequestLine() + "->" + e);
//                }
//
//                public void cancelled() {
//                    countDownLatch.countDown();
//                    System.out.println(request.getRequestLine() + " cancelled");
//                }
//            });
//            countDownLatch.await();
//        } finally {
//            httpClient.close();
//        }
//    }
//}
