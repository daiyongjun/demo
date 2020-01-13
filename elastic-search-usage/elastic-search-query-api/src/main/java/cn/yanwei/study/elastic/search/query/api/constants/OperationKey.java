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
     * rangeQuery lt
     */
    LESS,
    /**
     * rangeQuery gt
     */
    GREATER,
    /**
     * fuzzyQuery
     */
    FUZZY,
    /**
     * termsQuery
     */
    IN,
    /**
     * mustNot termsQuery
     */
    NOT_IN,
    /**
     * queryStringQuery
     */
    QUERY_STRING,
    /**
     * matchQuery
     */
    MATCH,
    /**
     * matchPhraseQuery
     */
    MATCH_PHRASE,
    MUST,
    MUST_NOT,
    SHOULD,
    EXISTS,
}
