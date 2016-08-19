package com.dfire;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by qqr on 16/6/4.
 */
public class SplitCol extends GenericUDTF {
    @Override
    public StructObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
        int length  = args.length;
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();
        for(int i=0;i<length;i++){
            fieldNames.add("col"+i);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        }


        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    @Override
    public void process(Object[] cols) throws HiveException {
//        System.out.println(Arrays.toString(cols));

        String[] field = cols[0].toString().split(",");
        int length = field.length;
        int rowLength = cols.length;
        for(int i=0;i<length;i++){
           String[] row = new String[rowLength];
           for(int m=0;m<rowLength;m++){
               row[m] = cols[m].toString().split(",")[i];
           }
//            System.out.println(Arrays.toString(row));
        }

    }

    @Override
    public void close() throws HiveException {

    }




    public static void main(String[] args) throws HiveException {
        Object xx[]  = new Object[]{"11,22,1","aa,bb,6"};
        SplitCol t = new SplitCol();
        t.process(xx);
    }
}
