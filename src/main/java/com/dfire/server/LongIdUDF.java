package com.dfire.server;

import com.dfire.util.MagicSnowFlake;
import org.apache.hadoop.hive.ql.exec.MapredContext;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.UDFType;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 18/3/8
 */
@UDFType(deterministic = false, stateful = true)
public class LongIdUDF extends GenericUDF {
    private static final char SEPARATOR = '_';
    private static final String ATTEMPT = "attempt";
    private long mapTaskId = 0l;
    private int increment = 0;

    private MagicSnowFlake snowFlake;



    @Override
    public void configure(MapredContext context) {
        increment = context.getJobConf().getNumMapTasks();
        if(increment == 0) {
            throw new IllegalArgumentException("mapred.map.tasks is zero");
        }

        mapTaskId = getInitId(context.getJobConf().get("mapred.task.id"),increment);
        if(mapTaskId == 0l) {
            throw new IllegalArgumentException("mapred.task.id");
        }
    }

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments)
            throws UDFArgumentException {
        return PrimitiveObjectInspectorFactory.javaLongObjectInspector;
    }

    @Override
    public Long evaluate(DeferredObject[] arguments) throws HiveException {
        if(snowFlake == null){
            int dataCenterId = Integer.parseInt(arguments[0].get().toString());
            snowFlake = new MagicSnowFlake(getMapTaskId(),dataCenterId);
        }
        return snowFlake.nextId();
    }

    @Override
    public String getDisplayString(String[] children) {
        return "getLongId(0)";
    }


    private synchronized long getMapTaskId() {
        return mapTaskId;
    }

    //attempt_1478926768563_0537_m_000004_0 // return 0+1
    private long getInitId (String taskAttemptIDstr,int numTasks)
            throws IllegalArgumentException {
        try {
            String[] parts = taskAttemptIDstr.split(Character.toString(SEPARATOR));
            if(parts.length == 6) {
                if(parts[0].equals(ATTEMPT)) {
                    if(!parts[3].equals("m") && !parts[3].equals("r")) {
                        throw new Exception();
                    }
                    long result = Long.parseLong(parts[4]);
                    if(result >= numTasks) { //if taskid >= numtasks
                        throw new Exception("TaskAttemptId string : " + taskAttemptIDstr  + "  parse ID [" + result + "] >= numTasks[" + numTasks + "] ..");
                    }
                    return result + 1;
                }
            }
        } catch (Exception e) {}
        throw new IllegalArgumentException("TaskAttemptId string : " + taskAttemptIDstr + " is  not properly formed");
    }


    public static void main(String[] args) {
        String s = "attempt_1478926768563_0537_m_000004_4";
        System.out.println(new LongIdUDF().getInitId(s,5));
    }

}
