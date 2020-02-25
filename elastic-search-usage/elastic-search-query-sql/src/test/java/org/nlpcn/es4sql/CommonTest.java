package org.nlpcn.es4sql;

import org.junit.After;
import org.junit.Test;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.query.ESActionFactory;
import org.elasticsearch.client.Client;
import org.nlpcn.es4sql.query.QueryAction;

import java.sql.SQLFeatureNotSupportedException;

public class CommonTest {

    private String sql = null;
    private Client client = null;

    @After
    public void execute() throws SQLFeatureNotSupportedException, SqlParseException {
        System.out.println("sql=\n" + sql);
        QueryAction qa = ESActionFactory.create(client, sql);
        System.out.println(qa.explain().toString());
    }

    @Test
    public void testSelectStar() throws SQLFeatureNotSupportedException, SqlParseException {
        sql = "select news_title,news_content from web where news_title = '疫情' AND posttime > '2019-07-01'";
    }
}
