package cn.yanwei.study.hadoop.usage.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 类的描述
 *
 * @author daiyongjun
 * @version 1.0
 * Created on date: 2020/8/18 14:56
 */
public class HiveLoadData {
    public static void main(String[] args) throws SQLException {
        //读取文件



        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection conn = DriverManager.getConnection("jdbc:hive2://103.46.128.49:44922/hive", "daiyongjun", "daiyongjun");
        Statement stmt = conn.createStatement();
        String databaseName = "hive";



//        stmt.execute("CREATE DATABASE " + databaseName);
//        System.out.println("Database " + databaseName + " created successfully.");
        stmt.execute("USE " + databaseName);
        //时间	标题	抓取来源	情感类型
        String tableName = "ceshi";
//        stmt.executeQuery("drop talbe ceshi");
        String sql = "create table IF NOT EXISTS ceshi (time string,title string,source string,type string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' STORED AS TEXTFILE";
        stmt.execute(sql);
        String filepath = "file:/C:/Users/Administrator/Desktop/1.txt";
        String loadQuery = "load data local inpath '" + filepath + "' into table " + tableName;
        stmt.execute(loadQuery);
        System.out.println(" Running " + loadQuery);
        System.out.println(String.format("Load Data into %s successful", tableName));
        //res.close();
        stmt.close();
        conn.close();
    }
}
