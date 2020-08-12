# elastic-search-query-sql
#### 查询
**全量查询**
```
select * from app
```
```
数据比较多.....
```
**全量查询,限定条数**
```
select news_posttime,news_title from app limit 1
```
```
{
  "headers": [
    "news_posttime",
    "news_title"
  ],
  "lines": [
    "2020-03-04 09:06:13\t美国新冠肺炎感染总数达117人 新增3例死亡病例"
  ]
}
```

**全量查询,条件查询**

```
select news_posttime,news_title from app where news_title = '疫情' and news_posttime >= '2020-03-09 00:00:00'
```

```
{
  "headers": [
    "news_posttime",
    "news_title"
  ],
  "lines": [
    "2020-03-09 09:35:44\t本来是个开酒吧的，疫情让我变成了送外卖啊",
    "2020-03-09 09:40:56\t中国留学人员援助国内抗击疫情纪实：巴塞学联众志成城“与时间赛跑”",
    "2020-03-09 09:15:07\t世卫组织：中国以外全球受新冠肺炎疫情影响的国家和地区已达101个",
    "2020-03-09 09:23:36\t疫情减弱绕路回家，拖挂房车海南过海两台车要收取多少费用？",
    "2020-03-09 09:36:00\t疫情通报：3月8日12-24时山东新型冠状病毒肺炎疫情情况",
    "2020-03-09 09:36:00\t新冠肺炎世界疫情形势 意大利大面积爆发",
    "2020-03-09 09:34:00\t美国哥伦比亚大学宣布因新冠肺炎疫情停课两天",
    "2020-03-09 09:29:00\t姚晨为什么为张伟丽加油！疫情时期！全世界都需要这样一场比赛！",
    "2020-03-09 09:22:37\t助力疫情防控，中南建设成功发行17亿元公司债券",
    "2020-03-09 09:16:00\t银行业周报204期：疫情中的银行理财子公司"
  ]
}
```

**全量查询,限定条数,设置排除条**
```
select news_posttime,news_title from app1_retention where news_title = '疫情' and not (news_title = '安徽' and news_title = '合肥') limit 10
```

```
{
  "headers": [
    "news_posttime",
    "news_title"
  ],
  "lines": [
    "2020-07-03 00:02:00\t罗湖疫情联防联控中闪耀着港籍志愿者的身影，他们服务独居高龄长者、官方信息“搬运工”……",
    "2020-07-03 00:11:23\t创新河北｜河北应对疫情多措并举 科技创新亮点纷呈",
    "2020-07-03 00:06:39\t李兰娟院士来重庆坐诊谈疫情：建议加大对冷链载体、物流货物等检验力度",
    "2020-07-03 00:23:55\t2020年，我们都磨练的见怪不怪了。维密宣布破产，Zara宣布关闭1000多家门店，星巴克减少400家+门店。除了这些耳熟能详的企业，国内每天都有成千上万的企业面临着破产。\\n\\n疫情之下，大多数人的生活大打折扣，还有不少人背上了巨额债务。\\n\\n这个世界上只有变化才是永恒不变的真理。\\n\\n昨天还一起学习飞机",
    "2020-07-03 00:28:00\t吉林省卫生健康委员会关于新型冠状病毒肺炎疫情情况通报（2020年7月2日公布）",
    "2020-07-03 00:32:53\t【夺取疫情防控和经济社会发展双胜利】保基层运转 添发展动力",
    "2020-07-03 00:29:43\t创新河北｜河北应对疫情多措并举 科技创新亮点纷呈",
    "2020-07-03 00:08:37\t高考即至，暑期工作怎么找？这个平台，方便快捷，看看呗。\\n   7月全国高考在即，再现千军万马过独木桥的场景。神兽即将出笼，老师将进入闭关修练。莘莘学子，十年寒窗，终于拔云见日了！\\n    普大喜奔的同时，你的孩子暑假期间如何度过？在当前疫情未退，跨省旅游暂未开放之际，这个暑期，令全国的家长心焦不己。",
    "2020-07-03 00:12:18\t罗湖疫情联防联控中闪耀着港籍志愿者的身影，他们服务独居高龄长者、官方信息“搬运工”……",
    "2020-07-03 00:12:00\t线上讲座 | 留英数年，见证数次历史！剑桥博士学长专业解读：疫情下的英国留学和就业现状"
  ],
  "scrollId": "",
  "total": 9438
}
```


**全量查询,条件查询,混合条件**
```
select news_posttime,news_title from app where (news_title = '疫情' OR news_content = '疫情') and news_posttime BETWEEN  '2020-03-07 00:00:00' AND '2020-03-08 00:00:00'
```

```
{
  "headers": [
    "news_posttime",
    "news_title"
  ],
  "lines": [
    "2020-03-07 21:44:00\t截至7日12时，泰安“清零”第一个24小时！何时能解除疫情防控？中央指导组回应了！",
    "2020-03-07 21:12:00\t世卫：各国应把遏制疫情作为首要任务",
    "2020-03-07 18:35:40\t三十四年头一回！美国西南偏南艺术节宣布取消 多家互联网巨头早已退展",
    "2020-03-07 09:26:11\t关门大吉！这是一次“爱的再见！”",
    "2020-03-07 10:35:00\t爱心相助过难关：临潭干部群众自发为疫情防控工作捐款",
    "2020-03-07 01:54:23\t钉钉双线“战疫”：2亿上班族和5000万学生“上云”",
    "2020-03-07 20:03:47\t支教研究生在家当“主播”收获千余粉丝",
    "2020-03-07 10:00:04\t儋州：厕改户3月份至6月份内完成厕改有奖补 最高1600元/户",
    "2020-03-07 08:01:00\t特斯拉的“减配门”到底错在哪里？你以为买车是“一锤子买卖”？",
    "2020-03-07 19:19:26\t开发商降价“投降”，地产专家12字“说破”真像"
  ]
}
```


**全量查询,条件查询,排序**
```
select news_posttime,news_title from app where news_title = '疫情' and news_posttime >= '2020-03-09 00:00:00' order by news_posttime desc
```


