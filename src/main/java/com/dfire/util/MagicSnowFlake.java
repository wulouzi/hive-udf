package com.dfire.util;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 18/3/8
 */
public class MagicSnowFlake {

    /** 其实时间戳   2017-01-01 00:00:00 */
    private final static long twepoch = 1483200000000l;




    /** map 的限制 65535，认为MAP的最大数量限制 14位 */
    private final static long ipIdMax = 1024*16L;

    /** job_id 组  64个个最多 */
    private final static long jobIdBits = 5L;

    /** 9 位地址 （mapIdBits + dataCenterIdBits ）*/
    private final static long mapIdBits = 9L;

    private final static long dataCenterIdMax = ~ (-1L << jobIdBits);

    /** 序列在id中占的位数 8bit */
    private final static long seqBits = 8L;

    /** 序列最大值 4095 即2的12次方减一 */
    private final static long seqMax = ~(-1L << seqBits);

    /** 64位的数字：首位0  随后41位表示时间戳 MAP_ID 最后 8位序列号 */
    private final static long dataCenterIdLeftShift = seqBits;
    private final static long mapIdLeftShift = seqBits + jobIdBits;
    private final static long timeLeftShift = seqBits  + jobIdBits + mapIdBits;

    /** IP标识(0~255) */
    private long ipId;

     /**  数据中心ID(0~3) */
    private long JobdId;

     /**  毫秒内序列(0~4095) */
    private long seq = 0L;

     /**  上次生成ID的时间截 */
    private long lastTime = -1L;

    public MagicSnowFlake(long ipId, long jobId) {
        if(ipId < 0 || ipId > ipIdMax) {
            System.out.println(" ---------- ipId不在正常范围内(0~"+ipIdMax +") " + ipId);
            System.exit(0);
        }

        if(JobdId < 0 || JobdId > dataCenterIdMax) {
            System.out.println(" ---------- dataCenterId不在正常范围内(0~"+dataCenterIdMax +") " + JobdId);
            System.exit(0);
        }

        this.ipId = ipId;
        this.JobdId = JobdId;
    }

    public synchronized long nextId() {
        long nowTime = System.currentTimeMillis();

        if(nowTime < lastTime) {
            System.out.println(" ---------- 当前时间前于上次操作时间，当前时间有误: " + nowTime);
            System.exit(0);
        }

        if(nowTime == lastTime) {
            seq = (seq + 1) & seqMax;
            if(seq == 0) {
                nowTime = getNextTimeStamp();
            }
        } else {
            seq = 0L;
        }

        lastTime = nowTime;


        return ((nowTime - twepoch) << timeLeftShift)
                | (ipId << mapIdLeftShift)
                | (JobdId << dataCenterIdLeftShift)
                | seq;
    }

    private long getNextTimeStamp() {
        long nowTime;
        do {
            nowTime = System.currentTimeMillis();
        } while(nowTime <= lastTime);
        return nowTime;
    }

    public static void main(String[] args) {
        MagicSnowFlake m = new MagicSnowFlake(12345,4);
        System.out.println(m.nextId());
        // 4149295021352300544
        // 259331017536450560
        // 259331629934268416
        // 259331692748910592
        // 1074562925634867202
        // 259342802721648640
        System.out.println("259331017536450560".length());


    }
}


