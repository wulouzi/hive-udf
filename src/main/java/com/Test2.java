package com;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 2019/5/22
 */
public class Test2 {

    private static String hexString = "0123456789ABCDEF";

    public static void main(String[] args) throws IOException {
        String s = "\\x22\\xE7\\x89\\x9B\\xE6\\xB2\\xB9\\xE6\\x9E\\x9C\\x22";
        String s1 = "\\xe6\\x88\\x91\\xe6\\x98\\xaf\\xe8\\xb0\\x81\\xef\\xbc\\x9f";
        //
        byte[] b = {(byte) 0xe6, (byte) 0x88, (byte) 0x91};


        byte[] bs = new byte[3];
        for (int i = 0; i < 3; i++) {
//            System.out.println(s1.getBytes()[i]);
            bs[i] = (byte) 0xe6;
        }
        writeHexByte("\"\"中\"".getBytes());
    }


    private static void writeHexByte(byte[] b) throws IOException {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            int temp1=(b[i] & 0xf0) >> 4;//byte转16进制
            char temp11=bin2char(temp1);
            int temp111=(int)temp11;
            ///////////////////////////////////
            int temp2=b[i] & 0x0f;//byte转16进制
            char temp22=bin2char(temp2);
            int temp222=(int)temp22;
            s.append(temp11);
        }
        System.out.println(s);
    }

    private static char bin2char(int bin) {
        char c;
        c=(char) (bin < 10 ? bin + '0' : bin - 10 + 'A');
        return c;
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



    public static String encode(String str){
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < str.length(); i++) {
            list.add(str.substring(i, i+1));
        }
        StringBuilder sb = new StringBuilder();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            sb.append("_");
            if("_".equals(list.get(i))){
                sb.append("\\UL");
            }else if(list.get(i).matches("^[0-9]$")){
                Integer v = Integer.valueOf(list.get(i));
                if(i<len-1&&v>0){
                    while(v>=0){
                        sb.append(list.get(i+1));
                        v--;
                    }
                }else{
                    sb.append(list.get(i));
                }
            }else{
                sb.append(list.get(i));
            }
        }
        sb.delete(0, 1);
        return sb.toString();
    }
    /**
     * 1.如果有\UL 则转为_；
     * 2.如果是重复的叠字，则转化为长度-1；
     * @param str
     * @return
     */
    public static String decode(String str){

        String[] s = str.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length; i++) {
            if(s[i].equals("\\UL")){
                sb.append("_");
            }else if(isSame(s[i])){
                sb.append(changeFromSame(s[i]));
            }else if("".equals(s[i])){
                int j=-1;
                while(i<(s.length-1)&&"".equals(s[i++])){
                    j++;
                }
                i--;
                sb.append(j-1).append("_");
            }else{
                sb.append(s[i]);
            }
        }

        return sb.toString();
    }
    /**
     * 如果是叠字，返回true，否则，返回false
     * @param str
     * @return
     */
    public static boolean isSame(String str){
        if(str == null || str.length() < 2){
            return false;
        }
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length-1; i++) {
            if(c[i] != c[i+1]){
                return false;
            }
        }
        return true;
    }

    /**
     * 将叠字转化为指定类型的值
     * @param str
     * @return
     */
    public static String changeFromSame(String str){
        Integer len = str.length()-1;
        return len.toString();
    }
}
