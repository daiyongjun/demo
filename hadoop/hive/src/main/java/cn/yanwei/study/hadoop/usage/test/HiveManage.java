package cn.yanwei.study.hadoop.usage.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HiveManage {

    private static final String URLHIVE = "jdbc:hive2://103.46.128.49:44922/hive";


    private static Connection connection = null;


    public static Connection getHiveConnection() {
        if (null == connection) {
            synchronized (HiveManage.class) {
                if (null == connection) {
                    try {
                        Class.forName("org.apache.hive.jdbc.HiveDriver");
                        connection = DriverManager.getConnection(URLHIVE, "daiyongjun", "daiyongjun");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }


    public static void main(String args[]) throws SQLException{
        try{
            String sql1="select * from news_29822 limit 10";
            PreparedStatement pstm = getHiveConnection().prepareStatement(sql1);
            ResultSet rs= pstm.executeQuery(sql1);
            List<String> rest = new ArrayList();
            while (rs.next()) {
                rest.add(rs.getString(1)+"\t"+rs.getString(2));
            }
            for(String res : rest){
                System.out.println(res);
            }
            pstm.close();
            rs.close();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


}