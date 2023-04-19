package com.security;

import com.logic.LoginCheck;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

public class Token {

    /**
     * 通过token返回username
     * */
    public static String GetUser(HttpServletRequest request) {
        String token = request.getParameter("token");

        //获取请求时的sessionId
        JwtUtils jwt = JwtUtils.getInstance();
        Claims claims = jwt.check(token);
        System.out.println(claims);
        if(claims != null ){
            String username = (String) claims.get("username");
            return username;
        }
        return null;
    }

    /**
     * 通过token返回username
     * */
    public static String GetUser(String token) {
        //获取请求时的sessionId
        JwtUtils jwt = JwtUtils.getInstance();
        Claims claims = jwt.check(token);
        System.out.println(claims);
        if(claims != null ){
            String username = (String) claims.get("username");
            return username;
        }
        return null;
    }

    /**
     * 判断是管理员还是用户
     * 返回 user是用户 admin是管理员 都不是为null
     */
    public static String GetIdentity(HttpServletRequest request) {
        String username = GetUser(request);
        boolean tmp = LoginCheck.checkUser(username);          //判断是否有该学生账户
        if( tmp == true ){
            return "user";
        }else{
            boolean tmp2 = LoginCheck.adminCheckUser(username);          //判断是否有该管理员账户
            if( tmp2 == true ){
                return "admin";
            }else{
                return null;
            }
        }
    }


    /**
     * 判断是管理员还是用户
     * 返回 user是用户 admin是管理员 都不是为null
     */
    public static String GetIdentity(String username) {
        boolean tmp = LoginCheck.checkUser(username);          //判断是否有该学生账户
        if( tmp == true ){
            return "user";
        }else{
            boolean tmp2 = LoginCheck.adminCheckUser(username);          //判断是否有该管理员账户
            if( tmp2 == true ){
                return "admin";
            }else{
                return null;
            }
        }
    }
    /**
     * 判断是token的正确性
     */
    public static boolean JudgeToken(String token){
        JwtUtils jwt = JwtUtils.getInstance();
        Claims claims = jwt.check(token);
        System.out.println(claims);
        if (claims != null) {
            return true;
        }else{
            return false;
        }
    }
}
