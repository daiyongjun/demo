package cn.yanwei.study.elastic.search.query.sql.parse;

import java.util.*;

import cn.yanwei.study.elastic.search.query.sql.domain.*;
import cn.yanwei.study.elastic.search.query.sql.exception.SqlParseException;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.ast.*;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlSelectGroupByExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;

/**
 * @author yanwei
 */
public class SqlParser {


    public SqlParser() {

    }

    public Select parseSelect(SQLQueryExpr mySqlExpr) throws SqlParseException {

        MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) mySqlExpr.getSubQuery().getQuery();

        return parseSelect(query);
    }

    /**
     * 将翻译后的SQL转成定义的对象
     *
     * @param query 翻译后的SQL
     * @return Select
     * @throws SqlParseException sql解析异常
     */
    Select parseSelect(MySqlSelectQueryBlock query) throws SqlParseException {

        Select select = new Select();

        WhereParser whereParser = new WhereParser(this, query);

        findSelect(query, select, query.getFrom().getAlias());

        select.getFrom().addAll(findFrom(query.getFrom()));

        select.setWhere(whereParser.findWhere());

        select.fillSubQueries();

        findLimit(query.getLimit(), select);

        findOrderBy(query, select);

        findGroupBy(query, select);

        return select;
    }


    private void findSelect(MySqlSelectQueryBlock query, Select select, String tableAlias) throws SqlParseException {
        List<SQLSelectItem> selectList = query.getSelectList();
        for (SQLSelectItem sqlSelectItem : selectList) {
            Field field = FieldMaker.makeField(sqlSelectItem.getExpr(), sqlSelectItem.getAlias(), tableAlias);
            select.addField(field);
        }
    }

    private void findGroupBy(MySqlSelectQueryBlock query, Select select) throws SqlParseException {
        SQLSelectGroupByClause groupBy = query.getGroupBy();
        SQLTableSource sqlTableSource = query.getFrom();
        if (groupBy == null) {
            return;
        }
        List<SQLExpr> items = groupBy.getItems();

        List<SQLExpr> standardGroupBys = new ArrayList<>();
        for (SQLExpr sqlExpr : items) {
            //todo: mysql expr patch
            if (sqlExpr instanceof MySqlSelectGroupByExpr) {
                MySqlSelectGroupByExpr sqlSelectGroupByExpr = (MySqlSelectGroupByExpr) sqlExpr;
                sqlExpr = sqlSelectGroupByExpr.getExpr();
            }
            boolean condition = (sqlExpr instanceof SQLParensIdentifierExpr || !(sqlExpr instanceof SQLIdentifierExpr || sqlExpr instanceof SQLMethodInvokeExpr));
            if (condition && !standardGroupBys.isEmpty()) {
                // flush the standard group bys
                select.addGroupBy(convertExprsToFields(standardGroupBys, sqlTableSource));
                standardGroupBys = new ArrayList<>();
            }

            if (sqlExpr instanceof SQLParensIdentifierExpr) {
                // single item with parens (should get its own aggregation)
                select.addGroupBy(FieldMaker.makeField(sqlExpr, null, sqlTableSource.getAlias()));
            } else if (sqlExpr instanceof SQLListExpr) {
                // multiple items in their own list
                SQLListExpr listExpr = (SQLListExpr) sqlExpr;
                select.addGroupBy(convertExprsToFields(listExpr.getItems(), sqlTableSource));
            } else {
                // everything else gets added to the running list of standard group bys
                standardGroupBys.add(sqlExpr);
            }
        }
        if (!standardGroupBys.isEmpty()) {
            select.addGroupBy(convertExprsToFields(standardGroupBys, sqlTableSource));
        }
    }

    private List<Field> convertExprsToFields(List<? extends SQLExpr> exprs, SQLTableSource sqlTableSource) throws SqlParseException {
        List<Field> fields = new ArrayList<>(exprs.size());
        for (SQLExpr expr : exprs) {
            //here we suppose groupby field will not have alias,so set null in second parameter
            fields.add(FieldMaker.makeField(expr, null, sqlTableSource.getAlias()));
        }
        return fields;
    }

    private void findOrderBy(MySqlSelectQueryBlock query, Select select) throws SqlParseException {
        SQLOrderBy orderBy = query.getOrderBy();

        if (orderBy == null) {
            return;
        }
        List<SQLSelectOrderByItem> items = orderBy.getItems();

        addOrderByToSelect(select, items);

    }

    private void addOrderByToSelect(Select select, List<SQLSelectOrderByItem> items) throws SqlParseException {
        for (SQLSelectOrderByItem sqlSelectOrderByItem : items) {
            SQLExpr expr = sqlSelectOrderByItem.getExpr();
            String orderByName = FieldMaker.makeField(expr, null, null).toString();

            if (sqlSelectOrderByItem.getType() == null) {
                sqlSelectOrderByItem.setType(SQLOrderingSpecification.ASC);
            }
            String type = sqlSelectOrderByItem.getType().toString();

            orderByName = orderByName.replace("`", "");
            if (null != null) {
                orderByName = orderByName.replaceFirst(null + "\\.", "");
            }
            select.addOrderBy(orderByName, type);

        }
    }

    private void findLimit(MySqlSelectQueryBlock.Limit limit, Select select) {

        if (limit == null) {
            return;
        }

        select.setRowCount(Integer.parseInt(limit.getRowCount().toString()));

        if (limit.getOffset() != null) {
            select.setOffset(Integer.parseInt(limit.getOffset().toString()));
        }
    }

    /**
     * Parse the from clause
     *
     * @param from the from clause.
     * @return list of From objects represents all the sources.
     */
    private List<From> findFrom(SQLTableSource from) {
        boolean isSqlExprTable = from.getClass().isAssignableFrom(SQLExprTableSource.class);

        if (isSqlExprTable) {
            SQLExprTableSource fromExpr = (SQLExprTableSource) from;
            String[] split = fromExpr.getExpr().toString().split(",");

            ArrayList<From> fromList = new ArrayList<>();
            for (String source : split) {
                fromList.add(new From(source.trim(), fromExpr.getAlias()));
            }
            return fromList;
        }

        SQLJoinTableSource joinTableSource = ((SQLJoinTableSource) from);
        List<From> fromList = new ArrayList<>();
        fromList.addAll(findFrom(joinTableSource.getLeft()));
        fromList.addAll(findFrom(joinTableSource.getRight()));
        return fromList;
    }
}
