>[Apache Hadoop 2.5.2 :MapReduce Tutorial](https://hadoop.apache.org/docs/r2.5.2/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html "MapReduce教程")

>[cnblogs:MapReduce 基本优化相关参数](https://www.cnblogs.com/yjt1993/p/9483032.html "MapReduce 基本优化相关参数")

>[csdn:-verbose:gc 查看GC情况](https://blog.csdn.net/qq_41701363/article/details/90551189 "-verbose:gc 查看GC情况")

>[cnblogs:MapReduce的Shuffle及调优](https://www.cnblogs.com/lenmom/p/11614058.html "MapReduce的Shuffle及调优")


#### 英语生词

- scheduler	美[ˈskɛdʒʊlər]
n.	调度程序，日程安排程序;

#### 如何启动
本文本建立在HDFS的章节之上

#### MapReduce架构


##### 背景


##### 介绍

##### 配置本地模式【伪分布式模式】
```
cd /opt/software/hadoop-2.5.0-cdh5.3.6/etc

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
vi mapred-site.xml

<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
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

<!-- Put site-specific property overrides in this file. -->
<configuration>
	<property>
		<name>mapreduce.framework.name</name>
		<value>yarn</value>
		<description>执行框架设置为Hadoop YARN</description>
	</property>
	<!-- map 相关的配置 -->
	<property>
		<name>mapreduce.map.memory.mb</name>
		<value>2048</value>
		<description>一个MapTask可使用的内存上限（单位:MB），默认为1024。如果MapTask实际使用的资源量超过该值，则会被强制杀死。</description>
	</property>
	<property>
		<name>mapreduce.map.java.opts</name>
		<value>-Xmx2048m -verbose:gc-Xloggc:/tmp/@taskid@.gc</value>
		<description>MapTask的JVM参数，你可以在此配置默认的javaheapsize等参数，例如："-Xmx1024m -verbose:gc-Xloggc:/tmp/@taskid@.gc"。</description>
	</property>
	<!-- reduce 相关的配置 -->
	<property>
		<name>mapreduce.reduce.memory.mb</name>
		<value>2048</value>
		<description>一个ReduceTask可使用的资源上限（单位:MB），默认为1024。如果ReduceTask实际使用的资源量超过该值，则会被强制杀死。</description>
	</property>
	<property>
		<name>mapreduce.reduce.java.opts</name>
		<value>-Xmx2048m -verbose:gc-Xloggc:/tmp/@taskid@.gc</value>
		<description>ReDuce的JVM参数，你可以在此配置默认的javaheapsize等参数，例如："-Xmx1024m -verbose:gc-Xloggc:/tmp/@taskid@.gc"。</description>
	</property>
	<property>
		<name>mapreduce.task.io.sort.mb</name>
		<value>512</value>
		<description>更高的内存限制，同时对数据进行排序以提高效率。</description>
	</property>
	<property>
		<name>mapreduce.task.io.sort.factor</name>
		<value>100</value>
		<description>排序文件时，更多流同时合并。</description>
	</property>
	<property>
		<name>mapreduce.reduce.shuffle.parallelcopies</name>
		<value>50</value>
		<description>reduce任务，设置可执行复制的过程使用多线程并发</description>
	</property>
	<!-- MapReduce JobHistory服务器的配置 -->
	<property>
		<name>mapreduce.jobhistory.address</name>
		<value>master:10020</value>
		<description>JobHistory Server 主机：端口</description>
	</property>
	<property>
		<name>mapreduce.jobhistory.webapp.address</name>
		<value>master:19888</value>
		<description>JobHistory Server WEB访问的主机和端口</description>
	</property>
	<property>
		<name>mapreduce.jobhistory.intermediate-done-dir</name>
		<value>${yarn.app.mapreduce.am.staging-dir}/history/done</value>
		<description>运行完成的Hadoop作业记录日志存放在HDFS上的位置</description>
	</property>
	<property>
		<name>mapreduce.jobhistory.done-dir</name>
		<value>${yarn.app.mapreduce.am.staging-dir}/history/done_intermediate</value>
		<description>正在运行的Hadoop作业记录日志存放在HDFS上的位置</description>
	</property>
</configuration>

#配置所有配置
cd ..
scp -r ./hadoop daiyongjun@slave1:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/
scp -r ./hadoop daiyongjun@slave2:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/
scp -r ./hadoop daiyongjun@slave3:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/
scp -r ./hadoop daiyongjun@slave4:/opt/software/hadoop-2.5.0-cdh5.3.6/etc/

#启动JobHistory服务
mr-jobhistory-daemon.sh start historyserver

#启动mapreduce服务
cd /opt/software/hadoop-2.5.0-cdh5.3.6/share/hadoop/mapreduce/
hadoop jar hadoop-mapreduce-examples-2.5.0-cdh5.3.6.jar grep /usr/local/daiyongjun/input  /usr/local/daiyongjun/output 'dfs[a-z.]+'

```