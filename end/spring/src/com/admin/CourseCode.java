package com.admin;

import com.dao.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

//课程代码
public class CourseCode {
    // create table course_code(code char(50),name char(50));
    public static boolean addCode(String code,String name){
        if( isCode(code,name) == false ){                //避免重复
            try {
                //数据库操作
                Connection conn = Dao.getConnection();
                PreparedStatement p = conn.prepareStatement("insert into course_code VALUES(?,?);");
                //对占位符进行补充
                p.setString(1,code);
                p.setString(2,name);
                p.executeUpdate();
                System.out.println("课程代码添加成功");
                Dao.close(p, conn);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("课程代码添加失败");
            }
            return false;
        }else {
            System.out.println("课程代码已经存在");
            return false;
        }
    }


    // 查看是否有该课程代码
    public static boolean isCode(String code,String name) {

        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from course_code where code=? and name=?;");       //查询
            p.setString(1, code);
            p.setString(2, name);
            ResultSet rs = p.executeQuery();
            if (rs.next()) {
                String tmp = rs.getString("name");
                Dao.close(rs, p, conn);
                return true;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 修改课程代码
    public static boolean modifyCode(String oldname,String oldcode,String newname,String newcode){
        try {
            //数据库操作
            Connection conn = Dao.getConnection();
            //update user_data set password='159' where username='1140777300'
            PreparedStatement p = conn.prepareStatement("update course_code set name=?,code=? where name=? and code=?;");
            //对占位符进行补充
            p.setString(1,newname);
            p.setString(2,newcode);
            p.setString(3,oldname);
            p.setString(4,oldcode);
            p.executeUpdate();
            System.out.println("修改课程代码成功");
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("修改课程代码失败");
        }
        return false;
    }

    // 删除课程代码
    public static boolean deleteCode(String code,String name){
        try{
            Connection conn = Dao.getConnection();              //连接数据库
            //select ticket,email from user_information where sfzh = '"+idc+"';
            //删除课程代码
            PreparedStatement p = conn.prepareStatement("delete from course_code where name=? and code=?;");
            p.setString(1, name);
            p.setString(2, code);
            p.executeUpdate();            //执行SQL语句(注意这里是execute，因为进行的是删除操作)
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // 显示所有的课程代码
    public static List showCode(){
        List list =  new ArrayList();
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from course_code");       //查看报名信息
            ResultSet rs = p.executeQuery();
            while(rs.next()){
                Map<String,String> data = new HashMap();
                data.put("code", rs.getString("code"));
                data.put("name", rs.getString("name"));
                System.out.println(data);
                list.add(data);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



}
