package com.controller;

import com.admin.CourseCode;
import com.admin.Examine;
import com.admin.SetAdmin;
import com.dao.SqlArray;
import com.logic.*;
import com.other.Announcement;
import com.other.FileUpload;
import com.other.Forgetpass;
import com.security.*;

import io.jsonwebtoken.Claims;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

//使用Controller来标识它是一个控制器
@Controller
@CrossOrigin                //解决跨域问题
//@ResponseBody               // 用来返回json包！！
public class WelcomeController {
    @RequestMapping(value = "/hello")
    public String say() {
        return "hello";
    }

    //测试链接
    @RequestMapping(value = "/cs")
    public @ResponseBody  Map<String,String> cs() {
        Map<String,String> data = new HashMap<String,String>();
        data.put("msg","HelloWorld" );
        return data;
    }

    //测试加密(其实是为了得到值保存到数据库中)
    @RequestMapping(value = "/sha256")
    public @ResponseBody  Map<String,String> sha256(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Map<String,String> data = new HashMap<String,String>();
        data.put("username", EncryUtils.JiaInfoEncry(username));
        data.put("password",EncryUtils.PassEncry(password));          //有错？？不知道为什么
        return data;
    }

/*

    @RequestMapping(value = "/sha256")
    public @ResponseBody  Map<String,String> sha256(HttpServletRequest request) {
        String code = request.getParameter("code");
        Map<String,String> data = new HashMap<String,String>();
        data.put("username",EncryUtils.JiaInfoEncry(code,"124wqhsajasdSAF==1"));
        return data;
    }
    */


