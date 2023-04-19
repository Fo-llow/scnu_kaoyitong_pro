package com.dao;

import java.util.ArrayList;
import java.util.List;

public class SqlArray {
    /*
    * 存储数组进数据库
    * 数据库用一个字段表示数组，字段类型为文本类型。
    * 程序存入数组到数据库的时候，利用join方法把数组转换为分隔符分隔的字符串，比如你的例子数组a[1]="第一步";a[2]="第二步";合并后为"第一步|第二步"，把这个合并后的字符串存入数据库你是会的。
    * 从数据库里面取出合并后的字符串"第一步|第二步"以后，利用split方法可以转换为数组。
    * 这个方法的最大的优点是可以保存个数不确定的数组，程序编写相当简单。
    * */

    //合并
    public static String Merge(String[] nameArr){
        String nameAll = "";
        for( int i = 0 ; i < nameArr.length ; i++ ){
            nameAll += (nameArr[i] + "|");
            if( i == nameArr.length - 1){
                nameAll += nameArr[i];
            }
        }
        return nameAll;
    }

    //合并
    public static String Merge(List nameArr){
        if( nameArr.size() == 1 )   return String.valueOf(nameArr.get(0));
        String nameAll = "";
        for( int i = 0 ; i < nameArr.size() ; i++ ){
            nameAll += String.valueOf((nameArr.get(i) + "|"));
            if( i == nameArr.size() - 1){
                nameAll += String.valueOf(nameArr.get(i));
            }
        }
        return nameAll;
    }

    //分离（数组形式）
    public static String[] SeparationArray(String nameAll){
        String[] nameArr = nameAll.split("\\|");
        return nameArr;
    }

    //分离（列表形式）
    public static List SeparationList(String nameAll){
        String[] nameArr = nameAll.split("\\|");
        List list = new ArrayList();
        for(String name:nameArr){
            list.add(name);
        }
        return list;
    }

}
