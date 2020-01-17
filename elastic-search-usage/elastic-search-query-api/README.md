

>[CSDN：Elasticsearch 6.4基本操作 - Java版](https://www.cnblogs.com/swordfall/p/9981883.html "引用文章")

>[Elasticsearch Guide：Java Low Rest Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-low.html "Java Low Rest Client")

>[Elasticsearch Guide：High Level REST Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/current/java-rest-high.html "High Level REST Client")

>[简书：spring data elasticsearch 使用及源码分析](https://www.jianshu.com/p/6facb870793e "spring data elasticsearch 使用及源码分析")

>[简书：Win10中Docker安装Elasticsearch](https://www.jianshu.com/p/9698cc75e00c "Docker安装Elasticsearch")

>[书籍：精通Elastic Stack](https://book.douban.com/subject/30326542/ "精通Elastic Stack")

### 1. Elasticsearch Java API有四类client连接方式
- [TransportClient](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch") 
- [RestClient](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")
- [Jest](https://www.cnblogs.com/swordfall/p/9981883.html "Spring Data Elasticsearch")
- [Spring Data Elasticsearch](https://docs.spring.io/spring-data/elasticsearch/docs/3.2.3.RELEASE/reference/html/#reference "Spring Data Elasticsearch")

#### 概述
**TransportClient** 和  **Rest Client** 都为Elasticsearch原生的api，官方维护，两者不同在于前者使用 [**Java序列化请求**](https://www.runoob.com/java/java-serialization.html "java序列化请求")，后者使用http请求。
个人理解：对象序列化概括的理解其实是将Java对象表示为字节序列，序列依然存储着对象类型和数据，Java序列化请求和http请求的更替，其实就是json作为数据传输媒介的胜利。**TransportClient** 将会在Elasticsearch 7.0弃用并在8.0中完成删除。所以本篇幅暂时不会描述**TransportClient**。

**Jest** 是Java社区开发的，是Elasticsearch的Java Http Rest客户端。**Spring Data Elasticsearch** 是spring集成的Elasticsearch开发包。

#### RestClient
 ##### 概述
ElasticSearch版本将主要使 **Rest Client**操作数据，
 **Rest Client**分为 **Java Low REST Client** 和 **Java High Level REST Client**。

 ##### RestClient Usage
 ###### 使用docker安装Elasticsearch 6.4.3版本
 ```
#下载并安装
docker search elasticsearch:6.4.3
docker pull elasticsearch:6.4.3
docker run -di --name=myes -p 9200:9200 -p 9300:9300 elasticsearch:6.4.3

#将ES的配置文件复制到本地系统文件中，方便修改和使用
docker cp myes:/usr/share/elasticsearch/config/elasticsearch.yml G:/docker/elasticsearch.yml
docker run -di --name=myes -p 9200:9200 -p 9300:9300 -v G:/docker/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml elasticsearch:6.4.3

curl http://127.0.0.1:9200/
{
  "name": "Qc-7L_W",
  "cluster_name": "docker-cluster",
  "cluster_uuid": "vEVexvOKSdOaScZM8NebpA",
  "version": {
    "number": "6.4.3",
    "build_flavor": "default",
    "build_type": "tar",
    "build_hash": "fe40335",
    "build_date": "2018-10-30T23:17:19.084789Z",
    "build_snapshot": false,
    "lucene_version": "7.4.0",
    "minimum_wire_compatibility_version": "5.6.0",
    "minimum_index_compatibility_version": "5.0.0"
  },
  "tagline": "You Know, for Search"
}
#安装elasticsearch 插件 ex：install elasticsearch-sql 由于es的插件国内网络无法直接访问于是就创建一个本地的文件服务器
#docker安装本地文件服务器
docker run --name nginx -d -p 7070:7070
#如安装es一样 copy nginx.conf
docker exec -it nginx bash
#重新删除nginx容器，重新启动容器
docker run --name nginx -d -p 7070:7070 -v G:\docker\nginx.conf:/etc/nginx/nginx.conf -v G:\docker\tools\files:/home/files nginx
#nginx.conf
server {
    listen       7070;
    server_name  localhost;
    charset utf-8;
    location /files {
        #在docker内nginx的目录
        alias /home/files;
        expires 1d;
        allow all;
        autoindex on;
    }
}
#安装插件
docker exec -it myes /bin/bash
#替换安装命令如下：
./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.5.0/elasticsearch-analysis-ik-6.5.0.zip

./bin/elasticsearch-plugin install http://192.168.124.162:7070/files/elasticsearch-sql-6.4.3.0.zip
 ```


###### 初始化
maven的依赖包：
```
<elasticsearch.version>6.4.3</elasticsearch.version>

 <dependency>
    <groupId>org.elasticsearch.client</groupId>
    <artifactId>elasticsearch-rest-client</artifactId>
    <version>${elasticsearch.version}</version>
    <scope>compile</scope>
</dependency>
```
 RestClient实例可以通过相应的内置 RestClientBuilder类，通过创建RestClient#builder(HttpHost...) 静态方法。唯一必需的参数是客户端将与之通信的一个或多个主机，以HttpHost的实例形式 提供 ，如下所示：
```
RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http"),new HttpHost("localhost", 9201, "http")).build();
```
RestClient该类是线程安全的，并且理想情况下与使用该类的应用程序具有相同的生命周期。重要的是，在不再需要它时将其关闭，以便正确释放它使用的所有资源以及基础的HTTP客户端实例及其线程：
```
restClient.close();
```
#### Java High Level REST Client 
*Java High Level REST Client*和*Java Low REST Client* 的区别

 ![区别](src\main\resources\restclient.png"区别")

#### spring data elastic search
#### 使用
使用我们可以参考
>[Spring Data Elasticsearch](https://docs.spring.io/spring-data/elasticsearch/docs/3.2.3.RELEASE/reference/html/#reference "Spring Data Elasticsearch")
#### 源码分析
>[简书：spring data elasticsearch 使用及源码分析](https://www.jianshu.com/p/6facb870793e "spring data elasticsearch 使用及源码分析")
```
switch (key) {
    case EQUALS:
		query = termQuery(fieldName, searchText);
		break;
    case IN:
		Collection<?> list = (Collection<?>) (value);
		query = termsQuery(fieldName, list);
		break;
    case LESS:
		query = rangeQuery(fieldName).lt(value);
		break;
    case GREATER:
		query = rangeQuery(fieldName).gt(value);
		break;
    case LESS_EQUAL:
		query = rangeQuery(fieldName).lte(value);
		break;
    case GREATER_EQUAL:
		query = rangeQuery(fieldName).gte(value);
		break;
    case BETWEEN:
		Object[] ranges = (Object[]) value;
		query = rangeQuery(fieldName).from(ranges[0]).to(ranges[1]);
		break;
```


#### grammar rule 基础语法
**term查询**
```
Criteria term = new Criteria("new_tile").is("测试规则");
String query = processor.createQueryFromCriteria(term).toString();

{
  "bool" : {
    "must" : [
      {
        "term" : {
          "new_tile" : {
            "value" : "测试规则",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```
**terms查询**
```
Criteria terms = new Criteria("new_tile").in((Object[]) new String[]{"测试规则","测试规则2"});
query = processor.createQueryFromCriteria(terms).toString();

{
  "bool" : {
    "must" : [
      {
        "terms" : {
          "new_tile" : [
            "测试规则",
            "测试规则2"
          ],
          "boost" : 1.0
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```
**terms(not in)查询**
```
Criteria exclude_terms = new Criteria("new_tile").in((Object[]) new String[]{"测试规则","测试规则2"}).not();
query = processor.createQueryFromCriteria(exclude_terms).toString();

{
  "bool" : {
    "must_not" : [
      {
        "terms" : {
          "new_tile" : [
            "测试规则",
            "测试规则2"
          ],
          "boost" : 1.0
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**greater than查询**

```
Criteria greater_than = new Criteria("news_posttime").greaterThan("2019-06-01T00:00:36");
query = processor.createQueryFromCriteria(greater_than).toString();

{
  "bool" : {
    "must" : [
      {
        "range" : {
          "news_posttime" : {
            "from" : "2019-06-01T00:00:36",
            "to" : null,
            "include_lower" : false,
            "include_upper" : true,
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**less than查询**
```
Criteria less_than = new Criteria("news_posttime").lessThan("2019-06-01T00:00:36");
query = processor.createQueryFromCriteria(less_than).toString();

{
  "bool" : {
    "must" : [
      {
        "range" : {
          "news_posttime" : {
            "from" : null,
            "to" : "2019-06-01T00:00:36",
            "include_lower" : true,
            "include_upper" : false,
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**greater than equal查询**

```
Criteria greater_than_equal = new Criteria("news_posttime").greaterThanEqual("2019-06-01T00:00:36");
query = processor.createQueryFromCriteria(greater_than_equal).toString();

{
  "bool" : {
    "must" : [
      {
        "range" : {
          "news_posttime" : {
            "from" : "2019-06-01T00:00:36",
            "to" : null,
            "include_lower" : true,
            "include_upper" : true,
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**less than equal查询**

```
Criteria less_than_equal = new Criteria("news_posttime").lessThanEqual("2019-06-01T00:00:36");
query = processor.createQueryFromCriteria(less_than_equal).toString();

{
  "bool" : {
    "must" : [
      {
        "range" : {
          "news_posttime" : {
            "from" : null,
            "to" : "2019-06-01T00:00:36",
            "include_lower" : true,
            "include_upper" : true,
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**between查询**

```
Criteria between = new Criteria("news_posttime").between("2019-06-01T00:00:36","2019-12-21T14:02:36");
query = processor.createQueryFromCriteria(between).toString();

{
  "bool" : {
    "must" : [
      {
        "range" : {
          "news_posttime" : {
            "from" : "2019-06-01T00:00:36",
            "to" : "2019-12-21T14:02:36",
            "include_lower" : true,
            "include_upper" : true,
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**match查询**

```
Criteria match = new Criteria("news_content").match("测试");
query = processor.createQueryFromCriteria(match).toString();

{
  "bool" : {
    "must" : [
      {
        "match" : {
          "news_content" : {
            "query" : "测试",
            "operator" : "OR",
            "prefix_length" : 0,
            "max_expansions" : 50,
            "fuzzy_transpositions" : true,
            "lenient" : false,
            "zero_terms_query" : "NONE",
            "auto_generate_synonyms_phrase_query" : true,
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**match phrase查询**

```
Criteria match_phrase = new Criteria("news_content").phrase("测试");
query = processor.createQueryFromCriteria(match_phrase).toString();

{
  "bool" : {
    "must" : [
      {
        "match_phrase" : {
          "news_content" : {
            "query" : "测试",
            "slop" : 0,
            "zero_terms_query" : "NONE",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**【正则】starts with查询**

```
Criteria starts_with = new Criteria("news_content").startsWith("测试");
query = processor.createQueryFromCriteria(starts_with).toString();

{
  "bool" : {
    "must" : [
      {
        "wildcard" : {
          "news_content" : {
            "wildcard" : "测试*",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**【正则】ends with查询**

```
Criteria ends_with = new Criteria("news_content").endsWith("测试");
query = processor.createQueryFromCriteria(ends_with).toString();

{
  "bool" : {
    "must" : [
      {
        "wildcard" : {
          "news_content" : {
            "wildcard" : "*测试",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```
**【正则】contains查询**

```
Criteria contains = new Criteria("news_content").contains("测试");
query = processor.createQueryFromCriteria(contains).toString();

{
  "bool" : {
    "must" : [
      {
        "wildcard" : {
          "news_content" : {
            "wildcard" : "*测试*",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```
**【正则】fuzzy查询**

```
Criteria fuzzy = new Criteria("news_content").fuzzy("测试");
query = processor.createQueryFromCriteria(fuzzy).toString();

{
  "bool" : {
    "must" : [
      {
        "fuzzy" : {
          "news_content" : {
            "value" : "测试",
            "fuzziness" : "AUTO",
            "prefix_length" : 0,
            "max_expansions" : 50,
            "transpositions" : false,
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```


#### logic relation 基础语法
**must逻辑关系**
```
Criteria must = new Criteria("new_tile").is("测试规则");
String query = processor.createQueryFromCriteria(must).toString();

{
  "bool" : {
    "must" : [
      {
        "term" : {
          "new_tile" : {
            "value" : "测试规则",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```

**should逻辑关系**
```
Criteria should = new Criteria("new_tile").is("测试规则").or();
query = processor.createQueryFromCriteria(should).toString();

{
  "bool" : {
    "should" : [
      {
        "term" : {
          "new_tile" : {
            "value" : "测试规则",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "minimum_should_match" : "1",
    "boost" : 1.0
  }
}
```

**must not逻辑关系**
```
Criteria must_not = new Criteria("new_tile").is("测试规则").not();
query = processor.createQueryFromCriteria(must_not).toString();

{
  "bool" : {
    "must_not" : [
      {
        "term" : {
          "new_tile" : {
            "value" : "测试规则",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```


#### complex logic relation 基础语法

**must[grammar...]**

```
Criteria must = new Criteria().and("news_title").phrase("测试1").and("news_content").phrase("测试2");
String query = processor.createQueryFromCriteria(must).toString();

{
  "bool" : {
    "must" : [
      {
        "match_phrase" : {
          "news_title" : {
            "query" : "测试1",
            "slop" : 0,
            "zero_terms_query" : "NONE",
            "boost" : 1.0
          }
        }
      },
      {
        "match_phrase" : {
          "news_content" : {
            "query" : "测试2",
            "slop" : 0,
            "zero_terms_query" : "NONE",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```
**should[grammar...]**

```
Criteria should = new Criteria().or("news_title").phrase("测试1").or("news_content").phrase("测试2");
query = processor.createQueryFromCriteria(should).toString();

{
  "bool" : {
    "should" : [
      {
        "match_phrase" : {
          "news_title" : {
            "query" : "测试1",
            "slop" : 0,
            "zero_terms_query" : "NONE",
            "boost" : 1.0
          }
        }
      },
      {
        "match_phrase" : {
          "news_content" : {
            "query" : "测试2",
            "slop" : 0,
            "zero_terms_query" : "NONE",
            "boost" : 1.0
          }
        }
      }
    ],
    "adjust_pure_negative" : true,
    "minimum_should_match" : "1",
    "boost" : 1.0
  }
}
```

**must[should[grammar...],should[grammar...]]**

```
Criteria c_must = new Criteria().and(should).and(should);
query = processor.createQueryFromCriteria(c_must).toString();

{
  "bool" : {
    "must" : [
      {
        "bool" : {
          "should" : [
            {
              "match_phrase" : {
                "news_title" : {
                  "query" : "测试1",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            },
            {
              "match_phrase" : {
                "news_content" : {
                  "query" : "测试2",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            }
          ],
          "disable_coord" : false,
          "adjust_pure_negative" : true,
          "minimum_should_match" : "1",
          "boost" : 1.0
        }
      },
      {
        "bool" : {
          "should" : [
            {
              "match_phrase" : {
                "news_title" : {
                  "query" : "测试1",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            },
            {
              "match_phrase" : {
                "news_content" : {
                  "query" : "测试2",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            }
          ],
          "disable_coord" : false,
          "adjust_pure_negative" : true,
          "minimum_should_match" : "1",
          "boost" : 1.0
        }
      }
    ],
    "disable_coord" : false,
    "adjust_pure_negative" : true,
    "boost" : 1.0
  }
}
```
**should[must[grammar...],must[grammar...]]**

```
Criteria c_should = new Criteria().or(must).or(must);
query = processor.createQueryFromCriteria(c_should).toString();

{
  "bool" : {
    "should" : [
      {
        "bool" : {
          "must" : [
            {
              "match_phrase" : {
                "news_title" : {
                  "query" : "测试1",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            },
            {
              "match_phrase" : {
                "news_content" : {
                  "query" : "测试2",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            }
          ],
          "disable_coord" : false,
          "adjust_pure_negative" : true,
          "boost" : 1.0
        }
      },
      {
        "bool" : {
          "must" : [
            {
              "match_phrase" : {
                "news_title" : {
                  "query" : "测试1",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            },
            {
              "match_phrase" : {
                "news_content" : {
                  "query" : "测试2",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            }
          ],
          "disable_coord" : false,
          "adjust_pure_negative" : true,
          "boost" : 1.0
        }
      }
    ],
    "disable_coord" : false,
    "adjust_pure_negative" : true,
    "minimum_should_match" : "1",
    "boost" : 1.0
  }
}

```

**实战 我们去video中查找相关的vivo和oppo的相关的文档**

```
//title中
Criteria title = new Criteria().and("news_title").phrase("vivo").and("news_title").phrase("oppo");
//content中
Criteria content = new Criteria().and("news_content").phrase("vivo").and("news_content").phrase("oppo");
Criteria criteria = new Criteria().or(title).or(content).and("news_posttime").between("2019-01-01T00:00:00", "2020-01-11T00:00:00");
query = processor.createQueryFromCriteria(criteria).toString();


{
  "bool" : {
    "must" : [
      {
        "range" : {
          "news_posttime" : {
            "from" : "2019-01-01T00:00:00",
            "to" : "2020-01-11T00:00:00",
            "include_lower" : true,
            "include_upper" : true,
            "boost" : 1.0
          }
        }
      }
    ],
    "should" : [
      {
        "bool" : {
          "must" : [
            {
              "match_phrase" : {
                "news_title" : {
                  "query" : "vivo",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            },
            {
              "match_phrase" : {
                "news_title" : {
                  "query" : "oppo",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            }
          ],
          "disable_coord" : false,
          "adjust_pure_negative" : true,
          "boost" : 1.0
        }
      },
      {
        "bool" : {
          "must" : [
            {
              "match_phrase" : {
                "news_content" : {
                  "query" : "vivo",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            },
            {
              "match_phrase" : {
                "news_content" : {
                  "query" : "oppo",
                  "slop" : 0,
                  "boost" : 1.0
                }
              }
            }
          ],
          "disable_coord" : false,
          "adjust_pure_negative" : true,
          "boost" : 1.0
        }
      }
    ],
    "disable_coord" : false,
    "adjust_pure_negative" : true,
    "minimum_should_match" : "1",
    "boost" : 1.0
  }
}

```

