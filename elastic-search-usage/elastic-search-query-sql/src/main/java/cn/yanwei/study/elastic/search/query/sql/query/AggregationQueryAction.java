package cn.yanwei.study.elastic.search.query.sql.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.yanwei.study.elastic.search.query.sql.domain.*;
import cn.yanwei.study.elastic.search.query.sql.domain.hints.Hint;
import cn.yanwei.study.elastic.search.query.sql.domain.hints.HintType;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.join.aggregations.JoinAggregationBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ReverseNestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import cn.yanwei.study.elastic.search.query.sql.exception.SqlParseException;
import cn.yanwei.study.elastic.search.query.sql.query.maker.AggMaker;
import cn.yanwei.study.elastic.search.query.sql.query.maker.QueryMaker;

/**
 * Transform SQL query to Elasticsearch aggregations query
 *
 * @author yanwei
 */
public class AggregationQueryAction extends QueryAction {

    private final Select select;
    private AggMaker aggMaker = new AggMaker();
    private SearchSourceBuilder searchSourceBuilder;

    AggregationQueryAction(Select select) {
        super(select);
        this.select = select;
        this.searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
    }

    @Override
    public Search explain() throws SqlParseException {
        setWhere(select.getWhere());
        AggregationBuilder lastAgg = null;
        for (List<Field> groupBy : select.getGroupBys()) {
            if (!groupBy.isEmpty()) {
                Field field = groupBy.get(0);
                lastAgg = getGroupAgg(field);
                if (lastAgg instanceof TermsAggregationBuilder && !(field instanceof MethodField)) {
                    //if limit size is too small, increasing shard  size is required
                    if (select.getRowCount() < 200) {
                        ((TermsAggregationBuilder) lastAgg).shardSize(2000);
                        for (Hint hint : select.getHints()) {
                            if (hint.getType() == HintType.SHARD_SIZE) {
                                if (hint.getParams() != null && hint.getParams().length != 0 && hint.getParams()[0] != null) {
                                    ((TermsAggregationBuilder) lastAgg).shardSize((Integer) hint.getParams()[0]);
                                }
                            }
                        }
                    }
                    if (select.getRowCount() > 0) {
                        ((TermsAggregationBuilder) lastAgg).size(select.getRowCount());
                    }
                }

                if (field.isNested()) {
                    AggregationBuilder nestedBuilder = createNestedAggregation(field);

                    if (insertFilterIfExistsAfter(lastAgg, groupBy, nestedBuilder, 1)) {
                        groupBy.remove(1);
                    } else {
                        nestedBuilder.subAggregation(lastAgg);
                    }
                    searchSourceBuilder.aggregation(wrapNestedIfNeeded(nestedBuilder, field.isReverseNested()));
                } else if (field.isChildren()) {
                    AggregationBuilder childrenBuilder = createChildrenAggregation(field);

                    if (insertFilterIfExistsAfter(lastAgg, groupBy, childrenBuilder, 1)) {
                        groupBy.remove(1);
                    } else {
                        childrenBuilder.subAggregation(lastAgg);
                    }
                    searchSourceBuilder.aggregation(childrenBuilder);
                } else {
                    searchSourceBuilder.aggregation(lastAgg);
                }

                for (int i = 1; i < groupBy.size(); i++) {
                    field = groupBy.get(i);
                    AggregationBuilder subAgg = getGroupAgg(field);
                    if (field.isNested()) {
                        AggregationBuilder nestedBuilder = createNestedAggregation(field);

                        if (insertFilterIfExistsAfter(subAgg, groupBy, nestedBuilder, i + 1)) {
                            groupBy.remove(i + 1);
                            i++;
                        } else {
                            nestedBuilder.subAggregation(subAgg);
                        }

                        lastAgg.subAggregation(wrapNestedIfNeeded(nestedBuilder, field.isReverseNested()));
                    } else if (field.isChildren()) {
                        AggregationBuilder childrenBuilder = createChildrenAggregation(field);

                        if (insertFilterIfExistsAfter(subAgg, groupBy, childrenBuilder, i + 1)) {
                            groupBy.remove(i + 1);
                            i++;
                        } else {
                            childrenBuilder.subAggregation(subAgg);
                        }

                        lastAgg.subAggregation(childrenBuilder);
                    } else {
                        lastAgg.subAggregation(subAgg);
                    }

                    lastAgg = subAgg;
                }
            }

            // add aggregation function to each groupBy
            explanFields(searchSourceBuilder, select.getFields(), lastAgg);
        }

        if (select.getGroupBys().size() < 1) {
            //add aggregation when having no groupBy script
            explanFields(searchSourceBuilder, select.getFields(), lastAgg);

        }

        Map<String, KVValue> groupMap = aggMaker.getGroupMap();
        // add field
        if (select.getFields().size() > 0) {
            setFields(select.getFields());
        }

        // add order
        if (lastAgg != null && select.getOrderBys().size() > 0) {
            for (Order order : select.getOrderBys()) {
                KVValue temp = groupMap.get(order.getName());
                if (temp != null) {
                    TermsAggregationBuilder termsBuilder = (TermsAggregationBuilder) temp.value;
                    switch (temp.key) {
                        case "COUNT":
                            termsBuilder.order(Terms.Order.count(isasc(order)));
                            break;
                        case "KEY":
                            termsBuilder.order(Terms.Order.term(isasc(order)));
                            // add the sort to the request also so the results get sorted as well
                            searchSourceBuilder.sort(order.getName(), SortOrder.valueOf(order.getType()));
                            break;
                        case "FIELD":
                            termsBuilder.order(Terms.Order.aggregation(order.getName(), isasc(order)));
                            break;
                        default:
                            throw new SqlParseException(order.getName() + " can not to order");
                    }
                } else {
                    searchSourceBuilder.sort(order.getName(), SortOrder.valueOf(order.getType()));
                }
            }
        }
        Search search = new Search();
        search.setIndex(Arrays.toString(query.getIndexArr()).replaceAll("[\\[\\]]",""));
        search.setQueryString(JSONObject.parseObject(searchSourceBuilder.toString()).toString());
        return search;
    }

