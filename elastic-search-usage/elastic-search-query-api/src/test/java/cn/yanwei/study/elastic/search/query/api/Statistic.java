package cn.yanwei.study.elastic.search.query.api;

import cn.yanwei.study.elastic.search.query.api.domain.CriteriaQueryProcessor;
import cn.yanwei.study.elastic.search.query.api.models.Criteria;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qingbo.bigdata.gatewayapi.utils.EsQueryAuth;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

/**
 * 基本测试类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 16:20
 */
class Statistic {
    private CriteriaQueryProcessor processor = new CriteriaQueryProcessor();
    private String[] citys = new String[]{"北京", "上海", "天津", "重庆", "哈尔滨", "长春", "沈阳", "呼和浩特", "石家庄", "乌鲁木齐", "兰州", "西宁", "西安", "银川", "郑州", "济南", "太原", "合肥", "武汉", "南京", "成都", "贵阳", "昆明", "南宁", "拉萨", "杭州", "南昌", "广州", "福州", "长沙", "海口", "齐齐哈尔", "吉林", "大连", "包头", "唐山", "伊宁", "酒泉", "玉树", "咸阳", "吴忠", "开封", "青岛", "运城", "芜湖", "宜昌", "苏州", "绵阳", "遵义", "大理", "北海", "日喀则", "宁波", "九江", "深圳", "厦门", "湘潭", "三亚", "牡丹江", "延边", "鞍山", "鄂尔多斯", "秦皇岛", "喀什", "武威", "延安", "洛阳", "烟台", "大同", "安庆", "襄阳", "无锡", "宜宾", "仁怀", "丽江", "桂林", "温州", "赣州", "佛山", "泉州", "株洲", "铁岭", "呼伦贝尔", "邯郸", "克拉玛依", "敦煌", "汉中", "安阳", "威海", "常州", "广安", "西双版纳", "义乌", "景德镇", "中山", "晋江", "岳阳", "南阳", "徐州", "香格里拉", "嘉兴", "潮州", "昆山", "绍兴", "漳州", "伊犁州"};
    private JSONObject mapping = JSONObject.parseObject("{'wx':'微信文章情感属性','weibo':'微博文章情感属性','journal':'报刊文章情感属性','toutiao':'头条文章情感属性','app':'客户端文章情感属性'}");
    private JSONObject map = JSONObject.parseObject("{'wx':'weixin1','weibo':'weibo1','journal':'web2','toutiao':'app1','app':'app1'}");
    private String[] repos = new String[]{
//            "wx", "weibo",
            "journal",
//            "toutiao", "app"
    };

    /**
     * 基础需求
     */
    @Test
    void test() throws Exception {
        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLosIPor5Xkvb_nlKgiLCJpc3MiOiJlY2hpc2FuIiwiZXhwIjo0NzEwMTMwODI5LCJpYXQiOjE1NTY1MzA4MjksInJvbCI6IlJPTEVfVVNFUiJ9.bL7Q4VIgS844DJrr_frbygAIVL7ReCQO4vrhNa20xgzlJdOGZzuNTEJeh2vJjfmeKSj8TlPmSHV3kfy47NDMwA";
        Criteria criteria;
        for (String repo : repos) {
            for (String city : citys) {
                criteria = new Criteria("news_title").phrase(city);
                if ("toutiao".equals(repo)) {
                    criteria = criteria.and("app_uuid").is("11");
                } else if ("app".equals(repo)) {
                    criteria = criteria.not("app_uuid").is("11");
                } else {
                    criteria = criteria.and("news_media").is(repo);
                }
                criteria = criteria.and("news_posttime").between("2019-01-01T00:00:00", "2019-12-31T00:00:00");
                SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
                searchSourceBuilder.query(processor.createQueryFromCriteria(criteria));
                searchSourceBuilder.from(0);
                searchSourceBuilder.size(0);
                RangeAggregationBuilder rangeAggregationBuilder = AggregationBuilders.range("news_positive").field("news_positive");
                rangeAggregationBuilder.addUnboundedTo("负面", 0.45);
                rangeAggregationBuilder.addRange("正常", 0.45, 0.75);
                rangeAggregationBuilder.addUnboundedFrom("消极", 0.75);
                searchSourceBuilder.aggregation(rangeAggregationBuilder);
                String statement = StringUtil.messageString(searchSourceBuilder.toString());
                long startTime = 1546272000000L;
                long endTime = 1577721600000L;
                String rep = EsQueryAuth.searchByStatement(JSON.parseObject(statement).toJSONString(), map.getString(repo), token, startTime, endTime);
                JSONArray reps = JSONArray.parseArray(rep).getJSONObject(0).getJSONObject("aggregations").getJSONObject("news_positive").getJSONArray("buckets");
                System.out.print(mapping.getString(repo) + "\t" + city);
                for (Object re : reps) {
                    String key = ((JSONObject) re).getString("key");
                    String doc_count = ((JSONObject) re).getString("doc_count");
                    System.out.print("\t" + key + "\t" + doc_count);
                }
                System.out.println();
            }
        }
    }
}
