package com.dfire;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 17/8/30
 */
public class UDFTimeZone extends UDF {

    /**
     *
     * @param unixTime4MS unix时间，单位毫秒
     * @param formats 输出格式，如不传，默认格式为：yyyy-MM-dd HH:mm:ss
     * @param _timeZone 时区字符串，格式：GMT+8，兼容时区配置项的格式（+8_1)
     * @return String
     */
    public static String timeToDate(Long unixTime4MS, String formats, String _timeZone){
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        if (!StringUtils.isEmpty(formats)){
            dateFormat = formats;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        int split = _timeZone.indexOf("_");
        if (split >= 0){
            _timeZone = _timeZone.substring(0, split);
        }
        TimeZone timeZone = null;
        if (StringUtils.isEmpty(_timeZone)) {
            timeZone = TimeZone.getDefault();
        } else {
            if (!_timeZone.startsWith("GMT")){
                _timeZone = "GMT" + _timeZone;
            }
            timeZone = TimeZone.getTimeZone(_timeZone);
        }
        sdf.setTimeZone(timeZone);
        return sdf.format(new Date( unixTime4MS ));
    }
//    原有的时区配置写反了，目前是（-8_1）形式，将统一改为+8形式。


    public static void main(String[] args) {
        System.out.println(timeToDate(1505196443418L,null,"+8"));
    }

    public  String evaluate(String unixTime4MS, String formats, String _timeZone){
        if (StringUtils.isEmpty(unixTime4MS)) {
            unixTime4MS = System.currentTimeMillis()+"";
        }
        return timeToDate(Long.parseLong(unixTime4MS),formats,_timeZone);
    }

}