```
{
  "headers": [
    "news_posttime",
    "news_title"
  ],
  "lines": [
    "2020-03-10 14:10:09\t湖北交投长江路桥公司助力老挝疫情防控宣传",
    "2020-03-10 14:09:59\t国外：不戴口罩，戴口罩被嘲笑、聚会很安全，再不重视疫情就晚了",
    "2020-03-10 14:09:56\t国际疫情蔓延，该怎么办？WHO：有中国经验，其他国家不必“从零开始”",
    "2020-03-10 14:09:51\t世卫谈中国疫情：中国正在控制住这场瘟疫走向结束",
    "2020-03-10 14:09:48\t最高法：妨害疫情防控刑事案件九成采用速裁或简易程序审理",
    "2020-03-10 14:09:40\t广西权威部门：疫情期间，网络简历可别乱投！",
    "2020-03-10 14:09:03\t生日遇上疫情 悉尼男子收厕纸“豪礼”欣喜若狂",
    "2020-03-10 14:09:02\t视频｜生日遇上疫情 悉尼男子收厕纸“豪礼”欣喜若狂",
    "2020-03-10 14:09:00\t疫情防控新闻发布会｜上海追踪到境外输入型病例密接18人",
    "2020-03-10 14:09:00\t涉案金额3008万余元！浙江公安破获利用疫情实施电信网络诈骗案件403起"
  ]
}
```
**全量查询,条件查询,自定义方法,使用方法按ES权威指南的描述**

**term**
```
select news_media,news_posttime,news_title from web2_retention,app1_retention where news_title = '疫情' and news_media= term(web) and news_posttime >= '2020-03-30 11:51:44' and news_posttime <= '2020-03-30 11:52:34' order by news_posttime desc
```

```
{
  "headers": [
    "news_media",
    "news_posttime",
    "news_title"
  ],
  "lines": [
    "web\t2020-03-30 11:52:33\t海外疫情简报：全球死亡病例超3万 武汉与纽约专家连线",
    "web\t2020-03-30 11:52:32\t最前线 受疫情影响，中金将云米2020年非GAAP净利润预测下调59%",
    "web\t2020-03-30 11:52:29\t九成在欧韩企称因新冠疫情遭受重创",
    "web\t2020-03-30 11:52:25\t一线记者调查：武汉 6893 个小区做到无疫情",
    "web\t2020-03-30 11:52:24\t疫情之下，国际足联祭出一个英明且暖心举措，中超联赛也从中受益",
    "web\t2020-03-30 11:52:22\t沈阳那家餐馆针对疫情横幅是不地道：但白岩松不必居高临下防蠢货",
    "web\t2020-03-30 11:52:19\t山东省疫情指挥部最新发布！事关自湖北入鲁返鲁人员！扩散提醒~",
    "web\t2020-03-30 11:52:19\t江苏省农业厅：加大疫情防控期间保险惠农投入",
    "web\t2020-03-30 11:52:18\t疫情会否二次爆发 疫情期间外国人入境政策 中国出境游市场发展潜力分析",
    "web\t2020-03-30 11:52:12\t海正药业走势稳步前行。日线走出突破型态。5日线上破10钱上功走势。疫情严重利好唯"
  ]
}
```


#### 聚合
**获取索引中总量**
```
select count(*) as 总量 from app 
```

```
{
  "headers": [
    "总量"
  ],
  "lines": [
    "1.4203461E7"
  ]
}
```

**指定字段求和**
```
select sum(news_read_count) as 总阅量 from app 
```

```
{
  "headers": [
    "总阅量"
  ],
  "lines": [
    "1.524083768E9"
  ]
}
```


**指定字段最小值**
```
select min(news_read_count) as 最低阅量 from app
```

```
{
  "headers": [
    "最低阅量"
  ],
  "lines": [
    "0.0"
  ]
}
```


**指定字段最大值**
```
select max(news_read_count) as 最高阅量 from app
```

```
{
  "headers": [
    "最高阅量"
  ],
  "lines": [
    "1.2198951E7"
  ]
}
```

**指定字段平均值**
```
select avg(news_read_count) as 平均阅量 from app 
```

```
{
  "headers": [
    "平均阅量"
  ],
  "lines": [
    "107.43428943441474"
  ]
}
```

**指定字段各项统计**
```
select stats(news_read_count) as 阅量 from app 
```

```
{
  "headers": [
    "阅量.count",
    "阅量.sum",
    "阅量.avg",
    "阅量.min",
    "阅量.max"
  ],
  "lines": [
    "14225981\t1.528006336E9\t107.40955832852582\t0.0\t1.2198951E7"
  ]
}
```

**指定字段各项统计【扩展】**
```
select extended_stats(news_read_count) as 阅量 from app 
```

```
{
  "headers": [
    "阅量.count",
    "阅量.sum",
    "阅量.avg",
    "阅量.min",
    "阅量.max",
    "阅量.sumOfSquares",
    "阅量.variance",
    "阅量.stdDeviation"
  ],
  "lines": [
    "14230366\t1.528268641E9\t107.39489349746873\t0.0\t1.2198951E7\t7.73541345190111E14\t5.434696598402144E7\t7372.039472494802"
  ]
}
```


**指定字段百分数统计**
```
select percentiles(news_read_count) as 阅量 from app
```

```
{
  "headers": [
    "阅量.1.0",
    "阅量.5.0",
    "阅量.25.0",
    "阅量.50.0",
    "阅量.75.0",
    "阅量.95.0",
    "阅量.99.0"
  ],
  "lines": [
    "0.0\t0.0\t0.0\t0.0\t0.0\t13.0\t800.3853961367931"
  ]
}
```


**分组计数**
```
select count(*) as 数量 from app,web group by  platform
```

```
{
  "headers": [
    "platform",
    "数量"
  ],
  "lines": [
    "app\t1.428232E7",
    "web\t1.0466105E7"
  ]
}
```

**多次分组计数**
```
select count(*) as 数量 from app,web group by platform,news_emotion
```

```
{
  "headers": [
    "platform",
    "news_emotion",
    "数量"
  ],
  "lines": [
    "app\t中性\t8454397.0",
    "app\t正面\t4791509.0",
    "app\t负面\t1050117.0",
    "web\t中性\t6817601.0",
    "web\t正面\t3020043.0",
    "web\t负面\t637151.0"
  ]
}
```

**多次分组计数并排序**
```
select count(*) as 数量 from app,web group by platform,news_emotion order by 数量 
```

```
{
  "headers": [
    "platform",
    "news_emotion",
    "数量"
  ],
  "lines": [
    "app\t负面\t1055113.0",
    "app\t正面\t4813810.0",
    "app\t中性\t8498219.0",
    "web\t负面\t640540.0",
    "web\t正面\t3034790.0",
    "web\t中性\t6854289.0"
  ]
}
```

**多次分组计数并排序**
```
select count(*) as 数量 from app,web group by platform,news_emotion order by 数量 LIMIT 1 
```

```
{
  "headers": [
    "platform",
    "news_emotion",
    "数量"
  ],
  "lines": [
    "app\t负面\t1056140.0",
    "app\t正面\t4818236.0",
    "app\t中性\t8506446.0"
  ]
}
```

**指定range进行分组**
```
select count(*)  as 数量 from app,web group by range(news_read_count,20,25,30,35,40) 
```

