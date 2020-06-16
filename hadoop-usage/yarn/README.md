>[Apache Hadoop 2.5.2 :Apache Hadoop NextGen MapReduce (YARN)](https://hadoop.apache.org/docs/r2.5.2/hadoop-yarn/hadoop-yarn-site/YARN.html "Apache Hadoop NextGen MapReduce (YARN)")

>[TouTiao:Yarn基本原理和资源调度解析](https://www.toutiao.com/a6597952619342201348/?timestamp=1588407901&app=news_article&group_id=6597952619342201348&req_id=20200502162501010129035139188F4EA1 "Yarn基本原理和资源调度解析")

#### 英语生词


#### 如何启动
本文本建立在HDFS的章节之上

#### YARE架构


##### 背景
在Hadoop 1.0及更早版本中，我们只能运行MapReduce，这导致图形处理、迭代计算等任务无法有效执行。在Hadoop 2.0及后续版本中  MapReduce 2.0（MRv2）或YARN，MapReduce的调度部分被外部化并重新编写为名为YARN的新组件，YARN最大的特点是执行调度与Hadoop上运行的任务类型无关。
![image](https://note.youdao.com/yws/public/resource/39b1184babb362474ea34a2179c3fa9c/26917500A0F340CBA7441DBF8B5AA67C)

##### 介绍
集群中的应用程序与YARN框架通信，要求分配特定于应用程序的容器资源，YARN框架会评估这些请求并尝试实现。每个容器都有一个协议，指定允许使用的系统资源，并在容器超出边界的情况下终止容器，以避免恶意影响其他应用程序。

YARN框架有意设计的尽可能简单，它不知道或不关心正在运行的应用程序类型，不保留有关集群上执行内容的任何历史信息，这些设计是YARN可以扩展到MapReduce之外的主要原因。

YARN可在Hadoop上执行除MapReduce以外的工作。但是，YARN可以弥补MapReduce的不足让mapreduce实时甚至近实时的数据处理。核心是分布式调度程序，负责两项工作。
- 响应客户端创建容器的请求(创建进程)
- 监视正在运行的容器，并在需要时终止



##### 架构

![image](https://note.youdao.com/yws/public/resource/39b1184babb362474ea34a2179c3fa9c/CEDAB53EDA104069A245CFC13DF4CE6B)
- ResourceManager  功能是仲裁Hadoop集群上的资源，响应客户端创建容器请求。ResourceManager根据资源要求安排工作，它使主机能够运行混合容器。

![image](https://note.youdao.com/yws/public/resource/39b1184babb362474ea34a2179c3fa9c/D16047B7A8764C1F94327471FF608E4E)
- NodeManager   它为来ResourceManager和ApplicationMaster的请求， 工作是创建、监视和杀死容器。并向ResourceManager报告容器的状态。
- ApplicationMaster 它负责管理特定于应用程序的容器。ApplicationMaster创建的特定于应用程序的进程。ApplicationManager本身也是一个由ResourceManager创建的容器。由ApplicationManager创建的容器可以是任意进程。
- Container 特定任务的进程，容器进程可以是简单的Linux命令，例如awk，Python应用程序或可由操作系统启动的任何进程


##### 配置本地模式【伪分布式模式】
```
cd /opt/software/hadoop-2.5.0-cdh5.3.6/etc
vi yarn-site.xml
#配置伪分布式下的yarn,配置yarn上运行mapreduce任务,支持spark,flink,storm等
<configuration>
	<property>
		<name>yarn.nodemanager.aux-services</name>
		<value>mapreduce_shuffle</value>
		<description>NodeManager上运行的附属服务。需配置成mapreduce_shuffle，才可运行MapReduce程序</description>
	</property>
</configuration>

vi mapred-site.xml
#配置mapreduce运行在yarn上
<configuration>
	<property>
		<name>mapreduce.framework.name</name>
		<value>yarn</value>
		<description>执行框架设置为Hadoop YARN</description>
	</property>
</configuration>

#启动和停止yarn
start-yarn.sh

#在hdfs下复制文件
hdfs dfs -mkdir -p /usr/local/daiyongjun/input
hdfs dfs -put hadoop/* input

#启动mapreduce程序，在hdfs下
cd /opt/software/hadoop-2.5.0-cdh5.3.6/share/hadoop/mapreduce/
hadoop jar hadoop-mapreduce-examples-2.5.0-cdh5.3.6.jar grep /usr/local/daiyongjun/input  /usr/local/daiyongjun/output 'dfs[a-z.]+'

```

##### 配置完全分布式模式
```
vi yarn-site.xml
<?xml version="1.0"?>
<!--
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License. See accompanying LICENSE file.
-->
<configuration>
	<!--ResourceManager和NodeManager的配置-->
	<property>
		<name>yarn.acl.enable</name>
		<value>true</value>
		<description>启用ACL？默认为false，访问控制列表（Access Control Lists，ACL）</description>
	</property>
	<property>
		<name>yarn.admin.acl</name>
		<value>daiyongjun daiyongjun</value>
		<description>设置管理ACL用户和用户组,用户和用户组之间必须有个空格</description>
	</property>
	<property>
		<name>yarn.log-aggregation-enable</name>
		<value>true</value>
		<description>是否启用日志聚合功能，日志聚合开启后保存到HDFS上</description>
	</property>
	<property>
		<name>yarn.resourcemanager.hostname</name>
		<value>master</value>
		<description>resourcemanager的hostname,可以代替设置所有yarn.resourcemanager * address资源。如果代替ResourceManager组件则提供默认端口。</description>
	</property>
	<property>
		<name>yarn.resourcemanager.address</name>
		<value>${yarn.resourcemanager.hostname}:8032</value>
		<description>RM对客户端暴露的地址，客户端通过该地址向RM提交应用程序等，使用的RM的hostname，同理host:port中host若设置不同与yarn.resourcemanager.hostname的值，可以替换掉该值</description>
	</property>
	<property>
		<name>yarn.resourcemanager.scheduler.address</name>
		<value>${yarn.resourcemanager.hostname}:8030</value>
		<description>RM对AM暴露的地址，AM通过地址想RM申请资源，释放资源等，使用的RM的hostname，同理host:port中host若设置不同与yarn.resourcemanager.hostname的值，可以替换掉该值</description>
	</property>
	<property>
		<name>yarn.resourcemanager.resource-tracker.address</name>
		<value>${yarn.resourcemanager.hostname}:8031</value>
		<description>RM对NM暴露地址，NM通过该地址向RM汇报心跳，领取任务等，同理host:port中host若设置不同与yarn.resourcemanager.hostname的值，可以替换掉该值</description>
	</property>
	<property>
		<name>yarn.resourcemanager.admin.address</name>
		<value>${yarn.resourcemanager.hostname}:8033</value>
		<description>RM对NM暴露地址，NM通过该地址向RM汇报心跳，领取任务等，同理host:port中host若设置不同与yarn.resourcemanager.hostname的值，可以替换掉该值</description>
	</property>
	<property>
		<name>yarn.resourcemanager.webapp.address</name>
		<value>${yarn.resourcemanager.hostname}:8088</value>
		<description>RM对外暴露的web  http地址，用户可通过该地址在浏览器中查看集群信息，使用的RM的hostname，同理host:port中host若设置不同与yarn.resourcemanager.hostname的值，可以替换掉该值</description>
	</property>
	<property>
		<name>yarn.resourcemanager.webapp.https.address</name>
		<value>${yarn.resourcemanager.hostname}:8090</value>
		<description>RM对外暴露的web  https地址，用户可通过该地址在浏览器中查看集群信息，使用的RM的hostname，同理host:port中host若设置不同与yarn.resourcemanager.hostname的值，可以替换掉该值</description>
	</property>
	<property>
		<name>yarn.resourcemanager.scheduler.class</name>
		<value>org.apache.hadoop.yarn.server.resourcemanager.scheduler.capacity.CapacityScheduler</value>
		<description>资源调度器主类</description>
	</property>
	<property>
		<name>yarn.resourcemanager.scheduler.client.thread-count</name>
		<value>50</value>
		<description>处理来自AM的RPC请求的handler数</description>
	</property>
	<property>
		<name>yarn.scheduler.minimum-allocation-mb</name>
		<value>512</value>
		<description>资源管理器中分配给每个容器请求的最小内存限制。可申请的最少内存资源，以MB为单位</description>
	</property>
	<property>
		<name>yarn.scheduler.maximum-allocation-mb</name>
		<value>2048</value>
		<description>资源管理器中分配给每个容器请求的最大内存限制。可申请的最大内存资源，以MB为单位</description>
	</property>
	<property>
		<name>yarn.resourcemanager.nodes.include-path</name>
		<value>master,slave1,slave2,slave3,slave4</value>
		<description>允许nodeManager列表</description>
	</property>
	<!--NodeManager的配置-->
	<property>
		<name>yarn.nodemanager.resource.memory-mb</name>
		<value>2048</value>
		<description>NM总的可用物理内存，以MB为单位。一旦设置，不可动态修改</description>
	</property>
	<property>
		<name>yarn.nodemanager.resource.cpu-vcores</name>
		<value>2</value>
		<description>NM可分配的CPU个数。一旦设置，不可动态修改</description>
	</property>
	<property>
		<name>yarn.nodemanager.vmem-pmem-ratio</name>
		<value>2.1</value>
		<description>物理内存和虚拟内存[swap交换分区]硬盘内存，默认是2.1,表示每1MB的物理内存可以使用2.1M的虚拟内存</description>
	</property>
	<property>
		<name>yarn.nodemanager.local-dirs</name>
		<value>${hadoop.tmp.dir}/nm-local-dir</value>
		<description>中间结果存放位置，可配置多目录多个地址用逗号进行分割</description>
	</property>
	<property>
		<name>yarn.nodemanager.log</name>
		<value>${hadoop.tmp.dir}/nm-local-dir</value>
		<description>NM的日志路径，可配置多目录多个地址用逗号进行分割</description>
	</property>
	<property>
		<name>yarn.nodemanager.log.retain-seconds</name>
		<value>10800</value>
		<description>在NodeManager上保留日志文件的默认时间（以秒为单位）仅在日志聚合时才适用</description>
	</property>
	<property>
		<name>yarn.nodemanager.remote-app-log-dir</name>
		<value>/usr/daiyongjun/tmp/logs</value>
		<description>日志聚合目录,日志的hdfs目录，仅在启用日志聚合的情况下适用</description>
	</property>
	<property>
		<name>yarn.nodemanager.aux-services</name>
		<value>mapreduce_shuffle</value>
		<description>NodeManager上运行的附属服务。需配置成mapreduce_shuffle，才可运行MapReduce程序</description>
	</property>
</configuration>

#配置所有配置
cd ..
scp -r ./hadoop daiyongjun@slave1:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/
scp -r ./hadoop daiyongjun@slave2:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/
scp -r ./hadoop daiyongjun@slave3:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/
scp -r ./hadoop daiyongjun@slave4:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/

#启动yarn服务[若失败建议看一下日志]
start-yarn.sh

#启动mapreduce服务
cd /opt/software/hadoop-2.5.0-cdh5.3.6/share/hadoop/mapreduce/
hadoop jar hadoop-mapreduce-examples-2.5.0-cdh5.3.6.jar grep /usr/local/daiyongjun/input  /usr/local/daiyongjun/output 'dfs[a-z.]+'
```