package com.dfire;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.ArrayList;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 2019/1/8
 */
public class SplitUdtf extends GenericUDTF {


    public void process(Object[] cols) throws HiveException {

        String[] field = cols[0].toString().split(";");
        int length = field.length;
        int rowLength = cols.length;
        for(int i=0;i<length;i++){
            String[] row = new String[rowLength];
            for(int m=0;m<rowLength;m++){
                String value = cols[m].toString().split(";")[i];
                if(!"".equals(value.trim())){
                    row[m] = value;
                }

            }
            forward(row);

        }
    }



    public StructObjectInspector initialize(ObjectInspector[] args) throws UDFArgumentException {
        if (args.length != 1) {
            throw new UDFArgumentLengthException("ExplodeMap takes only one argument");
        }
        if (args[0].getCategory() != ObjectInspector.Category.PRIMITIVE) {
            throw new UDFArgumentException("ExplodeMap takes string as a parameter");
        }
        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

        int length  = args.length;
        for(int i=0;i<length;i++){
            fieldNames.add("col"+i);
            fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        }

         //定义了行的列数和类型
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames,fieldOIs);
    }

    public void close() throws HiveException {

    }

}
