

>[CSDN：Elasticsearch 6.4基本操作 - Java版](https://www.cnblogs.com/swordfall/p/9981883.html "引用文章")

>[Elasticsearch Guide：High Level REST Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html "High Level REST Client")



### 1. Elasticsearch Java API有四类client连接方式
- [TransportClient](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch") 
- [RestClient](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")
- [Jest](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")
- [Spring Data Elasticsearch](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")

#### 概述
**TransportClient** 和  **Rest Client** 都为Elasticsearch原生的api，官方维护，两者不同在于前者使用 [**Java序列化请求**](https://www.runoob.com/java/java-serialization.html "java序列化请求")，后者使用http请求。
个人理解：对象序列化概括的理解其实是将Java对象表示为字节序列，序列依然存储着对象类型和数据，Java序列化请求和http请求的更替，其实就是json作为数据传输媒介的胜利。**TransportClient** 将会在Elasticsearch 7.0弃用并在8.0中完成删除。所以本篇幅暂时不会描述**TransportClient**。

**Jest** 是Java社区开发的，是Elasticsearch的Java Http Rest客户端。**Spring Data Elasticsearch** 是spring集成的Elasticsearch开发包。

#### RestClient
 ##### 概述
ElasticSearch版本将主要使 **Rest Client**操作数据，
 **Rest Client**分为 **Java Low REST Client** 和 **Java High Level REST Client**。
 
 ##### RestClient Usage
 ###### 使用docker安装Elasticsearch 6.4.3版本
 
 maven的依赖包：
 ```
<elasticsearch.version>6.4.3</elasticsearch.version>

 <dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-client</artifactId>
    <version>${elasticsearch.version}</version>
    <scope>compile</scope>
</dependency>
 ```
 
 
