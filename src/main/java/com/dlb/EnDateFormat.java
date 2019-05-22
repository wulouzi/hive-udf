package com.dlb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;


/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 2019/4/26
 */
public class EnDateFormat extends UDF {
    public SimpleDateFormat inputDate = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
    public SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Text evaluate(Text time)
    {
        String date = null;
        if (time == null) {
            return null;
        }
        if (StringUtils.isBlank(time.toString())) {
            return null;
        }
        String parse = time.toString().replaceAll("\"", "");
        try
        {
            Date parseDate = this.inputDate.parse(parse);
            date = this.outputDate.format(parseDate);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return new Text(date);
    }


    public static void main(String[] args) {
        SimpleDateFormat inputDate = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String time = "31/Aug/2015:00:04:37";
        String parse = time.toString().replaceAll("\"", "");
        try
        {
            Date parseDate = inputDate.parse(parse);
            String date = outputDate.format(parseDate);
            System.out.println(date);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}


