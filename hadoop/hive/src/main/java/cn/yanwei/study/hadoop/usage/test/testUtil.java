package cn.yanwei.study.hadoop.usage.test;

import cn.yanwei.study.hadoop.usage.util.ExportExcelUtil;
import cn.yanwei.study.hadoop.usage.util.HiveConnectUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/8/18 14:37
 */
public class testUtil {
    public static void main(String[] args) {


        String tablename = "news_29822";
        String sql = "select id ,title  from news_29822 LIMIT 10";
        String descPath = "C:\\Users\\Administrator\\Desktop\\myfile.xls";
        HiveConnectUtil connectUtil = new HiveConnectUtil();

        ArrayList<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
        LinkedHashMap<String, String> dataMap = null;

        try {
            Connection connect = connectUtil.getConnect();
            Map<String, String> tableColumn = connectUtil.getTableColumn(connect, tablename);

            //使用ResultSetMetaData ,因为 DatabaseMetaData会查询出隐藏的字段
            ResultSet resultSet = connectUtil.queryTable(connect, sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int length = metaData.getColumnCount();     //实际可见的字段列数

            while (resultSet.next()) {
                dataMap = new LinkedHashMap<String, String>();
                for (int i = 1; i < length + 1; i++) {   //字段名和值关联
                    String columnName = metaData.getColumnName(i);
                    int index = columnName.indexOf(".");
                    String substring = columnName.substring(index + 1);
                    System.out.println(tableColumn.get(substring) + ": " + resultSet.getString(i));
                    dataMap.put(tableColumn.get(substring), resultSet.getString(i));
                }
                dataList.add(dataMap);
            }
            connectUtil.closeConnect();
            //export
            ExportExcelUtil excelUtil = new ExportExcelUtil();
            excelUtil.export(dataList, descPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
