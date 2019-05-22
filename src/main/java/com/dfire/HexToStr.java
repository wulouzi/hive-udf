package com.dfire;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 17/8/30
 */
public class HexToStr extends UDF {

    public static String changeStr(String str){
        str = str.replaceAll("x","");
        str = str.replaceAll("\\\\","");

        byte[] b = new byte[str.length() / 2];
        for (int i = 0; i < b.length; i++) {
            // 将字符串每两个字符做为一个十六进制进行截取
            String a = str.substring(i * 2, i * 2 + 2);
            b[i] = (byte) Integer.parseInt(a, 16);
        }

        try {
            // 将字节数字以utf-8编码以字符串形式输出
            return new String(b, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("error str :"+str);
    }


    public static void main(String[] args) {
        String s = "\\x22\\xE7\\x89\\x9B\\xE6\\xB2\\xB9\\xE6\\x9E\\x9C\\x22";
        System.out.println(changeStr(s));
    }

    public  String evaluate(String str){
        return changeStr(str);
    }

}
