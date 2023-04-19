package com.logic;

import com.dao.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//系统开放时间
public class OpenTime {
    // 设置系统开放时间
    // DATETIME
    // create table open_time(begin timestamp,end timestamp);
    public static boolean setOpenTime(String begin,String end){
        try {
            //从前端或者自己模拟一个日期格式，转为String即可
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //将字符串日期转为日期
            Date datebegin = format.parse(begin);
            Date dateend = format.parse(end);
            // 让日期时间转换为数据库中的timestamp类型
            Timestamp timebegin = new Timestamp(datebegin.getTime());
            Timestamp timeend = new Timestamp(dateend.getTime());
            //数据库操作
            Connection conn = Dao.getConnection();
            PreparedStatement p = conn.prepareStatement("insert into open_time VALUES(?,?);");
            //对占位符进行补充
            p.setTimestamp(1,timebegin);
            p.setTimestamp(2,timeend);
            p.executeUpdate();
            System.out.println("开放时间设置成功");
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("开放时间设置失败");
        }
        return false;
    }

    // 修改系统开放时间
    public static boolean modifyOpenTime(String begin,String end){
        boolean a = deleteOpenTime();           //清空设置的开放时间
        boolean b = setOpenTime(begin,end);              //重新设置开放时间
        return a && b;
    }

    //判断当前时间是否在系统开放时间内
    public static boolean OpenTime(){
        Date begin ;
        Date end;
        Date now = new Date();
        Timestamp timebegin,timeend;
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from open_time;");       //查询
            //p.setString(1, username);
            ResultSet rs = p.executeQuery();
            //timestamp
            if (rs.next()) {
                timebegin = rs.getTimestamp("begin");
                timeend =  rs.getTimestamp("end");
                //Timestamp格式转化为Date
                begin = new Date(timebegin.getTime());
                end =  new Date(timeend.getTime());
                //若当调用此方法的Date对象在指定日期之后返回true,否则返回false。
                if( now.after(begin) && now.before(end) ){
                    return true;
                }else{
                    return false;
                }
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return true;            //这里是true表示没设置时间
    }

    //返回开放时间
    public static Map<String,String> getOpenTime() {
        Date begin;
        Date end;
        Date now = new Date();
        Timestamp timebegin, timeend;
        Map<String,String> data = new HashMap<>();
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from open_time;");       //查询
            //p.setString(1, username);
            ResultSet rs = p.executeQuery();
            //timestamp
            if (rs.next()) {
                timebegin = rs.getTimestamp("begin");
                timeend = rs.getTimestamp("end");
                //Timestamp格式转化为Date
                begin = new Date(timebegin.getTime());
                end = new Date(timeend.getTime());
                //转为字符串
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                data.put("begin",format.format(begin));
                data.put("end",format.format(end));
                return data;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return null;
    }


    //清空开放时间
    public static boolean deleteOpenTime(){
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            // 删除表中的所有数据
            PreparedStatement p = conn.prepareStatement("truncate table open_time;");       //查询
            //p.setString(1, username);
            int rs = p.executeUpdate();
            Dao.close( p, conn);
            return true;
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return false;            //这里是true表示没设置时间
    }

    //获取当前时间

}
