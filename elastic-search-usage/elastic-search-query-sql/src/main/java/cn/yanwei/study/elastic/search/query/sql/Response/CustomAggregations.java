package cn.yanwei.study.elastic.search.query.sql.Response;

import org.apache.lucene.util.SetOnce;
import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentParserUtils;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 自定义
 *
 * @author yanwei
 * @version 1.0
 * Created on date: 2020/3/9 18:30
 */
public class CustomAggregations extends Aggregations {
    public static Aggregations fromXContent(XContentParser parser) throws IOException {
        ArrayList aggregations = new ArrayList();

        XContentParser.Token token;
        while((token = parser.nextToken()) != XContentParser.Token.END_OBJECT) {
            if (token == XContentParser.Token.START_OBJECT) {
                SetOnce<Aggregation> typedAgg = new SetOnce();
                String currentField = parser.currentName();
                CustomXContentParserUtils.parseTypedKeysObject(parser, Aggregation.class, typedAgg::set);
                if (typedAgg.get() == null) {
                    throw new ParsingException(parser.getTokenLocation(), String.format(Locale.ROOT, "Could not parse aggregation keyed as [%s]", currentField), new Object[0]);
                }

                aggregations.add(typedAgg.get());
            }
        }

        return new Aggregations(aggregations);
    }
}
