>[Apache Hadoop 2.5.2 :HDFS Users Guide](https://hadoop.apache.org/docs/r2.5.2/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html "HDFS用户指南")

>[cnblogs:hadoop的安装与配置(完全分布式)](https://www.cnblogs.com/xieys-1993/articles/11983132.html "hadoop的安装与配置(完全分布式)")

>[csdn:Hadoop完全分布式环境搭建](https://blog.csdn.net/xiaoxsen/article/details/80462271 "hadoop的安装与配置(完全分布式)")

>[cnblogs:Hadoop学习(二)Hadoop配置文件参数详解](https://www.cnblogs.com/yinghun/p/6230436.html "cnblogs:Hadoop学习(二)Hadoop配置文件参数详解")

#### 英语生词

- fencing 美[ˈfensɪŋ]
n.	击剑运动; 栅栏; 篱笆; 围栏; 筑栅栏用的材料;
v.	(用栅栏、篱笆或围栏) 围住，隔开; 参加击剑运动; 搪塞; 支吾; 回避;
- quorum 美[ˈkwɔːrəm]
n.	(会议的) 法定人数;



#### 如何启动
[下载hadoop2.5.0版本](http://archive.apache.org/dist/hadoop/common/hadoop-2.5.0/ "下载hadoop2.5.0版本")


##### 配置本地模式
```
#创建软件安装目录
cd /opt
mkdir /opt/software
mkdir /opt/package

#修改安装目录的权限
cd /
sudo su 
chown -R daiyongjun opt
chgrp -R daiyongjun opt

#下载待安装的软件
su daiyongjun
cd /opt/daiyongjun/package
wget http://archive.apache.org/dist/hadoop/common/hadoop-2.5.0/hadoop-2.5.0-src.tar.gz
wget 

#解压缩软件并移动到softwore目录下
tar zxvf hadoop-2.5.0-src.tar.gz
mv hadoop-2.5.0-cdh5.3.6 ../software/

#配置环境变量
vi /etc/profile
#JAVA_HOME
export JAVA_HOME=/opt/software/jdk1.7.0_79
export CLASSPATH=$:CLASSPATH:$JAVA_HOME/lib/ 
export PATH=$PATH:$JAVA_HOME/bin

#HADOOP_HOME
export HADOOP_HOME=/opt/software/hadoop-2.5.0-cdh5.3.6
export PATH=$PATH:$HADOOP_HOME/bin:$HADOOP_HOOME/sbin:$HADOOP_HOME/lib 

source /etc/profile

#配置hdfs配置
cd /opt/software/hadoop-2.5.0-cdh5.3.6
vi /etc/hadoop/core-site.xml
#新增
<configuration>
	<property>
		<name>fs.defaultFS</name>
		<value>hdfs://localhost:9000</value>
	</property>
</configuration>

vi /etc/hadoop/hdfs-site.xml
#新增
<configuration>
	<property>
		<name>dfs.replication</name>
		<value>1</value>
	</property>
</configuration>


#配置ssh免密
ssh localhost
cd ~/.ssh/
ssh-keygen -t rsa 
cat id_rsa.pub >> authorized_keys
chmod 600 ./authorized_keys
ssh daiyongjun@localhost

#配置本地host
su root
vi /etc/hosts
#本地配置
192.168.31.103	master

#配置主机名
vi /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=master
NETWORKING_IPV6=no
PEERNTP=no
#重启网卡
service network restart

#启动
exit
hdfs namenode -format
start-dfs.sh

#检查
jps
98107 NameNode
11052 Jps
```
##### 配置集群模式
```
#停止dfs
stop-dfs.sh

#重新修改hdfs配置

cd /opt/software/hadoop-2.5.0-cdh5.3.6
vi /etc/hadoop/core-site.xml

#替换
<configuration>
	<property>
		<name>hadoop.tmp.dir</name>
		<value>/opt/software/hadoop-2.5.0-cdh5.3.6/custom/</value>
		<description>构建全局的路径</description>
	</property>
	<property>
		<name>fs.default.name</name>
		<value>hdfs://master:9000</value>
		<description>hdfs：//主机：端口/ NameNode URI</description>
	</property>
	<property>
		<name>io.file.buffer.size</name>
		<value>131072</value>
		<description>SequenceFiles中使用的读/写缓冲区的大小</description>
	</property>
</configuration>

vi /etc/hadoop/hdfs-site.xml

#替换
<configuration>
	<property>
		<name>dfs.namenode.name.dir</name>
		<value>file:///${hadoop.tmp.dir}/namenode,file:///${hadoop.tmp.dir}/duplicate/namenode</value>
		<description>命名空间和事务在本地文件系统永久存储的路径,逗号支持分隔多个本地路径，将命名空间和事务复制到多个目录实现冗余</description>
	</property>
	<property>
		<name>dfs.namenode.hosts</name>
		<value>slave1,slave2,slave3,slave4</value>
		<description>允许datanode节点列表,逗号支持分隔多个节点</description>
	</property>
	<property>
		<name>dfs.blocksize</name>
		<value>268435456</value>
		<description>设置大型文件系统的HDFS的blocksize为256MB</description>
	</property>
	<property>
		<name>dfs.namenode.handler.count</name>
		<value>100</value>
		<description>设置更多的namenode线程，处理从 datanode发出的大量RPC请求</description>
	</property>
	<property>
		<name>dfs.datanode.data.dir</name>
		<value>file:///${hadoop.tmp.dir}/datanode,file:///${hadoop.tmp.dir}/duplicate/datanode</value>
		<description>数据在本地文件系统永久存储的路径,逗号支持分隔多个本地路径，将命名空间和事务复制到多个目录实现冗余，通常在不同的设备上</description>
	</property>
	<property>
		<name>dfs.replication</name>
		<value>2</value>
		<description>数据冗余处理,文件副本数</description>
	</property>
</configuration>

vi /etc/hadoop/slaves

#新增
slave1
slave2
slave3
slave4

#配置HOST
su root
vi /etc/hosts
#本地配置
192.168.31.103	master
192.168.31.104 	slave1
192.168.31.105	slave2
192.168.31.106	slave3
192.168.31.107	slave4

#复制文件到其他机器
#host
scp -r /etc/hosts root@slave1:/etc/
scp -r /etc/hosts root@slave2:/etc/
scp -r /etc/hosts root@slave3:/etc/
scp -r /etc/hosts root@slave4:/etc/

#profile
scp -r /etc/profile root@slave1:/etc/
scp -r /etc/profile root@slave2:/etc/
scp -r /etc/profile root@slave3:/etc/
scp -r /etc/profile root@slave4:/etc/

#network
scp -r /etc/sysconfig/network root@slave1:/etc/sysconfig/
scp -r /etc/sysconfig/network root@slave2:/etc/sysconfig/
scp -r /etc/sysconfig/network root@slave3:/etc/sysconfig/
scp -r /etc/sysconfig/network root@slave4:/etc/sysconfig

#software
scp -r /opt/software  root@slave1:/opt/
scp -r /opt/software  root@slave2:/opt/
scp -r /opt/software  root@slave3:/opt/
scp -r /opt/software  root@slave4:/opt/

#操作slave1
ssh root@slave1
chown -p daiyongjun /opt
chgrp -p daiyongjun /opt

vi /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=slave1
NETWORKING_IPV6=no
PEERNTP=no

service network restart

#退出远程操作
exit

#操作slave2
ssh root@slave2
chown -p daiyongjun /opt
chgrp -p daiyongjun /opt

vi /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=slave2
NETWORKING_IPV6=no
PEERNTP=no

service network restart

#退出远程操作
exit


#操作slave3
ssh root@slave3
chown -p daiyongjun /opt
chgrp -p daiyongjun /opt

vi /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=slave3
NETWORKING_IPV6=no
PEERNTP=no

service network restart

#退出远程操作
exit

#操作slave4
ssh root@slave4
chown -p daiyongjun /opt
chgrp -p daiyongjun /opt

vi /etc/sysconfig/network
NETWORKING=yes
HOSTNAME=slave4
NETWORKING_IPV6=no
PEERNTP=no

service network restart

#退出远程操作
exit

#远程免密操作用户【daiyongjun】
su daiyongjun
cd ~/.ssh/
scp id_rsa.pub slave1:/home/daiyongjun/.ssh/id_rsa.pub.m
scp id_rsa.pub slave2:/home/daiyongjun/.ssh/id_rsa.pub.m
scp id_rsa.pub slave3:/home/daiyongjun/.ssh/id_rsa.pub.m
scp id_rsa.pub slave4:/home/daiyongjun/.ssh/id_rsa.pub.m

#操作slave1
ssh daiyongjun@slave1
cd ~/.ssh/
ssh-keygen -t rsa
scp id_rsa.pub master:/home/daiyongjun/.ssh/id_rsa.pub.s1
scp id_rsa.pub slave2:/home/daiyongjun/.ssh/id_rsa.pub.s1
scp id_rsa.pub slave3:/home/daiyongjun/.ssh/id_rsa.pub.s1
scp id_rsa.pub slave4:/home/daiyongjun/.ssh/id_rsa.pub.s1

#退出远程操作
exit

#操作slave2
ssh daiyongjun@slave2
cd ~/.ssh/
ssh-keygen -t rsa 
scp id_rsa.pub master:/home/daiyongjun/.ssh/id_rsa.pub.s2
scp id_rsa.pub slave1:/home/daiyongjun/.ssh/id_rsa.pub.s2
scp id_rsa.pub slave3:/home/daiyongjun/.ssh/id_rsa.pub.s2
scp id_rsa.pub slave4:/home/daiyongjun/.ssh/id_rsa.pub.s2

#退出远程操作
exit

#操作slave3
ssh daiyongjun@slave3
cd ~/.ssh/
ssh-keygen -t rsa
scp id_rsa.pub master:/home/daiyongjun/.ssh/id_rsa.pub.s3
scp id_rsa.pub slave1:/home/daiyongjun/.ssh/id_rsa.pub.s3
scp id_rsa.pub slave2:/home/daiyongjun/.ssh/id_rsa.pub.s3
scp id_rsa.pub slave4:/home/daiyongjun/.ssh/id_rsa.pub.s3

#退出远程操作
exit

#操作slave4
ssh daiyongjun@slave4
cd ~/.ssh/
ssh-keygen -t rsa 
scp id_rsa.pub
master:/home/daiyongjun/.ssh/id_rsa.pub.s4
scp id_rsa.pub slave1:/home/daiyongjun/.ssh/id_rsa.pub.s4
scp id_rsa.pub slave3:/home/daiyongjun/.ssh/id_rsa.pub.s4
scp id_rsa.pub slave2:/home/daiyongjun/.ssh/id_rsa.pub.s4

#退出远程操作
exit

#操作master
cat id_rsa.pub.s1 >> authorized_keys
cat id_rsa.pub.s2 >> authorized_keys
cat id_rsa.pub.s3 >> authorized_keys
cat id_rsa.pub.s4 >> authorized_keys
exit

#操作slave1
cat id_rsa.pub.m >> authorized_keys
cat id_rsa.pub.s2 >> authorized_keys
cat id_rsa.pub.s3 >> authorized_keys
cat id_rsa.pub.s4 >> authorized_keys
exit

#操作slave2
cat id_rsa.pub.s1 >> authorized_keys
cat id_rsa.pub.m >> authorized_keys
cat id_rsa.pub.s3 >> authorized_keys
cat id_rsa.pub.s4 >> authorized_keys
exit

#操作slave3
cat id_rsa.pub.s1 >> authorized_keys
cat id_rsa.pub.s2 >> authorized_keys
cat id_rsa.pub.m >> authorized_keys
cat id_rsa.pub.s4 >> authorized_keys
exit

#操作slave4
cat id_rsa.pub.s1 >> authorized_keys
cat id_rsa.pub.s2 >> authorized_keys
cat id_rsa.pub.m >> authorized_keys
cat id_rsa.pub.s4 >> authorized_keys
exit

#启动命令
hdfs namenode -format
start-dfs.sh
```

#### HDFS 启动命令

**hdfs namenode -format**
```
#详解命令
$HADOOP_PREFIX/bin/hdfs namenode -format <cluster_name>
#namenode节点上执行
hdfs namenode -format
#如果不在namenode上执行
hdfs namenode -format master
```

**start-dfs.sh相关**
```
#详解命令
$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script
hadoop-daemon.sh --script hdfs start namenode
hadoop-daemon.sh --script hdfs start datanode
```


**stop-dfs.sh相关**
```
#详解命令
$HADOOP_PREFIX/sbin/hadoop-daemon.sh --config $HADOOP_CONF_DIR --script
hadoop-daemon.sh --script hdfs start namenode
hadoop-daemon.sh --script hdfs start datanode
```


#### HDFS HA架构

##### 背景

在Hadoop 2.0.0之前，NameNode是HDFS集群中的单点故障（SPOF）。每个群集只有一个NameNode，并且如果该计算机或进程不可用，则整个群集将不可用，直到NameNode重新启动或在单独的计算机上启动。

##### 架构设计
HA群集中，将两个单独的计算机配置为NameNod，在任何时间点，一个NameNode都恰好处于活动状态，而另一个则处于Standby状态。
为了使备用节点保持其状态与活动节点同步，**当前的实现要求两个节点都可以访问共享存储设备上的目录，或者维护一个单独进程JournalNodes**
当活动节点执行任何名称空间修改时，它会持久地将修改记录记录到存储在**共享目录中**的编辑日志文件中或者**JournalNodes(JN)中**。Standby节点一直在监视此目录中的编辑或者编辑日志的更改，并同步。
为了提供快速的故障转移，备用节点还必须具有有关集群中块位置的最新信息。为了实现这一点，DataNodes被配置了两个NameNodes的位置，并向两者发送块位置信息和心跳信号。
##### 硬件需求
- "活动"和"备用" NameNode的计算机应具有彼此等效的硬件。
- 您将需要有一个共享目录，两台NameNode机器都可以对其进行读/写访问或者维护 JournalNodes(JN)的单独守护程序进行通信,注意：必须至少有3个JournalNode守护程序，因为必须将编辑日志修改写入大多数JN中，详细可以参考zookeeper的选举机制，这里就不展开说明。

##### 部署配置
###### 概述
设计新配置后，群集中的所有节点都可以具有相同的配置，而无需根据节点的类型将不同的配置文件部署到不同的机器上
###### 配置细节

* hdfs-site.xml 描述

参数|属性值|描述
---|---|---
dfs.nameservices|mycluster|dfs-nameservices名称服务定义一个逻辑名称
dfs.ha.namenodes.mycluster|mycluster|dfs-nameservices名称服务定义一个逻辑名称
dfs.nameservices|nn1,nn2|dfs-nameservices名称服务定义一个逻辑名称
dfs.nameservices|mycluster|dfs-nameservices名称服务定义一个逻辑名称
###### 部署细节


##### 配置HA架构模式

```
#login master
cd /opt/software/hadoop-2.5.0-cdh5.3.6
vi /etc/hadoop/hdfs-site.xml

#add
<configuration>
	<property>
		<name>dfs.nameservices</name>
		<value>mycluster</value>
		<description>dfs-nameservices名称服务定义一个逻辑名称</description>
	</property>
	<property>
		<name>dfs.ha.namenodes.mycluster</name>
		<value>nn1,nn2</value>
		<description>最多只能配置两个NameNode,名称服务定义namenodes定义逻辑名称,例如，如果您以前使用"mycluster"作为名称服务ID，并且想要使用"nn1"和"nn2"作为NameNode的各个ID,则可以这样配置</description>
	</property>
	<property>
		<name>dfs.namenode.rpc-address.ns1.nn1</name>
		<value>master:8020</value>
		<description>ns1.nn1的逻辑名称NameNode监听的标准RPC地址</description>
	</property>
	<property>
		<name>dfs.namenode.rpc-address.ns1.nn2</name>
		<value>slave1:8020</value>
		<description>ns1.nn2的逻辑名称NameNode监听的标准RPC地址</description>
	</property>
	<property>
		<name>dfs.namenode.rpc-address.ns1.nn1</name>
		<value>master:50070</value>
		<description>ns1.nn1的逻辑名称对应的namenode的http访问地址,注意：如果启用了Hadoop的安全性功能，则还应该为每个NameNode相似地设置https-地址</description>
	</property>
	<property>
		<name>dfs.namenode.rpc-address.ns1.nn2</name>
		<value>slave1:50070</value>
		<description>>ns1.nn2的逻辑名称对应的namenode的http访问地址,注意：如果启用了Hadoop的安全性功能，则还应该为每个NameNode相似地设置https-地址</description>
	</property>
	<property>
		<name>dfs.namenode.rpc-address.ns1.nn2</name>
		<value>slave1:50070</value>
		<description>>ns1.nn2的逻辑名称对应的namenode的http访问地址,注意：如果启用了Hadoop的安全性功能，则还应该为每个NameNode相似地设置https-地址</description>
	</property>
	<property>
		<name>dfs.namenode.shared.edits.dir</name>
		<value>qjournal://master:8485;slave1:8485;slave2:8485;slave3:8485;slave4:8485/ns1</value>
		<description>JournalNodes(JN)的单独守护程序，建议运行奇数个且运行数>=3</description>
	</property>
	<property>
		<name>dfs.client.failover.proxy.provider.mycluster</name>
		<value>org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider</value>
		<description>配置Java类的名称，DFS客户端将使用该Java类来确定哪个NameNode是当前的Active，从而确定哪个NameNode当前正在服务于客户端请求</description>
	</property>
	<property>
		<name>dfs.ha.fencing.methods</name>
		<value>sshfence</value>
		<description>故障转移期间隔离策略提供,ssh访问目标主机策略,同时也支持自定义shell策略模式</description>
	</property>
	<property>
		<name>dfs.ha.fencing.ssh.private-key-files</name>
		<value>/daiyongjun/.ssh/id_rsa</value>
		<description>sshkey</description>
	</property>
</configuration>

vi core-site.xml
#updata
<configuration>
	<property>
		<name>fs.defaultFS</name>
		<value>hdfs://mycluster</value>
		<description>使用新的启用HA的逻辑URI</description>
	</property>
	<property>
		<name>dfs.journalnode.edits.dir</name>
		<value>file:///${hadoop.tmp.dir}/path/to/journal/node/local/data</value>
		<description>JournalNode守护程序将存储其本地状态的路径</description>
	</property>
</configuration>

#scp file
scp -r /opt/software/hadoop-2.5.0-cdh5.3.6/etc/hadoop daiyongjun@slave1:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/
scp -r /opt/software/hadoop-2.5.0-cdh5.3.6/etc/hadoop daiyongjun@slave2:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/
scp -r /opt/software/hadoop-2.5.0-cdh5.3.6/etc/hadoop daiyongjun@slave3:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/
scp -r /opt/software/hadoop-2.5.0-cdh5.3.6/etc/hadoop daiyongjun@slave4:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/



```



#### HDFS Configuring 【非安全模式】
* hdfs-site.xml 描述【NameNode相关配置】

参数|属性值|描述
---|---|---
dfs.namenode.name.dir|file:///${hadoop.tmp.dir}/namenode,file:///${hadoop.tmp.dir}/duplicate/namenode|命名空间和事务在本地文件系统永久存储的路径,逗号支持分隔多个本地路径，将命名空间和事务复制到多个目录实现冗余
dfs.namenode.hosts / dfs.namenode.hosts.exclude|slave1,slave2,slave3,slave4|允许datanode节点列表,逗号支持分隔多个节点 
dfs.blocksize|268435456|大型文件系统的HDFS块大小为256MB
dfs.namenode.handler.count|100|设置更多的namenode线程，处理从 datanode发出的大量RPC请求


* hdfs-site.xml 描述【DataNode相关配置】

参数|属性值|描述
---|---|---
dfs.namenode.name.dir|file:///${hadoop.tmp.dir}/datanode,file:///${hadoop.tmp.dir}/duplicate/datanode|数据在本地文件系统永久存储的路径,逗号支持分隔多个本地路径，将命名空间和事务复制到多个目录实现冗余，通常在不同的设备上
dfs.replication|3|数据冗余处理,文件副本数


#### HDFS操作

参数|属性值|描述
---|---|---
hdfsdfs-appendToFile<localsrc>...<dst>|file:///${hadoop.tmp.dir}/datanode,file:///${hadoop.tmp.dir}/duplicate/datanode|数据在本地文件系统永久存储的路径,逗号支持分隔多个本地路径，将命名空间和事务复制到多个目录实现冗余，通常在不同的设备上
hdfsdfs-catURI[URI...]|hdfsdfs-cat/usr/daiyongjun|将源路径复制到标准输出，实际就是将源文件进行输出


