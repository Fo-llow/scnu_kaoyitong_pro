package com.dao;


import java.sql.*;

public class Dao {
    // 获取数据库连接
    public static Connection getConnection(){
        Connection conn = null;
            String url = "jdbc:mysql://8.134.142.145:3306/ugcourse";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Class.forName("com.mysql.jdbc.Driver");
            //数据库名字和密码在这里改！！！！
            conn = DriverManager.getConnection(url, "root", "@Lcj13286482015");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("数据库驱动加载出错");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("数据库出错");
        }
        return conn;
    }
    //关闭相关通道
    public static void close(ResultSet rs,PreparedStatement p,Connection conn)
    {

        try
        {
            if(!rs.isClosed()){
                rs.close();
            }
            if(!p.isClosed()){
                p.close();
            }
            if(!conn.isClosed()){
                conn.close();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("数据关闭出错");
        }
    }

    //关闭相关通道
    public static void close(PreparedStatement p,Connection conn)
    {
        try
        {
            if(!p.isClosed()){
                p.close();
            }
            if(!conn.isClosed()){
                conn.close();
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("数据关闭出错");
        }
    }

}
