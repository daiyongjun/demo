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
        System.out.println("sql=\n" + sql);
        QueryAction qa = EsActionFactory.create(sql);
        System.out.println(qa.explain().toString());
    }

    @Test
    public void testSelectStar() {
        sql = "select * from web where news_title = '疫情' AND posttime > '2019-07-01'";
    }
}