    /*
        HTTP请求：POST
        请求地址：地址/tokenlogin
        请求参数：
            token
        返回json
        {'msg':'ok',"username":  ,"name":""}
        {'msg':'fail',}
    */
    //token登录：根据token登录
    @RequestMapping(value = "tokenlogin", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> tokenlogin(HttpServletRequest request, HttpSession session,HttpServletResponse response) { // 从前台页面取得的值
        Map<String,String> data = new HashMap<>();
        String token = request.getParameter("token");
        try{
            if( "user".equals(Token.GetIdentity(request)) ){
                JwtUtils jwt = JwtUtils.getInstance();
                Claims claims = jwt.check(token);
                System.out.println(claims);
                if (claims != null) {
                    String username = (String) claims.get("username");
                    String name = (String) claims.get("name");
                    data.put("msg","ok");
                    data.put("username",username);
                    data.put("name",name);
                }else{
                    data.put("msg","fail");
                }
            }else{
                data.put("msg","fail");
            }
        }catch (Exception e){
            data.put("msg","fail");
        }
        return data;
    }


    /*
        HTTP请求：POST
        请求地址：地址/login
        请求参数：username   password
        返回json
        {'msg':'ok'}
        {'msg':'fail','error':'登录帐号或密码错误，请重试!'}
    */
    //登录：判断账户密码是否正确
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> login(HttpServletRequest request, HttpSession session,HttpServletResponse response) { // 从前台页面取得的值
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        /* 从session中取出生成的验证码内容 */
        //学习链接： https://blog.csdn.net/weixin_45073048/article/details/109745707
     //   String yzm2 = (String)request.getSession().getAttribute("verifyCode");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String yzm =  request.getParameter("yzm");

     //   System.out.println("验证码 "+yzm+" "+yzm2);


        Map<String,String> data = new HashMap<String,String>();

//        if (!yzm2.toLowerCase().equals(yzm.toLowerCase())) {        //转成小写，不区分大小写
//            System.out.println("验证码和图片不一致 "+yzm+" "+yzm2);
//            data.put("msg","fail" );
//            data.put("error","验证码和图片不一致，请重试!" );
//            return data;
//        }
        System.out.println(username+" "+password);
        //继续判断账户密码是否为空
        if( username == null || password == null || "".equals(username) || "".equals(password) ){
            data.put("msg","fail" );
            data.put("error","登录帐号或密码不能为空!" );

        }

        String user_name = LoginCheck.check(username, password);        //判断账户密码是否正确

        if (user_name != null && !("".equals(user_name))) {
            data.put("msg","ok" );
            //写进操作日志
            Log.setLog(new Date(),Log.getIPAddress(request),user_name,"登录报名系统","用户");
            data.put("username", user_name );
            data.put("ticket", username);
            String[] NameAndEmail = LoginCheck.FindNameAndEmail(username);     //获得姓名和邮箱
            if (NameAndEmail != null) {
                data.put("name", NameAndEmail[0]);              //向前端传递消息
                data.put("email", NameAndEmail[1]);            //将邮箱存储进session中

                //生成token
                JwtUtils jwt = JwtUtils.getInstance();
                String token = jwt
                        .setClaim("username",username)
                        .setClaim("name",NameAndEmail[0])
                        .generateToken();
                System.out.println(token);
                data.put("token",token);            //将token存储
            }
            return data;           //进入成功界面
        } else {
            data.put("msg","fail" );
            data.put("error","登录帐号或密码错误，请重试!" );
            return data;
        }
    }

    /*
           HTTP请求：POST
           请求地址：地址/register
           请求参数：ticket   userpass   tele    email   name    xb  sfzh
           返回json
           {'msg':'ok'}
           {'msg':'fail','error':'注册失败，该账户可能已经注册'}
       */
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> registSuccess(HttpServletRequest request, HttpServletResponse response) {
        //获取从表单传过来的账户密码(注意这里是根据name获取的，而不是id!!!!!)
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        /* 从session中取出生成的验证码内容 */
        //学习链接： https://blog.csdn.net/weixin_45073048/article/details/109745707
        String yzm2 = (String)request.getSession().getAttribute("verifyCode");


        String act = request.getParameter("ticket");
        String pwd = request.getParameter("userpass");
        String tele = request.getParameter("tele");
        String email = request.getParameter("email");
        String name = request.getParameter("name");
        //单选框（https://blog.csdn.net/judyge/article/details/49808899）
        String sex = request.getParameter("xb");
        String sfzh = request.getParameter("sfzh");

        String yzm =  request.getParameter("yzm");

        System.out.println("验证码 "+yzm+" "+yzm2);
        Map<String,String> data = new HashMap<String,String>();

        if (!yzm2.toLowerCase().equals(yzm.toLowerCase())) {        //转成小写，不区分大小写
            System.out.println("验证码和图片不一致 "+yzm+" "+yzm2);
            data.put("msg","fail" );
            data.put("error","验证码和图片不一致，请重试!" );
            return data;
        }

        if (RegistCheck.registCheck(act, pwd, tele, email, name, sex, sfzh)) {
            data.put("msg", "ok");
            //写进操作日志
            Log.setLog(new Date(),Log.getIPAddress(request),act,"注册账户","用户");
        } else {
            data.put("msg", "fail");
            data.put("error","注册失败，该账户可能已经注册");
        }
        return data;
    }

    //获取验证码的链接
    @RequestMapping(value="/getVerifyCode")
    public void getVerificationCode(HttpServletResponse response,HttpServletRequest request) {
        try {
            int width=200;
            int height=69;
            BufferedImage verifyImg=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            //生成对应宽高的初始图片
            String randomText = VerificationCode.drawRandomText(width,height,verifyImg);
            //单独的一个类方法，出于代码复用考虑，进行了封装。
            //功能是生成验证码字符并加上噪点，干扰线，返回值为验证码字符
            /* 将验证码存入session中 */
            request.getSession().setAttribute("verifyCode", randomText);
            System.out.println(randomText);
            System.out.println(request.getSession().getAttribute("verifyCode"));
            response.setContentType("image/png");//必须设置响应内容类型为图片，否则前台不识别
            OutputStream os = response.getOutputStream(); //获取文件输出流
            ImageIO.write(verifyImg,"png",os);//输出图片流
            os.flush();
            os.close();//关闭流
        } catch (IOException e) {
            //logger.error(e.getMessage());
            e.printStackTrace();
        }

    }

    /*
       返回个人信息
       HTTP请求：POST
       请求地址：地址/personinfo
       请求参数：token
       返回json
       {'msg':'ok'，'ticket':"","tele":"" ……}
       {'msg':'fail'}
   */
    //返回个人信息
    @RequestMapping(value = "personinfo", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> personInfo(HttpServletRequest request, HttpSession session) { // 从前台页面取得的值
        String username = Token.GetUser(request);   // 解析token
        System.out.println(username);
        Map<String,String> data = PersonInfo.showInfo(username);
        if ( data != null ) {
            data.put("msg","ok" );
            return data;           //进入成功界面
         } else {
            data.put("msg","fail" );
        }
        return data;
    }

    /*
      修改个人信息
      HTTP请求：POST
      请求地址：地址/modifyinfo
      请求参数：username tele email sfzh name sex token
      返回json
      {'msg':'ok'，'username':"","tele":"" ……}
      {'msg':'fail'}
  */
    //修改个人信息
    @RequestMapping(value = "modifyinfo", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> modifyInfo(HttpServletRequest request, HttpSession session) { // 从前台页面取得的值
        System.out.println("正在修改个人信息");
        String tokenusername = Token.GetUser(request);   // 解析token
        String username = request.getParameter("username");
        String tele = request.getParameter("tele");
        String email = request.getParameter("email");
        String sfzh = request.getParameter("sfzh");
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        Map<String,String> data = new HashMap<>();
        if( tokenusername != null && !"".equals(tokenusername)){
            if( tokenusername.equals(username) ){       //token是当前username才可以进行修改
                boolean tmp = PersonInfo.modifyInfo(username,tele,email,sex,sfzh,name);
                if ( tmp == true ) {
                    data.put("msg","ok" );
                    //写进操作日志
                    Log.setLog(new Date(),Log.getIPAddress(request),username,"修改个人信息","用户");
                } else {
                    data.put("msg","fail" );
                }
            }else{
                data.put("msg","fail" );
                data.put("error","警告，请勿破坏改系统，您已被记录！！！" );
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),tokenusername,"▲ 尝试修改其他用户个人信息","用户");

            }
        }
        return data;
    }

    /*
       HTTP请求：POST
       请求地址：地址/adminlogin
       请求参数：username password yzm
       返回json
       {'msg':'ok'，'permission': 1,'username':''}
       {'msg':'fail','error':'登录帐号或密码错误，请重试!'}
   */
    //管理员登录：判断账户密码是否正确
    @RequestMapping(value = "adminlogin", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> adminlogin(HttpServletRequest request,HttpServletResponse response) { // 从前台页面取得的值
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        /* 从session中取出生成的验证码内容 */
        //学习链接： https://blog.csdn.net/weixin_45073048/article/details/109745707
       // String yzm2 = (String)request.getSession().getAttribute("verifyCode");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String yzm =  request.getParameter("yzm");

       // System.out.println("验证码 "+yzm+" "+yzm2);
        Map<String,String> data = new HashMap<String,String>();
//        if (!yzm2.toLowerCase().equals(yzm.toLowerCase())) {        //转成小写，不区分大小写
//            System.out.println("验证码和图片不一致 "+yzm+" "+yzm2);
//            data.put("msg","fail" );
//            data.put("error","验证码和图片不一致，请重试!" );
//            return data;
//        }

        System.out.println(username+" "+password);
        //继续判断账户密码是否为空
        if( username == null || password == null || "".equals(username) || "".equals(password) ){
            data.put("msg","fail" );
            data.put("error","登录帐号或密码不能为空!" );
        }

        String user_name = LoginCheck.admincheck(username, password);        //判断账户密码是否正确

        if (user_name != null && !("".equals(user_name))) {
            data.put("msg","ok" );

            //写进操作日志
            Log.setLog(new Date(),Log.getIPAddress(request),username,"登录后台系统","管理员");
            int permission = SetAdmin.getPermission(username);
            data.put("permission", ""+permission);
            data.put("username",username);

            //生成token
            JwtUtils jwt = JwtUtils.getInstance();
            String token = jwt
                    .setClaim("username",username)
                    .setClaim("permission",""+permission)
                    .generateToken();
            System.out.println(token);
            data.put("token",token);            //将token存储
            return data;           //进入成功界面
        } else {
            data.put("msg","fail" );
            data.put("error","登录帐号或密码错误，请重试!" );
            return data;
        }
    }

    /*
      HTTP请求：POST
      请求地址：地址/tokenadminlogin
      请求参数：
      token
      返回json
      {'msg':'ok'，'permission': 1,'username':''}
      {'msg':'fail'}
  */
    //管理员登录：token
    @RequestMapping(value = "tokenadminlogin", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> tokenadminlogin(HttpServletRequest request,HttpServletResponse response) { // 从前台页面取得的值

        Map<String,String> data = new HashMap<>();
        String token = request.getParameter("token");
        try{
            if( "admin".equals(Token.GetIdentity(request))){
                JwtUtils jwt = JwtUtils.getInstance();
                Claims claims = jwt.check(token);
                System.out.println(claims);
                if (claims != null) {
                    String username = (String) claims.get("username");
                    String permission = (String) claims.get("permission");
                    data.put("msg","ok");
                    data.put("username",username);
                    data.put("permission",permission);
                }else{
                    System.out.println(data+"123");
                    data.put("msg","fail");
                }
            }else{
                System.out.println(data+"124");
                data.put("msg","fail");
            }

        }catch (Exception e){
            e.printStackTrace();
            data.put("msg","fail");
            System.out.println(data+"125");
        }
        return data;
    }

    /*
       HTTP请求：POST
       请求地址：地址/forgetpass
       请求参数：username      (用户名/准考证号)
       返回json
       {'msg':'ok'}
       {'msg':'fail','error':''}
       待完善：
            验证码
    */
    //修改密码
    @RequestMapping(value = "forgetpass", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> forgetpass(HttpServletRequest request,HttpServletResponse response
    ) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String username = request.getParameter("username");
        Map<String,String> data = new HashMap<String,String>();
        String codeNum = Forgetpass.newCodeNum();
        request.getSession().setAttribute("EverifyCode",codeNum);              //存到session中
        System.out.println(request.getSession().getAttribute("EverifyCode"));

        //System.out.println("zxcz"+(String)request.getSession().getAttribute("verifyCode"));
        String email = Forgetpass.FindTicketAndEmail(username);            //根据准考证号获取邮箱
        if( "".equals(email) ){
            data.put("msg","fail");
            data.put("error","该用户尚未注册");
            return data;
        }
        boolean isemail = Forgetpass.SendEmail(codeNum, email);        //发送邮件
        if( isemail == true ){
            data.put("msg","ok");
            //写进操作日志
            Log.setLog(new Date(),Log.getIPAddress(request),username,"正在找回密码，已发送重置码到邮箱","用户");

        }else{
            data.put("msg","fail");
            data.put("error","邮箱发送失败");
        }
        return data;
    }

    /*
      HTTP请求：POST
      请求地址：地址/modifypass
      请求参数：
          username
          password
          eyzm
      返回json
      {'msg':'ok'}
      {'msg':'fail','error':''}
      待完善：
           验证码
   */
    //修改密码
    @RequestMapping(value = "modifypass", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> modifypass( HttpServletRequest request,HttpServletResponse response
    ) {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String eyzm = request.getParameter("eyzm");

        /* 从session中取出生成的验证码内容 */
        //学习链接： https://blog.csdn.net/weixin_45073048/article/details/109745707
        String eyzm2 = (String)request.getSession().getAttribute("EverifyCode");

        System.out.println(eyzm+ " "+eyzm2);

        Map<String,String> data = new HashMap<String,String>();
        if( eyzm != "" && eyzm.equals(eyzm2) ){             //密码重置码正确（区分大小写）

            boolean tmp = Forgetpass.ChangePass(username,password);

            if( tmp == true ){
                data.put("msg","ok");
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),username,"修改密码","用户");

            }else{
                data.put("msg","fail");
                data.put("error","密码修改失败");
            }
        }else{
            data.put("msg","fail");
            data.put("error","密码重置码不一致");
        }
        return data;
    }

    /*
        设置开放时间
        HTTP请求：POST
        请求地址：地址/setopentime
        请求参数：（注意格式）
              token
              begin
              end
        返回json
        {'msg':'ok'}
        {'msg':'fail'}
    */
    //设置开放时间
    @RequestMapping(value = "setopentime", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> setOpentime(HttpServletRequest request) { // 从前台页面取得的值
        String people = Token.GetIdentity(request);             //判断是管理员还是用户
        Map<String,String> data = new HashMap<String,String>();
        if( "admin".equals(people)){
            Date begin = new Date(request.getParameter("begin")) ;
            Date end = new Date(request.getParameter("end"));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(format.format(begin)+" "+end);

            boolean time = OpenTime.setOpenTime(format.format(begin),format.format(end));

            if ( time == true ) {
                data.put("msg","ok" );
                String username = Token.GetUser(request);
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),username,"设置开放时间","管理员");

                return data;
            } else {
                data.put("msg","fail" );
                data.put("error","时间设置失败" );
                return data;
            }
        }else{
            data.put("msg","fail" );
            data.put("error","权限不足" );
            return data;
        }
    }

    /*
       清空开放时间
       HTTP请求：POST
       请求地址：地址/deleteopentime
       请求参数：无
           token
       返回json
       {'msg':'ok'}
       {'msg':'fail'}
   */
    //清空开放时间
    @RequestMapping(value = "deleteopentime", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> deleteOpentime(HttpServletRequest request) { // 从前台页面取得的值
        String people = Token.GetIdentity(request);             //判断是管理员还是用户
        Map<String,String> data = new HashMap<String,String>();
        if( "admin".equals(people)){
            //String username = request.getParameter("username");
            boolean time = OpenTime.deleteOpenTime();
            if ( time == true ) {
                data.put("msg","ok" );
                String username = Token.GetUser(request);
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),username,"清空开放时间","管理员");

            } else {
                data.put("msg","fail" );
            }
            return data;
        }else{
            data.put("msg","fail" );
            data.put("error","权限不足" );
            return data;
        }
    }

