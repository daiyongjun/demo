### 准备工作

#### 软件准备

```
#VmWare
链接：https://pan.baidu.com/s/19pMmUEviadFr2op5U5gJQw 
提取码：2oez 
复制这段内容后打开百度网盘手机App，操作更方便哦

#Centos
链接：https://pan.baidu.com/s/1oe0MpgsonBer5alNzkc5tQ 
提取码：3cjd

#Xshell社区版
链接：https://pan.baidu.com/s/1WANGHCMdk_GMruYupSJe4A 
提取码：yks1
```
#### 基本组件
```
ZooKeeper\HDFS\Yarn\MapReduce\Hive
```

#### 集群大小

```
机器数量：5台
机器配置：内存：4G 硬盘：20G CPU 核：4核心 网卡：内网百兆+外网
```

#### 环境准备
```
机器磁盘

操作系统
1、创建普通用户
#创建daiyongjun这个用户【用于安装软件，不用root用户防止rm *等操作】

2、配置普通用户的sudo权限

3、设置固定IP地址
192.168.31.108
192.168.31.109
192.168.31.110
192.168.31.111
192.168.31.112
192.168.31.113
4、设置主机名
cn.hadoop2.cluster.master
cn.hadoop2.cluster.slave1
cn.hadoop2.cluster.slave2
cn.hadoop2.cluster.slave3
cn.hadoop2.cluster.slave4
5、IP和主机名映射
#cn.hadoop2.cluster
192.168.31.109	cn.hadoop2.cluster.master
192.168.31.110	cn.hadoop2.cluster.slave1
192.168.31.111	cn.hadoop2.cluster.slave2
192.168.31.112	cn.hadoop2.cluster.slave3
192.168.31.113	cn.hadoop2.cluster.slave4
6、关闭防火墙、修改文件打开数量和用户最大进程数、关闭SElinux、服务机器之间的时间同步
```

#### 软件准备【Hadoop的发行版本】
```
#Apache【目前免费】
基于HDFS等，详细见我们之前的文档

#CDH【部分服务收费】
安装方式一、
http://archive.cloudera.com/cdh5/cdh/5/【tar包的方式】
安装方式二【文档介绍这种方式】、
http://archive.cloudera.com/cdh5/redhat【rpm包】
安装方式三、
parcels【官方推荐】需要使用Cloudera Manager进行安装

#HDP【部分服务收费】
https://cn.cloudera.com/

```


### 搭建机器,并配置相应环境

**搭建机器**
```
#安装 VmWare SoftWare 【内置激活步骤】

#装载 CentOS-7-x86_64-Everything-2003.iso【内置安装详解】

```

**使用普通用户登录，并设置sudo权限**
```
> su root
> *****【密码】
> chmod u+x /etc/sudoers
> vi /etc/sudoers
daiyongjun  ALL=(root)NOPASSWD:ALL
> exit
> sudo chmod u-x /etc/sudoers
#设置固定IP地址
> ifconfig
ens33 xxxxxxxxx
> cd /etc/sysconfig/network-scripts/
> ll | grep ens33
ifcfg-ens33
> vi ifcfg-ens33
#配置静态IP地址
BOOTPROTO=static # 使用静态IP地址，默认为dhcp 
IPADDR=192.168.31.109 # 设置的静态IP地址
NETMASK=255.255.255.0 # 子网掩码
GATEWAY=192.168.31.1 # 网关地址 
DNS1=114.114.114.114 # DNS服务器（此设置没有用到，所以我的里面没有添加）
DNS2=8.8.8.8
ONBOOT=yes  #设置网卡启动方式为 开机启动 并且可以通过系统服务管理器 systemctl 控制网卡
> sudo service network restart
```

**修改服务器主机名**
```
> sudo vi /etc/hostname
cn.hadoop2.cluster.master
> sudo vi /etc/hosts
127.0.0.1   localhost cn.hadoop2.cluster.master localhost4 localhost4.localdomain4
::1         localhost cn.hadoop2.cluster.master localhost6 localhost6.localdomain6

> sudo vi /etc/hostname
cn.hadoop2.cluster.slave1
> sudo vi /etc/hosts
127.0.0.1   localhost cn.hadoop2.cluster.slave1 localhost4 localhost4.localdomain4
::1         localhost cn.hadoop2.cluster.slave1 localhost6 localhost6.localdomain6

> sudo vi /etc/hostname
cn.hadoop2.cluster.slave2
> sudo vi /etc/hosts
127.0.0.1   localhost cn.hadoop2.cluster.slave2 localhost4 localhost4.localdomain4
::1         localhost cn.hadoop2.cluster.slave2 localhost6 localhost6.localdomain6

> sudo vi /etc/hostname
cn.hadoop2.cluster.slave3
> sudo vi /etc/hosts
127.0.0.1   localhost cn.hadoop2.cluster.slave3 localhost4 localhost4.localdomain4
::1         localhost cn.hadoop2.cluster.slave3 localhost6 localhost6.localdomain6

> sudo vi /etc/hostname
cn.hadoop2.cluster.slave4
> sudo vi /etc/hosts
127.0.0.1   localhost cn.hadoop2.cluster.slave4 localhost4 localhost4.localdomain4
::1         localhost cn.hadoop2.cluster.slave4 localhost6 localhost6.localdomain6
```

