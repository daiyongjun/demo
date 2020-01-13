### 提要
当阅读本书时，将会遇到有关 Elasticsearch 分布式特性的补充章节。这些章节将介绍有关集群扩容、故障转移(集群内的原理) 、应对文档存储(分布式文档存储) 、执行分布式搜索(执行分布式检索) ，以及分区（shard）及其工作原理(分片内部原理) 。以下内容会以章节内容**集群内的原理**、**分布式文档存储**、**执行分布式检索**、**分片内部原理**为内容进行说明。
>[Elasticsearch: 权威指南 集群内的原理](https://www.elastic.co/guide/cn/elasticsearch/guide/current/distributed-cluster.html "集群内的原理")

>[Elasticsearch: 权威指南 分布式文档存储](https://www.elastic.co/guide/cn/elasticsearch/guide/current/distributed-docs.html "分布式文档存储")

>[Elasticsearch: 权威指南 执行分布式检索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/distributed-search.html "执行分布式检索")

>[Elasticsearch: 权威指南 分片内部原理](https://www.elastic.co/guide/cn/elasticsearch/guide/current/inside-a-shard.html "分片内部原理")

>[Elasticsearch: 权威指南 索引管理](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index-management.html "索引管理")

>[CNblog: ElasticSearch 6.5.4版本启动集群](https://www.cnblogs.com/sxdcgaq8080/p/10449332.html "ElasticSearch 6.5.4版本启动集群")

### 集群内的原理

#### 空内容节点
##### 节点【node】
运行的一个elasticsearch实例我们称为一个节点。
##### 集群【cluster】
由一个节点或者多个节点拥有相同的cluster.name配置节点组成。
##### 集群
集群内的节点共同承担着数据和负载的压力，集群内可以增加和减少节点，在节点修改过程中集群内的数据将重新平均分布。
##### 节点
节点可以分为主节点和除去主节点所有节点。主节点主要管理集群范围内的所有变更，集群内的索引变更以及集群内的节点变更。用户对文档级别的添加和查询是可以随机分发到任意节点。主节点并非所有操作的中枢，每个节点中含有所有数据信息。扩展还存在一种叫做Client节点，这种节点如果将node.master属性和node.data属性都设置为false，在zookeeper中类似一种观察者的角色。

##### 空内容节点
实际操作启动一个ES实例
![空内容节点](https://www.elastic.co/guide/cn/elasticsearch/guide/current/images/elas_0201.png "空内容节点")
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
```

#### 拥有一个索引的单集群索引

##### 什么是索引【Index】
Es用于存储数据的地方就是索引，相当于数据库中表。索引实际指的是
一个或者多个物理分片的逻辑命名空间。

##### 分片【sharder】
Es用于存储数据的地方就分片，索引和分片的关系则是一个或者多个分片的逻辑命名空间【抽象命名】。

##### 索引
Es实际存储数据是在分片中。但是一个分片它保存的仅仅是所有数据的一部分。Es利用sharder将数据平均分到集群内的各个node中，索引将与分散在分片数据建立一个联系形成一个逻辑整体。应用程序是直接与索引进行交互。

##### 分片
Es利用sharder将数据平均分到集群内的各个node，当集群内增加或者减少节点，集群内的数据都会重新分配，这里提及的数据实际就是分片。分片分为主分片和副本分片，主分片平均集群内的所有数据，副本分片则是主分片的备份，通过数据冗余达到系统的高可用【副本分片在节点故障可以升级为主分片】。副本分片除了冗余功能，还为搜索提供读取的服务。增加查询响应时间。

##### 拥有一个索引的单集群索引
包含一个空节点的集群内创建名为 blogs 的索引

![一个索引的单集群索引](https://www.elastic.co/guide/cn/elasticsearch/guide/current/images/elas_0202.png "一个索引的单集群索引")

###### 为blogs分配3个主分片和一份副本
```
[PUT]http://127.0.0.1:9200/blogs
{
   "settings" : {
      "number_of_shards" : 3,
      "number_of_replicas" : 1
   }
}
```

###### 通过命令查看集群的健康
```
http://127.0.0.1:9200/_cluster/health

[Reponse]
{
  "cluster_name": "docker-cluster",
  "status": "yellow",
  "timed_out": false,
  "number_of_nodes": 1,
  "number_of_data_nodes": 1,
  "active_primary_shards": 3,
  "active_shards": 3,
  "relocating_shards": 0,
  "initializing_shards": 0,
  "unassigned_shards": 3,
  "delayed_unassigned_shards": 0,
  "number_of_pending_tasks": 0,
  "number_of_in_flight_fetch": 0,
  "task_max_waiting_in_queue_millis": 0,
  "active_shards_percent_as_number": 50.0
}
```
green
所有的主分片和副本分片都正常运行。
yellow
所有的主分片都正常运行，但不是所有的副本分片都正常运行。
red
有主分片没能正常运行。

**es集群默认会将主分片和副本分片分到不同节点中，同一个节点上既保存原始数据又保存副本是没有意义的**

#### 拥有两个节点的集群——所有主分片和副本分片都已被分配【解决单点故障问题】
##### 拥有两个节点的集群

![两个节点的集群](https://www.elastic.co/guide/cn/elasticsearch/guide/current/images/elas_0203.png "两个节点的集群")

###### docker启动两个节点的集群
```
# 使用Docker启动两个ES实例

docker run -di --name=myes1 -p 9200:9200 -p 9300:9300 -v G:/docker/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml elasticsearch:6.4.3

docker run -di --name=myes2 -p 9201:9201 -p 9301:9301 -v G:/docker/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml elasticsearch:6.4.3

# 查看两个ES实例在Docker容器内的Ip

docker inspect myes1
docker inspect myes2

[Reponse]
IPAddress: 172.17.0.2
IPAddress: 172.17.0.3

# 修改G:/docker/elasticsearch.yml
discovery.zen.minimum_master_nodes: 2
discovery.zen.ping.unicast.hosts: ["127.0.0.1","172.17.0.2:9200","172.17.0.3:9201","172.17.0.2:9300","172.17.0.3:9301"]

# 重启ES实例

docker restart myes1 myes2

```
###### 通过命令查看集群的健康
```
http://127.0.0.1:9200/_cluster/health

[Reponse]
{
  "cluster_name": "docker-cluster",
  "status": "green",
  "timed_out": false,
  "number_of_nodes": 2,
  "number_of_data_nodes": 2,
  "active_primary_shards": 3,
  "active_shards": 6,
  "relocating_shards": 0,
  "initializing_shards": 0,
  "unassigned_shards": 0,
  "delayed_unassigned_shards": 0,
  "number_of_pending_tasks": 0,
  "number_of_in_flight_fetch": 0,
  "task_max_waiting_in_queue_millis": 0,
  "active_shards_percent_as_number": 100.0
}
```
###### Es一个节点只能有3个分片吗
我们修改索引的副本数量，索引无法修改主分片的数量但可以修改副本的数量。我们增加副本数。
```
[PUT]
http://127.0.0.1:9200/blogs/_settings
{
   "number_of_replicas" : 2
}

[Reponse]
{
    "acknowledged": true
}
```
###### 通过命令查看集群的健康
```
http://127.0.0.1:9200/_cluster/health

[Reponse]
{
  "cluster_name": "docker-cluster",
  "status": "green",
  "timed_out": false,
  "number_of_nodes": 2,
  "number_of_data_nodes": 2,
  "active_primary_shards": 3,
  "active_shards": 6,
  "relocating_shards": 0,
  "initializing_shards": 0,
  "unassigned_shards": 0,
  "delayed_unassigned_shards": 0,
  "number_of_pending_tasks": 0,
  "number_of_in_flight_fetch": 0,
  "task_max_waiting_in_queue_millis": 0,
  "active_shards_percent_as_number": 100.0
}
```
我们发现结果并不是我们想象的，分片并未重新分配。我们会想起来集群内增加或者减少节点的时候才会分片才会重新分配。由此我们重新创建一个索引分片主分片4副本数为1查看集群状态。
###### 重新创建一个索引
```
[PUT]http://127.0.0.1:9200/blogs1

[Reponse]
{
   "settings" : {
      "number_of_shards" : 4,
      "number_of_replicas" : 1
   }
}
```

###### 通过命令查看集群的健康
```
http://127.0.0.1:9200/_cat/indices?v

[Reponse]
health status index  uuid                   pri rep docs.count docs.deleted store.size pri.store.size
green  open   blogs1 JuAsmZ0NRz6FqqxcWMbuJQ   4   1          0            0      1.7kb           920b
yellow open   blogs  vu4fDMv0Q0Ka1mIm3ndXFg   3   2          0            0      1.5kb           783b
```
由此我们可以看出节点的分片数并不是固定，他是按集群内索引创建和节点的加入，针对当前集群状态进行平均分配的。
**以上内容来源于>[Elasticsearch: 权威指南 集群内的原理](https://www.elastic.co/guide/cn/elasticsearch/guide/current/distributed-cluster.html "集群内的原理")**


### 分布式文档存储
##### 路由一个文档到一个分片中
回顾以前的知识，索引是什么？索引实际指的是一个或者多个物理分片的逻辑命名空间。
当索引一个文档的时候，数据为了均匀分布在分片内，使用的是轮询算法通过文档的_id **【可以自定义】****【API中可以指定路由】**，通过hash算法与当前分片求余数。达到均匀分布在分片内。反过来看理解成索引有着一套路由，知道文档存储在哪一个分片中。**【路由规则是和主分片数量有关的，这也就是集群内我们可以修改副本数量却不能修改主分片数量】**


##### 主分片和副本分片如何交互
###### 构建有三个节点和一个索引的集群


![三个节点的集群](https://www.elastic.co/guide/cn/elasticsearch/guide/current/images/elas_0401.png "三个节点的集群")
当前集群情况
```
http://127.0.0.1:9200/_cat/indices?v

[Reponse]
health status index  uuid                   pri rep docs.count docs.deleted store.size pri.store.size
green  open   blogs1 JuAsmZ0NRz6FqqxcWMbuJQ   4   1          0            0        2kb            1kb
yellow open   blogs  vu4fDMv0Q0Ka1mIm3ndXFg   3   2          0            0      1.5kb           783b
```
删除blogs1的索引,集群内增加节点
```
#删除blogs1的索引
[DELETE] http://127.0.0.1:9200/blogs1

[Reponse]
{
    "acknowledged": true
}

#启动新一个节点
docker run -di --name=myes3 -p 9202:9202 -p 9302:9302 -v G:/docker/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml elasticsearch:6.4.3

#查看两个ES实例在Docker容器内的Ip
docker inspect myes3

[Reponse]
IPAddress: 172.17.0.4

#修改G:/docker/elasticsearch.yml
discovery.zen.minimum_master_nodes: 3
discovery.zen.ping.unicast.hosts: ["127.0.0.1","172.17.0.2:9200","172.17.0.3:9201","172.17.0.4:9202","172.17.0.2:9300","172.17.0.3:9301","172.17.0.4:9302"]

#重启节点
docker restart myes1 myes2 myes3

#查看集群状态和索引状态
http://127.0.0.1:9200/_cluster/health

[Reponse]
{
  "cluster_name": "docker-cluster",
  "status": "green",
  "timed_out": false,
  "number_of_nodes": 3,
  "number_of_data_nodes": 3,
  "active_primary_shards": 3,
  "active_shards": 9,
  "relocating_shards": 0,
  "initializing_shards": 0,
  "unassigned_shards": 0,
  "delayed_unassigned_shards": 0,
  "number_of_pending_tasks": 0,
  "number_of_in_flight_fetch": 0,
  "task_max_waiting_in_queue_millis": 0,
  "active_shards_percent_as_number": 100.0
}

http://127.0.0.1:9200/_cat/indices?v

[Reponse]
health status index uuid                   pri rep docs.count docs.deleted store.size pri.store.size
green  open   blogs vu4fDMv0Q0Ka1mIm3ndXFg   3   2          0            0      2.2kb           783b

```
###### 处理发送到集群的请求
集群内的任意节点，具有处理任意请求的能力。任意节点都是知道集群内任意数据的位置。可以将需要的请求转发到需要的节点中。我们把处理发送到集群的节点称为**协调节点**。**【建议处理发送到集群的节点可以通过轮询的方式，均匀的分到集群内的所有节点】**

###### 新建、删除、索引、文档



