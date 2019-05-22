package com;

import java.io.UnsupportedEncodingException;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 2019/5/22
 */
public class Test2 {

    private static String hexString = "0123456789ABCDEF";

    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "\\x22\\xE7\\x89\\x9B\\xE6\\xB2\\xB9\\xE6\\x9E\\x9C\\x22";
        String s1 = "\\xe6\\x88\\x91\\xe6\\x98\\xaf\\xe8\\xb0\\x81\\xef\\xbc\\x9f";
        //
        byte[] b = {(byte) 0xe6, (byte) 0x88, (byte) 0x91};


        byte[] bs = new byte[3];
        for (int i = 0; i < 3; i++) {
//            System.out.println(s1.getBytes()[i]);
            bs[i] = (byte) 0xe6;
        }
        t2(s);
    }


    public static void t2(String str1) {//字符串转换为ASCII码
        String str = "e4b8ade59bbd2ce4b8ade59bbde9a699e6b8af";// 需要转换的字符串
        str = str1.replaceAll("x", "");
        str = str.replaceAll("\\\\", "");

        byte[] b = new byte[str.length() / 2];// 每两个字符为一个十六进制确定数字长度
        for (int i = 0; i < b.length; i++) {
            // 将字符串每两个字符做为一个十六进制进行截取
            String a = str.substring(i * 2, i * 2 + 2);
            b[i] = (byte) Integer.parseInt(a, 16);// 将如e4转成十六进制字节，放入数组
        }

        try {
            // 将字节数字以utf-8编码以字符串形式输出
            System.out.println(new String(b, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