**关闭服务器防火墙【修改所有机器】**
```
#关闭防火墙
> sudo systemctl status firewalld.service
> sudo systemctl stop firewalld.service
#永久关闭
> sudo systemctl disable firewalld.service
```

**设置当前用户文件打开数量和用户最大进程数【修改所有机器】**
```
#查看所有
> ulimit -a (ulimit -n) (ulimit -u)
open files                      (-n) 2048
max user processes              (-u) 2048
> sudo vi /etc/security/limits.conf
* soft nofile 2048
* hard nofile 2048
* soft nproc 2048
* hard nproc 2048
> sudo reboot
```

**关闭SElinux**

centos的所有访问权限都是有SELinux来管理的，为了避免我们安装中由于权限关系而导致的失败，我们将其关闭
```
> /usr/sbin/sestatus –v
# 查看selinux的状态
# enabled【表示开启状态】
# disabled【修改成关闭状态】
SELinux status:                 enabled

> sudo vi /etc/selinux/config
SELINUX=disabled
# 在内存中关闭SElinux
> sudo setenforce 0 

# 检查内存中状态
> getenforce
# 日志显示结果为disabled或者permissive
```

**服务机器之间的时间同步**
```
#找一台机器作为时间服务器【cn.hadoop2.cluster.master】
> sudo rpm -qa|grep -E 'ntp|ntpdate' ntp | grep ntpdate
ntpdate-4.2.6p5-29.el7.centos.2.x86_64
ntp-4.2.6p5-29.el7.centos.2.x86_64
> sudo yum install ntp ntpdate
> sudo rpm -qa|grep -E 'ntp|ntpdate' ntp | grep ntpdate
ntpdate-4.2.6p5-29.el7.centos.x86_64【已安装】
ntp-4.2.6p5-29.el7.centos.2.x86_64【已安装】

#配置ntp服务
> sudo vi /etc/ntp.conf
restrict 192.168.31.0 mask 255.255.255.0 nomodify notrap
server 127.127.1.0
Fudge 127.127.1.0 stratum 10

# 实现硬件时间与系统时间同步
> sudo vi /etc/sysconfig/ntpd 
OPTIONS="-u ntp:ntp -p /var/run/ntpd.pid -g"
SYNC_HWCLOCK=yes

#开启ntpd服务
> sudo systemctl start ntpd
> sudo systemctl status ntpd
#永久开启
sudo systemctl disable chronyd.service
sudo systemctl enable ntpd.service

#登录其他机器,开启同步操作
> sudo crontab -l
> sudo crontab -e
# 分    时      日      月      周      cmd
*/5     *       *       *       *       ntpdate 192.168.31.109

```

https://docs.cloudera.com/documentation/enterprise/6/6.3/topics/configure_cm_repo.html#cm_repo

**使用CDH搭建Hadoop集群**
```
#rpm 安装 + yum本地源
# tar包安装比较困难【编译】
# rpm 安装
> sudo wget https://archive.cloudera.com/cm6/6.3.1/redhat7/yum/cloudera-manager.repo -P /etc/yum.repos.d/
> sudo rpm --import https://archive.cloudera.com/cm6/6.3.1/redhat7/yum//RPM-GPG-KEY-cloudera
```

**安装jdk**
```
#安装上传软件
> sudo yum -y install lrzsz
> mkdir -p /opt/package
> cd /opt/package
> rz jdk-8u261-linux-x64.tar.gz
> tar -zxvf jdk-8u261-linux-x64.tar.gz -C /opt/software

#配置hosts,方便远程复制
> sudo vi /etc/hosts
192.168.31.109  cn.hadoop2.cluster.master
192.168.31.110  cn.hadoop2.cluster.slave1
192.168.31.111  cn.hadoop2.cluster.slave2
192.168.31.112  cn.hadoop2.cluster.slave3
192.168.31.113  cn.hadoop2.cluster.slave4

#修改各个服务器中/OPT的权限
> sudo chown -R daiyongjun:daiyongjun /opt

> sudo scp -r ./software daiyongjun@cn.hadoop2.cluster.slave1:/opt
> sudo scp -r ./software daiyongjun@cn.hadoop2.cluster.slave2:/opt
> sudo scp -r ./software daiyongjun@cn.hadoop2.cluster.slave3:/opt
> sudo scp -r ./software daiyongjun@cn.hadoop2.cluster.slave4:/opt


> sudo vi /etc/profile
#java配置
export JAVA_HOME=/opt/software/jdk1.8.0_261
export CLASSPATH=$:CLASSPATH:$JAVA_HOME/lib/ 
export PATH=$PATH:$JAVA_HOME/bin

> source /etc/profile
```


