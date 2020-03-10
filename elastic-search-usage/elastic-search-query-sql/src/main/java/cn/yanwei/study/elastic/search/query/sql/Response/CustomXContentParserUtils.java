package cn.yanwei.study.elastic.search.query.sql.Response;

import org.elasticsearch.common.ParsingException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentLocation;
import org.elasticsearch.common.xcontent.XContentParser;

import java.io.IOException;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/3/9 18:32
 */
public final class CustomXContentParserUtils {
    private CustomXContentParserUtils() {
    }

    public static void ensureFieldName(XContentParser parser, XContentParser.Token token, String fieldName) throws IOException {
        ensureExpectedToken(XContentParser.Token.FIELD_NAME, token, parser::getTokenLocation);
        String currentName = parser.currentName();
        if (!currentName.equals(fieldName)) {
            String message = "Failed to parse object: expecting field with name [%s] but found [%s]";
            throw new ParsingException(parser.getTokenLocation(), String.format(Locale.ROOT, message, fieldName, currentName), new Object[0]);
        }
    }

    public static void throwUnknownField(String field, XContentLocation location) {
        String message = "Failed to parse object: unknown field [%s] found";
        throw new ParsingException(location, String.format(Locale.ROOT, message, field), new Object[0]);
    }

    public static void throwUnknownToken(XContentParser.Token token, XContentLocation location) {
        String message = "Failed to parse object: unexpected token [%s] found";
        throw new ParsingException(location, String.format(Locale.ROOT, message, token), new Object[0]);
    }

    public static void ensureExpectedToken(XContentParser.Token expected, XContentParser.Token actual, Supplier<XContentLocation> location) {
        if (actual != expected) {
            String message = "Failed to parse object: expecting token of type [%s] but found [%s]";
            throw new ParsingException((XContentLocation) location.get(), String.format(Locale.ROOT, message, expected, actual), new Object[0]);
        }
    }

    public static Object parseStoredFieldsValue(XContentParser parser) throws IOException {
        XContentParser.Token token = parser.currentToken();
        Object value = null;
        if (token == XContentParser.Token.VALUE_STRING) {
            value = parser.text();
        } else if (token == XContentParser.Token.VALUE_NUMBER) {
            value = parser.numberValue();
        } else if (token == XContentParser.Token.VALUE_BOOLEAN) {
            value = parser.booleanValue();
        } else if (token == XContentParser.Token.VALUE_EMBEDDED_OBJECT) {
            value = new BytesArray(parser.binaryValue());
        } else {
            throwUnknownToken(token, parser.getTokenLocation());
        }

        return value;
    }

    public static <T> void parseTypedKeysObject(XContentParser parser, Class<T> objectClass, Consumer<T> consumer) throws IOException {
        if (parser.currentToken() != XContentParser.Token.START_OBJECT && parser.currentToken() != XContentParser.Token.START_ARRAY) {
            throwUnknownToken(parser.currentToken(), parser.getTokenLocation());
        }
        String currentFieldName = parser.currentName();
        if (Strings.hasLength(currentFieldName)) {
            consumer.accept(parser.namedObject(objectClass, "lterms", currentFieldName));
        } else {
            throw new ParsingException(parser.getTokenLocation(), "Failed to parse object: empty key", new Object[0]);
        }
    }
}
