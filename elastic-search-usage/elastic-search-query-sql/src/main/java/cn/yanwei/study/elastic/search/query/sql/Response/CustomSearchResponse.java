package cn.yanwei.study.elastic.search.query.sql.Response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchResponseSections;
import org.elasticsearch.action.search.ShardSearchFailure;
import org.elasticsearch.common.ParseField;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentParserUtils;
import org.elasticsearch.rest.action.RestActions;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.profile.SearchProfileShardResults;
import org.elasticsearch.search.suggest.Suggest;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/3/9 18:27
 */
public class CustomSearchResponse extends SearchResponse {
    private static final ParseField SCROLL_ID = new ParseField("_scroll_id", new String[0]);
    private static final ParseField TOOK = new ParseField("took", new String[0]);
    private static final ParseField TIMED_OUT = new ParseField("timed_out", new String[0]);
    private static final ParseField TERMINATED_EARLY = new ParseField("terminated_early", new String[0]);
    private static final ParseField NUM_REDUCE_PHASES = new ParseField("num_reduce_phases", new String[0]);

    public static SearchResponse fromXContent(XContentParser parser) throws IOException {
        XContentParserUtils.ensureExpectedToken(XContentParser.Token.START_OBJECT, parser.nextToken(), parser::getTokenLocation);
        String currentFieldName = null;
        SearchHits hits = null;
        Aggregations aggs = null;
        Suggest suggest = null;
        SearchProfileShardResults profile = null;
        boolean timedOut = false;
        Boolean terminatedEarly = null;
        int numReducePhases = 1;
        long tookInMillis = -1L;
        int successfulShards = -1;
        int totalShards = -1;
        int skippedShards = 0;
        String scrollId = null;
        ArrayList failures = new ArrayList();

        while (true) {
            XContentParser.Token token;
            label96:
            while ((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
                if (token == XContentParser.Token.FIELD_NAME) {
                    currentFieldName = parser.currentName();
                } else if (token.isValue()) {
                    if (SCROLL_ID.match(currentFieldName)) {
                        scrollId = parser.text();
                    } else if (TOOK.match(currentFieldName)) {
                        tookInMillis = parser.longValue();
                    } else if (TIMED_OUT.match(currentFieldName)) {
                        timedOut = parser.booleanValue();
                    } else if (TERMINATED_EARLY.match(currentFieldName)) {
                        terminatedEarly = parser.booleanValue();
                    } else if (NUM_REDUCE_PHASES.match(currentFieldName)) {
                        numReducePhases = parser.intValue();
                    } else {
                        parser.skipChildren();
                    }
                } else if (token == XContentParser.Token.START_OBJECT) {
                    if ("hits".equals(currentFieldName)) {
                        hits = SearchHits.fromXContent(parser);
                    } else if ("aggregations".equals(currentFieldName)) {
                        aggs = CustomAggregations.fromXContent(parser);
                    } else if ("suggest".equals(currentFieldName)) {
                        suggest = Suggest.fromXContent(parser);
                    } else if ("profile".equals(currentFieldName)) {
                        profile = SearchProfileShardResults.fromXContent(parser);
                    } else if (!RestActions._SHARDS_FIELD.match(currentFieldName)) {
                        parser.skipChildren();
                    } else {
                        while (true) {
                            while (true) {
                                if ((token = parser.nextToken()) == XContentParser.Token.END_OBJECT) {
                                    continue label96;
                                }

                                if (token == XContentParser.Token.FIELD_NAME) {
                                    currentFieldName = parser.currentName();
                                } else if (token.isValue()) {
                                    if (RestActions.FAILED_FIELD.match(currentFieldName)) {
                                        parser.intValue();
                                    } else if (RestActions.SUCCESSFUL_FIELD.match(currentFieldName)) {
                                        successfulShards = parser.intValue();
                                    } else if (RestActions.TOTAL_FIELD.match(currentFieldName)) {
                                        totalShards = parser.intValue();
                                    } else if (RestActions.SKIPPED_FIELD.match(currentFieldName)) {
                                        skippedShards = parser.intValue();
                                    } else {
                                        parser.skipChildren();
                                    }
                                } else if (token == XContentParser.Token.START_ARRAY) {
                                    if (RestActions.FAILURES_FIELD.match(currentFieldName)) {
                                        while (parser.nextToken() != XContentParser.Token.END_ARRAY) {
                                            failures.add(ShardSearchFailure.fromXContent(parser));
                                        }
                                    } else {
                                        parser.skipChildren();
                                    }
                                } else {
                                    parser.skipChildren();
                                }
                            }
                        }
                    }
                }
            }

            SearchResponseSections searchResponseSections = new SearchResponseSections(hits, aggs, suggest, timedOut, terminatedEarly, profile, numReducePhases);
            return new SearchResponse(searchResponseSections, scrollId, totalShards, successfulShards, skippedShards, tookInMillis, (ShardSearchFailure[]) failures.toArray(new ShardSearchFailure[failures.size()]));
        }
    }

}
