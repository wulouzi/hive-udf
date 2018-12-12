package com.dfire.monitor;

import org.apache.hadoop.hive.ql.exec.UDF;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseMR extends UDF {

    public String evaluate(final String instr) {
        long sumMapNum = 0;
        long sumReduceNum = 0;
        double sumCPUTime = 0.0;
        long sumHDFSWriteByte = 0;
        long sumHDFSReadByte = 0;
        long mapNum = 0;
        long reduceNum = 0;
        double cpuTime = 0.0;
        long readByte = 0;
        long writeByte = 0;

        String outStr = String.format(
            "sumMapNum:%d,sumReduceNum:%d,sumCPUTime:%f,sumHDFSWriteByte:%d,sumHDFSReadByte:%d", sumMapNum,
            sumReduceNum, sumCPUTime, sumHDFSWriteByte, sumHDFSReadByte);

        if (!"null".equals(instr) && instr != null) {
            String desstr = instr.replaceAll(" +", " ");

            String MRregex = "Map: (\\d+) Reduce: (\\d+) Cumulative CPU: ([\\d\\.]+) sec HDFS Read: (\\d+) HDFS Write: (\\d+) SUCCESS";
            Pattern MRpattern = Pattern.compile(MRregex);
            Matcher mMatcher = MRpattern.matcher(desstr);
            while (mMatcher.find()) {
                mapNum = Long.parseLong(mMatcher.group(1));
                reduceNum = Long.parseLong(mMatcher.group(2));
                cpuTime = Double.parseDouble(mMatcher.group(3));
                readByte = Long.parseLong(mMatcher.group(4));
                writeByte = Long.parseLong(mMatcher.group(5));

                sumMapNum += mapNum;
                sumReduceNum += reduceNum;
                sumCPUTime += cpuTime;
                sumHDFSReadByte += readByte;
                sumHDFSWriteByte += writeByte;

/*				System.out.println(mMatcher.group(0));
				System.out.println(mapNum);
				System.out.println(reduceNum);
				System.out.println(cpuTime);
				System.out.println(readByte);
				System.out.println(writeByte);*/
            }

            MRregex = "Map: (\\d+) Cumulative CPU: ([\\d\\.]+) sec HDFS Read: (\\d+) HDFS Write: (\\d+) SUCCESS";
            MRpattern = Pattern.compile(MRregex);
            mMatcher = MRpattern.matcher(desstr);
            while (mMatcher.find()) {
                mapNum = Long.parseLong(mMatcher.group(1));
                reduceNum = 0;
                cpuTime = Double.parseDouble(mMatcher.group(2));
                readByte = Long.parseLong(mMatcher.group(3));
                writeByte = Long.parseLong(mMatcher.group(4));

                sumMapNum += mapNum;
                sumReduceNum += reduceNum;
                sumCPUTime += cpuTime;
                sumHDFSReadByte += readByte;
                sumHDFSWriteByte += writeByte;

/*				System.out.println(mMatcher.group(0));
				System.out.println(mapNum);
				System.out.println(reduceNum);
				System.out.println(cpuTime);
				System.out.println(readByte);
				System.out.println(writeByte);*/
            }

            outStr = String.format(
                "sumMapNum:%d,sumReduceNum:%d,sumCPUTime:%d,sumHDFSWriteByte:%d,sumHDFSReadByte:%d", sumMapNum,
                sumReduceNum, (new Double(sumCPUTime)).intValue(), sumHDFSWriteByte, sumHDFSReadByte);
        }

        return outStr;
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ParseMR pmr = new ParseMR();

        System.out.println(pmr.evaluate(null));
    }

}
