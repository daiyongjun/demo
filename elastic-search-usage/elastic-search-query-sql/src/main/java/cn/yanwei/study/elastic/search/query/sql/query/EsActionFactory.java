package cn.yanwei.study.elastic.search.query.sql.query;


import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.parser.*;
import cn.yanwei.study.elastic.search.query.sql.domain.Select;
import cn.yanwei.study.elastic.search.query.sql.exception.SqlParseException;
import cn.yanwei.study.elastic.search.query.sql.parse.ElasticSqlExprParser;
import cn.yanwei.study.elastic.search.query.sql.parse.SqlParser;
import cn.yanwei.study.elastic.search.query.sql.parse.SubQueryExpression;

import java.sql.SQLFeatureNotSupportedException;

/**
 * @author yanwei
 */
public class EsActionFactory {
    /**
     * 解析sql，并转成成dsl
     *
     * @param sql sql
     * @return QueryAction
     * @throws SqlParseException               自定义异常
     * @throws SQLFeatureNotSupportedException sql不支持异常
     */
    public static QueryAction create(String sql) throws SqlParseException, SQLFeatureNotSupportedException {
        sql = sql.replaceAll("\n", " ");
        String firstWord = sql.substring(0, sql.indexOf(' '));
        switch (firstWord.toUpperCase()) {
            case "SELECT":
                SQLQueryExpr sqlExpr = (SQLQueryExpr) toSqlExpr(sql);
                Select select = new SqlParser().parseSelect(sqlExpr);
                handleSubQueries(select);
                return handleSelect(select);
            default:
                throw new SQLFeatureNotSupportedException(String.format("Unsupported query: %s", sql));
        }
    }

    private static void handleSubQueries(Select select) throws SqlParseException {
        if (select.containsSubQueries()) {
            for (SubQueryExpression subQueryExpression : select.getSubQueries()) {
                handleSelect(subQueryExpression.getSelect());
            }
        }
    }

    private static QueryAction handleSelect(Select select) {
        if (select.isAgg) {
            return new AggregationQueryAction(select);
        } else {
            return new DefaultQueryAction(select);
        }
    }

    /**
     * 解析sql,验证sql是否合法
     *
     * @param sql 待检测的sql
     * @return SQLExpr
     */
    private static SQLExpr toSqlExpr(String sql) {
        SQLExprParser parser = new ElasticSqlExprParser(sql);
        SQLExpr expr = parser.expr();
        if (parser.getLexer().token() != Token.EOF) {
            throw new ParserException("illegal sql expr : " + sql);
        }
        return expr;
    }


}