    /*
      获取开放时间
      HTTP请求：POST
      请求地址：地址/getopentime
      请求参数：无
      返回json
      {'msg':'ok'}
      {'msg':'fail'}
  */
    //获取开放时间
    @RequestMapping(value = "getopentime", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> getOpentime(HttpServletRequest request) { // 从前台页面取得的值
        Map<String,String> data = OpenTime.getOpenTime();
        if ( data != null ) {
            data.put("msg","ok" );
        } else {
            data.put("msg","fail" );
        }
        return data;
    }

    /*
         判断当前是否为开放时间
         HTTP请求：POST
         请求地址：地址/r
         请求参数：无
         返回json
         {'msg':'ok'}
         {'msg':'fail'}
     */
    //判断时间当前是否为开放时间
    @RequestMapping(value = "opentime", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> judgeOpentime(HttpServletRequest request) { // 从前台页面取得的值
        boolean time = OpenTime.OpenTime();
        Map<String,String> data = new HashMap<String,String>();
        if ( time == true ) {
            data.put("msg","ok" );
            return data;           //进入成功界面
        } else {
            data.put("msg","fail" );
            return data;
        }
    }

    /*
         当前报名状态
         HTTP请求：POST
         请求地址：地址/signupres
         请求参数：token
         返回json
         {'msg':'ok','res':报名状态}
         res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
     */
    // signupres
    @RequestMapping(value = "signupres", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> signUpRes(HttpServletRequest request) {
        String user = Token.GetUser(request);   // 解析token
        int tmp = SignUp.check(user);
        Map<String,String> data = new HashMap<String,String>();
        data.put("msg","ok");
        if (tmp == 2) {         //说明没开始报名或者没有该用户
            data.put("res","2");
        } else {
            data.put("res",Integer.toString(tmp));                   //已报名
        }
        return data;
    }

    /*
           有报名信息
           HTTP请求：POST
           请求地址：地址/signupinfo
           请求参数：token
           返回json
           {'msg':'ok','name': , ''; ……}
           res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
       */
    //点击报名信息 signupinfo
    @RequestMapping(value = "signupinfo", method = RequestMethod.POST)
    public void signupInformation(HttpServletRequest request,HttpServletResponse response)  throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter wirte = null;
        JSONObject json = new JSONObject();
        String ticket = Token.GetUser(request);         //token中获取username
        String[] tmp = SignUp.SignupInfo(ticket);
        try {
            wirte = response.getWriter();
            if (tmp == null) {
                json.put("msg", "fail");
            } else {
                json.put("msg", "ok");

                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),ticket,"查看报名信息","用户");

                json.put("name", tmp[0]);
                //1是男生 2是女生
                if ("1".equals(tmp[1])) {
                    json.put("sex", "男");
                } else if ("2".equals(tmp[1])) {
                    json.put("sex", "女");
                }
                json.put("sfzh", tmp[2]);
                json.put("ticket", tmp[3]);
                json.put("tele", tmp[4]);
                json.put("email", tmp[5]);
                json.put("zymc", tmp[6]);
                json.put("zycc", tmp[7]);
                json.put("kcdm", tmp[8]);
                json.put("file", tmp[9]);
                //最后res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
                if ("0".equals(tmp[10])) {
                    json.put("res", "未审核");
                } else if ("1".equals(tmp[10])) {
                    json.put("res", "审核通过");
                } else if ("-1".equals(tmp[10])) {
                    json.put("res", "审核不通过");
                } else if ("2".equals(tmp[10])) {
                    json.put("res", "未开始报名");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "fail");
        } finally {
            wirte.print(json);
            wirte.flush();
            wirte.close();
        }
        response.getWriter().print(json);
        return;
    }

    /*
       返回报名人数
       HTTP请求：POST
       请求地址：地址/signupnum
       请求参数：无
       返回json
       {'msg':'ok','num': , ''}
     */
    //返回报名人数 signupnum
    @RequestMapping(value = "signupnum", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> signUpNum(HttpServletRequest request, HttpSession session) { // 从前台页面取得的值
        Map<String,String> data = new HashMap<String,String>();
        int num = Examine.queryNum();
        data.put("msg","ok" );
        data.put("num", ""+num);
        return data;
    }


    /*
       返回搜索公告的数量
       HTTP请求：POST
       请求地址：地址/searchannouncenum
       请求参数：无
       返回json
       {'msg':'ok','num': , ''}
     */
    // searchannouncenum
    @RequestMapping(value = "searchannouncenum", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> searchAnnounceNum(HttpServletRequest request, HttpSession session) { // 从前台页面取得的值
        Map<String,String> data = new HashMap<String,String>();
        String find = request.getParameter("find");
        int num = Announcement.searchAnnounceNum(find);
        data.put("msg","ok" );
        data.put("num", ""+num);
        return data;
    }


    /*
       返回搜索人数
       HTTP请求：POST
       请求地址：地址/searchnum
       请求参数：
           page
           content
           sex
           zycc
           zymc
           res
           search
           token
       返回json
       {'msg':'ok','num': , ''}
     */
    //返回报名人数 searchnum
    @RequestMapping(value = "searchnum", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> searchNum(HttpServletRequest request, HttpSession session) { // 从前台页面取得的值
        Map<String,String> data = new HashMap<String,String>();
        String page = request.getParameter("page");
        String content = request.getParameter("content");
        String sex = request.getParameter("sex");
        String zycc = request.getParameter("zycc");
        String zymc = request.getParameter("zymc");
        String res = request.getParameter("res");
        String search = request.getParameter("search");
        System.out.println(page + content + sex + zycc + zymc + res + search );
        if( "0".equals(content) ){
            content = null;
        }
        if( "0".equals(sex) ){
            sex = null;
        }else{
            if( "男".equals(sex) ){
                sex = "1";
            }else if( "女".equals(sex) ){
                sex = "2";
            }
        }
        if( "0".equals(zycc) ){
            zycc = null;
        }
        if( "0".equals(zymc) ){
            zymc = null;
        }
        if( "0".equals(res) ){
            res = null;
        }else{
            //最后res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
            if( "审核通过".equals(res) ){
                res = "1";
            }else if( "审核不通过".equals(res) ){
                res = "-1";
            }else if( "未审核".equals(res) ){
                res = "0";
            }

        }
        if( "".equals(search) ){
            search = null;
        }
        int num = Examine.searchNum(content,sex,zycc,zymc,res,search);
        data.put("msg","ok" );
        data.put("num", ""+num);
        return data;           //进入成功界面
    }

    /*
       返回报名信息
       HTTP请求：POST
       请求地址：地址/examine
       请求参数：
       page
       token
       （没有page则返回全部）
       返回json
       {'msg':'ok','examine": , ''}
    */
    //返回报名信息 examine
    @RequestMapping(value = "examine", method = RequestMethod.POST)
    public void Examine(HttpServletRequest request,HttpServletResponse response)  throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String page = request.getParameter("page");
        if( "admin".equals(Token.GetIdentity(request))){
            PrintWriter wirte = null;
            JSONObject json = new JSONObject();
            List list;
            if( page == null || "".equals(page)){       //page为空
                list = Examine.signupList();            //返回全部
            }else{
                list = Examine.signupList(Integer.parseInt(page),10);          //每页10个
            }

              try {
                wirte = response.getWriter();
                if (list == null) {
                    json.put("msg", "fail");
                } else {
                    json.put("msg", "ok");
                    json.put("examine",list);
                }
            } catch (Exception e) {
                e.printStackTrace();
                json.put("msg", "fail");
            } finally {
                wirte.print(json);
                wirte.flush();
                wirte.close();
            }
            response.getWriter().print(json);
        }
        return;
    }

    /*
       返回报名信息
       HTTP请求：POST
       请求地址：地址/search
       请求参数：
       page
       content
       sex
       zycc
       zymc
       res
       search
       返回json
       {'msg':'ok','examine": , ''}
    */
    //返回查找的报名信息 search
    @RequestMapping(value = "search", method = RequestMethod.POST)
    public void Search(HttpServletRequest request,HttpServletResponse response)  throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        String page = request.getParameter("page");
        String content = request.getParameter("content");
        String sex = request.getParameter("sex");
        String zycc = request.getParameter("zycc");
        String zymc = request.getParameter("zymc");
        String res = request.getParameter("res");
        String search = request.getParameter("search");
        System.out.println(page + content + sex + zycc + zymc + res + search );
        if( "0".equals(content) ){
            content = null;
        }
        if( "0".equals(sex) ){
            sex = null;
        }else{
            if( "男".equals(sex) ){
                sex = "1";
            }else if( "女".equals(sex) ){
                sex = "2";
            }
        }
        if( "0".equals(zycc) ){
            zycc = null;
        }
        if( "0".equals(zymc) ){
            zymc = null;
        }
        if( "0".equals(res) ){
            res = null;
        }else{
            //最后res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
            if( "审核通过".equals(res) ){
                res = "1";
            }else if( "审核不通过".equals(res) ){
                res = "-1";
            }else if( "未审核".equals(res) ){
                res = "0";
            }

        }

        if( "".equals(search) ){
            search = null;
        }
        PrintWriter wirte = null;
        JSONObject json = new JSONObject();

        List list;
        if( page == null || "".equals(page) ){          //page为空，则返回全部
            list = Examine.searchList(content,sex,zycc,zymc,res,search);          //每页10个
        }else{                                          //page不为空，返回对应内容的数据
            list = Examine.searchList(Integer.parseInt(page),10,content,sex,zycc,zymc,res,search);          //每页10个
        }
        try {
            wirte = response.getWriter();
            if (list == null) {
                json.put("msg", "fail");
            } else {
                json.put("msg", "ok");
                json.put("examine",list);
            }
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "fail");
        } finally {
            wirte.print(json);
            wirte.flush();
            wirte.close();
        }
        response.getWriter().print(json);
        return;
    }

    /*
    *    上传报名信息
         HTTP请求：POST
         请求地址：地址/upload
         请求参数：
            name
            sex
            sfzh
            ticket
            tele
            email
            zymc
            zycc
            kcdm    多选
            file    文件
         返回json
         {'msg':'ok','res':报名状态}
         res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
    * */
    //文件上传（报名信息的上传）
    //Signup(String name, String sex, String sfzh, String ticket, String tele, String email, String zymc, String zycc, String kcdm, String dz) {
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> upload(@RequestParam(value = "file",required = false) MultipartFile[] file,
                         HttpServletRequest request) {
        Map<String,String> data = new HashMap<>();
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        String sfzh = request.getParameter("sfzh");
        String ticket = request.getParameter("ticket");
        String tele = request.getParameter("tele");
        String email = request.getParameter("email");
        String zymc = request.getParameter("zymc");
        String zycc = request.getParameter("zycc");
        String kcdm = request.getParameter("kcdm");
        String token = request.getParameter("token");
        if ( Token.JudgeToken(token) ){
            if( ticket != null && ticket.equals(Token.GetUser(token) )){            // token和报名的ticket一致
                System.out.println( name + " " + sex +" " + sfzh +" " + ticket +" " + tele +" " + email + " " +zymc +" " + zycc +" " + kcdm );
                System.out.println("总共有"+file.length+"个文件");
                // 文件上传到服务器的位置“/files”
                System.out.println("正在上传文件");

                List<String> fileList = new ArrayList<>();           //全部的数组
                for( MultipartFile f:file){
                    String filelocation = FileUpload.SaveServer(f,request);
                    if( filelocation != null ){
                        fileList.add(filelocation);
                    }else{
                        data.put("msg","fail");
                        data.put("error","文件上传失败");
                        return data;
                    }
                }
                //合并
                String all_file = SqlArray.Merge(fileList);     //全部的
                System.out.println(all_file);

                //进行报名
                boolean signupres = SignUp.Signup(name, sex, sfzh, ticket, tele, email, zymc, zycc, kcdm,all_file);
                if (signupres == true) {            //如果报名成功，则继续保存文件
                    data.put("msg","ok");

                    //写进操作日志
                    Log.setLog(new Date(),Log.getIPAddress(request),Token.GetUser(token),"提交报名信息","用户");

                } else {
                    data.put("msg","fail");
                    data.put("error","数据库存储错误");
                }
            }else{
                data.put("msg","fail");
                data.put("error","权限不足，请先登录");
            }
        }else{
            data.put("msg","fail");
            data.put("error","禁止修改准考证号");
        }
        return data;
    }

    /*
   *    修改报名信息
        HTTP请求：POST
        请求地址：地址/modifyupload
        请求参数：
           name
           sex
           sfzh
           ticket
           tele
           email
           zymc
           zycc
           kcdm    多选
           file    文件
        返回json
        {'msg':'ok','res':报名状态}
        res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
   * */
    //文件上传（修改报名信息的上传）
    //Signup(String name, String sex, String sfzh, String ticket, String tele, String email, String zymc, String zycc, String kcdm, String dz) {
    @RequestMapping(value = "modifyupload", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> modifyupload(@RequestParam(value = "file",required = false) MultipartFile[] file,
                                                   HttpServletRequest request) {
        Map<String,String> data = new HashMap<>();
        String name = request.getParameter("name");
        String sex = request.getParameter("sex");
        String sfzh = request.getParameter("sfzh");
        String ticket = request.getParameter("ticket");
        String tele = request.getParameter("tele");
        String email = request.getParameter("email");
        String zymc = request.getParameter("zymc");
        String zycc = request.getParameter("zycc");
        String kcdm = request.getParameter("kcdm");
        String token = request.getParameter("token");
        if ( Token.JudgeToken(token) ){
            if( ticket != null && ticket.equals(Token.GetUser(token) )){
                System.out.println( name + " " + sex +" " + sfzh +" " + ticket +" " + tele +" " + email + " " +zymc +" " + zycc +" " + kcdm );
                System.out.println("总共有"+file.length+"个文件");
                // 文件上传到服务器的位置“/files”
                System.out.println("正在上传文件");

                List<String> fileList = new ArrayList<>();           //全部的数组
                for( MultipartFile f:file){
                    String filelocation = FileUpload.SaveServer(f,request);
                    if( filelocation != null ){
                        fileList.add(filelocation);
                    }else{
                        data.put("msg","fail");
                        data.put("error","文件上传失败");
                        return data;
                    }
                }
                //合并
                String all_file = SqlArray.Merge(fileList);     //全部的
                System.out.println(all_file);

                //进行报名
                boolean signupres = SignUp.modifySignup(name, sex, sfzh, ticket, tele, email, zymc, zycc, kcdm,all_file);
                if (signupres == true) {            //如果报名成功，则继续保存文件
                    data.put("msg","ok");
                    //写进操作日志
                    Log.setLog(new Date(),Log.getIPAddress(request),Token.GetUser(token),"修改报名信息","用户");
                } else {
                    data.put("msg","fail");
                    data.put("error","数据库存储错误");
                }
            }else{
                data.put("msg","fail");
                data.put("error","权限不足，请先登录");
            }
        }else{
            data.put("msg","fail");
            data.put("error","禁止修改准考证号");
        }
        return data;
    }


    //（第一个参数是准考证号，第二个参数是1或者-1,1是通过，-1是不通过）
/*
    *    删除报名信息
         HTTP请求：POST
         请求地址：地址/deletesignup
         请求参数：
            token
         返回json
         {'msg':'ok'}
         {'msg':'fail'}
         res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
    * */
    @RequestMapping(value = "deletesignup", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> deletesignUp(HttpServletRequest request) {
        String user = Token.GetUser(request);   // 解析token
        int tmp = SignUp.check(user);
        Map<String,String> data = new HashMap<String,String>();
        if( tmp == -1 ){            //只有审核不通过的时候才能重新开始报名
            boolean a =  SignUp.deleteSignUp(user);
            if( a == true ){
                data.put("msg","ok");
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),user,"重新开始报名","用户");

            }else{
                data.put("msg","fail");
            }
        }else{
            data.put("msg","fail");
            data.put("error","审核已经通过或者未审核，无法重新报名");
        }
        return data;
    }


    //（第一个参数是准考证号，第二个参数是1或者-1,1是通过，-1是不通过）
/*
    *    进行审核（通过和不通过）
         HTTP请求：POST
         请求地址：地址/auditstatus
         请求参数：
            ticket          (准考证号)
            res             （结果：1或者-1,1是通过，-1是不通过）
            token
         返回json
         {'msg':'ok'}
         {'msg':'fail'}
         res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
    * */
    //Signup(String name, String sex, String sfzh, String ticket, String tele, String email, String zymc, String zycc, String kcdm, String dz) {
    @RequestMapping(value = "auditstatus", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> auditStatus(HttpServletRequest request) {
        String ticket = request.getParameter("ticket");
        String res = request.getParameter("res");
        Map<String,String> data = new HashMap<>();
        if( "admin".equals(Token.GetIdentity(request) )) {          //管理员才能进行审核
            //写进操作日志
            String thing = "";
            if( res == "1" ){           //审核通过
               thing = "审核通过用户["+ticket+"]";
            }else if( res == "-1"){     //审核不通过
                thing = "审核不通过用户["+ticket+"]";
            }else if(res == "0"){       //撤销审核
                thing = "撤销对用户["+ticket+"]的审核";
            }
            Log.setLog(new Date(),Log.getIPAddress(request),Token.GetUser(request),thing,"用户");
            boolean tmp = Examine.auditStatus(ticket, Integer.parseInt(res));
            if (tmp == true) {
                data.put("msg", "ok");
            } else {
                data.put("msg", "fail");
            }
        }
        return data;
    }


    /*
    *    添加课程代码
         HTTP请求：POST
         请求地址：地址/addcode
         请求参数：
           code
           name
         返回json
         {'msg':'ok'}
         {'msg':'fail'}
    * */
    @RequestMapping(value = "addcode", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> addCode(HttpServletRequest request) {
        Map<String,String> data = new HashMap<String,String>();
        if( "admin".equals(Token.GetIdentity(request))) {           //管理员才能添加
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            //System.out.println(code + " " + name);
            boolean tmp = CourseCode.addCode(code, name);
            if (tmp) {
                data.put("msg", "ok");
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),Token.GetUser(request),"添加课程代码："+name,"管理员");
            } else {
                data.put("msg", "fail");
            }
        }else{
            data.put("msg", "fail");
            data.put("error", "权限不足");
        }
        return data;
    }

    /*
   *    删除课程代码
        HTTP请求：POST
        请求地址：地址/deletecode
        请求参数：
          code
          name
        返回json
        {'msg':'ok'}
        {'msg':'fail'}
   * */
    @RequestMapping(value = "deletecode", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> deleteCode(HttpServletRequest request) {
        String people = Token.GetIdentity(request);             //判断是管理员还是用户
        Map<String,String> data = new HashMap<String,String>();
        if( "admin".equals(people)) {
            String code = request.getParameter("code");
            String name = request.getParameter("name");
            //System.out.println(code + " " + name);
            boolean tmp = CourseCode.deleteCode(code, name);
            if (tmp) {
                data.put("msg", "ok");
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),Token.GetUser(request),"删除课程代码："+name,"管理员");

            } else {
                data.put("msg", "fail");
            }
        }else{
            data.put("msg", "fail");
            data.put("error", "权限不足");
        }
        return data;
    }

