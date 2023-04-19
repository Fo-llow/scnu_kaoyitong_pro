package com.logic;

import com.dao.Dao;
import com.security.EncryUtils;
import com.security.Encryption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class PersonInfo {
    //返回个人信息
    //private static String passkey = "ogovZLSHVbFoHVgin7qy2A==";
    public static Map<String,String> showInfo(String username){
        Map<String,String> data = new HashMap<>();
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from user_information where ticket=?;");       //查询
            p.setString(1, EncryUtils.JiaInfoEncry(username));
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                data.put("ticket",EncryUtils.JieInfoDecry(rs.getString("ticket")));
                data.put("tele",EncryUtils.JieInfoDecry(rs.getString("tele")));
                data.put("email",EncryUtils.JieInfoDecry(rs.getString("email")));
                data.put("name",EncryUtils.JieInfoDecry(rs.getString("name")));
                data.put("sex",EncryUtils.JieInfoDecry(rs.getString("sex")));
                data.put("sfzh",EncryUtils.JieInfoDecry(rs.getString("sfzh")));
                Dao.close(rs, p, conn);
                return data;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //修改个人信息(用户名不可修改)
    public static boolean modifyInfo(String oldusername,String tele,String email,String sex,String sfzh,String name){
        try {
            //数据库操作
            Connection conn = Dao.getConnection();
            //update user_data set password='159' where username='1140777300'
            PreparedStatement p = conn.prepareStatement("update user_information set name=?,tele=?,email=?,sex=?,sfzh=? where ticket=?;");
            //对占位符进行补充

            p.setString(1,EncryUtils.JiaInfoEncry(name));
            p.setString(2,EncryUtils.JiaInfoEncry(tele));
            p.setString(3, EncryUtils.JiaInfoEncry(email));
            p.setString(4,EncryUtils.JiaInfoEncry(sex));
            p.setString(5,EncryUtils.JiaInfoEncry(sfzh));
            p.setString(6,EncryUtils.JiaInfoEncry(oldusername));
            p.executeUpdate();
            System.out.println("修改个人信息成功");
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("修改个人信息失败");
        }
        return false;
    }
}