```
{
  "headers": [
    "range(news_read_count,20,25,30,35,40)",
    "数量"
  ],
  "lines": [
    "20.0-25.0\t78292.0",
    "25.0-30.0\t50595.0",
    "30.0-35.0\t32385.0",
    "35.0-40.0\t20134.0"
  ]
}
```

**指定时间进行分组**
```
select count(*) as '文章数' from app,web group by date_histogram(field='news_posttime','interval'='1d','format'='yyyy-MM-dd','min_doc_count'=5)
```

```
{
  "headers": [
    "date_histogram(field=news_posttime,interval=1d,format=yyyy-MM-dd,min_doc_count=5)",
    "'文章数'"
  ],
  "lines": [
    "2020-03-01\t2097420.0",
    "2020-03-02\t2858905.0",
    "2020-03-03\t2857089.0",
    "2020-03-04\t2859570.0",
    "2020-03-05\t2834611.0",
    "2020-03-06\t2817686.0",
    "2020-03-07\t2175247.0",
    "2020-03-08\t2025813.0",
    "2020-03-09\t2730430.0",
    "2020-03-10\t2129026.0"
  ]
}
```


**固定时间间隔进行分组**
```
select count(*) as '文章数' from app,web group by date_histogram(field='news_posttime','interval'='1d','format'='yyyy-MM-dd','min_doc_count'=5,'alias'='自定义分组')
```

```
{
  "headers": [
    "自定义分组",
    "'文章数'"
  ],
  "lines": [
    "2020-03-01\t2097422.0",
    "2020-03-02\t2858906.0",
    "2020-03-03\t2857094.0",
    "2020-03-04\t2859585.0",
    "2020-03-05\t2834625.0",
    "2020-03-06\t2817700.0",
    "2020-03-07\t2175277.0",
    "2020-03-08\t2025848.0",
    "2020-03-09\t2730707.0",
    "2020-03-10\t2135461.0"
  ]
}
```


**指定时间间隔进行分组**
```
select count(*) as '文章数' from app,web  group by date_range(field='news_posttime','format'='yyyy-MM-dd' ,'2020-03-01','2020-03-10','now-3d','now-2d','now-1d','now')
```

```
{
  "headers": [
    "date_range(field=news_posttime,format=yyyy-MM-dd,2020-03-01,2020-03-10,now-3d,now-2d,now-1d,now)",
    "'文章数'"
  ],
  "lines": [
    "2020-03-01-2020-03-10\t2.3257806E7",
    "2020-03-07-2020-03-08\t2112819.0",
    "2020-03-08-2020-03-09\t2274246.0",
    "2020-03-09-2020-03-10\t2704684.0",
    "2020-03-10-2020-03-07\t0.0"
  ]
}
```

**按时间并对情感属性【由于七牛库中只有情感数值没有情感枚举类型如：负面，中性，正面】**
```
select count(*) as 数量 from app1_retention,web2_retention,weibo1_retention,weixin1_retention where news_content='肺炎' and news_posttime >'2020-06-01 00:00:00' group by date_histogram(field='news_posttime','interval'='1d','format'='yyyy-MM-dd','min_doc_count'=5,'alias'='时间'),range(news_negative,0,0.45,0.75,1)
```


```
{
  "headers": [
    "时间",
    "range(news_negative,0,0.45,0.75,1)",
    "数量"
  ],
  "lines": [
    "2020-06-01\t0.0-0.45\t72365.0",
    "2020-06-01\t0.45-0.75\t217091.0",
    "2020-06-01\t0.75-1.0\t28177.0",
    "2020-06-02\t0.0-0.45\t59508.0",
    "2020-06-02\t0.45-0.75\t220473.0",
    "2020-06-02\t0.75-1.0\t41648.0",
    ......
  ],
  "scrollId": "",
  "total": 4584390
}
```


#### 下载
**单条语句,自定义列名,默认条数**
```
select news_posttime as 时间,news_title as 标题,collection_from as 抓取来源,case when news_negative < 0.45 then '正' when news_negative < 0.75 then '中' else '负' end as 情感类型 from app1_retention
```

```
时间	标题	抓取来源	情感类型
2020/6/16 1:08	精选几道美味下饭的家常菜，香味十足，简单易做，营养又解馋	mirror_refine_3g.k.sohu.com/t/n[ID]	中
2020/6/16 1:07	这个是什么剧来着	mirror_refine_m.hupu.com/bbs/[ID].html	中
2020/6/16 1:08	来摆摊吧！派林大药房发布地摊支持计划！	mirror_refine_3g.k.sohu.com/t/n[ID]	中
2020/6/16 1:08	想跟FM105明星主播一起自驾旅行？快来pick你最期待的，谁能C位出道？就看你的了！	mirror_refine_3g.k.sohu.com/t/n[ID]	中
2020/6/16 1:08	教您正宗鱼香肉丝的做法，酸甜可口，简单易做，老婆每天吵着要吃	mirror_refine_3g.k.sohu.com/t/n[ID]	中
2020/6/16 1:09	婚姻中，女人的这5个行为会引起男人的反感，让感情陷入危机！	monitor_account_qktoutiao_key	中
2020/6/16 1:06	和平年代！最为可爱的一群人！谢谢你们，默默为人民负重前行！	monitor_account_baijia_key	正
2020/6/16 0:27	长子娶回个傻儿媳，爸爸气得饭桌上喝闷酒，没想到儿媳的傻是装的	monitor_account_k.sina_key	中
2020/6/16 1:07	八竿子打不着的杨丞琳和李荣浩，是怎么成为被柠檬的恩爱夫妻的？	monitor_account_wangyi_key	正
2020/6/16 0:43	女朋友男人有时候很可怜，多关爱一下他们吧	monitor_account_qq_key	中
```

