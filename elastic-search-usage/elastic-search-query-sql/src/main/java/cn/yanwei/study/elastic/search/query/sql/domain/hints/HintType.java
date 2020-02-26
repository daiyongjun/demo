package cn.yanwei.study.elastic.search.query.sql.domain.hints;

/**
 *
 * @author Eliran
 * @date 29/8/2015
 */
public enum HintType
{
    HASH_WITH_TERMS_FILTER,
    JOIN_LIMIT,
    USE_NESTED_LOOPS,
    NL_MULTISEARCH_SIZE,
    USE_SCROLL,
    IGNORE_UNAVAILABLE,
    DOCS_WITH_AGGREGATION,
    ROUTINGS,
    SHARD_SIZE,
    HIGHLIGHT,
    MINUS_FETCH_AND_RESULT_LIMITS,
    MINUS_USE_TERMS_OPTIMIZATION;
}