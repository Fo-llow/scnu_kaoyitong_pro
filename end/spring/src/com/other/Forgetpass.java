package com.other;

//数据库相关的库

import com.dao.Dao;
import com.logic.LoginCheck;
import com.security.EncryUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;


public class Forgetpass {
    private static String passkey = "ogovZLSHVbFoHVgin7qy2A==";
    //根据用户名找到用户名和邮箱（返回的字符第一个是准考证号和邮箱）
    public static String FindTicketAndEmail(String username) {
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            //select ticket,email from user_information where sfzh = '"+idc+"';
            PreparedStatement p = conn.prepareStatement("select email from user_information where ticket=?;");       //根据身份证查询准考证号（用户名）和邮箱
            p.setString(1, EncryUtils.JiaInfoEncry(username));
            ResultSet rs = p.executeQuery();
            String res;
            if (rs.next()) {
                String email = EncryUtils.JieInfoDecry(rs.getString("email"));
                System.out.println("邮箱是"+ email);
                res = email;
                Dao.close(rs, p, conn);
                return res;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //发送邮件(验证码和发送的邮箱)
    public static boolean SendEmail(String codeNum, String tto) {
        String ttitle = "密码重置——华南师范大学计算机学院本科教学信息管理系统";
        String tcontent = "密码重置的验证码为：" + codeNum + "。如果不是本人操作，请忽略此邮件。";
        return SendEmail.SendEmailAll(tto,ttitle,tcontent);            //发送邮件
    }

    //修改密码（没旧密码，忘记密码）
    public static boolean ChangePass(String username, String newpassword) {
        System.out.println(username+" "+newpassword);
        //System.out.println("adsa ");
        try {
            //System.out.println("adsaas ");
            Connection conn = Dao.getConnection();              //连接数据库
            //select ticket,email from user_information where sfzh = '"+idc+"';
            //更改密码
            PreparedStatement p = conn.prepareStatement("update user_data set password=? where username=?;");
            p.setString(1, EncryUtils.PassEncry(newpassword));
            p.setString(2, EncryUtils.JiaInfoEncry(username));
            p.executeUpdate();            //执行SQL语句(注意这里是execute，因为进行的是修改操作)
            //System.out.println("adsaas "+p);
            Dao.close(p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(username +" "+newpassword);
        String res2 = LoginCheck.check(username,newpassword);
        if("".equals(res2) ||  res2 == null ) {           //查看密码是否修改成功
            System.out.println("密码修改失败");
            return false;
        }else{
            System.out.println("密码修改成功");
            return true;
        }
    }



    //生成邮箱验证码
    public static String newCodeNum(){
        //生成6位验证码(数字，字母)[https://blog.csdn.net/zml_2015/article/details/77235512]
        String codeNum = "";
        codeNum = "";
        int [] code = new int[3];
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int num = random.nextInt(10) + 48;
            int uppercase = random.nextInt(26) + 65;
            int lowercase = random.nextInt(26) + 97;
            code[0] = num;
            code[1] = uppercase;
            code[2] = lowercase;
            codeNum+=(char)code[random.nextInt(3)];
        }
        return codeNum;
    }

    //修改密码（有旧密码，修改密码）
    public static boolean ModifyPass(String username, String oldpassword,String newpassword) {
        String res = LoginCheck.check(username,oldpassword);            //检查旧密码

        if("".equals(res) ||  res == null ) {           //查看密码是否修改成功
            System.out.println("旧密码错误");
            return false;
        }else{
            boolean res2 = ChangePass(username,newpassword);
            if( res2 == true ){
                System.out.println("密码修改成功");
                return true;
            }else{
                System.out.println("密码修改失败");
                return false;
            }
        }

    }



}