**多条语句,自定义列名,默认条数,可以支持更多**
```
select news_posttime as 时间,news_title as 标题,collection_from as 抓取来源,case when news_negative < 0.45 then '正' when news_negative < 0.75 then '中' else '负' end as 情感类型 from app1_retention,web2_retention UNION select news_posttime as 时间,news_title as 标题,collection_from as 抓取来源,case when news_negative < 0.45 then '正' when news_negative < 0.75 then '中' else '负' end as 情感类型 from weixin1_retention
```
**UNION A**
```
时间	标题	抓取来源	情感类型
2020/6/16 1:08	精选几道美味下饭的家常菜，香味十足，简单易做，营养又解馋	mirror_refine_3g.k.sohu.com/t/n[ID]	中
2020/6/16 1:07	这个是什么剧来着	mirror_refine_m.hupu.com/bbs/[ID].html	中
2020/6/16 1:08	来摆摊吧！派林大药房发布地摊支持计划！	mirror_refine_3g.k.sohu.com/t/n[ID]	中
2020/6/16 1:08	想跟FM105明星主播一起自驾旅行？快来pick你最期待的，谁能C位出道？就看你的了！	mirror_refine_3g.k.sohu.com/t/n[ID]	中
2020/6/16 1:08	教您正宗鱼香肉丝的做法，酸甜可口，简单易做，老婆每天吵着要吃	mirror_refine_3g.k.sohu.com/t/n[ID]	中
2020/6/16 1:09	婚姻中，女人的这5个行为会引起男人的反感，让感情陷入危机！	monitor_account_qktoutiao_key	中
2020/6/16 1:06	和平年代！最为可爱的一群人！谢谢你们，默默为人民负重前行！	monitor_account_baijia_key	正
2020/6/16 0:27	长子娶回个傻儿媳，爸爸气得饭桌上喝闷酒，没想到儿媳的傻是装的	monitor_account_k.sina_key	中
2020/6/16 1:07	八竿子打不着的杨丞琳和李荣浩，是怎么成为被柠檬的恩爱夫妻的？	monitor_account_wangyi_key	正
2020/6/16 0:43	女朋友男人有时候很可怜，多关爱一下他们吧	monitor_account_qq_key	中
```

**UNION B**
```
时间	标题	抓取来源	情感类型
2020/6/16 8:00	一杯解决各种老胃病，简单有效！	xinhuawang	中
2020/6/16 8:28	美加新闻播报丨中美航班翻倍，美联航、达美获批！佛洛伊德6岁孤女当迪士尼股东！美国三大州新增确诊病例创新高丨北京时间6月16日	xinhuawang	中
2020/6/16 8:30	【预警】美国CPSC拟豁免部分纤维测试	xinhuawang	中
2020/6/16 7:00	风吹麦浪 新粒归仓	gsdata	中
2020/6/16 8:30	二货给客服美女打电话，套路深笑懵姐了！	xinhuawang	中
2020/6/16 8:30	紧急提醒！	xinhuawang	中
2020/6/16 8:12	达摩108手中医正骨手法——第一节颈椎五把推	xinhuawang	中
2020/6/16 8:30	急寻密接者！四川确诊1例病例，活动轨迹公布！	xinhuawang	中
2020/6/16 8:30	城市地下管廊及给排水设计	xinhuawang	中
2020/6/16 8:30	我怀孕坐月子，婆婆如此待我，如今该是我好好“报答”她的时候了	xinhuawang	中
```

**多条语句,自定义列名,更多数据(如果它有的话)**
```
select news_posttime as 时间,news_title as 标题 from app1_retention,web2_retention LIMIT 1001
```

```
时间	标题
2020/6/16 1:08	精选几道美味下饭的家常菜，香味十足，简单易做，营养又解馋
2020/6/16 1:07	这个是什么剧来着
2020/6/16 1:08	来摆摊吧！派林大药房发布地摊支持计划！
2020/6/16 1:08	想跟FM105明星主播一起自驾旅行？快来pick你最期待的，谁能C位出道？就看你的了！
2020/6/16 1:08	教您正宗鱼香肉丝的做法，酸甜可口，简单易做，老婆每天吵着要吃
2020/6/16 1:09	婚姻中，女人的这5个行为会引起男人的反感，让感情陷入危机！
2020/6/16 1:06	和平年代！最为可爱的一群人！谢谢你们，默默为人民负重前行！
2020/6/16 0:27	长子娶回个傻儿媳，爸爸气得饭桌上喝闷酒，没想到儿媳的傻是装的
2020/6/16 1:07	八竿子打不着的杨丞琳和李荣浩，是怎么成为被柠檬的恩爱夫妻的？
......
```

#### 替换现有业务   
**通用文章下载-统计总数【参考任务号：26949】**
```
select count(*) from app1_retention where news_posttime > '2020-06-17 00:00:30' and news_posttime < '2020-06-22 09:20:30' and (news_title = '巍巍天山' OR news_digest = '巍巍天山' OR news_content = '巍巍天山') and (news_title = '中国新疆反恐记忆' OR news_digest = '中国新疆反恐记忆' OR news_content = '中国新疆反恐记忆')
```
**总数据量**
```
3844
```

**通用文章下载-使用下载总数进行下载**
```
select case when news_media = 'weibo' then '微博' when news_media = 'wx' then '微信' when news_media = 'web' then '网页' when news_media = 'app' then '客户端' when news_media = 'forum' then '论坛' when news_media = 'journal' then '网页' else '未知' end as 来源媒体,app_name as 媒体名称,news_channel1 as 频道,news_title as 标题,news_digest as 摘要,case when news_negative < 0.45 then '正面' when news_negative < 0.75 then '中性' else '负面' end as 情感属性,news_posttime as 发文时间,news_url as 链接,content_city as 文章提及城市,content_province as 文章提及省,news_read_count as 阅读数,news_comment_count as 评论数,news_is_origin 是否原创,media_type as 媒体类型,origin_author_name as 原创作者,news_url as 原文链接,news_uuid,sim_hash,news_keywords_list as 主题词,news_province as 地域,news_city as 发布热区,author_gender as 性别,news_negative as 情感分值,app_code,news_postdate as 发布日期,case when 1 = 1 then '巍巍天山AND中国新疆反恐记忆' end as 查询条件 from app1_retention where news_posttime > '2020-06-17 00:00:30' and news_posttime < '2020-06-22 09:20:30' and (news_title = '巍巍天山' OR news_digest = '巍巍天山' OR news_content = '巍巍天山') and (news_title = '中国新疆反恐记忆' OR news_digest = '中国新疆反恐记忆' OR news_content = '中国新疆反恐记忆') LIMIT 3844
```

