package com.dfire.server;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 2019/1/16
 */
public class Md5Udf  extends UDF  {

    static MessageDigest digester;

    static {
        try {
            digester = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String evaluate(String... args) {
       if(args.length == 0){
           throw new NullPointerException("need firlds");
       }
       StringBuilder sb = new StringBuilder(32);
       for(String s : args){
           sb.append(s);
       }

        String digest = null;
        StringBuffer buffer = new StringBuffer();
        try {

            byte[] digestArray = digester.digest(sb.toString().getBytes("UTF-8"));
            for (int i = 0; i < digestArray.length; i++) {
                buffer.append(String.format("%02x", digestArray[i]));
            }
            digest = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return digest;
    }


    public static void main(String[] args) {
        Md5Udf md5Udf = new Md5Udf();
        System.out.println(md5Udf.evaluate(new String[]{"sdfd","234"}));
    }

}