    private AggregationBuilder getGroupAgg(Field field) throws SqlParseException {
        boolean refrence = false;
        AggregationBuilder lastAgg = null;
        for (Field temp : select.getFields()) {
            if (temp instanceof MethodField && "script".equals(temp.getName())) {
                MethodField scriptField = (MethodField) temp;
                for (KVValue kv : scriptField.getParams()) {
                    if (kv.value.equals(field.getName())) {
                        lastAgg = aggMaker.makeGroupAgg(scriptField);
                        refrence = true;
                        break;
                    }
                }
            }
        }

        if (!refrence) {
            lastAgg = aggMaker.makeGroupAgg(field);
        }

        return lastAgg;
    }

    private AggregationBuilder wrapNestedIfNeeded(AggregationBuilder nestedBuilder, boolean reverseNested) {
        if (!reverseNested) {
            return nestedBuilder;
        }
        if (reverseNested && !(nestedBuilder instanceof NestedAggregationBuilder)) {
            return nestedBuilder;
        }
        //we need to jump back to root
        return AggregationBuilders.reverseNested(nestedBuilder.getName() + "_REVERSED").subAggregation(nestedBuilder);
    }

    private AggregationBuilder createNestedAggregation(Field field) {
        AggregationBuilder nestedBuilder;

        String nestedPath = field.getNestedPath();

        if (field.isReverseNested()) {
            String flag = "~";
            if (nestedPath == null || !nestedPath.startsWith(flag)) {
                ReverseNestedAggregationBuilder reverseNestedAggregationBuilder = AggregationBuilders.reverseNested(getNestedAggName(field));
                if (nestedPath != null) {
                    reverseNestedAggregationBuilder.path(nestedPath);
                }
                return reverseNestedAggregationBuilder;
            }
            nestedPath = nestedPath.substring(1);
        }

        nestedBuilder = AggregationBuilders.nested(getNestedAggName(field), nestedPath);

        return nestedBuilder;
    }

