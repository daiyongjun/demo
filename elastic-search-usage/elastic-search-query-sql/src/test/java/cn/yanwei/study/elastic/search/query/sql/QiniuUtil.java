package cn.yanwei.study.elastic.search.query.sql;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 请求工具类
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2019/11/11 11:13
 */
public class QiniuUtil {
    /**
     * 获取服务器时间
     *
     * @return String
     */
    public static String getServerTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(calendar.getTime());
    }



    /**
     * 使用传入response构建响应
     *
     * @param response 响应内容
     * @return String
     */
    public static String customResponse(String response) {
        JSONObject ss = JSONObject.parseObject(response);
        JSONArray ja = ss.getJSONArray("responses");
        return ja.getJSONObject(0).toJSONString();
    }

}
