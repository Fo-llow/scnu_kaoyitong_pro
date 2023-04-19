package com.security;

import com.dao.Dao;
import com.other.SendEmail;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

//日志
//什么时候 什么地点 什么人 做什么事  类型
//【2022/4/1 9:18:36 | 127.0.0.1】—— 用户名为 orall  登录 【用户】
//【2022/4/1 9:18:36 | 127.0.0.1】—— 用户名为 orall  登录 【管理员】
//create table data_log(time timestamp,ip char(100),username char(100),thing char(250),type char(50));
public class Log {
    //设置日志(Timestamp)
    public static boolean setLog(Timestamp time, String ip, String username, String thing, String type){
        try {
            //数据库操作
            Connection conn = Dao.getConnection();
            PreparedStatement p = conn.prepareStatement("insert into data_log VALUES(?,?,?,?,?);");
            //对占位符进行补充
            p.setTimestamp(1,time);
            p.setString(2,ip);
            p.setString(3,username);
            p.setString(4,thing);
            p.setString(5,type);
            p.executeUpdate();

            System.out.println("日志登记成功");
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("日志登记失败");
        }
        return false;
    }

    //设置日志(Date)
    public static boolean setLog(Date time, String ip, String username, String thing, String type){
        try {
            //数据库操作
            Connection conn = Dao.getConnection();
            PreparedStatement p = conn.prepareStatement("insert into data_log VALUES(?,?,?,?,?);");
            //对占位符进行补充
            // 让日期时间转换为数据库中的timestamp类型
            Timestamp ttime = new Timestamp(time.getTime());
            p.setTimestamp(1,ttime);
            p.setString(2,ip);
            p.setString(3,username);
            p.setString(4,thing);
            p.setString(5,type);
            p.executeUpdate();

            System.out.println("日志登记成功");
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("日志登记失败");
        }
        return false;
    }

    //显示历史操作
    public static List showLog(){
        List list =  new ArrayList();
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            /*PreparedStatement p = conn.prepareStatement("select * from announcement limit ?,?;");       //查询
            p.setInt(1, rows);
            p.setInt(2, offset);*/
            PreparedStatement p = conn.prepareStatement("select * from data_log order by time DESC;");
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                Timestamp time = rs.getTimestamp("time");
                //Timestamp格式转化为Date
                Date dtime = new Date(time.getTime());
                //转为字符串
                ////create table data_log(time timestamp,ip char(100),username char(100),thing char(250),type char(50));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                //String tmp = " 112.174.9.124 - 【用户：orall】 于 2021-04-03 20:13:36 登录系统";
                String tmp = " "+rs.getString("ip")+" - 【"+rs.getString("type")+"："+ rs.getString("username") +"】 于 "+format.format(time)+" "+rs.getString("thing");
                list.add(tmp);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return list;
    }

    //获取IP地址
    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-real-ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
