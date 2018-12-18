package com.dfire.server;


import org.apache.hadoop.hive.ql.exec.UDF;


/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 18/6/12
 */
public class ShortUrlEncode extends UDF {


    /**
     *  @排骨 @冰石 负责
     */
    public static final String CODE_STRING = "vPh7zZwA2LyU4bGq5tcVfIMxJi6XaSoK9CNp0OWljYTHQ8REnmu31BrdgeDkFs";




    public  String evaluate(String code){
        Long lng = Long.parseLong(code);
        return  encode(lng);
    }


    /**
     * 数字转短网址
     *
     * @param lng
     * @return 短网址字符串
     */
    public static String encode(Long lng) {
        String out = "";
        double val = lng;
        for (double t = Math.floor(Math.log10(val) / Math.log10(62)); t >= 0; t--) {
            double a = Math.floor(val / Math.pow(62, t));
            out = out + CODE_STRING.substring((int) a, (int) a + 1);
            val = val - (a * Math.pow(62, t));
        }
        return out;
    }

    /**
     * 短网址转数字
     *
     * @param str
     * @return 数字字符串
     */
    public static Long decode(String str) {
        double out = 0;
        int len = str.length() - 1;
        for (int t = 0; t <= len; t++) {
            out = out + CODE_STRING.indexOf(str.substring(t, t + 1)) * Math.pow(62, len - t);
        }
        return (long) out;
    }

    public static void main(String[] args) {
        System.out.println(ShortUrlEncode.encode(3072L));
    }

}
