package com.dfire.server;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.UUID;

/**
 * UUID 生成工具
 * Created by qqr on 16/6/16.
 */
public class Test extends UDF {

    /**
     * 构造ID生成器.
     */
    public Test() {
    }

    /**
     * 得到一个UUID.
     *
     * @return 新的UUID.
     */
    public static String evaluate(String str) {
        return "hello world "+str;
    }

    public static String format(String str) {
        return "hello world "+str;
    }
}
