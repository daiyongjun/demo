package cn.yanwei.study.elastic.search.query.api.domain;

import cn.yanwei.study.elastic.search.query.api.constants.OperationKey;
import cn.yanwei.study.elastic.search.query.api.models.Criteria;
import org.apache.lucene.queryparser.flexible.core.util.StringUtils;
import org.apache.lucene.queryparser.flexible.standard.QueryParserUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.util.Assert;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * 核心查询处理类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/12/23 15:44
 */
public class CriteriaQueryProcessor {

    public QueryBuilder createQueryFromCriteria(Criteria criteria) {
        if (criteria == null) {
            return null;
        }
        List<QueryBuilder> shouldQueryBuilderList = new LinkedList<>();
        List<QueryBuilder> mustNotQueryBuilderList = new LinkedList<>();
        List<QueryBuilder> mustQueryBuilderList = new LinkedList<>();

        ListIterator<Criteria> chainIterator = criteria.getCriteriaChain().listIterator();
        QueryBuilder queryFragmentForCriteria = null;
        int length = 1;
        while (chainIterator.hasNext()) {
            Criteria chainedCriteria = chainIterator.next();
            if (chainedCriteria.getRecursion() > 0) {
                queryFragmentForCriteria = createQueryFromCriteria(chainedCriteria.getCriteriaChain().get(0));
            } else {
                queryFragmentForCriteria = createQueryFragmentForCriteria(chainedCriteria);
            }
            if (queryFragmentForCriteria != null) {
                if (chainedCriteria.isOr()) {
                    shouldQueryBuilderList.add(queryFragmentForCriteria);
                } else if (chainedCriteria.isNegating()) {
                    mustNotQueryBuilderList.add(queryFragmentForCriteria);
                } else {
                    mustQueryBuilderList.add(queryFragmentForCriteria);
                }
            }
            length++;
        }
        BoolQueryBuilder query = null;

        if (!shouldQueryBuilderList.isEmpty() || !mustNotQueryBuilderList.isEmpty() || !mustQueryBuilderList.isEmpty()) {
            query = boolQuery();
            for (QueryBuilder qb : shouldQueryBuilderList) {
                query.should(qb);
                query.minimumShouldMatch(1);
            }
            for (QueryBuilder qb : mustNotQueryBuilderList) {
                query.mustNot(qb);
            }
            for (QueryBuilder qb : mustQueryBuilderList) {
                query.must(qb);
            }
        }

        return query;
    }

    private QueryBuilder createQueryFragmentForCriteria(Criteria chainedCriteria) {
        if (chainedCriteria.getQueryCriteriaEntries().isEmpty()) {
            return null;
        }
        Iterator<Criteria.CriteriaEntry> it = chainedCriteria.getQueryCriteriaEntries().iterator();
        boolean singeEntryCriteria = (chainedCriteria.getQueryCriteriaEntries().size() == 1);

        String fieldName = chainedCriteria.getField().getName();
        Assert.notNull(fieldName, "Unknown field");
        QueryBuilder query;

        if (singeEntryCriteria) {
            Criteria.CriteriaEntry entry = it.next();
            query = processCriteriaEntry(entry, fieldName);
        } else {
            query = boolQuery();
            while (it.hasNext()) {
                Criteria.CriteriaEntry entry = it.next();
                ((BoolQueryBuilder) query).must(Objects.requireNonNull(processCriteriaEntry(entry, fieldName)));
            }
        }
        addBoost(query, chainedCriteria.getBoost());
        return query;
    }

    private QueryBuilder processCriteriaEntry(Criteria.CriteriaEntry entry, String fieldName) {
        OperationKey key = entry.getKey();
        Object value = entry.getValue();

        if (value == null) {

            if (key == OperationKey.EXISTS) {
                return existsQuery(fieldName);
            } else {
                return null;
            }
        }

        String searchText = QueryParserUtil.escape(value.toString());

        QueryBuilder query = null;

        switch (key) {
            case EQUALS:
                query = termQuery(fieldName, searchText);
                break;
            case CONTAINS:
                query = wildcardQuery(fieldName, "*" + searchText + "*");
                break;
            case STARTS_WITH:
                query = wildcardQuery(fieldName, searchText + "*");
                break;
            case ENDS_WITH:
                query = wildcardQuery(fieldName, "*" + searchText);
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
            case LESS:
                query = rangeQuery(fieldName).lt(value);
                break;
            case GREATER:
                query = rangeQuery(fieldName).gt(value);
                break;
            case FUZZY:
                query = fuzzyQuery(fieldName, searchText);
                break;
            case IN:
                Collection<?> list = (Collection<?>) (value);
                query = termsQuery(fieldName, list);
                break;
            case NOT_IN:
                query = boolQuery().mustNot(termsQuery(fieldName, toStringList((Iterable<Object>) value)));
                break;
            case QUERY_STRING:
                query = queryStringQuery((String) value).defaultField(fieldName).defaultOperator(Operator.AND);
                break;
            case MATCH:
                query = matchQuery(fieldName, searchText);
                break;
            case MATCH_PHRASE:
                query = matchPhraseQuery(fieldName, searchText);
                break;
            default:
                break;
        }
        return query;
    }

    private static List<String> toStringList(Iterable<?> iterable) {
        List<String> list = new ArrayList<>();
        for (Object item : iterable) {
            list.add(StringUtils.toString(item));
        }
        return list;
    }

    private void addBoost(QueryBuilder query, float boost) {
        if (Float.isNaN(boost)) {
            return;
        }
        query.boost(boost);
    }
}