**安装mysql**
```
# 安装MySql
> cd /opt/package
> wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm
> sudo rpm -ivh mysql-community-release-el7-5.noarch.rpm
> sudo yum update
> sudo yum install mysql-server
> sudo systemctl start mysqld
> sudo systemctl stop mysqld

# 修改访问权限
> sudo mysql
mysql> GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '1qaz2wsx' WITH GRANT OPTION;
mysql> GRANT ALL PRIVILEGES ON *.* TO 'daiyongjun'@'%' IDENTIFIED BY '1qaz2wsx' WITH GRANT OPTION;
mysql> use mysql;
mysql> select host,user,password from user;
+---------------------------+------------+-------------------------------------------+
| host                      | user       | password                                  |
+---------------------------+------------+-------------------------------------------+
| localhost                 | root       |                                           |
| cn.hadoop2.cluster.master | root       |                                           |
| 127.0.0.1                 | root       |                                           |
| ::1                       | root       |                                           |
| localhost                 |            |                                           |
| cn.hadoop2.cluster.master |            |                                           |
| %                         | root       | *F846B31F10DD4389C384272E70B9BBA3AD9E1F94 |
| %                         | daiyongjun | *F846B31F10DD4389C384272E70B9BBA3AD9E1F94 |
mysql> delete from user where password = '';
mysql> flush privileges;
mysql> exid
```


**修改本地repo源**
```
> sudo wget http://mirrors.aliyun.com/repo/Centos-7.repo -P /etc/yum.repos.d/
> sudo mv CentOs-Base.repo CentOs-Base.repo.bak
> sudo mv Centos-7.repo CentOs-Base.repo
> sudo yum makecache
> sudo yum update
```


**搭建本地yum源**

yum工具是基于rpm的，其一个重要的特性就是可以自动解决依赖问题。但是yum的本质依旧是把后缀名.rpm的包下载到本地，然后按次序安装之。但是每次执行yum install xxx，会自动安装并且安装完毕后把rpm包自动删除。而且有的yum源速度真的很慢每次都需要重复下载特别麻烦。因此构建本地yum源确实是个不错的选择。

**构建mysql的yum源为例**
```
# 把rpm包及其相关依赖全部都下载到本地，保存好
> cd /opt/package
> wget http://repo.mysql.com/mysql-community-release-el7-5.noarch.rpm
> sudo rpm -ivh mysql-community-release-el7-5.noarch.rpm
> sudo yum update
> yum install mysql-community-server --downloadonly --downloaddir=/opt/repo
```


```
.
[repo]
#name后面是注释信息，随意书写。
name=repo local repository
#baseurl这后面就是填写本地仓库路径了，file://表示使用本地文件协议，后面的/repo本地rpm包存放路径。
baseurl=file:///opt/repo
#gpgcheck=0这是和验证包的安全信息的，最好设置成0，表示关闭安全验证，否则还需要准备安全验证文件，麻烦一堆一堆的。
gpgcheck=0
#表示启用本仓库，0启用，1关闭。
enabled=1
```

```
# 生成repodate信息
> sudo createrepo /opt/repo/
> cd /opt/repo
> ll | grep repodata
repodata【存在表示成功】
#如何repo中下载新的rpm包，如后续我们需要下载clouder manager 相关的rpm包
cloudera-manager-agent-6.3.1-1466458.el7.x86_64.rpm	2019-10-11 08:42	10.00MB
cloudera-manager-daemons-6.3.1-1466458.el7.x86_64.rpm	2019-10-11 08:42	1.12GB
cloudera-manager-server-6.3.1-1466458.el7.x86_64.rpm	2019-10-11 08:42	11.22KB
cloudera-manager-server-db-2-6.3.1-1466458.el7.x86_64.rpm	2019-10-11 08:42	10.74KB
enterprise-debuginfo-6.3.1-1466458.el7.x86_64.rpm	2019-10-11 08:42	13.55MB
oracle-j2sdk1.8-1.8.0+update181-1.x86_64.rpm	2019-10-11 08:42	176.42MB
#将上述包移动到/opt/repo目录，重新执行,则可读以上rpm包
> sudo createrepo /opt/repo/
```


