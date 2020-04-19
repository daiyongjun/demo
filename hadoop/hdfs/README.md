>[HDFS Users Guide: Apache Hadoop 2.5.2](https://hadoop.apache.org/docs/r2.5.2/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html "HDFS用户指南")

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


```


Configuring the Hadoop Daemons in Non-Secure Mode 
非安全模式下的配置,来源于官网
hdfs-site.xml 描述【NameNode相关配置】
参数|属性值|描述
---|---|-------
dfs.namenode.name.dir|file:///${hadoop.tmp.dir}/namenode,file:///${hadoop.tmp.dir}/duplicate/namenode|命名空间和事务在本地文件系统永久存储的路径,逗号支持分隔多个本地路径，将命名空间和事务复制到多个目录实现冗余
dfs.namenode.hosts/dfs.namenode.hosts.exclude|slave1,slave2,slave3,slave4|允许datanode节点列表,逗号支持分隔多个节点
dfs.blocksize|268435456|大型文件系统的HDFS块大小为256MB
dfs.namenode.handler.count|100|设置更多的namenode线程，处理从datanode发出的大量RPC请求


hdfs-site.xml描述【DataNode相关配置】
参数|属性值|描述
---|---|-------
dfs.namenode.name.dir|file:///${hadoop.tmp.dir}/datanode,file:///${hadoop.tmp.dir}/duplicate/datanode|数据在本地文件系统永久存储的路径,逗号支持分隔多个本地路径，将命名空间和事务复制到多个目录实现冗余，通常在不同的设备上
dfs.replication|3|数据冗余处理,文件副本数

11
参数|属性值|描述
---|---|-------
hdfsdfs-appendToFile<localsrc>...<dst>|file:///${hadoop.tmp.dir}/datanode,file:///${hadoop.tmp.dir}/duplicate/datanode|数据在本地文件系统永久存储的路径,逗号支持分隔多个本地路径，将命名空间和事务复制到多个目录实现冗余，通常在不同的设备上
hdfsdfs-catURI[URI...]|hdfsdfs-cat/usr/daiyongjun|将源路径复制到标准输出，实际就是将源文件进行输出
