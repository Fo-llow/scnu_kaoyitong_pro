package com.admin;

import com.dao.Dao;
import com.logic.LoginCheck;
import com.logic.RegistCheck;
import com.security.EncryUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

//设置管理员权限
public class SetAdmin {
    //添加管理员
    //create table admin_data(account char(20),password char(20),permission int,note char(50));				# 管理员
    public static boolean addAdmin(String nowUsername,String nowPassword,String newUsername,String newPassword,int permission,String note){
        if( isSuper(nowUsername,nowPassword) ){             //权限或者密码
            //添加管理员
            return RegistCheck.adminRegistCheck(newUsername,newPassword,permission,note);
        }else{
            return false;
        }
    }

    //获取操作权限等级（1级为最高，可以添加用户）
    public static int getPermission(String user){
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select permission from admin_data where account=? ;");       //查询
            p.setString(1, EncryUtils.JiaInfoEncry(user));
            ResultSet rs = p.executeQuery();
            //timestamp
            if (rs.next()) {
                int permission = rs.getInt("permission");
                return permission;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return -1;          //负数表示获取失败
    }

    //删除管理员
    public static boolean delAdmin(String nowUsername,String user){
        if( getPermission(nowUsername) == 1 ){
            //删除管理员
            try {
                Connection conn = Dao.getConnection();              //连接数据库
                PreparedStatement p = conn.prepareStatement("delete  from admin_data where account=? ;");
                p.setString(1, EncryUtils.JiaInfoEncry(user));
                p.executeUpdate();
                Dao.close(p, conn);
                return true;
            } catch (Exception e) {
                System.out.println("异常");
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }

    //编辑管理员


    //显示管理员
    public static List showAdmin (String user){
        List list = new ArrayList<>();
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            //再一次核对权限
            int per = getPermission(user);
            PreparedStatement p = null;
            if( per != 1 ){             //只显示自己
                p = conn.prepareStatement("select * from admin_data where account=?;");       //查询
                p.setString(1, EncryUtils.JiaInfoEncry(user));
            }else{
                p = conn.prepareStatement("select * from admin_data;");       //查询
            }

            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                Map<String,String> data = new HashMap<>();
                data.put("account",EncryUtils.JieInfoDecry(rs.getString("account")));
                if( rs.getInt("permission") == 1 ){
                    data.put("permission","超级管理员");
                }else{
                    data.put("permission","普通管理员");
                }
                data.put("note",EncryUtils.JieInfoDecry(rs.getString("note")));
                list.add(data);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("异常");
            e.printStackTrace();
        }
        return list;

    }

    //超级管理员（删除，编辑，显示）
    public static boolean isSuper(String nowUsername,String nowPassword){
        String user =  LoginCheck.admincheck(nowUsername,nowPassword);             //验证当前操作员的密码
        if (user != null && !("".equals(user))) {
            int oldpermission = getPermission(nowUsername);
            if( oldpermission != 1 ){
                return false;           //权限不足
            }else{
                return true;
            }
        }else{
            return false;           //密码错误
        }
    }

    //超级管理员（删除，编辑，显示）
    public static boolean isSuper(String nowUsername) {
        int oldpermission = getPermission(nowUsername);
        if (oldpermission != 1) {
            return false;           //权限不足
        } else {
            return true;
        }
    }
}
