package com.filter;

import com.alibaba.fastjson.JSONObject;
import com.security.JwtUtils;
import io.jsonwebtoken.Claims;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// https://blog.csdn.net/qq_32363305/article/details/82469451
public class CrossFilter implements Filter{

    // 要排除的链接
    static Map<String,String> excludedPage = new HashMap<>();
    /*static String[] excludedPageArray =
            {
                    "/register","/getVerifyCode","/login","/adminlogin",
                    "/forgetpass","/modifypass","/opentime","/sha256"
            };*/

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("*"));
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Max-Age", "2700000");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, Cookie");
        response.addHeader("Access-Control-Allow-Credentials", "true");


        String token = request.getParameter("token");
        System.out.println(token);

        // 判断url是否在
        boolean isExcludedPage = false;
        //System.out.println(((HttpServletRequest)request).getServletPath());
        String page = ((HttpServletRequest)request).getServletPath();
        if( excludedPage.get(page)  != null ){
            isExcludedPage = true;
        }
        /*for (String page : excludedPageArray) {             //判断是否在过滤url之外
            if(((HttpServletRequest)request).getServletPath().equals(page)){
                isExcludedPage = true;
                break;
            }
        }*/
        if( isExcludedPage == false ){
            //获取请求时的sessionId
            JwtUtils jwt = JwtUtils.getInstance();
            Claims claims = jwt.check(token);
            System.out.println(claims);
            if (claims != null){
                String username = (String) claims.get("username");
                //String name = (String) claims.get("name");
                if( username != null && "".equals(username) ){
                    request.setAttribute("username",username);
                }
                System.out.println(username);
                //System.out.println(name);
                chain.doFilter(request,response);
            }else {
                System.out.println("非法token");
                // 登录信息已过期，请重新登录
                System.out.println("登录信息失效，请重新登录");
                //response.getWriter().write("登录信息失效，请重新登录");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                JSONObject tmp = new JSONObject();
                tmp.put("msg", "fail");
                tmp.put("error", "登录信息失效，请重新登录");
                out.append(tmp.toString());
            }
        }else{              //true 在过滤名单内
            chain.doFilter(request,response);
        }
        return;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //system.out.println("开始初始化");
        /*static String[] excludedPageArray =
                {
                        "/register","/getVerifyCode","/login","/adminlogin",
                        "/forgetpass","/modifypass","/opentime","/sha256"upload
                };*/
        excludedPage.put("/register","1");
        excludedPage.put("/getVerifyCode","1");
        excludedPage.put("/login","1");
        excludedPage.put("/adminlogin","1");
        excludedPage.put("/forgetpass","1");
        excludedPage.put("/modifypass","1");
        excludedPage.put("/opentime","1");
        excludedPage.put("/upload","1");
        excludedPage.put("/addannounce","1");
        excludedPage.put("/modifyupload","1");

        excludedPage.put("/sha256","1");

    }

    @Override
    public void destroy() {
        // system.out.println("销毁完成");
    }
}