    /*
   *    修改课程代码
        HTTP请求：POST
        请求地址：地址/modifycode
        请求参数：
          oldcode
          oldname
          newcode
          newname

        返回json
        {'msg':'ok'}
        {'msg':'fail'}
   * */
    @RequestMapping(value = "modifycode", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> modifyCode(HttpServletRequest request) {
        String people = Token.GetIdentity(request);             //判断是管理员还是用户
        Map<String,String> data = new HashMap<String,String>();
        if( people == "admin") {
            String oldcode = request.getParameter("oldcode");
            String oldname = request.getParameter("oldname");
            String newcode = request.getParameter("newcode");
            String newname = request.getParameter("newname");

            boolean tmp = CourseCode.modifyCode(oldname, oldcode, newname, newcode);
            if (tmp) {
                data.put("msg", "ok");
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),Token.GetUser(request),"修改课程代码："+oldname,"管理员");
            } else {
                data.put("msg", "fail");
            }
        }else{
            data.put("msg", "fail");
            data.put("error", "权限不足");
        }
        return data;
    }

    /*
   *    显示所有的课程代码
        HTTP请求：POST
        请求地址：地址/showcode
        请求参数：
          code
          name
        返回json
        {'msg':'ok'，"course":""}
        {'msg':'fail'}
   * */
    @RequestMapping(value = "showcode", method = RequestMethod.POST)
    public void showCode(HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println("正在获取~");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter wirte = null;
        JSONObject json = new JSONObject();
        try {
            wirte = response.getWriter();
            json.put("msg", "ok");
            List list = CourseCode.showCode();
            System.out.println(list);
            json.put("course",list);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "fail");
        } finally {
            wirte.print(json);
            wirte.flush();
            wirte.close();
        }
        response.getWriter().print(json);
        return;
    }

    /*
   *    发布公告
        HTTP请求：POST
        请求地址：地址/addannounce
        请求参数：
          code
          name
        返回json
        {'msg':'ok'}
        {'msg':'fail'}
   * */
    @RequestMapping(value = "addannounce", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> addAnnounce(@RequestParam(value = "file",
            required = false) MultipartFile[] file, HttpServletRequest request) {
        String token = request.getParameter("token");
        String title = request.getParameter("title");
        String time = request.getParameter("time");
        String author = request.getParameter("author");
        String body = request.getParameter("body");
        String ismail_tmp = request.getParameter("isemail");

        Map<String,String> data = new HashMap<String,String>();
        if( Token.JudgeToken(token) ){              //有文件上传所以没在过滤器中，所以这里多一步判断
           if( "admin".equals(Token.GetIdentity(Token.GetUser(token)))  ){      //管理员才能
                System.out.println("总共有"+file.length+"个文件");
                // 文件上传到服务器的位置“/files”
                System.out.println("正在上传文件");

                List<String> fileList = new ArrayList<>();           //全部的数组
                for( MultipartFile f:file){
                    String filelocation = FileUpload.SaveServer(f,request);
                    if( filelocation != null ){
                        fileList.add(filelocation);
                    }else{
                        data.put("msg","fail");
                        data.put("error","文件上传失败");
                        return data;
                    }
                }
                //合并
                String all_file = SqlArray.Merge(fileList);     //全部的
                System.out.println(all_file);

                System.out.println( title +" "+time+" "+author+" "+body+" "+ismail_tmp);
                boolean isemail = false;
                if(  "false".equals(ismail_tmp) ){
                    isemail = false;
                }else if( "true".equals(ismail_tmp) ){
                    isemail = true;
                }
                boolean tmp = Announcement.setAnnounce(title,time,author,body,isemail,all_file);

                if (tmp) {
                    data.put("msg","ok");
                } else {
                    data.put("msg","fail");
                }
            }
        }else{
            data.put("msg", "fail");
            data.put("error", "权限不足");
        }
        return data;
    }

    /*
   *    显示公告（分页）
        HTTP请求：POST
        请求地址：地址/showsomeannounce
        请求参数：
            page
        返回json
        {'msg':'ok'，"announce":""}
        {'msg':'fail'}
   * */
    @RequestMapping(value = "showsomeannounce", method = RequestMethod.POST)
    public void showSomeAnnounce(HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println("正在获取~");
        response.setContentType("text/html;charset=UTF-8");
        String page = request.getParameter("page");
        PrintWriter wirte = null;
        JSONObject json = new JSONObject();
        try {
            wirte = response.getWriter();
            json.put("msg", "ok");
            List list = Announcement.showAnnounce(Integer.parseInt(page),5);         // 每页显示5条

            System.out.println(list);
            json.put("announce",list);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "fail");
        } finally {
            wirte.print(json);
            wirte.flush();
            wirte.close();
        }
        response.getWriter().print(json);
        return;
    }

    /*
       返回公告数量
       HTTP请求：POST
       请求地址：地址/announcenum
       请求参数：
       token
       返回json
       {'msg':'ok','num': , ''}
     */
    //返回报名人数 signupnum
    @RequestMapping(value = "announcenum", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> announceNum(HttpServletRequest request, HttpSession session) { // 从前台页面取得的值
        Map<String,String> data = new HashMap<String,String>();
        int num = Announcement.announceNum();
        data.put("msg","ok" );
        data.put("num", ""+num);
        return data;
    }

    /*
 *    查找公告（分页）
      HTTP请求：POST
      请求地址：地址/searchannounce
      请求参数：
          page
      返回json
      {'msg':'ok'，"announce":""}
      {'msg':'fail'}
 * */
    @RequestMapping(value = "searchannounce", method = RequestMethod.POST)
    public void searchAnnounce(HttpServletRequest request,HttpServletResponse response) throws IOException {
        System.out.println("正在获取~");
        response.setContentType("text/html;charset=UTF-8");
        String page = request.getParameter("page");
        String find = request.getParameter("find");
        PrintWriter wirte = null;
        JSONObject json = new JSONObject();
        try {
            wirte = response.getWriter();
            json.put("msg", "ok");
            List list = Announcement.searchAnnounce(Integer.parseInt(page),5,find);         // 每页显示10条
            if( "admin".equals(Token.GetIdentity(request))){
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),Token.GetUser(request),"查找公告："+find,"管理员");
            }else{
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),Token.GetUser(request),"查找公告："+find,"用户");
            }
            System.out.println(list);
            json.put("announce",list);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "fail");
        } finally {
            wirte.print(json);
            wirte.flush();
            wirte.close();
        }
        response.getWriter().print(json);
        return;
    }

    /*
   *    删除公告
        HTTP请求：POST
        请求地址：地址/deleteannounce
        请求参数：
          code
          name
        返回json
        {'msg':'ok'}
        {'msg':'fail'}
   * */
    @RequestMapping(value = "deleteannounce", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> deleteAnnounce(HttpServletRequest request) {
        Map<String, String> data = new HashMap<String, String>();
        if( "admin".equals(Token.GetIdentity(request))) {
            String title = request.getParameter("title");
            String time = request.getParameter("time");
            boolean tmp = Announcement.deleteAnnounce(title, time);
            if (tmp) {
                data.put("msg", "ok");
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),Token.GetUser(request),"删除公告："+title,"管理员");

            } else {
                data.put("msg", "fail");
            }
        }else{
            data.put("msg", "fail");
            data.put("error","权限不足");
        }
        return data;
    }

    /*
   *    显示所有的公告
        HTTP请求：POST
        请求地址：地址/showannounce
        请求参数：
          code
          name
        返回json
        {'msg':'ok'，"announce":""}
        {'msg':'fail'}
   * */
    @RequestMapping(value = "showannounce", method = RequestMethod.POST)
    public void showAnnounce(HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter wirte = null;
        JSONObject json = new JSONObject();
        try {
            wirte = response.getWriter();
            json.put("msg", "ok");
            List list = Announcement.showAnnounce();
            System.out.println(list);
            json.put("announce",list);
        } catch (Exception e) {
            e.printStackTrace();
            json.put("msg", "fail");
        } finally {
            wirte.print(json);
            wirte.flush();
            wirte.close();
        }
        response.getWriter().print(json);
        return;
    }


    /*
  *    获取指定的公告
       HTTP请求：POST
       请求地址：地址/getannounce
       请求参数：
         title
         time
         token
       返回json
       {'msg':'ok'，"title":"","time":"","body":"","isemail":"",}
       {'msg':'fail'}
  * */
    @RequestMapping(value = "getannounce", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> getAnnounce(HttpServletRequest request) {
        String title = request.getParameter("title");
        String time = request.getParameter("time");
        Map<String,String> data = Announcement.getAnnounce(title,time);
        if ( data != null ) {
            data.put("msg","ok");
            //写进操作日志
            //Log.setLog(new Date(),Log.getIPAddress(request),user_name,"登录报名系统","用户");
        } else {
            data.put("msg","fail");
        }
        return data;
    }

    /*
     * 添加管理员
      HTTP请求：POST
      请求地址：地址/addadmin
      请求参数：
        oldpassword
        newusername
        newpassword
        permission
        note
        token
      返回json
      {'msg':'ok'}
      {'msg':'fail'}
 * */
    //添加管理员
    @RequestMapping(value = "addadmin", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> addAdmin(HttpServletRequest request) {
        String token = request.getParameter("token");
        String oldpassword = request.getParameter("oldpassword");
        String newusername = request.getParameter("newusername");
        String newpassword = request.getParameter("newpassword");
        int permission = Integer.parseInt(request.getParameter("permission"));
        String note = request.getParameter("note");
        String user = Token.GetUser(token);
        Map<String,String> data = new HashMap<>();
        if( SetAdmin.isSuper(user) ){            //如果是超级管理员才进行
            boolean tmp = SetAdmin.addAdmin(user,oldpassword,newusername,newpassword,permission,note);
            if ( tmp ) {
                data.put("msg","ok");
                //写进操作日志
                Log.setLog(new Date(),Log.getIPAddress(request),user,"添加管理员："+newusername,"用户");

            } else {
                data.put("msg","fail");
                data.put("error","权限或密码错误");
            }
        }else{
            data.put("msg","fail");
            data.put("error","当前权限无法添加管理员");
        }
        return data;
    }


    /*
    * 显示管理员
     HTTP请求：POST
     请求地址：地址/showadmin
     请求参数：
       token
     返回json
     {'msg':'ok'，"admin":""}
     {'msg':'fail'}
* */
    //显示管理员
    @RequestMapping(value = "showadmin", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> showAdmin(HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        try {
            String account = Token.GetUser(request);
            List list = SetAdmin.showAdmin(account);        //会根据权限决定显示全部还是本身
            if (list != null) {
                data.put("msg", "ok");
                data.put("admin", list);
            } else {
                data.put("msg", "fail");
                data.put("error", "获取失败");

            }
        }catch (Exception e){
            e.printStackTrace();
            data.put("msg", "fail");
            data.put("error", "出现异常");
        }
        return data;
    }

    /*
        * 删除管理员
         HTTP请求：POST
         请求地址：地址/deleteadmin
         请求参数：
           token
           delaccount
         返回json
         {'msg':'ok'}
         {'msg':'fail'}
    * */
    //删除管理员
    @RequestMapping(value = "deleteadmin", method = RequestMethod.POST)
    public @ResponseBody Map<String,String> deleteAdmin(HttpServletRequest request) {
        Map<String,String> data = new HashMap<>();
        try {
            String nowaccount = Token.GetUser(request);
            String delaccount = request.getParameter("delaccount");
            if( nowaccount != null && nowaccount.equals(delaccount) ){
                data.put("msg", "fail");
                data.put("error", "删除失败，无法删除本身");
                return data;
            }
            if( SetAdmin.isSuper(nowaccount)){
                boolean tmp = SetAdmin.delAdmin(nowaccount,delaccount);
                if ( tmp == true ) {
                    data.put("msg", "ok");
                    //写进操作日志
                    Log.setLog(new Date(),Log.getIPAddress(request),nowaccount,"删除管理员："+delaccount,"管理员");
                } else {
                    data.put("msg", "fail");
                    data.put("error", "删除失败，可能权限不足");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            data.put("msg", "fail");
            data.put("error", "出现异常");
        }
        return data;
    }

    /*
        * 显示历史操作
         HTTP请求：POST
         请求地址：地址/showlog
         请求参数：
            token
         返回json
         {'msg':'ok'}
         {'msg':'fail'}
    * */
    //显示历史操作记录
    @RequestMapping(value = "showlog", method = RequestMethod.POST)
    public @ResponseBody Map<String,Object> showLog(HttpServletRequest request) {
        Map<String,Object> data = new HashMap<>();
        if( "admin".equals(Token.GetIdentity(request))){            // 后面考虑是否要超级管理员才能看
            try {
                List list = Log.showLog();
                if( list != null ){
                    data.put("msg","ok");
                    data.put("log",list);
                }else{
                    data.put("msg","fail");
                }
            }catch (Exception e){
                data.put("msg","fail");
            }
        }
        return data;
    }

}
