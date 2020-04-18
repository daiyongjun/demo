## 谷歌的三篇论文


GFS:文件系统  ===>  hdfs
mapreduce:数据处理框架  ===> mapreduce
bigTalbe：存储结构化数据 ===> hbase

## google架构

爬虫  ==> 获取网页的,标题，正文，关键词等
mapreduce ==> 进行爬虫的数据进行分析和解析
hdfs ==>
原始数据存储
hbase ==>
最终存储在结构化数据

爬虫框架
nutch[开源web搜索引擎]
lucence【开源的全文检索工具】
=============
*MapReduce，对爬虫的数据进行分析和计算
    分布式【可扩展】
    思想：
        分而治之
        大的数据集合分为小的数据集合
        map:大的数据集合分成的小的数据集合，进行逻辑业务处理
      
        reduce：将小的逻辑处理结果合并成最终结果
*HDFS 存储爬虫的网页数据的原始数据 
    存储海量数据
    分布式
    安全性
        副本数据

 *yarn
    分布式资源管理框架
    * 管理真个集群的资源（内存，cpu核心数）
    *分配调度

*common模块：
    hadoop的基础模块
    
###hadoop的安装

apache官方历史版本软件的位置
http://archive.apache.org/dist/
找到hadoop/common目录


安装java
export JAVA_HOME=/opt/jdk1.7.0_79
export CLASSPATH=$:CLASSPATH:$JAVA_HOME/lib/ 
export PATH=$PATH:$JAVA_HOME/bin
source /etc/profile

下载hadoop 2.50
从官网中找到document
找到
https://hadoop.apache.org/docs/r2.5.2/hadoop-project-dist/hadoop-common/SingleCluster.html

单机版的构建


```
#java配置
export JAVA_HOME=/opt/jdk1.7.0_79
export CLASSPATH=$:CLASSPATH:$JAVA_HOME/lib/ 
export PATH=$PATH:$JAVA_HOME/bin
#hadoop
export HADOOP_PREFIX=/opt/hadoop-2.5.0-cdh5.3.6
export PATH=$PATH:$HADOOP_PREFIX/bin

```
执行
```
执行hadoop 查看命令

hadoop jar /opt/hadoop-2.5.0-cdh5.3.6/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.5.0-cdh5.3.6.jar grep input output 'dfs[a-z.]+'

```

伪分布式版的构建
etc/hadoop/core-site.xml:
配置访问hdfs地址
```
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
```
hdfs的核心配置
etc/hadoop/hdfs-site.xml:
```
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>
```

配置免登陆root用用户下，参照官网
非root用户如下：
```
su daiyongjun	
cd ~/.ssh/                     # 若没有该目录，请先执行一次ssh localhost
ssh-keygen -t rsa              # 会有提示，都按回车就可以
cat id_rsa.pub >> authorized_keys  # 加入授权
chmod 600 ./authorized_keys    # 修改文件权限
ssh daiyongjun@localhost    #执行验证
```

启动hdfs【第一次启动需要格式化文件】
```
hdfs namenode -format

start-dfs.sh
```
hdfs提供一个web页面供使用操作
http://localhost:50070/





在hdfs中创建文件夹和目录【执行完默认切换到指定目录下】
```
hdfs dfs -mkdir /user

hdfs dfs -mkdir /user/daiyongjun
```
![image](50B3BB423B954CF09F9FB7D733D2CD66)
复制操作系统下的文件到hdfs目录下
```
hdfs dfs -put /operate/input input
```
![image](881687A827544A5CAB1457EB7C69E647)



执行mapreduce目录【默认使用的是hdfs文件系统】
```
hadoop jar /opt/hadoop-2.5.0-cdh5.3.6/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.5.0-cdh5.3.6.jar grep input output 'dfs[a-z.]+'
```

![image](2250D4A5B5F5467EA6F754F73926C62D)

查看hdfs文件内容
1、将hdfs的文件移动到操作系统的文件系统中
```
bin/hdfs dfs -get output output
cat output/*
```
2、在hdfs中查看文件内容
```
hdfs dfs -cat output/*
```

关闭hdfs文件系统
```
sbin/stop-dfs.sh
```

将hadoop运行在yarn上【基于上面的所有操作】
伪分布式构建在site下
cp mapred-site.xml.template mapred-site.xml
```
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
```
配置 vi yarn-site.xml
```
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
```
启动yarn的	
start-yarn.sh
浏览器查看ResourceManager的管理界面
 http://localhost:8088/
 
 ![image](F847E43CD9E042EEA85682FF50CE744E)
 
 关闭防火墙
 ```
sudo systemctl stop firewalld 临时关闭
sudo systemctl disable firewalld ，然后reboot 永久关闭
sudo systemctl status  firewalld 查看防火墙状态。
 ```
 