package com.dfire;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * nginx 专用
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 2019/5/29
 */
public class ChangeNgLog extends UDF {

    static Pattern emoJi = Pattern.compile("([\000\\x{10000}-\\x{10ffff}\ud800-\udfff])");
    static Pattern precent = Pattern.compile("%");
    static Pattern split = Pattern.compile("\\\\x");


    public static String changeStr(String str) throws UnsupportedEncodingException {
        //todo 需要优化
        str = filterUnicode(str, precent, "%25");
        str = filterUnicode(str, split, "%");
        str = URLDecoder.decode(str, "utf-8");
        str = filterUnicode(str, emoJi, "");
        return str;
    }


    public static void main(String[] args) throws UnsupportedEncodingException, JSONException {
        String s0 = "\\x22\\xE7\\x89\\x9B\\xE6\\xB2\\xB9\\xE6\\x9E\\x9C\\x22";
        String s1 = "{100.121.134.234 10.111.0.251 - [28/May/2019:21:41:28 +0800] app.dailuobo.com \"POST /getAd.cgi HTTP/1.1\" 200 264 0.014 \"10.111.7.67:4567\" \"0.014\" \"-\" \"okhttp/3.8.1\" \"112.32.94.90\" {\\x22deveiceModel\\x22:\\x22vivo X9\\x22,\\x22longitude\\x22:\\x22117.383997\\x22,\\x22IDFA\\x22:\\x22\\x22,\\x22system\\x22:\\x22android\\x22,\\x22deveiceToken\\x22:\\x227+m8cxTPpBF9NzAW\\x5C/XeOeqQdJT2vKPxYxQbcjKVCj8U=\\x22,\\x22shopId\\x22:\\x22179\\x22,\\x22IMEI\\x22:\\x22866042030886514\\x22,\\x22appVersion\\x22:\\x223.0.1\\x22,\\x22isDebug\\x22:\\x220\\x22,\\x22version\\x22:\\x227.1.2\\x22,\\x22latitude\\x22:\\x2231.877184\\x22,\\x22netType\\x22:\\x22wifi\\x22,\\x22cityId\\x22:\\x2230\\x22,\\x22data\\x22:{}}\n";
        String s2 = "100.121.136.5 10.111.101.38 - [28/May/2019:17:55:01 +0800] _ \"HEAD / HTTP/1.0\" 200 0 0.000 \"-\" \"-\" \"-\" \"-\" \"-\"";
        String s3 = "61.219.11.153 10.111.143.208 - [29/May/2019:11:48:24 +0800] _ \"\\x01\\x02\\x03\\x04\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\\x00\" 400 591 0.000 \"-\" \"-\" \"-\" \"-\" \"-\"\n";

        System.out.println(getNginxLog(s2));

    }

    public static String getNginxLog(String str) throws UnsupportedEncodingException, JSONException {
        JSONObject object = new JSONObject();
        String[] org = str.split("\"");
        String[] f0 = org[0].split(" ");
        String[] f1 = org[1].split(" ");
        String[] f2 = org[2].split(" ");
        String[] f11 = org[11].split(",");

        object.put("remote_addr", getMesage(f0,0));
        object.put("server_addr", getMesage(f0,1));
        object.put("remote_user", getMesage(f0,2));
        object.put("time_local", getMesage(f0,3) + " " + getMesage(f0,4));
        object.put("domain", getMesage(f0,5));
        object.put("method", getMesage(f1,0));
        object.put("request", getMesage(f1,1));
        object.put("http_version", getMesage(f1,2));
        object.put("status", getMesage(f2,1));
        object.put("body_byte_send", getMesage(f2,2));
        object.put("request_time", getMesage(f2,3));
        object.put("upstream_addr", getMesage(org,3));
        object.put("upstream_response_time", getMesage(org,5));
        object.put("referrer", getMesage(org,7));
        object.put("agent", getMesage(org,9));
        object.put("http_x_forwarded_for", getMesage(f11,0));
        object.put("post_message", changeStr(getMesage(org,12)));
        return object.toString();
    }

    private static String getMesage(String[] f,int index){
        if(f.length - 1 >= index){
            return f[index];
        }
        return "";
    }


    public String evaluate(String str) {
        try {
            return getNginxLog(str);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(str);
        }
        throw new IllegalArgumentException("error : " + str);
    }


    public static String filterUnicode(String source, Pattern pattern, String replace) {
        if (source != null) {

            Matcher emojiMatcher = pattern.matcher(source);
            if (emojiMatcher.find()) {
                source = emojiMatcher.replaceAll(replace);
                return source;
            }
            return source;
        }
        return source;
    }
}

