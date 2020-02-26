package cn.yanwei.study.elastic.search.query.sql.query;

import cn.yanwei.study.elastic.search.query.sql.domain.BaseQuery;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import cn.yanwei.study.elastic.search.query.sql.exception.SqlParseException;

import java.util.ArrayList;
import java.util.Map;

/**
 * Abstract class. used to transform Select object (Represents SQL query) to
 * SearchRequestBuilder (Represents ES query)
 */
public abstract class QueryAction {

    protected BaseQuery query;

    public QueryAction(BaseQuery query) {
        this.query = query;
    }

    protected HighlightBuilder.Field parseHighlightField(Object[] params) {
        if (params == null || params.length == 0 || params.length > 2) {
            //todo: exception.
        }
        HighlightBuilder.Field field = new HighlightBuilder.Field(params[0].toString());
        if (params.length == 1) {
            return field;
        }
        Map<String, Object> highlightParams = (Map<String, Object>) params[1];

        for (Map.Entry<String, Object> param : highlightParams.entrySet()) {
            switch (param.getKey()) {
                case "type":
                    field.highlighterType((String) param.getValue());
                    break;
                case "boundary_chars":
                    field.boundaryChars(fromArrayListToCharArray((ArrayList) param.getValue()));
                    break;
                case "boundary_max_scan":
                    field.boundaryMaxScan((Integer) param.getValue());
                    break;
                case "force_source":
                    field.forceSource((Boolean) param.getValue());
                    break;
                case "fragmenter":
                    field.fragmenter((String) param.getValue());
                    break;
                case "fragment_offset":
                    field.fragmentOffset((Integer) param.getValue());
                    break;
                case "fragment_size":
                    field.fragmentSize((Integer) param.getValue());
                    break;
                case "highlight_filter":
                    field.highlightFilter((Boolean) param.getValue());
                    break;
                case "matched_fields":
                    field.matchedFields((String[]) ((ArrayList) param.getValue()).toArray(new String[((ArrayList) param.getValue()).size()]));
                    break;
                case "no_match_size":
                    field.noMatchSize((Integer) param.getValue());
                    break;
                case "num_of_fragments":
                    field.numOfFragments((Integer) param.getValue());
                    break;
                case "order":
                    field.order((String) param.getValue());
                    break;
                case "phrase_limit":
                    field.phraseLimit((Integer) param.getValue());
                    break;
                case "post_tags":
                    field.postTags((String[]) ((ArrayList) param.getValue()).toArray(new String[((ArrayList) param.getValue()).size()]));
                    break;
                case "pre_tags":
                    field.preTags((String[]) ((ArrayList) param.getValue()).toArray(new String[((ArrayList) param.getValue()).size()]));
                    break;
                case "require_field_match":
                    field.requireFieldMatch((Boolean) param.getValue());
                    break;
                default:
                    break;

            }
        }
        return field;
    }

    private char[] fromArrayListToCharArray(ArrayList arrayList) {
        char[] chars = new char[arrayList.size()];
        int i = 0;
        for (Object item : arrayList) {
            chars[i] = item.toString().charAt(0);
            i++;
        }
        return chars;
    }

    public abstract SearchSourceBuilder explain() throws SqlParseException;
}
