package com.security;
import com.security.Encryption;

// 封装加密解密
public class EncryUtils {
    private static String passkey = "Fo4x+NFFOUe//VWn+L4DXWJUyENWTg5NkvwNNcfpoME=";
    /**
     * 加密密码 (密码加密是SHA256)
     * 参数：未加密前的密码
     * 返回：加密后的密码
     */
    public static String PassEncry(String password){
        return Encryption.getSHA256StrJava(password);
    }
    /**
     * 加密普通信息(AES)
     * 参数：未加密前的信息
     * 返回：加密后的信息
     */
    public static String JiaInfoEncry(String info){
        //System.out.println("要加密的数据是"+info);
        //System.out.println(Encryption.AESdecrypt(passkey,"124wqhsajasdSAF==1"));
        //System.out.println(Encryption.AESencrypt(info,Encryption.AESdecrypt(passkey,"124wqhsajasdSAF==1")));
        //passkey 其实是 "ogovZLSHVbFoHVgin7qy2A=="
        //return Encryption.AESencrypt(info,Encryption.AESdecrypt(passkey,"124wqhsajasdSAF==1"));
        return Encryption.AESencrypt(info,"ogovZLSHVbFoHVgin7qy2A==");
    }

    /**
     * 解密普通信息
     * 参数：加密后的信息
     * 返回：未加密前的信息
     */
    public static String JieInfoDecry(String info){
        //return Encryption.AESdecrypt(info,Encryption.AESdecrypt(passkey,"124wqhsajasdSAF==1"));
        return Encryption.AESdecrypt(info,"ogovZLSHVbFoHVgin7qy2A==");
    }

    /**
     *  加密信息——为了可以模糊搜索
     * 参数：加密前的信息
     * 返回：加密后的信息
     */
    public static String JiaFindEncry(String str){
        return str;
    }

    /**
     * 解密信息
     */
    public static String JieFindDecry(String str){
        return str;
    }



}
