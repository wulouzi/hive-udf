package com.dfire;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 17/8/30
 */
public class HexToStr extends UDF {

    public static String changeStr(String str) throws UnsupportedEncodingException {
        //todo 需要优化
        str = str.replaceAll("%", "%25");
        str = str.replaceAll("\\\\x", "%");
        String decode = URLDecoder.decode(str, "utf-8");
        return decode;

    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        String s0 = "\\x22220\\xE7\\x89\\x9B\\xE6\\xB2\\xB9\\xE6\\x9E\\x9C\\x22";
        s0 = "100.121.134.224 10.111.7.92 - [28/May/2019:07:18:04 +0800] app.dailuobo.com \"POST /getSearchContent.cgi HTTP/1.1\" 200 84 0.005 \"10.111.7.127:4567\" \"0.005\" \"-\" \"Dalvik/2.1.0 (Linux; U; Android 8.1.0; PAFM00 Build/OPM1.171019.026)\" \"183.160.100.110\" {\\x22IDFA\\x22:\\x22\\x22,\\x22IMEI\\x22:\\x22867278049761590\\x22,\\x22appVersion\\x22:\\x223.0.1\\x22,\\x22cityId\\x22:\\x2230\\x22,\\x22deveiceModel\\x22:\\x22PAFM00\\x22,\\x22deveiceToken\\x22:\\x2288KwF/NjxGBcz1kLIJSLg5aCbuaqCx/OKpyFnI1mcpY\\x5Cu003d\\x22,\\x22isDebug\\x22:\\x220\\x22,\\x22latitude\\x22:\\x2231.911211\\x22,\\x22longitude\\x22:\\x22117.327551\\x22,\\x22netType\\x22:\\x22wifi\\x22,\\x22shopId\\x22:\\x22522\\x22,\\x22system\\x22:\\x22android\\x22,\\x22version\\x22:\\x228.1.0\\x22,\\x22data\\x22:{\\x22productName\\x22:\\x22\\xE7\\x95\\x85\\xE6\\x84\\x8F100%\\x22}}";
        System.out.println(s0);
        System.out.println(changeStr(s0));

    }

    public String evaluate(String str) {
        try {
            return changeStr(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("error : " + str);
    }

}
