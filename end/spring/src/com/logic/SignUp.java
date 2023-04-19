package com.logic;

import com.dao.Dao;
import com.security.EncryUtils;
import com.security.Encryption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


// 由于加密后模糊搜索尚未解决，所以这里姓名，身份证号，准考证号，电子邮箱，手机号码暂时用简单的加密
public class SignUp {
    //create table user_signup(name char(20),sex char(20),sfzh char(20),ticket char(20),tele char(20),email char(20),zymc char(20),zycc char(20),kcdm char(150),file longtext,res int);
    //最后res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）

    //进行报名
    public static boolean Signup(String name, String sex, String sfzh, String ticket, String tele, String email, String zymc, String zycc, String kcdm,String filelocation) {
        //ticket就是准考证号，准考证号就是登录名
        int signup_res = SignUp.check(ticket);           //已经报名的就不能再报名
        if (signup_res == 2) {          //如果未报名
            try {
                Connection conn = Dao.getConnection();
                /* 对报名的数据库进行补充 */
                PreparedStatement p2 = conn.prepareStatement("insert into user_signup(name,sex,sfzh,ticket,tele,email,zymc,zycc,kcdm,file,res) VALUES (?,?,?,?,?,?,?,?,?,?,0);");
                //对占位符进行补充
                p2.setString(1, EncryUtils.JiaFindEncry(name));
                p2.setString(2, EncryUtils.JiaInfoEncry(sex));
                p2.setString(3, EncryUtils.JiaFindEncry(sfzh));
                p2.setString(4, EncryUtils.JiaFindEncry(ticket));
                p2.setString(5, EncryUtils.JiaFindEncry(tele));
                p2.setString(6, EncryUtils.JiaFindEncry(email));
                p2.setString(7, EncryUtils.JiaInfoEncry(zymc));
                p2.setString(8, EncryUtils.JiaInfoEncry(zycc));
                p2.setString(9, EncryUtils.JiaInfoEncry(kcdm));
                p2.setString(10, EncryUtils.JiaInfoEncry(filelocation));
                //  p2.setString(11, res);
                p2.executeUpdate();
                System.out.println("报名成功");
                Dao.close(p2, conn);
                return true;
            } catch (Exception e) {
                System.out.println("报名失败");
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("该账户已经报名过，请耐心等待结果，不要重复报名！");
            return false;
        }

    }

    //修改报名信息
    public static boolean modifySignup(String name, String sex, String sfzh, String ticket, String tele, String email, String zymc, String zycc, String kcdm,String filelocation) {
        //ticket就是准考证号，准考证号就是登录名
        int signup_res = SignUp.check(ticket);           //已经报名的就不能再报名
        if (signup_res == 0) {          //已经报名且未审核
            try {
                Connection conn = Dao.getConnection();
                /* 对报名的数据库进行补充 */
                //PreparedStatement p = conn.prepareStatement("update course_code set name=?,code=? where name=? and code=?;");
                PreparedStatement p2 = conn.prepareStatement("update user_signup set name=?,sex=?,sfzh=?,tele=?,email=?,zymc=?,zycc=?,kcdm=?,file=? where ticket=?;");
                //对占位符进行补充
                p2.setString(1, EncryUtils.JiaFindEncry(name));
                p2.setString(2, EncryUtils.JiaInfoEncry(sex));
                p2.setString(3, EncryUtils.JiaFindEncry(sfzh));
                p2.setString(4, EncryUtils.JiaFindEncry(tele));
                p2.setString(5, EncryUtils.JiaFindEncry(email));
                p2.setString(6, EncryUtils.JiaInfoEncry(zymc));
                p2.setString(7, EncryUtils.JiaInfoEncry(zycc));
                p2.setString(8, EncryUtils.JiaInfoEncry(kcdm));
                p2.setString(9, EncryUtils.JiaInfoEncry(filelocation));
                p2.setString(10, EncryUtils.JiaFindEncry(ticket));

                p2.executeUpdate();
                System.out.println("修改报名信息成功");
                Dao.close(p2, conn);
                return true;
            } catch (Exception e) {
                System.out.println("修改报名信息失败");
                e.printStackTrace();
                return false;
            }
        } else {
            System.out.println("该账户未报名或者已经审核！");
            return false;
        }
    }

    //根据学号返回报名结果
    public static int check(String ticket){
        int tmp_res = 2;        //2表示没开始报名
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select res from user_signup where ticket=?;");       //查询
            p.setString(1, EncryUtils.JiaFindEncry(ticket));
            System.out.println(p);
            ResultSet rs = p.executeQuery();
            System.out.println(rs);
            if (rs.next()) {
                System.out.println("查找报名结果成功");
                tmp_res = rs.getInt("res");
                return tmp_res;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("查找报名结果异常");
            e.printStackTrace();
        }
        return tmp_res;
    }

    //删除报名信息
    public static boolean deleteSignUp(String ticket){
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("delete from user_signup where ticket=?;");       //查询
            p.setString(1, EncryUtils.JiaFindEncry(ticket));
            System.out.println(p);
            p.executeUpdate();
            System.out.println("删除报名信息成功");
            Dao.close( p, conn);
            return true;
        } catch (Exception e) {
            System.out.println("删除报名信息失败");
            e.printStackTrace();
        }
        return false;
    }

    //返回报名信息
    //name,sex,sfzh,ticket,tele,email,zymc,zycc,kcdm,res int);
    public static String[] SignupInfo(String ticket){
        String[] tmp = new String[11];
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from user_signup where ticket=?;");       //查询
            p.setString(1, EncryUtils.JiaFindEncry(ticket));
            ResultSet rs = p.executeQuery();
            //System.out.println("正在查询~");
            if (rs.next()) {
                tmp[0] = EncryUtils.JieFindDecry(rs.getString("name"));
                tmp[1] = EncryUtils.JieInfoDecry(rs.getString("sex"));
                tmp[2] = EncryUtils.JieFindDecry(rs.getString("sfzh"));
                tmp[3] = EncryUtils.JieFindDecry(rs.getString("ticket"));
                tmp[4] = EncryUtils.JieFindDecry(rs.getString("tele"));
                tmp[5] = EncryUtils.JieFindDecry(rs.getString("email"));
                tmp[6] = EncryUtils.JieInfoDecry(rs.getString("zymc"));
                tmp[7] = EncryUtils.JieInfoDecry(rs.getString("zycc"));
                tmp[8] = EncryUtils.JieInfoDecry(rs.getString("kcdm"));

                tmp[9] = EncryUtils.JieInfoDecry(rs.getString("file"));
                tmp[10] = rs.getInt("res") + "";            //int转为String
                System.out.println("查询到的结果是"+tmp[10]);

                Dao.close(rs, p, conn);
                return tmp;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            System.out.println("查询异常");
            e.printStackTrace();
        }

        return null;

    }
}