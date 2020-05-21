### 背景
从自己方面，最初接触**Zookeeper**的是阿里的分布式框架dubble，以及当当网的elastic-job，和在学习HDFS时，配置的HA的时候都有接触到Zookeeper，但一直未深入了解。Zookeeper本身演变过程，是 Google 的 Chubby一个开源的实现。是 Hadoop 的分布式协调服务。它包含一个简单的原语集，分布式应用程序可以基于 它实现同步服务，配置维护和命名服务等。

分布式服务


#### 英语生词
### 架构
#### Paxos算法
##### 背景
Paxos算法解决的什么问题呢？poxas-著名的分布式数据一致性算法，解决的就是保证每个节点执行相同的操作序列。



### Zookeeper的组成部分。
#### Zookeeper文件系统
每个子目录项如 NameService 都被称作为znode，和文件系统一样，我们能够自由的增加、删除znode，在一个znode下增加、删除子znode，唯一的不同在于znode是可以存储数据的。

znode类型

类型 | 解释
---|---
PERSISTENT-持久化目录节点  | 客户端与zookeeper断开连接后，该节点依旧存在 
PERSISTENT_SEQUENTIAL-持久化顺序编号目录节点  | 客户端与zookeeper断开连接后，该节点依旧存在，只是Zookeeper给该节点名称进行顺序编号 
EPHEMERAL-临时目录节点  | 客户端与zookeeper断开连接后，该节点被删除 
EPHEMERAL_SEQUENTIAL-临时顺序编号目录节点   | 客户端与zookeeper断开连接后，该节点被删除，只是Zookeeper给该节点名称进行顺序编号 



![image](6BC2EB2A83FE4CA893072D5C78FFF5B9)
#### 通知机制
客户端注册监听它关心的目录节点，当目录节点发生变化（数据改变、被删除、子目录节点增加删除）时，zookeeper会通知客户端。

### 基于Zookeeper的文件系统和通知机制我们做些什么呢？
#### 命名服务【注册中心】
在Zookeeper的文件系统里创建一个目录，即有唯一的path。该path代表该系统，如文件系统的图片中的NameService，以及子目录下的Service1、Service2等。当系统无法知道系统中部署的那些服务，可以相互约定固定path来相互发现。
#### 配置管理【配置中心】
在Zookeeper通知机制中，客户端注册监听它关心的目录节点，当目录节点发生变化（数据改变、被删除、子目录节点增加删除）时，zookeeper会通知客户端。客户端重新加载一遍配置即可。
![image](A3ADF4F301AA4FA68660780F0772063A)
#### 集群管理 【HA主从切换】

#### 分布式锁

#### 队列管理 



>[Apache :Zookeeper官方文档 ](https://zookeeper.apache.org/doc/current/zookeeperOver.html "Zookeeper官方文档")


>[简书 :Zookeeper](https://www.jianshu.com/p/e68c06a5d002 "Zookeeper")

>[cnblogs :Zookeeper的功能以及工作原理
](https://www.jianshu.com/p/e68c06a5d002 "Zookeeper的功能以及工作原理
")

>[百度百科 :Paxos 算法](https://baike.baidu.com/item/Paxos%20%E7%AE%97%E6%B3%95/10688635?fr=aladdin "Paxos 算法")



