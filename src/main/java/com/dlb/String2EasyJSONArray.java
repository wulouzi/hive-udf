package com.dlb;

import com.alibaba.fastjson.JSONArray;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class String2EasyJSONArray extends UDF {
    public String evaluate(String input) {
        return Arrays.stream(JSONArray.parseArray(input).toArray())
                .map(Object::toString)
                .filter(Objects::nonNull)
                .filter(item->!item.equalsIgnoreCase("null"))
                .collect(Collectors.joining("###"));
    }

//    public String evaluate(String input) {
//        JSONArray jsonArray = JSONArray.parseArray(input);
//
//        String output = "";
//        if (jsonArray.size() > 0) {
//            for (int i = 0; i < jsonArray.size(); i++) {
//                if (i == jsonArray.size() - 1 && jsonArray.getJSONObject(i).toString() != "null") {
//                    output += (jsonArray.getJSONObject(i).toString());
//                }
//                else if (jsonArray.getJSONObject(i).toString() != "null") {
//                    output += (jsonArray.getJSONObject(i).toString()) + "###";
//                }
//
//            }
//        }
//        return output;
//    }

    public static void main(String[] args) {
        String2EasyJSONArray string2EasyJSONArray = new String2EasyJSONArray();
        String input = "[{\"id\":1},,{\"id\":2}]";
        System.out.println(string2EasyJSONArray.evaluate(input));
    }
}