    private AggregationBuilder createChildrenAggregation(Field field) {
        AggregationBuilder childrenBuilder;

        String childType = field.getChildType();

        childrenBuilder = JoinAggregationBuilders.children(getChildrenAggName(field), childType);

        return childrenBuilder;
    }

    private String getNestedAggName(Field field) {
        String prefix;

        if (field instanceof MethodField) {
            String nestedPath = field.getNestedPath();
            if (nestedPath != null) {
                prefix = nestedPath;
            } else {
                prefix = field.getAlias();
            }
        } else {
            prefix = field.getName();
        }
        return prefix + "@NESTED";
    }

    private String getChildrenAggName(Field field) {
        String prefix;

        if (field instanceof MethodField) {
            String childType = field.getChildType();

            if (childType != null) {
                prefix = childType;
            } else {
                prefix = field.getAlias();
            }
        } else {
            prefix = field.getName();
        }

        return prefix + "@CHILDREN";
    }

    private boolean insertFilterIfExistsAfter(AggregationBuilder agg, List<Field> groupBy, AggregationBuilder builder, int nextPosition) throws SqlParseException {
        if (groupBy.size() <= nextPosition) {
            return false;
        }
        Field filterFieldCandidate = groupBy.get(nextPosition);
        if (!(filterFieldCandidate instanceof MethodField)) {
            return false;
        }
        MethodField methodField = (MethodField) filterFieldCandidate;
        String filter = "filter";
        if (!filter.equals(methodField.getName().toLowerCase())) {
            return false;
        }
        builder.subAggregation(aggMaker.makeGroupAgg(filterFieldCandidate).subAggregation(agg));
        return true;
    }


    private boolean isasc(Order order) {
        return "ASC".equals(order.getType());
    }

    private void setFields(List<Field> fields) {
        if (select.getFields().size() > 0) {
            ArrayList<String> includeFields = new ArrayList<>();

            for (Field field : fields) {
                if (field != null) {
                    includeFields.add(field.getName());
                }
            }

            searchSourceBuilder.fetchSource(includeFields.toArray(new String[0]), null);
        }
    }


    private void explanFields(SearchSourceBuilder searchSourceBuilder, List<Field> fields, AggregationBuilder groupByAgg) throws SqlParseException {
        for (Field field : fields) {
            if (field instanceof MethodField) {

                if ("script".equals(field.getName())) {
                    searchSourceBuilder.storedField(field.getAlias());
                    DefaultQueryAction defaultQueryAction = new DefaultQueryAction(select);
                    defaultQueryAction.initialize(searchSourceBuilder);
                    List<Field> tempFields = Lists.newArrayList(field);
                    defaultQueryAction.setFields(tempFields);
                    continue;
                }

                AggregationBuilder makeAgg = aggMaker.makeFieldAgg((MethodField) field, groupByAgg);
                if (groupByAgg != null) {
                    groupByAgg.subAggregation(makeAgg);
                } else {
                    searchSourceBuilder.aggregation(makeAgg);
                }
            } else if (field instanceof Field) {
                searchSourceBuilder.storedField(field.getName());
            } else {
                throw new SqlParseException("it did not support this field method " + field);
            }
        }
    }

    /**
     * Create filters based on
     * the Where clause.
     *
     * @param where the 'WHERE' part of the SQL query.
     * @throws SqlParseException 解析异常
     */
    private void setWhere(Where where) throws SqlParseException {
        if (where != null) {
            QueryBuilder whereQuery = QueryMaker.explan(where, this.select.isQuery);
            searchSourceBuilder.query(whereQuery);
        }
    }
}
