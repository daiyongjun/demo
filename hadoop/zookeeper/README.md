# zookeeper学习笔记
git冲突问题:
版本库修改都是有版本号
 如:同一个分支下，master初始版本我们简称为m1后面迭代升级都是基于现有版本号+1称为m2，一个人使用和只有一个分支情况下不会出现冲突，但真实的git环境下，都是多个用户本地维护一个版本库副本【随时更新】，进而操作一个版本库。基于一个分支下提交前提是基于一个初始修改版本如m2。冲突往往就在随时更新时候，提高自身修改内容的初始版本，如我修改版本基于m1。此时我已经修改若干文件，需要提交代码我们昵称，需要提交到m2版本。可是提交过程发现已经存在m2版本，更甚至有了m3和m4版本。所以此时我需要修改自身版本为m4。但是发现在我修改若干文件中，m4版本中也有修改，那问题来了?都是修改，我该留哪一个呢，此时就有了冲突内容。系统将这些内容交由开发者手动操作。
 另一种情况，不同分支下的合并操作，同样我们这边有master和dev分支。dev是基于m2有了m2dev1,m2dev2……此时需要将m2dev2与m3进行合并。此时m2到m2dev2修改的文件与m2到m3之间修改的文件进行合并，若两者之间出现操作相同的文件都会产生冲突。
 
## zookeeper是什么
zxid 数据修改的版本号
Epoch 投票次数版本号或者理解为投票次数【随着投票次数增加而增加】

选举过程分为三个阶段
第一阶段:读取自身的zxid
第二阶段:发送投票信息【投自己+投别人】
发送投票信息，信息内容包括【leader的severid zxid  Epoch】
第三阶段:接收投票信息并将投票信息广播给其他server
获取投票信息按Epoch的值进行不同处理
发送Epoch时钟与当前的epoch进行比较。
若epoch旧:自己早就投过票
若epoch相同
若

每个服务器都是独立的，在启动时均从初始状态开始参与选举


## paoax是什么呢