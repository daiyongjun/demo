

>[Elasticsearch 6.4基本操作 - Java版](https://www.cnblogs.com/swordfall/p/9981883.html "引用文章")

### 1. Elasticsearch Java API有四类client连接方式
- [TransportClient](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch") 
- [RestClient](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")
- [Jest](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")
- [Spring Data Elasticsearch](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")

TransportClient和RestCliet都为Elasticsearch原生的api，两者不同在于前者使用[java序列化请求](https://www.runoob.com/java/java-serialization.html "java序列化请求")，后者使用http请求。
个人理解：对象序列化概括的理解其实是将java对象表示为字节序列，序列依然存储着对象类型和数据，java序列化请求和http请求的更替，其实就是json作为数据传输媒介的胜利。TransportClient将会在Elasticsearch 7.0弃用并在8.0中完成删除。所以本篇幅暂时不会描述TransportClient。


## java api 学习
