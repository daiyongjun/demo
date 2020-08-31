package cn.yanwei.study.hadoop.usage.util;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
public class HiveConnectUtil {
    private static final String DriverName = "org.apache.hive.jdbc.HiveDriver";
    private static final String URL = "jdbc:hive2://103.46.128.49:44922/hive";  //后面的是数据库
    private static final String USERNAme = "daiyongjun";
    private static final String PASSWORD = "daiyongjun";
    private static Connection connection;

    public Connection getConnect() throws Exception {
        Class.forName(DriverName);
        connection = DriverManager.getConnection(URL,USERNAme,PASSWORD);
        return  connection;
    }

    //提供一个连接其它数据源的构造方法 备用
    public static Connection getConnect(String DriverName,String URL,String USERNAME,String PASSWORD) throws Exception {
        Class.forName(DriverName);
        connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        return connection;
    }

    public void closeConnect() throws SQLException {
        if (connection != null){
            connection.close();
        }

    }

    //获取表名的备注  metaData.getTables()
    public String getTableRemark(Connection connect,String tablename) throws SQLException {
        String remarks = null;
        //获取元数据
        DatabaseMetaData metaData = connect.getMetaData();
        //获取表结构元数据
        ResultSet tables = metaData.getTables(null, null, tablename, new String[]{"TABLE"});

        while (tables.next()){
            remarks = tables.getString("REMARKS");
        }
        return remarks;
    }

    //获取表的字段与字段备注  metaData.getColumns()
    public Map<String,String> getTableColumn(Connection connect,String tablename) throws SQLException {
        LinkedHashMap<String, String> map = new LinkedHashMap<String,String>();
        DatabaseMetaData metaData = connect.getMetaData();
        //
        ResultSet columns = metaData.getColumns(null, "%", tablename, "%");
        while (columns.next()){
            String column = columns.getString("COLUMN_NAME");
            String remarks = columns.getString("REMARKS");
            remarks = remarks == null? column: remarks;
            //让查询出来的列与列的备注进行关联
            map.put(column,remarks);
        }
        return map;
    }

    //提供一个查询表数据的方法
    public ResultSet queryTable(Connection connect,String sql) throws SQLException {
        Statement statement = connect.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet;
    }

}
