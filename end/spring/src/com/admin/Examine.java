//报名审核
package com.admin;

import com.dao.Dao;
import com.security.EncryUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Examine {
    // select count(*) from user_signup;
    //查询报名人数
    //// 由于加密后模糊搜索尚未解决，所以这里姓名，身份证号，准考证号，电子邮箱，手机号码暂时用简单的加密
    public static int queryNum(){
        int res = 0;
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select count(*) from user_signup;");       //查看报名的人数
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

    //返回全部报名人及信息
    public static List signupList(){
        //参考链接：https://blog.csdn.net/weixin_28779183/article/details/117810091
        List list =  new ArrayList();
        int res = 0;
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select * from user_signup;");       //查看报名信息
            ResultSet rs = p.executeQuery();

            while(rs.next()){
                /*
                list.add(rs.getString("name"));
                list.add(rs.getString("sex"));*/

                Map<String,String> data = new HashMap();
                data.put("name", EncryUtils.JieFindDecry(rs.getString("name")));

                String tmp1 = EncryUtils.JieInfoDecry(rs.getString("sex"));

                if( "1".equals(tmp1) ){
                    data.put("sex", "男");
                }else{
                    data.put("sex","女");
                }

                data.put("sfzh", EncryUtils.JieFindDecry(rs.getString("sfzh")));
                data.put("ticket",EncryUtils.JieFindDecry(rs.getString("ticket")));
                data.put("tele", EncryUtils.JieFindDecry(rs.getString("tele")));
                data.put("email",EncryUtils.JieFindDecry(rs.getString("email")));
                data.put("zymc", EncryUtils.JieInfoDecry(rs.getString("zymc")));
                data.put("zycc", EncryUtils.JieInfoDecry(rs.getString("zycc")));
                data.put("kcdm", EncryUtils.JieInfoDecry(rs.getString("kcdm")));
                data.put("file", EncryUtils.JieInfoDecry(rs.getString("file")));

                int tmp = rs.getInt("res");
                //最后res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
                if( tmp == 0 ){
                    data.put("res",  "未审核");
                }else if( tmp == 1 ){
                    data.put("res",  "审核通过");
                }else if( tmp == -1 ){
                    data.put("res",  "审核不通过");
                }

                //System.out.println(data);
                list.add(data);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(list);
        return list;
    }

    //返回部分报名人及信息
    public static List signupList(int page,int limit){
        //参考链接：https://blog.csdn.net/weixin_28779183/article/details/117810091
        List list =  new ArrayList();
        int res = 0;
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            // page 页码 limit 每页数量
            // select * from TABLE_NAME where ... order by ... limit (page -1) * limit, limit
            PreparedStatement p = conn.prepareStatement("select * from user_signup limit ?,?;");       //查看报名信息
            p.setInt(1,(page -1) * limit);
            p.setInt(2, limit);
            ResultSet rs = p.executeQuery();

            while(rs.next()){
                /*
                list.add(rs.getString("name"));
                list.add(rs.getString("sex"));*/

                Map<String,String> data = new HashMap();

                data.put("name", EncryUtils.JieFindDecry(rs.getString("name")));

                String tmp1 = EncryUtils.JieInfoDecry(rs.getString("sex"));
                if( "1".equals(tmp1) ){
                    data.put("sex", "男");
                }else{
                    data.put("sex","女");
                }

                data.put("sfzh",EncryUtils.JieFindDecry(rs.getString("sfzh")));
                data.put("ticket", EncryUtils.JieFindDecry(rs.getString("ticket")));
                data.put("tele", EncryUtils.JieFindDecry(rs.getString("tele")));
                data.put("email",EncryUtils.JieFindDecry(rs.getString("email")));
                data.put("zymc", EncryUtils.JieInfoDecry(rs.getString("zymc")));
                data.put("zycc", EncryUtils.JieInfoDecry(rs.getString("zycc")));
                data.put("kcdm", EncryUtils.JieInfoDecry(rs.getString("kcdm")));
                data.put("file", EncryUtils.JieInfoDecry(rs.getString("file")));

                int tmp = rs.getInt("res");
                //最后res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
                if( tmp == 0 ){
                    data.put("res",  "未审核");
                }else if( tmp == 1 ){
                    data.put("res",  "审核通过");
                }else if( tmp == -1 ){
                    data.put("res",  "审核不通过");
                }
                //System.out.println(data);
                list.add(data);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(list);
        return list;
    }

    //返回部分搜索的报名人及信息
    public static List searchList(int page,int limit,String content,String sex,String zycc,String zymc,String res,String find){
        //参考链接：https://blog.csdn.net/weixin_28779183/article/details/117810091
        List list =  new ArrayList();

        int iswhere = 0;            //0表示sql语句还没有where
        String sql = "select * from user_signup";

        if( sex != null && !"".equals(sex)){               //说明有筛选类别
            sex = EncryUtils.JiaInfoEncry(sex);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where sex='" +sex+"'");
            }else{
                sql += (" and sex='" +sex+"'");
            }
        }
        if( zycc != null && !"".equals(zycc)){               //说明有筛选类别
            zycc = EncryUtils.JiaInfoEncry(zycc);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where zycc='" +zycc+"'");
            }else{
                sql += (" and zycc='" +zycc+"'");
            }
        }
        if( zymc != null && !"".equals(zymc)){               //说明有筛选类别
            zymc = EncryUtils.JiaInfoEncry(zymc);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where zymc='" +zymc+"'");
            }else{
                sql += (" and zymc='" +zymc+"'");
            }
        }
        if( res != null && !"".equals(res)){               //说明有筛选类别
            //res = res;            res没有加密
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where res=" +res);
            }else{
                sql += (" and res=" +res);
            }
        }
        System.out.println( find + " " + content);
        if( find != null && !"".equals(find) ){
            //System.out.println("1234");
            //find = EncryUtils.JiaInfoEncry(find);
            if( content != null && !"".equals(content)){               //说明有筛选类别
                //System.out.println("123");
                if (iswhere == 0) {
                    System.out.println("12");
                    iswhere = 1;
                    if ("姓名".equals(content)) {
                        System.out.println("1");
                        sql += (" where name like '%" + find + "%'");
                    } else if ("身份证号".equals(content)) {
                        sql += (" where sfzh like '%" + find + "%'");
                    } else if ("准考证号".equals(content)) {
                        sql += (" where ticket like '%" + find + "%'");
                    } else if ("电子邮箱".equals(content)) {
                        sql += (" where email like '%" + find + "%'");
                    } else if ("手机号码".equals(content)) {
                        sql += (" where tele like '%" + find + "%'");
                    }
                } else {
                    if ("姓名".equals(content)) {
                        sql += (" and name like '%" + find + "%'");
                    } else if ("身份证号".equals(content)) {
                        sql += (" and sfzh like '%" + find + "%'");
                    } else if ("准考证号".equals(content)) {
                        sql += (" and ticket like '%" + find + "%'");
                    } else if ("电子邮箱".equals(content)) {
                        sql += (" and email like '%" + find + "%'");
                    } else if ("手机号码".equals(content)) {
                        sql += (" and tele like '%" + find + "%'");
                    }
                }

            }else{                              //没有筛选结果，就都查
                if( find != null && !"".equals(find)){
                    if( iswhere == 0 ){
                        iswhere = 1;
                        sql += (" where name like '%"+ find +"%' or sfzh like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find+"%'");
                    }else{
                        sql += ("and name like '%"+ find +"%' or sfzh like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find+"%'");
                    }
                }
            }
        }




        sql += " limit ?,?;";

        try {
            Connection conn = Dao.getConnection();              //连接数据库
            // page 页码 limit 每页数量
            // select * from TABLE_NAME where ... order by ... limit (page -1) * limit, limit
            PreparedStatement p = conn.prepareStatement(sql);       //查看报名信息
            p.setInt(1,(page -1) * limit);
            p.setInt(2, limit);
            System.out.println(p);
            ResultSet rs = p.executeQuery();

            while(rs.next()){
                /*
                list.add(rs.getString("name"));
                list.add(rs.getString("sex"));*/

                Map<String,String> data = new HashMap();

                data.put("name", EncryUtils.JieFindDecry(rs.getString("name")));

                String tmp1 = EncryUtils.JieInfoDecry(rs.getString("sex"));
                if( "1".equals(tmp1) ){
                    data.put("sex", "男");
                }else{
                    data.put("sex","女");
                }

                data.put("sfzh", EncryUtils.JieFindDecry(rs.getString("sfzh")));
                data.put("ticket", EncryUtils.JieFindDecry(rs.getString("ticket")));
                data.put("tele", EncryUtils.JieFindDecry(rs.getString("tele")));
                data.put("email",EncryUtils.JieFindDecry(rs.getString("email")));
                data.put("zymc", EncryUtils.JieInfoDecry(rs.getString("zymc")));
                data.put("zycc", EncryUtils.JieInfoDecry(rs.getString("zycc")));
                data.put("kcdm", EncryUtils.JieInfoDecry(rs.getString("kcdm")));
                data.put("file", EncryUtils.JieInfoDecry(rs.getString("file")));

                int tmp = rs.getInt("res");
                //最后res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
                if( tmp == 0 ){
                    data.put("res",  "未审核");
                }else if( tmp == 1 ){
                    data.put("res",  "审核通过");
                }else if( tmp == -1 ){
                    data.put("res",  "审核不通过");
                }
                //System.out.println(data);
                list.add(data);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(list);
        return list;
    }

    //返回全部搜索的报名人及信息
    public static List searchList(String content,String sex,String zycc,String zymc,String res,String find){
        //参考链接：https://blog.csdn.net/weixin_28779183/article/details/117810091
        List list =  new ArrayList();

        int iswhere = 0;            //0表示sql语句还没有where
        String sql = "select * from user_signup";

        if( sex != null && !"".equals(sex)){               //说明有筛选类别
            sex = EncryUtils.JiaInfoEncry(sex);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where sex='" +sex+"'");
            }else{
                sql += (" and sex='" +sex+"'");
            }
        }
        if( zycc != null && !"".equals(zycc)){               //说明有筛选类别
            zycc = EncryUtils.JiaInfoEncry(zycc);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where zycc='" +zycc+"'");
            }else{
                sql += (" and zycc='" +zycc+"'");
            }
        }
        if( zymc != null && !"".equals(zymc)){               //说明有筛选类别
            zymc = EncryUtils.JiaInfoEncry(zymc);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where zymc='" +zymc+"'");
            }else{
                sql += (" and zymc='" +zymc+"'");
            }
        }
        if( res != null && !"".equals(res)){               //说明有筛选类别
            res = EncryUtils.JiaInfoEncry(res);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where res=" +res);
            }else{
                sql += (" and res=" +res);
            }
        }
        System.out.println( find + " " + content);
        if( find != null && !"".equals(find) ){
            //System.out.println("1234");
            //find = EncryUtils.JiaInfoEncry(find);
            if( content != null && !"".equals(content)){               //说明有筛选类别
                //System.out.println("123");
                if (iswhere == 0) {
                    System.out.println("12");
                    iswhere = 1;
                    if ("姓名".equals(content)) {
                        System.out.println("1");
                        sql += (" where name like '%" + find + "%'");
                    } else if ("身份证号".equals(content)) {
                        sql += (" where sfzh like '%" + find + "%'");
                    } else if ("准考证号".equals(content)) {
                        sql += (" where ticket like '%" + find + "%'");
                    } else if ("电子邮箱".equals(content)) {
                        sql += (" where email like '%" + find + "%'");
                    } else if ("手机号码".equals(content)) {
                        sql += (" where tele like '%" + find + "%'");
                    }
                } else {
                    if ("姓名".equals(content)) {
                        sql += (" and name like '%" + find + "%'");
                    } else if ("身份证号".equals(content)) {
                        sql += (" and sfzh like '%" + find + "%'");
                    } else if ("准考证号".equals(content)) {
                        sql += (" and ticket like '%" + find + "%'");
                    } else if ("电子邮箱".equals(content)) {
                        sql += (" and email like '%" + find + "%'");
                    } else if ("手机号码".equals(content)) {
                        sql += (" and tele like '%" + find + "%'");
                    }
                }

            }else{                              //没有筛选结果，就都查
                if( find != null && !"".equals(find)){
                    if( iswhere == 0 ){
                        iswhere = 1;
                        sql += (" where name like '%"+ find +"%' or sfzh like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find);
                    }else{
                        sql += ("and name like '%"+ find +"%' or sfzh like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find);
                    }
                }
            }
        }




        //sql += " limit ?,?;";

        try {
            Connection conn = Dao.getConnection();              //连接数据库
            // page 页码 limit 每页数量
            // select * from TABLE_NAME where ... order by ... limit (page -1) * limit, limit
            PreparedStatement p = conn.prepareStatement(sql);       //查看报名信息
            //p.setInt(1,(page -1) * limit);
            //p.setInt(2, limit);
            System.out.println(p);
            ResultSet rs = p.executeQuery();

            while(rs.next()){
                /*
                list.add(rs.getString("name"));
                list.add(rs.getString("sex"));*/

                Map<String,String> data = new HashMap();

                data.put("name", EncryUtils.JieFindDecry(rs.getString("name")));

                String tmp1 = EncryUtils.JieInfoDecry(rs.getString("sex"));
                if( "1".equals(tmp1) ){
                    data.put("sex", "男");
                }else{
                    data.put("sex","女");
                }

                data.put("sfzh", EncryUtils.JieFindDecry(rs.getString("sfzh")));
                data.put("ticket", EncryUtils.JieFindDecry(rs.getString("ticket")));
                data.put("tele", EncryUtils.JieFindDecry(rs.getString("tele")));
                data.put("email",EncryUtils.JieFindDecry(rs.getString("email")));
                data.put("zymc", EncryUtils.JieInfoDecry(rs.getString("zymc")));
                data.put("zycc", EncryUtils.JieInfoDecry(rs.getString("zycc")));
                data.put("kcdm", EncryUtils.JieInfoDecry(rs.getString("kcdm")));
                data.put("file", EncryUtils.JieInfoDecry(rs.getString("file")));

                int tmp = rs.getInt("res");
                //最后res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
                if( tmp == 0 ){
                    data.put("res",  "未审核");
                }else if( tmp == 1 ){
                    data.put("res",  "审核通过");
                }else if( tmp == -1 ){
                    data.put("res",  "审核不通过");
                }
                //System.out.println(data);
                list.add(data);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(list);
        return list;
    }


    //返回搜索的数量
    public static int searchNum(String content, String sex, String zycc, String zymc, String res, String find){
        //参考链接：https://blog.csdn.net/weixin_28779183/article/details/117810091
        List list =  new ArrayList();

        int iswhere = 0;            //0表示sql语句还没有where
        String sql = "select count(*) from user_signup";

        if( sex != null && !"".equals(sex)){               //说明有筛选类别
            sex = EncryUtils.JiaInfoEncry(sex);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where sex='" +sex+"'");
            }else{
                sql += (" and sex='" +sex+"'");
            }
        }
        if( zycc != null && !"".equals(zycc)){               //说明有筛选类别
            zycc = EncryUtils.JiaInfoEncry(zycc);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where zycc='" +zycc+"'");
            }else{
                sql += (" and zycc='" +zycc+"'");
            }
        }
        if( zymc != null && !"".equals(zymc)){               //说明有筛选类别
            zymc = EncryUtils.JiaInfoEncry(zymc);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where zymc='" +zymc+"'");
            }else{
                sql += (" and zymc='" +zymc+"'");
            }
        }
        if( res != null && !"".equals(res)){               //说明有筛选类别
            res = EncryUtils.JiaInfoEncry(res);
            if( iswhere == 0 ){
                iswhere = 1;
                sql += (" where res=" +res);
            }else{
                sql += (" and res=" +res);
            }
        }
        System.out.println( find + " " + content);
        if( find != null && !"".equals(find) ){
            //System.out.println("1234");
            find = EncryUtils.JiaInfoEncry(find);
            if( content != null && !"".equals(content)){               //说明有筛选类别
                //System.out.println("123");
                if (iswhere == 0) {
                    System.out.println("12");
                    iswhere = 1;
                    if ("姓名".equals(content)) {
                        System.out.println("1");
                        sql += (" where name like '%" + find + "%'");
                    } else if ("身份证号".equals(content)) {
                        sql += (" where sfzh like '%" + find + "%'");
                    } else if ("准考证号".equals(content)) {
                        sql += (" where ticket like '%" + find + "%'");
                    } else if ("电子邮箱".equals(content)) {
                        sql += (" where email like '%" + find + "%'");
                    } else if ("手机号码".equals(content)) {
                        sql += (" where tele like '%" + find + "%'");
                    }
                } else {
                    if ("姓名".equals(content)) {
                        sql += (" and name like '%" + find + "%'");
                    } else if ("身份证号".equals(content)) {
                        sql += (" and sfzh like '%" + find + "%'");
                    } else if ("准考证号".equals(content)) {
                        sql += (" and ticket like '%" + find + "%'");
                    } else if ("电子邮箱".equals(content)) {
                        sql += (" and email like '%" + find + "%'");
                    } else if ("手机号码".equals(content)) {
                        sql += (" and tele like '%" + find + "%'");
                    }
                }

            }else{                              //没有筛选结果，就都查
                if( find != null && !"".equals(find)){
                    if( iswhere == 0 ){
                        iswhere = 1;
                        sql += (" where name like '%"+ find +"%' or sfzh like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find);
                    }else{
                        sql += ("and name like '%"+ find +"%' or sfzh like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find+"%' or ticket like '%" + find);
                    }
                }
            }
        }
        //sql += " limit ?,?;";
        int ans = 0;
        try {
            Connection conn = Dao.getConnection();              //连接数据库
           PreparedStatement p = conn.prepareStatement(sql);       //查看报名信息
           System.out.println(p);
            ResultSet rs = p.executeQuery();
            if(rs.next()){
                ans = rs.getInt(1);
                Dao.close(rs, p, conn);
                return ans;
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(list);
        return ans;
    }

    //审核状态（第一个参数是准考证号，第二个参数是1或者-1,1是通过，-1是不通过）
    public static boolean auditStatus(String ticket,int res) {
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            //select ticket,email from user_information where sfzh = '"+idc+"';
            //更改密码
            PreparedStatement p = conn.prepareStatement("update user_signup set res=? where ticket=?;");
            p.setInt(1, res);
            //p.setString(1, res);
            p.setString(2, EncryUtils.JiaFindEncry(ticket));
            p.executeUpdate();            //执行SQL语句(注意这里是execute，因为进行的是修改操作)
            Dao.close(p, conn);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return false;
    }
}