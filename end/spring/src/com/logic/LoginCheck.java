//登陆验证
package com.logic;

import com.dao.Dao;
import com.security.EncryUtils;
import com.security.Encryption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginCheck {

    //private static String passkey = "ogovZLSHVbFoHVgin7qy2A==";         //密钥
    //检测学生账户密码是否正确，正确返回的是账户名，错误返回空字符
    public static String check(String username,String password) {
        System.out.println("正在监测学生账户是否正常");
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from user_data where username=? and password=?;");       //查询
            //System.out.println(p);
            p.setString(1, EncryUtils.JiaInfoEncry(username));
            p.setString(2, EncryUtils.PassEncry(password));
            System.out.println(p);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                String user_name = EncryUtils.JieInfoDecry(rs.getString("username"));
                Dao.close(rs, p, conn);
                return user_name;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    //检测是否有该学生账户,有则返回真，没有则返回假
    public static boolean checkUser(String username) {
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from user_data where username=?;");       //查询
            p.setString(1, EncryUtils.JiaInfoEncry(username));
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                //String user_name = rs.getString("username");
                Dao.close(rs, p, conn);
                return true;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    //create table admin_data(account char(20),password char(20),permission int);
    //insert into admin_data VALUES("admin","admin");     #所以管理员默认账户密码都是admin
    //检测管理员账户密码是否正确，正确返回的是账户名，错误返回空字符

    //检测是否有该管理员账户,有则返回真，没有则返回假
    public static boolean adminCheckUser(String username) {
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from admin_data where account=?;");       //查询
            p.setString(1, EncryUtils.JiaInfoEncry(username));
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                //String user_name = rs.getString("account");
                Dao.close(rs, p, conn);
                return true;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static String admincheck(String username,String password) {
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from admin_data where  account=? and password=?;");       //查询
            p.setString(1, EncryUtils.JiaInfoEncry(username));
            p.setString(2, EncryUtils.PassEncry(password));
            System.out.println(p);
            ResultSet rs = p.executeQuery();
            System.out.println("正在检测管理员账户密码是否正常~");
            if(rs.next()){
                String user_name = EncryUtils.JieInfoDecry(rs.getString("account"));
                int permission = rs.getInt("permission");       //整形不加密
                System.out.println("管理员账户密码正常！");
                Dao.close(rs, p, conn);
                return user_name;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    //返回名字
    public static String[] FindNameAndEmail(String username) {
        String[] tmp = new String[2];
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select name,email from user_information where ticket=?;");       //查询
            p.setString(1, EncryUtils.JiaInfoEncry(username));
            ResultSet rs = p.executeQuery();
            if (rs.next()) {

                tmp[0] = EncryUtils.JieInfoDecry(rs.getString("name"));
                tmp[1] = EncryUtils.JieInfoDecry(rs.getString("email"));
                Dao.close(rs, p, conn);
                return tmp;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }

        return null;
    }

}