package com.logic;

import com.dao.Dao;
import com.security.EncryUtils;
import com.security.Encryption;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegistCheck {
    //(act,pwd,tele,email,name,sex,sfzh)
    //private static String passkey = "ogovZLSHVbFoHVgin7qy2A==";
    public static boolean registCheck(String ticket,String password,String tele,String email,String name,String sex,String sfzh){
        //ticket就是准考证号，准考证号就是登录名
        System.out.println("正在注册~");
        if(LoginCheck.checkUser(ticket) == false ){          //已经注册的就不能再注册
            try {
                Connection conn = Dao.getConnection();
                /* 对账户密码的数据库进行补充 */
                PreparedStatement p1 = conn.prepareStatement("insert into user_data(username,password) VALUES (?,?);");
                //对占位符进行补充
                System.out.println(p1);
                System.out.println(ticket+"" + password+" "+tele + " "+name  );
                p1.setString(1, EncryUtils.JiaInfoEncry(ticket));
                p1.setString(2, EncryUtils.PassEncry(password));
                p1.executeUpdate();
                System.out.println(p1);
                /* 对个人信息的数据库进行补充 */
                PreparedStatement p2 = conn.prepareStatement("insert into user_information(ticket,tele,email,name,sex,sfzh) VALUES (?,?,?,?,?,?);");
                //对占位符进行补充
                p2.setString(1, EncryUtils.JiaInfoEncry(ticket));
                p2.setString(2, EncryUtils.JiaInfoEncry(tele));
                p2.setString(3, EncryUtils.JiaInfoEncry(email));
                p2.setString(4, EncryUtils.JiaInfoEncry(name));
                p2.setString(5, EncryUtils.JiaInfoEncry(sex));
                p2.setString(6, EncryUtils.JiaInfoEncry(sfzh));
                p2.executeUpdate();
                System.out.println(p2);
                System.out.println(name);
                System.out.println("注册成功");
                Dao.close(p1, conn);
                Dao.close(p2, conn);


                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("该账户已经注册过，请不要重复注册！");
            return false;
        }
        return false;
    }

    public static boolean adminRegistCheck(String username,String password,int permission,String note){
        //ticket就是准考证号，准考证号就是登录名
        if(LoginCheck.adminCheckUser(username) == false ){          //已经注册的就不能再注册
            try {
                Connection conn = Dao.getConnection();
                /* 对账户密码的数据库进行补充 */
                PreparedStatement p1 = conn.prepareStatement("insert into admin_data(account,password,permission,note) VALUES (?,?,?,?);");
                //对占位符进行补充
                p1.setString(1, EncryUtils.JiaInfoEncry(username));
                p1.setString(2, EncryUtils.PassEncry(password));
                p1.setInt(3, permission);
                p1.setString(4,EncryUtils.JiaInfoEncry(note));
                p1.executeUpdate();
                System.out.println("管理员账号注册成功");
                Dao.close(p1, conn);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("该管理员账户已经注册过，请不要重复注册！");
            return false;
        }
        return false;
    }

}



