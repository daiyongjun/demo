package cn.yanwei.study.elastic.search.query.api;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/1/14 14:49
 */
public class StringUtil {
    public static String messageString(String message) {
        message = message.replaceAll("\"","'");
        return message.replaceAll("\n| ", "");
    }
}