```
来源媒体	媒体名称	频道	标题	摘要	情感属性	发文时间	原文链接	文章提及城市	文章提及省	阅读数	评论数	是否原创	媒体类型	原创作者	news_uuid	sim_hash	主题词	地域	发布热区	性别	情感分值	app_code	发布日期	查询条件
客户端	封面新闻	封面新闻	以岁月为经，以讲述为纬，带你走进中国新疆反恐记忆	“风暴不会永远不住，痛苦使人意志更坚”。反恐之路上，有热血与热泪，也有冻土与寒冰。用我们的镜头，记录下牺牲者、亲历者、幸存者，每个人的经历都是中国新疆反恐全景的一片。CGTN即将推出纪录片，敬请期待。封面新闻已获得本文转载授权，未经版权方授权，任何第三方不得转载。	正面	2020/6/18 12:47	https://m.thecover.cn/news_details.html?from=androidapp&id=4497043	[]	[新疆维吾尔自治区]	0	0	0	media	央视新闻客户端	4f2a43c4793deb45791cb23bf268316c	e566fd5a8a7495a9fc81663353504cb4	[中国, 新疆, 亲历者, 天山, 意志, 敬请期待, 记忆, 新闻, 镜头, 封面, 幸存者, 全景, 冻土, 邵希炜, 牺牲者, 热泪, 风暴, 寒冰, 版权, 经历, 个人, 纪录片, 热血]	未知	未知	未知	0.2		2020/6/18	巍巍天山AND中国新疆反恐记忆
客户端	央视新闻	时间链	刷屏全球的新疆反恐纪录片又出新作：巍巍天山——中国新疆反恐记忆 CGTN即将独家呈现	还记得半年前那两部揭开恐怖分子为祸中国新疆真相，刷屏全球的纪录片吗？时隔半年，CGTN重磅推出新疆反恐纪录片第三部：巍巍天山——中国新疆反恐记忆！片中包含大量独家专访及首次公布画面，进一步为你揭开新疆的反恐之路。正片将于明日于CGTN播出，敬请期待！	中性	2020/6/18 12:46	http://m.news.cctv.com/2020/06/18/ARTIvvVyQT3m3c3LkZp3UfpN200618.shtml	[]	[新疆维吾尔自治区]	0	0	1	门户媒体,main_news,wuxianji,shunya		f70b060d252e27417802b0537906387d	e3228a5b817030e616001048de1a3a8e	[新疆, 中国, 纪录片, 天山, 敬请期待, 记忆, 画面, 正片, 恐怖分子, 重磅, 邵希炜, 那两部, 全球, 刷屏, 真相]	北京市	北京市	未知	0.5		2020/6/18	巍巍天山AND中国新疆反恐记忆
客户端	央视新闻	推荐	以岁月为经 以讲述为纬 ——走进中国新疆反恐记忆	“风暴不会永远不住，痛苦使人意志更坚”。反恐之路上，有热血与热泪，也有冻土与寒冰。用我们的镜头，记录下牺牲者、亲历者、幸存者，每个人的经历都是中国新疆反恐全景的一片。CGTN即将推出纪录片，敬请期待。	正面	2020/6/18 12:54	http://www.toutiao.com/item/6839546628886495747	[]	[新疆维吾尔自治区]	24	0	1	客户端,wuxianji,shunya		ddc471e2dfebc1c21e136d0c17e965bf	e566fd5a8a7495a9fc81663353504cb4	[中国, 新疆, 亲历者, 天山, 意志, 敬请期待, 记忆, 镜头, 幸存者, 全景, 冻土, 牺牲者, 热泪, 风暴, 寒冰, 经历, 个人, 纪录片, 热血]	未知	未知	未知	0.2	{"schema":"sslocal://profile?uid=4492956276&refer=video","avatar_url":"http://sf6-ttcdn-tos.pstatp.com/img/mosaic-legacy/fee50000adb7e90d73a2~120x256.image","user_id":4492956276,"user_auth_info":"{\"auth_type\":\"0\",\"auth_info\":\"中央电视台新闻中心官方账号\"}","name":"央视新闻","description":"中央广播电视总台新闻新媒体中心官方账号","media_id":"4492956276","follow":false,"verified_content":"中央电视台新闻中心官方账号","follower_count":0,"live_info_type":1,"user_verified":true}	2020/6/18	巍巍天山AND中国新疆反恐记忆
客户端	澎湃新闻	搜狐新闻	新疆反恐纪录片第三部明起播出，含独家专访及首次公布画面	央视新闻6月18日消息，“风暴不会永远不住，痛苦使人意志更坚”。反恐之路上，有热血与热泪，也有冻土与寒冰。用我们的镜头，记录下牺牲者、亲历者、幸存者，每个人的经历都是中国新疆反恐全景的一片。还记得半年前那两部揭开恐怖分子为祸中国新疆真相，刷屏全球的纪录片吗？时隔半年，CGTN重磅推出新疆反恐纪录片第三部：巍巍天山——中国新疆反恐记忆！片中包含大量独家专访及首次公布画面，进一步为你揭开新疆的反恐之路。正片将于明日于CGTN播出，敬请期待！	中性	2020/6/18 13:31	http://3g.k.sohu.com/t/n459730949	[]	[新疆维吾尔自治区]	0	0	0	客户端,main_news,media,wuxianji,shunya		10bcd772858458eebcee95457b9d3cdb	e566fd5a8a7495a9fc81663353504cb4	[新疆, 中国, 记忆, 纪录片, 亲历者, 新闻, 画面, 正片, 镜头, 重磅, 幸存者, 全景, 冻土, 那两部, 全球, 央视, 热泪, 寒冰, 经历, 个人, 天山, 意志, 敬请期待, 消息, 恐怖分子, 岁月, 牺牲者, 刷屏, 风暴, 真相]	北京市	北京市	未知	0.5		2020/6/18	巍巍天山AND中国新疆反恐记忆
客户端	澎湃新闻	搜狐新闻	新疆反恐纪录片第三部明起播出，含独家专访及首次公布画面	央视新闻6月18日消息，“风暴不会永远不住，痛苦使人意志更坚”。反恐之路上，有热血与热泪，也有冻土与寒冰。用我们的镜头，记录下牺牲者、亲历者、幸存者，每个人的经历都是中国新疆反恐全景的一片。还记得半年前那两部揭开恐怖分子为祸中国新疆真相，刷屏全球的纪录片吗？时隔半年，CGTN重磅推出新疆反恐纪录片第三部：巍巍天山——中国新疆反恐记忆！片中包含大量独家专访及首次公布画面，进一步为你揭开新疆的反恐之路。正片将于明日于CGTN播出，敬请期待！	中性	2020/6/18 13:32	http://3g.k.sohu.com/t/n459731149	[]	[新疆维吾尔自治区]	0	0	0	客户端,main_news,media,wuxianji,shunya		ac3f89169e834946cca1d5677245fda7	e566fd5a8a7495a9fc81663353504cb4	[新疆, 中国, 新闻, 记忆, 纪录片, 亲历者, 画面, 正片, 镜头, 重磅, 幸存者, 全景, 冻土, 那两部, 全球, 央视, 热泪, 寒冰, 经历, 个人, 天山, 意志, 敬请期待, 消息, 恐怖分子, 岁月, 牺牲者, 刷屏, 风暴, 真相]	北京市	北京市	未知	0.5		2020/6/18	巍巍天山AND中国新疆反恐记忆
客户端	央视新闻	时间链	以岁月为经 以讲述为纬 ——走进中国新疆反恐记忆	“风暴不会永远不住，痛苦使人意志更坚”。反恐之路上，有热血与热泪，也有冻土与寒冰。用我们的镜头，记录下牺牲者、亲历者、幸存者，每个人的经历都是中国新疆反恐全景的一片。CGTN即将推出纪录片，敬请期待。	正面	2020/6/18 12:53	http://m.news.cctv.com/2020/06/18/ARTI47nxplc6YYjgzg1qUdTG200618.shtml	[]	[新疆维吾尔自治区]	0	0	1	门户媒体,main_news,wuxianji,shunya		2ccebf84ccdbbd1d6a3f252005095e09	e566fd5a8a7495a9fc81663353504cb4	[中国, 新疆, 亲历者, 天山, 意志, 敬请期待, 记忆, 镜头, 幸存者, 全景, 冻土, 邵希炜, 牺牲者, 热泪, 风暴, 寒冰, 经历, 个人, 纪录片, 热血]	北京市	北京市	未知	0.2		2020/6/18	巍巍天山AND中国新疆反恐记忆
客户端	西藏头条	政知	新疆反恐纪录片第三部明起播出	央视新闻6月18日消息，“风暴不会永远不住，痛苦使人意志更坚”。反恐之路上，有热血与热泪，也有冻土与寒冰。用我们的镜头，记录下牺牲者、亲历者、幸存者，每个人的经历都是中国新疆反恐全景的一片。还记得半年前那两部揭开恐怖分子为祸中国新疆真相，刷屏全球的纪录片吗？时隔半年，CGTN重磅推出新疆反恐纪录片第三部：巍巍天山——中国新疆反恐记忆！片中包含大量独家专访及首次公布画面，进一步为你揭开新疆的反恐之路。正片将于明日于CGTN播出，敬请期待！	中性	2020/6/18 13:39	https://app.bjtitle.com/newshow.php?newsid=5659502&typeid=91	[]	[新疆维吾尔自治区]	0	0	1			5bcfb3af7a28553b13438ac1ef2458f9	e566fd5a8a7495a9fc81663353504cb4	[新疆, 中国, 记忆, 纪录片, 亲历者, 新闻, 画面, 正片, 镜头, 重磅, 幸存者, 全景, 冻土, 那两部, 全球, 央视, 热泪, 寒冰, 经历, 个人, 天山, 意志, 敬请期待, 消息, 恐怖分子, 岁月, 牺牲者, 刷屏, 风暴, 真相]	未知	未知	未知	0.5		2020/6/18	巍巍天山AND中国新疆反恐记忆
客户端	央视网	东方号	新疆反恐纪录片第三部明起播出，含独家专访及首次公布画面	央视新闻6月18日消息，“风暴不会永远不住，痛苦使人意志更坚”。反恐之路上，有热血与热泪，也有冻土与寒冰。用我们的镜头，记录下牺牲者、亲历者、幸存者，每个人的经历都是中国新疆反恐全景的一片。还记得半年前那两部揭开恐怖分子为祸中国新疆真相，刷屏全球的纪录片吗？时隔半年，CGTN重磅推出新疆反恐纪录片第三部：巍巍天山——中国新疆反恐记忆！片中包含大量独家专访及首次公布画面，进一步为你揭开新疆的反恐之路。正片将于明日于CGTN播出，敬请期待！	中性	2020/6/18 13:30	http://mini.eastday.com/app/200618133014962.html	[]	[新疆维吾尔自治区]	0	0	0	自媒体,main_news,wuxianji,shunya		9877107c89355ca2e59944b67bf39281	e566fd5a8a7495a9fc81663353504cb4	[新疆, 中国, 记忆, 纪录片, 亲历者, 新闻, 画面, 正片, 镜头, 重磅, 幸存者, 全景, 冻土, 那两部, 全球, 央视, 热泪, 寒冰, 经历, 个人, 天山, 意志, 敬请期待, 消息, 恐怖分子, 岁月, 牺牲者, 刷屏, 风暴, 真相]	上海市	上海市	未知	0.5	2E+14	2020/6/18	巍巍天山AND中国新疆反恐记忆
客户端	环球网	大风号	牺牲者 亲历者 幸存者……刷屏全球的新疆反恐纪录片再出新作	本文转自；“风暴不会永远不住，痛苦使人意志更坚”。反恐之路上，有热血与热泪，也有冻土与寒冰。用我们的镜头，记录下牺牲者、亲历者、幸存者，每个人的经历都是中国新疆反恐全景的一片。还记得半年前那两部揭开恐怖分子为祸中国新疆真相，刷屏全球的纪录片吗？时隔半年，CGTN重磅推出新疆反恐纪录片第三部：巍巍天山——中国新疆反恐记忆！片中包含大量独家专访及首次公布画面，进一步为你揭开新疆的反恐之路。正片将于明日于CGTN播出，敬请期待！	中性	2020/6/18 14:57	https://ishare.ifeng.com/c/s/v002j4JcFpmqOTHsBTQQjE3VnkHb8oYwgkD7NABAAUgvuJo__	[]	[新疆维吾尔自治区]	0	0	0	null,wuxianji,yanruyu		625470518e7229979355b358a4cbd575	e566fd5a8a7495a9fc81663353504cb4	[新疆, 中国, 纪录片, 亲历者, 新闻, 记忆, 画面, 正片, 镜头, 重磅, 幸存者, 全景, 冻土, 那两部, 全球, 央视, 热泪, 寒冰, 经历, 个人, 客户端, 天山, 意志, 敬请期待, 恐怖分子, 邵希炜, 牺牲者, 刷屏, 风暴, 真相]	未知	未知	未知	0.65	767468	2020/6/18	巍巍天山AND中国新疆反恐记忆
......
```