```
#检查以及使用
> yum repoinfo repo
Loaded plugins: fastestmirror, langpacks
Determining fastest mirrors
 * base: mirrors.ustc.edu.cn
 * extras: mirrors.ustc.edu.cn
 * updates: mirrors.ustc.edu.cn
repo                                                        | 2.9 kB  00:00:00     
repo/primary_db                                             |  11 kB  00:00:00     
Repo-id      : repo
Repo-name    : repo local repository
Repo-status  : enabled
Repo-revision: 1598346685
Repo-updated : Tue Aug 25 05:11:26 2020
Repo-pkgs    : 5
Repo-size    : 88 M
Repo-baseurl : file:///opt/repo/
Repo-expire  : 21,600 second(s) (last: Tue Aug 25 05:18:22 2020)
  Filter     : read-only:present
Repo-filename: /etc/yum.repos.d/repo.repo

repolist: 5
> sudo yum clean all
> sudo yum repolist 
Loaded plugins: fastestmirror, langpacks
Loading mirror speeds from cached hostfile
 * base: mirrors.njupt.edu.cn
 * extras: mirrors.163.com
 * updates: mirrors.163.com
repo id                                                                               repo name                                                                      status
base/7/x86_64                                                                         CentOS-7 - Base                                                                10,070
extras/7/x86_64                                                                       CentOS-7 - Extras                                                                 413
mysql-connectors-community/x86_64                                                     MySQL Connectors Community                                                        165
mysql-tools-community/x86_64                                                          MySQL Tools Community                                                             115
mysql56-community/x86_64                                                              MySQL 5.6 Community Server                                                        547
repo                                                                                  repo local repository                                                               5
updates/7/x86_64                                                                      CentOS-7 - Updates                                                              1,112
repolist: 12,427

#安装mysql
> sudo yum install mysql-community-server
```

```
#安装mysql-connector
> cd /opt/package
> wget https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-5.1.46.tar.gz
> tar zxvf mysql-connector-java-5.1.46.tar.gz -C /opt/software
> sudo mkdir -p /usr/share/java/
> cd /opt/software/mysql-connector-java-5.1.46
> sudo cp mysql-connector-java-5.1.46-bin.jar /usr/share/java/mysql-connector-java.jar

> sudo systemctl start mysqld

```

```
#创建一个远程的repo库
> sudo yum install httpd
> sudo systemctl start httpd
> mkdir -p /opt/www/html/cloudera-repos/cm6
#下载
> cd /opt/package/
> rz cm6.3.1-redhat7.tar.gz
> tar xvfz cm6.3.1-redhat7.tar.gz -C /opt/www/html/cloudera-repos/cm6 --strip-components=1
> sudo chmod -R ugo+rX /opt/www/html/cloudera-repos/cm6
# --continue是断点续传
# CDH 6.3.2 or lower
> sudo wget --continue --recursive --no-parent --no-host-directories https://archive.cloudera.com/cdh6/6.3.2/redhat7/ -P /opt/www/html/cloudera-repos
> sudo wget --continue --recursive --no-parent --no-host-directories https://archive.cloudera.com/gplextras6/6.3.2/redhat7/ -P /opt/www/html/cloudera-repos
> sudo chmod -R ugo+rX /opt/www/html/cloudera-repos/cdh6
> sudo chmod -R ugo+rX /opt/www/html/cloudera-repos/gplextras6
# Apache Accumulo for CDH
> sudo wget --continue --recursive --no-parent --no-host-directories https://archive.cloudera.com/accumulo-c5/redhat/ -P /opt/www/html/cloudera-repos
> sudo chmod -R ugo+rX /opt/www/html/cloudera-repos/accumulo-c5
```
**启动server**
```
> sudo systemctl start cloudera-scm-server
> sudo tail -f /var/log/cloudera-scm-server/cloudera-scm-server.log

> sudo systemctl status cloudera-scm-agent
```

**安装cdh**
```
#parcels
> wet https://archive.cloudera.com/cdh6/6.3.2/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554-el7.parcel
> wet https://archive.cloudera.com/cdh6/6.3.2/parcels/CDH-6.3.2-1.cdh6.3.2.p0.1605554-el7.parcel.sha11

#设置交换区的大小
> cat /proc/sys/vm/swappiness
60
> sudo sysctl -w vm.swappiness=0
> cat /proc/sys/vm/swappiness
0
> echo 'vm.swappiness=0' >> /etc/sysctl.conf
> su root 
> echo never > /sys/kernel/mm/transparent_hugepage/defrag
> echo never > /sys/kernel/mm/transparent_hugepage/enabled
```

```
> CREATE DATABASE amon DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
> GRANT ALL ON amon.* TO 'daiyongjun'@'%';
```

#### 引用相关文章
>[cloudera : 官方安装Documentation](https://docs.cloudera.com/documentation/enterprise/6/6.3/topics/install_cm_cdh.html "Hive的mysql安装配置")