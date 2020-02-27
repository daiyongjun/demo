package cn.yanwei.study.elastic.search.query.sql.query;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import cn.yanwei.study.elastic.search.query.sql.domain.*;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import cn.yanwei.study.elastic.search.query.sql.exception.SqlParseException;
import cn.yanwei.study.elastic.search.query.sql.query.maker.QueryMaker;

/**
 * Transform SQL query to standard Elasticsearch search query
 *
 * @author yanwei
 */
public class DefaultQueryAction extends QueryAction {

    private final Select select;
    private SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

    void initialize(SearchSourceBuilder searchSourceBuilder) {
        this.searchSourceBuilder = searchSourceBuilder;
    }

    DefaultQueryAction(Select select) {
        super(select);
        this.select = select;
    }

    @Override
    public Search explain() throws SqlParseException {
        Search search = new Search();
        setFields(select.getFields());
        setWhere(select.getWhere());
        setSorts(select.getOrderBys());
        setLimit(select.getOffset(), select.getRowCount());
        search.setIndex(Arrays.toString(query.getIndexArr()).replaceAll("[\\[\\]]",""));
        search.setQueryString(JSONObject.parseObject(searchSourceBuilder.toString()).toString());
        return search;
    }


    /**
     * Set source filtering on a search request.
     *
     * @param fields list of fields to source filter.
     */
    void setFields(List<Field> fields) throws SqlParseException {
        if (select.getFields().size() > 0) {
            ArrayList<String> includeFields = new ArrayList<>();
            ArrayList<String> excludeFields = new ArrayList<>();

            for (Field field : fields) {
                if (field instanceof MethodField) {
                    MethodField method = (MethodField) field;
                    if ("script".equals(method.getName().toLowerCase())) {
                        handleScriptField(method);
                    } else if ("include".equalsIgnoreCase(method.getName())) {
                        for (KVValue kvValue : method.getParams()) {
                            includeFields.add(kvValue.value.toString());
                        }
                    } else if ("exclude".equalsIgnoreCase(method.getName())) {
                        for (KVValue kvValue : method.getParams()) {
                            excludeFields.add(kvValue.value.toString());
                        }
                    }
                } else if (field instanceof Field) {
                    includeFields.add(field.getName());
                }
            }

            searchSourceBuilder.fetchSource(includeFields.toArray(new String[0]), excludeFields.toArray(new String[0]));
        }
    }

    private void handleScriptField(MethodField method) throws SqlParseException {
        List<KVValue> params = method.getParams();
        int valve = 2;
        int valve1 = 3;
        if (params.size() == valve) {
            searchSourceBuilder.scriptField(params.get(0).value.toString(), new Script(params.get(1).value.toString()));
        } else if (params.size() == valve1) {
            searchSourceBuilder.scriptField(params.get(0).value.toString(), new Script(ScriptType.INLINE, params.get(1).value.toString(), params.get(2).value.toString(), null));
        } else {
            throw new SqlParseException("scripted_field only allows script(name,script) or script(name,lang,script)");
        }
    }

    /**
     * Create filters or queries based on the Where clause.
     *
     * @param where the 'WHERE' part of the SQL query.
     * @throws SqlParseException 解析异常
     */
    private void setWhere(Where where) throws SqlParseException {
        if (where != null) {
            BoolQueryBuilder boolQuery = QueryMaker.explan(where, this.select.isQuery);
            searchSourceBuilder.query(boolQuery);
        }
    }

    /**
     * Add sorts to the elasticsearch query based on the 'ORDER BY' clause.
     *
     * @param orderBys list of Order object
     */
    private void setSorts(List<Order> orderBys) {
        for (Order order : orderBys) {
            searchSourceBuilder.sort(order.getName(), SortOrder.valueOf(order.getType()));
        }
    }

    /**
     * Add from and size to the ES query based on the 'LIMIT' clause
     *
     * @param from starts from document at position from
     * @param size number of documents to return.
     */
    private void setLimit(int from, int size) {
        searchSourceBuilder.from(from);

        if (size > -1) {
            searchSourceBuilder.size(size);
        }
    }
}
