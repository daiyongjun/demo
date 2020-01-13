

>[CSDN：Elasticsearch 6.4基本操作 - Java版](https://www.cnblogs.com/swordfall/p/9981883.html "引用文章")



>[Elasticsearch Guide：Java Low Rest Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-low.html "Java Low Rest Client")

>[Elasticsearch Guide：High Level REST Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html "High Level REST Client")

>[简书：Win10中Docker安装Elasticsearch](https://www.jianshu.com/p/9698cc75e00c "Docker安装Elasticsearch")

>[书籍：精通Elastic Stack](https://book.douban.com/subject/30326542/ "精通Elastic Stack")

### 1. Elasticsearch Java API有四类client连接方式
- [TransportClient](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch") 
- [RestClient](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")
- [Jest](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")
- [Spring Data Elasticsearch](https://docs.spring.io/spring-data/elasticsearch/docs/3.2.3.RELEASE/reference/html/#reference "Spring Data Elasticsearch")

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
 ```
#下载并安装
docker search elasticsearch:6.4.3
docker pull elasticsearch:6.4.3
docker run -di --name=myes -p 9200:9200 -p 9300:9300 elasticsearch:6.4.3

#将ES的配置文件复制到本地系统文件中，方便修改和使用
docker cp myes:/usr/share/elasticsearch/config/elasticsearch.yml G:/docker/elasticsearch.yml
docker run -di --name=myes -p 9200:9200 -p 9300:9300 -v G:/docker/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml elasticsearch:6.4.3

curl http://127.0.0.1:9200/
{
  "name": "Qc-7L_W",
  "cluster_name": "docker-cluster",
  "cluster_uuid": "vEVexvOKSdOaScZM8NebpA",
  "version": {
    "number": "6.4.3",
    "build_flavor": "default",
    "build_type": "tar",
    "build_hash": "fe40335",
    "build_date": "2018-10-30T23:17:19.084789Z",
    "build_snapshot": false,
    "lucene_version": "7.4.0",
    "minimum_wire_compatibility_version": "5.6.0",
    "minimum_index_compatibility_version": "5.0.0"
  },
  "tagline": "You Know, for Search"
}
#安装elasticsearch 插件 ex：install elasticsearch-sql 由于es的插件国内网络无法直接访问于是就创建一个本地的文件服务器
#docker安装本地文件服务器
docker run --name nginx -d -p 7070:7070
#如安装es一样 copy nginx.conf
docker exec -it nginx bash
#重新删除nginx容器，重新启动容器
docker run --name nginx -d -p 7070:7070 -v G:\docker\nginx.conf:/etc/nginx/nginx.conf -v G:\docker\tools\files:/home/files nginx
#nginx.conf
server {
    listen       7070;
    server_name  localhost;
    charset utf-8;
    location /files {
        #在docker内nginx的目录
        alias /home/files;
        expires 1d;
        allow all;
        autoindex on;
    }
}
#安装插件
docker exec -it myes /bin/bash
#替换安装命令如下：
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.5.0/elasticsearch-analysis-ik-6.5.0.zip

./bin/elasticsearch-plugin install http://192.168.124.162:7070/files/elasticsearch-sql-6.4.3.0.zip
 ```


###### 初始化
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
 RestClient实例可以通过相应的内置 RestClientBuilder类，通过创建RestClient#builder(HttpHost...) 静态方法。唯一必需的参数是客户端将与之通信的一个或多个主机，以HttpHost的实例形式 提供 ，如下所示：
```
RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http"),new HttpHost("localhost", 9201, "http")).build();
```
RestClient该类是线程安全的，并且理想情况下与使用该类的应用程序具有相同的生命周期。重要的是，在不再需要它时将其关闭，以便正确释放它使用的所有资源以及基础的HTTP客户端实例及其线程：
```
restClient.close();
```
设置一个侦听器，该侦听器在每次节点发生故障时得到通知，以防需要采取措施。启用嗅探失败时在内部使用。
```
RestClientBuilder builder = RestClient.builder(
    new HttpHost("localhost", 9200, "http"));
Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
builder.setDefaultHeaders(defaultHeaders);
```
 
 RequestConfigCallback和HttpClientConfigCallback ，允许对
![image](0C201E44AB5743F281C65CA322B196E6)


#### spring data elastic search
String... args 为java1.5版本后引入的新特性
其实就是String[] args的简写版。

SimpleElasticsearchRepository