**全网提及量查询【参考：26348】**

```
select count(*) as 总数 from weibo1_retention,weibo1_retention,app1_retention,web2_retention,video where (news_title = '密云' OR news_content = '密云') and (news_title = '菜' OR news_content = '菜') and news_posttime > '2020-01-01 00:00:08' and news_posttime < '2020-03-13 00:00:08' group by date_histogram(field='news_posttime','interval'='1d','format'='yyyy-MM-dd','min_doc_count'=5,'alias'='时间') union 
select count(*) as 总数 from weibo1_retention,weibo1_retention,app1_retention,web2_retention,video where (news_title = '密云' OR news_content = '密云') and (news_title = '蔬菜' OR news_content = '蔬菜') and news_posttime > '2020-01-01 00:00:08' and news_posttime < '2020-03-13 00:00:08' group by date_histogram(field='news_posttime','interval'='1d','format'='yyyy-MM-dd','min_doc_count'=5,'alias'='时间')
```

**微博账号查询【需要支持其他库】**
```
接口支持，目前开发版本正在支持
```


**指定网站下载【参考：27712】【app_uuid : "9969,9961,9968,42"】**
```
select count(*) as 总数 from app1_retention where news_posttime > '2020-07-07 09:00:51' and news_posttime < '2020-07-07 12:00:51' and app_uuid in(9969,9961,9968,42)
```
**总数据量**
```
57
```

