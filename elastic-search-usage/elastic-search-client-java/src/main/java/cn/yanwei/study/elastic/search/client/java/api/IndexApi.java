package cn.yanwei.study.elastic.search.client.java.api;//package cn.yanwei.study.elastic.search.client.java.api;
//
///**
// * Index API
// *
// * @author daiyongjun
// * @version 1.0
// * Created on date: 2019/12/14 14:59
// */
//public class IndexApi {
//
//    public void IndexRequest(){
//        IndexRequest request = new IndexRequest("posts");
//        request.id("1");
//        String jsonString = "{" +
//                "\"user\":\"kimchy\"," +
//                "\"postDate\":\"2013-01-30\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}";
//        request.source(jsonString, XContentType.JSON);
//    }
//}
