### 提要
摘自《Elasticsearch: 权威指南》中的内容,
当阅读本书时，将会遇到有关 Elasticsearch 分布式特性的补充章节。这些章节将介绍有关集群扩容、故障转移(集群内的原理) 、应对文档存储(分布式文档存储) 、执行分布式搜索(执行分布式检索) ，以及分区（shard）及其工作原理(分片内部原理) 。以下内容会以章节内容**集群内的原理**、**分布式文档存储**、**执行分布式检索**、**分片内部原理**为内容进行说明。

### 集群内的原理
#### 空内容节点
##### 实际操作启动一个ES实例
![空内容节点](https://www.elastic.co/guide/cn/elasticsearch/guide/current/images/elas_0201.png "空内容节点")

#### 相关名称介绍
##### 节点【node】
运行的一个elasticsearch实例我们称为一个节点。

节点可以分为主节点和除去主节点所有节点。主节点主要管理集群范围内的所有变更，集群内的索引变更以及集群内的节点变更。用户对文档级别的添加和查询是可以随机分发到任意节点。主节点并非所有操作的中枢，每个节点中含有所有数据信息。扩展还存在一种叫做Client节点，这种节点如果将node.master属性和node.data属性都设置为false，在zookeeper中类似一种观察者的角色。
##### 集群【cluster】
由一个节点或者多个节点拥有相同的cluster.name配置节点组成。

集群内的节点共同承担着数据和负载的压力，集群内可以增加和减少节点，在节点修改过程中集群内的数据将重新平均分布。


##### 下载并安装【基于windows,使用docker安装Elasticsearch 6.4.3版本】

```
#下载并安装
> docker search elasticsearch:6.4.3
> docker pull elasticsearch:6.4.3
> docker run -di --name=myes -p 9200:9200 -p 9300:9300 elasticsearch:6.4.3

#将ES的配置文件复制到本地系统文件中，方便修改和使用
> docker cp myes:/usr/share/elasticsearch/config/elasticsearch.yml G:/docker/elasticsearch.yml
> docker run -di --name=myes -p 9200:9200 -p 9300:9300 -v G:/docker/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml elasticsearch:6.4.3

> curl http://127.0.0.1:9200/
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
##### 在一个空节点的集群内创建名为 blogs 的索引

![一个索引的单集群索引](https://www.elastic.co/guide/cn/elasticsearch/guide/current/images/elas_0202.png "一个索引的单集群索引")


#### 相关名称介绍
##### 索引【Index】
Es用于存储数据的地方就是索引，相当于数据库中表。索引实际指的是
一个或者多个物理分片的逻辑命名空间。

Es实际存储数据是在分片中。但是一个分片它保存的仅仅是所有数据的一部分。Es利用sharder将数据平均分到集群内的各个node中，索引将与分散在分片数据建立一个联系形成一个逻辑整体。应用程序是直接与索引进行交互。

##### 分片【sharder】
Es用于存储数据的地方就分片，索引和分片的关系则是一个或者多个分片的逻辑命名空间【抽象命名】。

Es利用sharder将数据平均分到集群内的各个node，当集群内增加或者减少节点，集群内的数据都会重新分配，这里提及的数据实际就是分片。分片分为主分片和副本分片，主分片平均集群内的所有数据，副本分片则是主分片的备份，通过数据冗余达到系统的高可用【副本分片在节点故障可以升级为主分片】。副本分片除了冗余功能，还为搜索提供读取的服务。增加查询响应时间。
#### 相关操作
##### 为blogs分配3个主分片和一份副本
```
[PUT]http://127.0.0.1:9200/blogs
{
   "settings" : {
      "number_of_shards" : 3,
      "number_of_replicas" : 1
   }
}
```


##### 通过命令查看集群的健康



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


状态 | 描述
---|---
green | 所有的主分片和副本分片都正常运行。
yellow | 所有的主分片都正常运行，但不是所有的副本分片都正常运行。
red | 有主分片没能正常运行。




### 引用相关文章
>[Elasticsearch: 权威指南 集群内的原理？](https://www.elastic.co/guide/cn/elasticsearch/guide/current/distributed-cluster.html "集群内的原理")