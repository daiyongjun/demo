### 背景
```
Kafka is used for building real-time data pipelines and streaming apps. It is horizontally 
scalable, fault-tolerant, wicked fast, and runs in production in thousands of companies.
```

### 架构

#### 消息队列
Kafka会对数据进行持久化存储（至于存放多长时间，这是可以配置的），消费者端会记录一个offset，表明该消费者当前消费到哪条数据，所以下次消费者想继续消费，只需从offset+1的位置继续消费就好了。
![image](640A97E9D5E141FCAF1CEFD5EF6BFD20)

但是，这个Kafka用一条消息队列实现了消息中间件，这样的简单实现存在不少问题？
1. 各种topic混杂一起
2. 吞吐量低


#### Partition
Kafka引入了Partition的概念，分布式存储，也就是采用多条队列， 每条队列里面的消息都是相同的topic。来解决一条队列带来的topic中杂糅在一起，且吞吐量低的情况。

![image](B4EF4ECBEEE74880A261B2E78B2E1386)

但是这也带来了另一个问题。稳定性。分布式应用的稳定性问题，也就是高可用性。

#### Broker集群
Kafka对集群的支持也是非常友好的。在Kafka中，集群里的每个实例叫做Broker，如下图。每个partition不再只有一个，而是有一个leader(红色)和多个replica(蓝色)。使用副本的架构可以参考我们之前提及的hdfs架构。
![image](3FCBF8469F3447D2AB70F18516B12287)


### 启动
#### 单机模式
```
> cd /opt/package
#下载
> wget  http://archive.apache.org/dist/kafka/1.0.0/kafka_2.11-1.0.0.tgz

> tar -zxvf kafka_2.11-1.0.0.tgz -C /opt/software

#set kafka environment
> sudo vi /etc/profile

#kafka environment
> export KAFKA_HOME=/opt/software/kafka_2.11-1.0.0
PATH=${KAFKA_HOME}/bin:$PATH

> source /etc/profile

> cd /opt/software/kafka_2.11-1.0.0
#启动内置的zookeeper(zookeeper的目录在/tmp路径下)
> zookeeper-server-start.sh config/zookeeper.properties
#(kafka默认存储路径/tmp)控制台启动，实时打印日志
> kafka-server-start.sh config/server.properties
```
#### 单机模式(操作)
```
#创建一个主题
> kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test
Created topic "test".
#查看主题列表
> kafka-topics.sh --list --zookeeper localhost:2181
test
#发送一些消息
> kafka-console-producer.sh --broker-list localhost:9092 --topic test
This is a message
This is another message
#启动一个消费者
> kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-beginning
This is a message
This is another message
```

#### 分布式模式【非官方文档的内容，使用自己构建的zookeeper服务】
```
#修改配置
> vi /usr/local/kafka/config/server.properties


#按所在机器位置变化如：id=1,id=2,id=3
> broker.id=1
#根据机器的变化而变化如：listeners master,listeners slave1......
> listeners=PLAINTEXT://master:9092
> listeners=PLAINTEXT://slave1:9092
> listeners=PLAINTEXT://slave2:9092
> listeners=PLAINTEXT://slave3:9092
> listeners=PLAINTEXT://slave4:9092

> zookeeper.connect=master:2181,slave1:2181,slave2:2181,slave3:2181,slave4:2181

#迁移kafka
> scp -r ./kafka_2.11-1.0.0 daiyongjun@master:/opt/software/
> scp -r ./kafka_2.11-1.0.0 daiyongjun@slave1:/opt/software/
> scp -r ./kafka_2.11-1.0.0 daiyongjun@slave2:/opt/software/
> scp -r ./kafka_2.11-1.0.0 daiyongjun@slave3:/opt/software/
> scp -r ./kafka_2.11-1.0.0 daiyongjun@slave4:/opt/software/

#后台启动(日志的位置kafka_2.11-1.0.0/logs/server.log)
#master
> cd /opt/software/kafka_2.11-1.0.0/
> kafka-server-start.sh -daemon ./config/server.properties
#slave1
> cd /opt/software/kafka_2.11-1.0.0/
> kafka-server-start.sh -daemon ./config/server.properties
#slave2
> cd /opt/software/kafka_2.11-1.0.0/
> kafka-server-start.sh -daemon ./config/server.properties
#slave3
> cd /opt/software/kafka_2.11-1.0.0/
> kafka-server-start.sh -daemon ./config/server.properties
#slave4
> cd /opt/software/kafka_2.11-1.0.0/
> kafka-server-start.sh -daemon ./config/server.properties
```
### 分布式模式(操作)
```
#创建一个主题
> kafka-topics.sh --create --zookeeper master:2181 --replication-factor 2 --partitions 3 --topic ceshi
Created topic "ceshi".
#查看主题列表
> kafka-topics.sh --list --zookeeper master:2181
ceshi
#发送一些消息
> kafka-console-producer.sh --broker-list master:9092 --topic ceshi
> This is a message
> This is another message
#启动一个消费者
> kafka-console-consumer.sh --bootstrap-server master:9092 --topic ceshi --from-beginning
> This is a message
> This is another message
```
### 分布式模式(导入/导出数据)
```
> cd /opt/software/kafka_2.11-1.0.0/
#创建一个文件
> echo -e "foo\nbar" > test.txt
#使用kafka connect导入和导出数据(可以尝试查看三个配置文件的内容)
> connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties
> kafka-console-consumer.sh --bootstrap-server master:9092 --topic connect-test --from-beginning

{"schema":{"type":"string","optional":false},"payload":"foo"}
{"schema":{"type":"string","optional":false},"payload":"bar"}
...

```








### kafka-manager
为了简化开发者和服务工程师维护Kafka集群的工作，yahoo构建了一个叫做Kafka管理器的基于Web工具。
```
#下载服务
cd /opt/package
> wget https://github.com/yahoo/CMAK/archive/1.3.3.22.zip
> unzip CMAK-master.zip -d /opt/software/


#安装stb
> yum install sbt分

#后台启动

> nohup bin/kafka-manager -Dconfig.file=conf/application.conf -Dhttp.port=8080  &
```

[详细使用参考这篇文章](https://www.jianshu.com/p/6a592d558812/)




### 参考文献&学习资源

>[简书 :Kafka集群搭建与配置 ](https://www.jianshu.com/p/bdd9608df6b3 "Kafka集群搭建与配置")

>[简书 :Kafka简明教程 ](https://www.jianshu.com/p/7b77723d4f96 "Kafka简明教程")

>[官网 :Kafka中文文档 ](http://kafka.apachecn.org/intro.html "Kafka中文文档")


