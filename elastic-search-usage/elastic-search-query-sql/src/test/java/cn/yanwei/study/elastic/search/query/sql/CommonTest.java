package cn.yanwei.study.elastic.search.query.sql;

import cn.yanwei.study.elastic.search.query.sql.query.EsActionFactory;
import org.junit.After;
import org.junit.Test;
import cn.yanwei.study.elastic.search.query.sql.exception.SqlParseException;
import cn.yanwei.study.elastic.search.query.sql.query.QueryAction;

import java.sql.SQLFeatureNotSupportedException;

public class CommonTest {

    private String sql = null;

    @After
    public void execute() throws SQLFeatureNotSupportedException, SqlParseException {
//        TermsAggregationBuilder.

        System.out.println("sql=\n" + sql);
        QueryAction qa = EsActionFactory.create(sql);
        System.out.println(qa.explain().toString());
    }

    @Test
    public void testSelectStar() {
        sql = "select * from weibo_202001";
    }

    @Test
    public void testSelectWhere() {
        sql = "select * from weibo_202001 where news_title = '疫情' AND posttime > '2019-07-01' group by news_title LIMIT 2";
    }

    @Test
    public void testSelectGourp() {
//        sql = "select a,case when c='1' then 'haha' when c='2' then 'book' else 'hbhb' end as gg from tbl_a group by "; // order by a asc,c desc,d asc limit 8,12";
        sql = "select a, floor(num) my_b, case when os = 'iOS' then 'hehe' else 'haha' end as my_os from t_zhongshu_test";


    }
    @Test
    public void testSelectMethod() {
//        sql = "select a,case when c='1' then 'haha' when c='2' then 'book' else 'hbhb' end as gg from tbl_a group by "; // order by a asc,c desc,d asc limit 8,12";
        sql = "select count(*) d from weibo_202001 LIMIT 1";
//        sql = "select news_positive,case when news_positive > 0.45 then '消极' else '积极' end as positive from weibo_202001 LIMIT 1";
    }


}
