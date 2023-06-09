package com.other;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.UUID;

public class FileUpload {

    //避免重命名
    public static String getUuidFileName(String fileName){
        //文件名以：uuid+"_"+文件的原始名称
        return UUID.randomUUID().toString()+ "_" +fileName;
    }

    //获取本来的名字

    //保存到服务器
    //返回位置
    public static String SaveServer(MultipartFile file, HttpServletRequest request){
        System.out.println("正在上传文件");
        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        //String realpath = request.getServletContext().getRealPath("/WEB-INF/files");
        //暂时就不保存在WEB-INF那里，先放在files
        String realpath = request.getServletContext().getRealPath("files");

        //获取文件名字
        String fileName = getUuidFileName(file.getOriginalFilename());

        //文件上传
        File targetFile = new File(realpath, fileName);

        //如果不存在，创建文件
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        // 上传
        try {
            file.transferTo(targetFile);
            System.out.println("上传成功");
            //return realpath + '\\' + fileName;
            //返回相对路径就行
            return "\\files\\" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
