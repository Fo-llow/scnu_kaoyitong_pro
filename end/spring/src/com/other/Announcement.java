package com.other;


import com.dao.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import com.other.SendEmail;

//发布公告
public class Announcement {
    //create table announcement(title char(100),time char(100),author char(100),body longtext,isemail int);
    //最后一个1表示发送邮箱，0表示没发送邮箱
    //发布公告
    //最后一个参数表示是否要发送邮箱
    public static boolean setAnnounce(String title,String time,String author,String body,boolean isemail,String filename){
        try {
            //数据库操作
            Connection conn = Dao.getConnection();
            PreparedStatement p = conn.prepareStatement("insert into announcement(title, time, author, body, isemail, filename,id) VALUES(?,?,?,?,?,?,null);");
            //对占位符进行补充
            p.setString(1,title);
            p.setString(2,time);
            p.setString(3,author);
            p.setString(4,body);

            if( isemail == true )
                p.setInt(5,1);
            else
                p.setInt(5,0);
            p.setString(6,filename);
            System.out.println(p);
            p.executeUpdate();
            if( isemail == true ){          //要发送邮箱
                //给每个人都发送公告
                List emailpeople = SendEmail.FindEmailAll();
                for(int i = 0 ; i < emailpeople.size() ; i++ ) {
                    try{
                        String tto = (String) emailpeople.get(i);
                        SendEmail.SendEmailAll(tto,title,body+"\n"+author+"\n"+time);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("公告发布成功");
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("公告发布失败");
        }
        return false;
    }


    //删除公告(根据公告标题和时间来删除)
    public static boolean deleteAnnounce(String title,String time){
        try {
            //数据库操作
            Connection conn = Dao.getConnection();
            PreparedStatement p = conn.prepareStatement("delete from announcement where title=? and time=?;");
            //对占位符进行补充
            p.setString(1,title);
            p.setString(2,time);
            //后期还要考虑删除附件里面的
            p.executeUpdate();
            System.out.println("删除公告成功");
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除公告失败");
        }
        return false;
    }

    /*
    //修改公告(根据公告标题和时间来删除)
    public static boolean modifyAnnounce(String title,String time){
        try {
            //数据库操作
            Connection conn = Dao.getConnection();
            //update user_data set password='159' where username='1140777300'
            PreparedStatement p = conn.prepareStatement("update announcement set  where title=? and time=?;");
            //对占位符进行补充
            p.setString(1,title);
            p.setString(2,time);
            p.executeUpdate();
            System.out.println("删除公告成功");
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除公告失败");
        }
        return false;
    }
     */

    //显示全部公告
    /*
    SELECT * FROM table LIMIT [offset,] rows | rows OFFSET offset
    这是两个参数，第一个是偏移量，第二个是数目
    select * from employee limit 3, 7; // 返回4-11行
    select * from employee limit 3,1; // 返回第4行

    * */
    //第一个参数是第几行（从0开始），第二个参数是偏移量
    //title char(100),time char(100),author char(100),body longtext,isemail
    // 0 表示无 ， 1 表示 有

    //查询公告数量
    public static int announceNum(){
        int res = 0;
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select count(*) from announcement;");       //查看公告的数量
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                res = rs.getInt(1);
                Dao.close(rs, p, conn);
                return res;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static List showAnnounce(){
        //int rows,int offset
        List list =  new ArrayList();
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            /*PreparedStatement p = conn.prepareStatement("select * from announcement limit ?,?;");       //查询
            p.setInt(1, rows);
            p.setInt(2, offset);*/
            PreparedStatement p = conn.prepareStatement("select * from announcement order by id DESC;");
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                Map<String,String> data = new HashMap();
                data.put("title",rs.getString("title"));
                data.put("time",rs.getString("time"));
                data.put("author",rs.getString("author"));
                data.put("body",rs.getString("body"));
                data.put("file",rs.getString("filename"));
                //整型转字符
                if( rs.getInt("isemail") == 1  ){
                    data.put("isemail","是");
                }else{
                    data.put("isemail","否");
                }
                list.add(data);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return list;
    }

    //显示部分公告
    /*
    SELECT * FROM table LIMIT [offset,] rows | rows OFFSET offset
    这是两个参数，第一个是偏移量，第二个是数目
    select * from employee limit 3, 7; // 返回4-11行
    select * from employee limit 3,1; // 返回第4行

    * */
    //第一个参数是第几行（从0开始），第二个参数是偏移量
    //title char(100),time char(100),author char(100),body longtext,isemail
    // 0 表示无 ， 1 表示 有
    public static List showAnnounce(int page,int limit){
        //int rows,int offset
        List list =  new ArrayList();
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            /*PreparedStatement p = conn.prepareStatement("select * from announcement limit ?,?;");       //查询
            p.setInt(1, rows);
            p.setInt(2, offset);*/
            PreparedStatement p = conn.prepareStatement("select * from announcement order by id DESC limit ?,?;");

            p.setInt(1,(page -1) * limit);
            p.setInt(2, limit);


            ResultSet rs = p.executeQuery();
            while(rs.next()){
                Map<String,String> data = new HashMap();
                data.put("title",rs.getString("title"));
                data.put("time",rs.getString("time"));
                data.put("author",rs.getString("author"));
                data.put("body",rs.getString("body"));
                data.put("file",rs.getString("filename"));
                //整型转字符
                if( rs.getInt("isemail") == 1  ){
                    data.put("isemail","是");
                }else{
                    data.put("isemail","否");
                }
                list.add(data);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return list;
    }

    //显示查找的部分公告
    /*
    SELECT * FROM table LIMIT [offset,] rows | rows OFFSET offset
    这是两个参数，第一个是偏移量，第二个是数目
    select * from employee limit 3, 7; // 返回4-11行
    select * from employee limit 3,1; // 返回第4行

    * */
    //第一个参数是第几行（从0开始），第二个参数是偏移量
    //title char(100),time char(100),author char(100),body longtext,isemail
    // 0 表示无 ， 1 表示 有
    public static List searchAnnounce(int page,int limit,String find){
        //int rows,int offset
        List list =  new ArrayList();
        String sql = "select * from announcement where title like '%"+find+"%' or body like '%"+find+"%' order by id DESC limit ?,?;";
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            /*PreparedStatement p = conn.prepareStatement("select * from announcement limit ?,?;");       //查询
            p.setInt(1, rows);
            p.setInt(2, offset);*/
            PreparedStatement p = conn.prepareStatement(sql);
            p.setInt(1,(page -1) * limit);
            p.setInt(2, limit);

            ResultSet rs = p.executeQuery();
            while(rs.next()){
                Map<String,String> data = new HashMap();
                data.put("title",rs.getString("title"));
                data.put("time",rs.getString("time"));
                data.put("author",rs.getString("author"));
                data.put("body",rs.getString("body"));
                data.put("file",rs.getString("filename"));
                //整型转字符
                if( rs.getInt("isemail") == 1  ){
                    data.put("isemail","是");
                }else{
                    data.put("isemail","否");
                }
                list.add(data);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return list;
    }

    public static int searchAnnounceNum(String find){
        //int rows,int offset
        List list =  new ArrayList();
        int res = 0;
        String sql = "select count(*) from announcement where title like '%"+find+"%' or body like '%"+find+"%';";
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement(sql);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                res = rs.getInt(1);
                Dao.close(rs, p, conn);
                return res;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return res;
    }

    //获取指定公告
    //title char(100),time char(100),author char(100),body longtext,isemail
    // 0 表示无 ， 1 表示 有
    public static Map<String,String> getAnnounce(String title,String time){
        //int rows,int offset
        List list =  new ArrayList();
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from announcement where title=? and time=?;");
            p.setString(1,title);
            p.setString(2,time);
            ResultSet rs = p.executeQuery();
            Map<String,String> data = new HashMap();
            if(rs.next()){
                data.put("title",rs.getString("title"));
                data.put("time",rs.getString("time"));
                data.put("author",rs.getString("author"));
                data.put("body",rs.getString("body"));
                data.put("file",rs.getString("filename"));

                //整型转字符
                if( rs.getInt("isemail") == 1  ){
                    data.put("isemail","是");
                }else{
                    data.put("isemail","否");
                }
            }
            Dao.close(rs, p, conn);
            return data;
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return null;
    }

}
