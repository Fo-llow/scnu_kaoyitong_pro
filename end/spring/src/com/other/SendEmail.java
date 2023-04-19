package com.other;

import com.dao.Dao;

import javax.mail.*;
import javax.mail.internet.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class SendEmail {
    //发送邮件(发送的人，发送的主题，发送的内容)
    public static boolean SendEmailAll(String tto,String ttitle,String tcontent) {
        //http://www.jspkongjian.com/news.jsp?id=860
        String qm = "GLFGPMUUBKEDPWKQ";          //注意这里是邮箱授权码！！！（邮箱必须开启SMTP服务）
        String tu = "163.com";                  //你邮箱的后缀域名
        // String tto="XX@qq.com";
        //String ttitle = "密码重置——华南师范大学计算机学院本科教学信息管理系统";
        //String tcontent = "密码重置的验证码为：" + codeNum + "。如果不是本人操作，请忽略此邮件。";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp." + tu);//发信的主机，这里我填写的是我们公司的主机！可以不用修改！
        //添加的部分，换成465SSL发信
        //props.put("mail.smtp.port", "465");
        //props.put("mail.transport.protocol", "smtp");
        //props.put( "mail.smtp.ssl.enable", true);

        props.put("mail.smtp.auth", "true");
        Session s = Session.getInstance(props);
        s.setDebug(true);
        MimeMessage message = new MimeMessage(s);
        //给消息对象设置发件人/收件人/主题/发信时间
        try {
            InternetAddress from = new InternetAddress("13202048244@" + tu); //这里的改为您发信的邮箱号
            message.setFrom(from);
            InternetAddress to = new InternetAddress(tto);
            message.setRecipient(Message.RecipientType.TO, to);
            message.setSubject(ttitle);
            message.setSentDate(new Date());
            //给消息对象设置内容
            BodyPart mdp = new MimeBodyPart();//新建一个存放信件内容的BodyPart对象
            mdp.setContent(tcontent, "text/html;charset=gb2312");//给BodyPart对象设置内容和格式/编码方式
            Multipart mm = new MimeMultipart();//新建一个MimeMultipart对象用来存放BodyPart对
            //象(事实上可以存放多个)
            mm.addBodyPart(mdp);//将BodyPart加入到MimeMultipart对象中(可以加入多个BodyPart)
            message.setContent(mm);//把mm作为消息对象的内容
            message.saveChanges();
            Transport transport = s.getTransport("smtp");
            transport.connect("smtp." + tu, "13202048244", qm); //这里的也要修改为邮箱号
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("邮箱发送成功");
            return true;
        } catch (AddressException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            System.out.println(e);
            e.printStackTrace();
        } catch (MessagingException e) {
            System.out.println(e);
            e.printStackTrace();
        }
        System.out.println("邮箱发送失败");
        return false;
    }

    //找到所有人的邮箱
    public static List FindEmailAll(){
        List list =  new ArrayList();
        try {
            Connection conn = Dao.getConnection();              //连接数据库
            PreparedStatement p = conn.prepareStatement("select email from user_information;");       //返回所有邮箱地址
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                String email = rs.getString("email");
                list.add(email);
            }
            Dao.close(rs, p, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }




}