**指定网站下载-使用下载总数进行下载**
```
select case when news_media = 'weibo' then '微博' when news_media = 'wx' then '微信' when news_media = 'web' then '网页' when news_media = 'app' then '客户端' when news_media = 'forum' then '论坛' when news_media = 'journal' then '网页' else '未知' end as 来源媒体,app_name as 媒体名称,news_title as 标题,news_digest as 摘要,case when news_negative < 0.45 then '正面' when news_negative < 0.75 then '中性' else '负面' end as 情感属性,news_posttime as 发文时间,news_url as 链接,content_city as 文章提及城市,content_province as 文章提及省,news_read_count as 阅读数,news_comment_count as 评论数,news_is_origin 是否原创,media_type as 媒体类型,content_cate as 新闻类型,origin_author_name as 原创作者,news_uuid,sim_hash ,case when app_uuid = 9969 then 'app_uuid:9969' when app_uuid = 9961 then 'app_uuid:9961' when app_uuid = 9968 then 'app_uuid:9968' when app_uuid = 42 then 'app_uuid:42' end as 查询条件 from app1_retention where news_posttime > '2020-07-07 09:00:51' and news_posttime < '2020-07-07 12:00:51' and app_uuid in (9969,9961,9968,42) ORDER BY app_uuid LIMIT 57
```

```
来源媒体	媒体名称	标题	摘要	情感属性	发文时间	链接	文章提及城市	文章提及省	阅读数	评论数	是否原创	媒体类型	新闻类型	原创作者	news_uuid	sim_hash	查询条件
客户端	央视体育VIP	冠军教你练游泳 自由泳侧身腿	央视网消息：本视频游泳冠军徐田龙子带来自由泳侧身腿练习。	中性	2020/7/7 11:38	https://sports.cctv.com/2020/07/07/VIDEi71DMT1YvHg3mJll8Nw7200707.shtml			0	10	1	门户媒体,main_news,shunya	体育		5599f9d3582e24ef7f7b502dcd39a2a4	52e5e8bfe829ea5aa4f4740aee4ffb2a	app_uuid:9968
客户端	央视新闻	六月全国菜价上行 处同期较高水平	a36f4b71ad134811b1975c4376f6bcfa,0,1,16:9,news农业农村部监测的最新数据显示，六月份，全国菜篮子价格指数为114.41，较上年同期上涨3.50个点。蔬菜价格指数较上年同期上涨6.31个点，蔬菜价格处历史同期较高水平。	中性	2020/7/7 9:02	https://sannong.cctv.com/2020/07/07/ARTIUb3nIIla3IRPsZ5FKsXG200707.shtml			0	0	1	门户媒体,main_news,shunya	社会		f21d968485503553cdc4613924dd0642	2363d2944b4c922be9ac1ddb3525e03a	app_uuid:9969
客户端	央视新闻	福奇：美国仍在第一波疫情中泥足深陷	统计显示，在刚刚过去的一周里，全美新增新冠肺炎确诊病例比之前一周增加了27%。美国知名流行病学家、白宫冠状病毒应对工作组关键成员福奇6日表示，眼下的情况不太妙，美国仍在第一波疫情中泥足深陷。福奇说，不久之前，美国的单日新增确诊病例数还在两万左右，但随着多地贸然重启经济，近几天的这一数字窜上了4万，甚至达到了57000多。算下来，也就是相隔了差不多十天，数字就激增了一倍还多。	中性	2020/7/7 9:18	http://m.news.cctv.com/2020/07/07/ARTITMwDXEkOcmmQ1ketDCOh200707.shtml			0	0	1	门户媒体,main_news,wuxianji,shunya	健康		01dfe19775865486dcfdf0512bfc6e9f	f31ac588702cd280f26c4acb83da1f75	app_uuid:9969
客户端	央视新闻	离岸人民币兑美元收复7关口 刷新近四个月高位	北京时间7月7日，离岸人民币兑美元收复7关口，刷新近四个月高位。业内人士指出，今年上半年，人民币对美元贬值1.24%，同期新兴市场货币指数下跌4.42%。稳定的人民币汇率，增强了包括债券在内的人民币资产的吸引力。	中性	2020/7/7 9:39	http://m.news.cctv.com/2020/07/07/ARTI1enUJis22biDDxaIAuMz200707.shtml	[北京市]	[北京市]	0	0	1	门户媒体,main_news,wuxianji,shunya	财经		09fa7db4c4765242909689672d3674a3	285bf7c6580715f7105bc1ca6bc37065	app_uuid:9969
客户端	央视新闻	英伦观察丨立法多 执法严 看看英国人如何实施“国安法 ”	中国香港国安法实施前后，人们看到了某些西方国家的两副面孔：一方面千方百计阻挠香港国安法立法，一方面本国国安法规琳琅满目，对于各类危害国家安全的犯罪行为严惩不贷，执法手段堪称“全球楷模”。英国20年间9次修法完善以反恐为主的国安类法律体系	中性	2020/7/7 9:38	http://m.news.cctv.com/2020/07/07/ARTIiGN9JshqeHyngzrP59Ri200707.shtml	[香港特别行政区]	[香港特别行政区]	0	0	1	门户媒体,main_news,wuxianji,shunya	社会		d5588a3cb4c24102bfb5c4b2e8ce39f3	ab7d5c7d000de0ec372c0563fc4982a6	app_uuid:9969
客户端	央视新闻	国企改革推“升级版”　下半年将多策连发	三年行动方案的审议通过，意味着国企改革进入新阶段，将围绕重点领域推出“升级版”举措。记者了解到，深化混改的实施意见、推进国有经济布局优化和结构调整的意见以及“十四五”全国国有资本布局与结构战略性调整规划等多项政策正在制定，有望在下半年出台。	中性	2020/7/7 9:33	https://jingji.cctv.com/2020/07/07/ARTIF066mkAcrCDxMLImcgMK200707.shtml	[]	[山东省, 甘肃省, 山西省]	0	0	1	门户媒体,main_news,shunya	时政		5c5bc3fd984366853a25d1082b3a0496	96a89c506bd31f411446cba2babde982	app_uuid:9969
客户端	央视新闻	【复兴网评】致考生：心怀静气 全力以赴	今天，1071万考生迈进高考考场，以多年所学接受人生大考的检验，为触及更大的梦想奋力一搏。这是需要满怀激情、全力以赴的时刻，也是需要心怀静气、从容应对的时刻。受疫情影响，今年高考非比寻常。考试时间上，从6月延迟到了7月；防疫举措上，考生考前14天每日进行健康监测、行踪报备，进入考场除常规检查外还要接受体温检测，部分地区考生要全程佩戴口罩，考试中考生如有发热、咳嗽等呼吸道症状，还将可能进入隔离考场进行考试……毫无疑问，这是确保高考安全、相关人员安全的必要之举，但无形中也给本就严肃的高考增添了些许紧张气氛。越是这样，越要正确认识高考、理性对待高考。	中性	2020/7/7 10:19	https://opinion.cctv.com/2020/07/07/ARTIK2Gwd8g4yzRXu2WZEyui200707.shtml			0	0	1	门户媒体,main_news,shunya	教育		3dcb5ba5a976ad1a15a7ceb29a560d00	94410cde1844e28ed678190368d4d15d	app_uuid:9969
客户端	央视新闻	重要指标频现积极信号 中国经济“下半场”值得期待	不久前，国际货币基金组织在发布更新内容时认为，世界经济面临自上世纪30年代经济大萧条以来最严重衰退。IMF同时表示：“预计2020年，主要经济体中唯一能够实现正增长的，只有中国。”这一判断，引发了海内外对于中国经济走势的高度关注。	中性	2020/7/7 10:09	https://jingji.cctv.com/2020/07/07/ARTIzq2TORTt8LQpWg2DqMAY200707.shtml			0	0	1	门户媒体,main_news,shunya	财经		183ccabd50a550735bd58f3514bf0780	263a5f0db0ccefeed53ce7e2e5cb0da3	app_uuid:9969
客户端	央视新闻	商务部等八部门联合部署推动家政服务消费加快回补	7月6日，商务部、发展改革委、民政部等八部门联合发布)指出，各地相关部门要加强横向协作、纵向联动，加快推进家政服务业信用体系建设，有序推动家政服务企业复工营业，保居民就业、保基本民生、保市场主体，推动家政服务消费加快回补。	中性	2020/7/7 10:04	https://jingji.cctv.com/2020/07/07/ARTIQItBiEZRDsb3NChLy3nE200707.shtml			0	0	1	门户媒体,main_news,shunya	时政		ef1eddac6535ac6c624d3ddf4a671e62	d5862ac5cd0169d5c9ae0bc1b24c575c	app_uuid:9969
......
```

