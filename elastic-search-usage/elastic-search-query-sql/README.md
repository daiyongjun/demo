# es-sql-query-common
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

**case when... else ... end**
```
select news_posttime as 时间,case when news_negative < 0.45 then '正' when news_negative < 0.75 then '中' else '负' end as 情感类型 from oversea
```

```
{
  "headers": [
    "时间",
    "情感类型"
  ],
  "lines": [
    "2020-03-10 14:10:09\t负",
    "2020-03-10 14:09:59\t负",
    "2020-03-10 14:09:56\t中",
    "2020-03-10 14:09:51\t负",
    "2020-03-10 14:09:48\t负",
    "2020-03-10 14:09:40\t负",
    "2020-03-10 14:09:03\t正",
    "2020-03-10 14:09:02\t负",
    "2020-03-10 14:09:00\t正",
    "2020-03-10 14:09:00\t负"
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