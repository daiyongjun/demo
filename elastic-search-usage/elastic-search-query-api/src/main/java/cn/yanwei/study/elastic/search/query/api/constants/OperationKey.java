package cn.yanwei.study.elastic.search.query.api.constants;

/**
 * 操作key
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/1/7 9:39
 */
public enum OperationKey {
    /**
     * termQuery
     */
    EQUALS,
    /**
     * termsQuery
     */
    IN,
    /**
     * rangeQuery lt
     */
    LESS,
    /**
     * rangeQuery gt
     */
    GREATER,
    /**
     * rangeQuery lte
     */
    LESS_EQUAL,
    /**
     * rangeQuery gte
     */
    GREATER_EQUAL,
    /**
     * rangeQuery
     */
    BETWEEN,
    /**
     * matchQuery
     */
    MATCH,
    /**
     * matchPhraseQuery
     */
    MATCH_PHRASE,
    /**
     * wildcardQuery
     */
    CONTAINS,
    /**
     * * + wildcardQuery
     */
    STARTS_WITH,
    /**
     * wildcardQuery + *
     */
    ENDS_WITH,
    /**
     * fuzzyQuery
     */
    FUZZY,
    EXISTS,
}