#### 新版本下载【支持目前所有ES库,同时支持动态扩展】
**枚举类型**

DataSouce(关键词) | (资源描述)描述
---|---
es | 老舆情_阿里云es
dy | 中科大洋_阿里云es
mg | 新舆情_阿里云es
peace | 和平区_阿里云es
account | 微博账号_阿里云es
config | 配置中心_阿里云es
oversea | 海外_阿里云es
qiniu(不写) | 舆情_七牛云es

**海外舆情相关统计**
```
select count(*) as 数据量 from oversea.oversea_web where (platform_domain_pri = 'reuters.com' or platform_domain_pri = 'sputniknews.com' or platform_domain_pri = 'scmp.com' or platform_domain_pri = 'ptinews.com' or platform_domain_pri = 'afp.com' or platform_domain_pri = 'straitstimes.com' or platform_domain_pri = 'nytimes.com' or platform_domain_pri = 'thetimes.co.uk' or platform_domain_pri = 'english.kyodonews.net' or platform_domain_pri = 'hindustantimes.com') and (news_title = 'China' or news_title = 'Anhui' or news_title = 'Beijing' or news_title = 'Chongqing' or news_title = 'Fujian' or news_title = 'Guangdong' or news_title = 'Gansu' or news_title = 'Guangxi' or news_title = 'Guizhou' or news_title = 'Henan' or news_title = 'Hubei' or news_title = 'Hebei' or news_title = 'Hainan' or news_title = 'Hong Kong' or news_title = 'Heilongjiang' or news_title = 'Hunan' or news_title = 'Jilin' or news_title = 'Jiangsu' or news_title = 'Jiangxi' or news_title = 'Liaoning' or news_title = 'Macau' or news_title = 'Inner Mongolia' or news_title = 'Ningxia' or news_title = 'Qinghai' or news_title = 'Sichuan' or news_title = 'Shandong' or news_title = 'Shanghai' or news_title = 'Shaanxi' or news_title = 'Shanxi' or news_title = 'Tianjin' or news_title = 'Taiwan' or news_title = 'Xinjiang' or news_title = 'Tibet' or news_title = 'Yunnan' or news_title = 'Zhejiang') and news_posttime > '2018-12-01 00:00:00' and news_posttime < '2020-07-25 00:00:00' group by platform_domain_pri
```

```
platform_domain_pri  数据量
reuters.com  21006
straitstimes.com  1693
scmp.com  1528
hindustantimes.com  1422
afp.com  716
thetimes.co.uk  544
sputniknews.com  515
nytimes.com  497
ptinews.com  129
```

**ES库中有包含按时间拆分的索引和没有按时间拆分的索引如：微博账号ES集群中weiboaccount_01和Qiniu集群中gateway_collect索引【注意七牛索引无需加_ns】等**
```
#没有按时间拆分索引可以加后缀_ns，如：weiboaccount_01_ns
select gender as 性别,udt as 时间 from account.weiboaccount_01_ns LIMIT 10
```

```
性别    时间
女  2019-12-16 23:10:25
男  2019-12-16 23:10:25
男  2019-12-16 23:10:25
女  2019-12-16 23:10:25
女  2019-12-16 23:10:25
女  2019-12-16 23:10:25
男  2019-12-16 23:10:25
女  2019-12-16 23:10:25
女  2019-12-16 23:10:25
```
#### 新版本下载【支持内查询如：select * from table where id in (select id from table)】

**需要获取最热的TOP100的相关文章**
```
select new_title as 标题，news_posttime as 时间 from web2_retention where sim_hash in (select sim_hash from web2_retention where (news_title = '新基建' OR news_content = '新基建') group by sim_hash LIMIT 100)
```

```
标题	时间
中北宏远集团开启四川5G建设新篇章 助力成都智慧物流发展	2020/8/11 9:30
应勇王晓东与“知名民企湖北行”企业家代表座谈 时间：2020-08-11 09:12:44   来源：荆楚网    分享到：	2020/8/11 9:12
劳动力市场活跃度回升（经济新方位）	2020/8/11 9:46
上海上半年实到外资增长5.4% “魔都”有什么魔力？	2020/8/11 6:03
黄海皮卡布局国六低价位区间市场，打造6万级国六皮卡性价比之王	2020/8/11 8:56
人工智能为“天津智港”插上翅膀	2020/8/11 9:49
湖南省新增17个年度重点建设项目 总投资680亿	2020/8/11 8:11
劳动力市场活跃度回升（经济新方位）	2020/8/11 9:55
【财经早报】美股涨跌不一 道指创半年新高	2020/8/11 9:34
5G为新基建注入强动力落实“六稳”“六保”	2020/8/11 9:42
```