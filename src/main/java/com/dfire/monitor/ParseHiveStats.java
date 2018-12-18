package com.dfire.monitor;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseHiveStats extends GenericUDTF{

    //解析table信息的正则
    private static final String tableRegex = "(\\w+)\\.(\\w+) stats: \\[numFiles=(\\d+), numRows=(\\d+), totalSize=(\\d+), rawDataSize=(\\d+)]";
    //解析Partition信息的正则
    private static final String ptRegex = "(\\w+)\\.(\\w+)\\{pt=(\\w+)} stats: \\[numFiles=(\\d+), numRows=(\\d+), totalSize=(\\d+), rawDataSize=(\\d+)]";

    @Override
    public StructObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
        if (args.length != 1) {
            throw new UDFArgumentLengthException(
                    "log takes only one argument");
        }
        if (args[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentException(
                    "log takes string as a parameter");
        }
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

        fieldNames.add("dbName");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("tableName");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("pt");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("numFiles");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("numRows");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("totalSize");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("rawDataSize");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(
                fieldNames, fieldOIs);
    }

    @Override
    public void process(Object[] objects) throws HiveException {
        String inputStr = objects[0].toString();
        String desstr = inputStr.replaceAll(" +", " ");

        String[] result = new String[7];

        Pattern tablePatten = Pattern.compile(tableRegex);
        Matcher tableMatcher = tablePatten.matcher(desstr);
        while (tableMatcher.find()){
            //dbName
            result[0] = tableMatcher.group(1);
            //tableName
            result[1] = tableMatcher.group(2);
            //pt
            result[2]= "0";
            //numFiles
            result[3]= tableMatcher.group(3);
            //numRows
            result[4]= tableMatcher.group(4);
            //totalSize
            result[5]= tableMatcher.group(5);
            //rawDataSize
            result[6]= tableMatcher.group(6);

            forward(result);
        }

        Pattern ptPatten = Pattern.compile(ptRegex);
        Matcher ptMacher = ptPatten.matcher(desstr);
        while (ptMacher.find()) {
            //dbName
            result[0] = ptMacher.group(1);
            //tableName
            result[1] = ptMacher.group(2);
            //pt
            result[2]= ptMacher.group(3);
            //numFiles
            result[3]= ptMacher.group(4);
            //numRows
            result[4]= ptMacher.group(5);
            //totalSize
            result[5]= ptMacher.group(6);
            //rawDataSize
            result[6]= ptMacher.group(7);

            forward(result);
        }
    }

    @Override
    public void close() throws HiveException {

    }

    public static void main(String[] args) throws HiveException {
        ParseHiveStats phs = new ParseHiveStats();
        String s = "sdaTable abc.dbaas stats: [numFiles=11, numRows=22, totalSize=33, rawDataSize=44]asd  asdh sdaTable qwe.rty stats: [numFiles=22, numRows=33, totalSize=44, rawDataSize=55]qwe";
        Object[] arg = new Object[]{s};
        phs.process(arg);
    }
}
