package com.dfire.util;

/**
 * @author <a href="mailto:huoguo@2dfire.com">火锅</a>
 * @time 18/3/8
 */
public class MagicSnowFlake {

    //其实时间戳   2017-01-01 00:00:00
    private final static long twepoch = 1483200000000l;

    // 改到16位 65535，认为MAP 的最大数量限制
    private final static long ipIdBits = 16L;

    //65535。
    private final static long ipIdMax = ~ (-1L << ipIdBits);

    // 默认1位
    private final static long dataCenterIdBits = 1L;

    //数字标识id最大值 3  即2的2次方减一。
    private final static long dataCenterIdMax = ~ (-1L << dataCenterIdBits);

    //序列在id中占的位数 12bit
    private final static long seqBits = 12L;

    //序列最大值 4095 即2的12次方减一。
    private final static long seqMax = ~(-1L << seqBits);

    // 64位的数字：首位0  随后41位表示时间戳 随后10位工作机器id（8位IP标识 + 2位数字标识） 最后12位序列号
    private final static long dataCenterIdLeftShift = seqBits;
    private final static long ipIdLeftShift = seqBits + dataCenterIdBits;
    private final static long timeLeftShift = seqBits  + dataCenterIdBits + ipIdLeftShift;

    //IP标识(0~255)
    private long ipId;

    // 数据中心ID(0~3)
    private long dataCenterId;

    // 毫秒内序列(0~4095)
    private long seq = 0L;

    // 上次生成ID的时间截
    private long lastTime = -1L;

    public MagicSnowFlake(long ipId, long dataCenterId) {
        if(ipId < 0 || ipId > ipIdMax) {
            System.out.println(" ---------- ipId不在正常范围内(0~"+ipIdMax +") " + ipId);
            System.exit(0);
        }

        if(dataCenterId < 0 || dataCenterId > dataCenterIdMax) {
            System.out.println(" ---------- dataCenterId不在正常范围内(0~"+dataCenterIdMax +") " + dataCenterId);
            System.exit(0);
        }

        this.ipId = ipId;
        this.dataCenterId = dataCenterId;
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


        System.out.println((nowTime - twepoch)+':'+timeLeftShift+":"+ipId+":"+ipIdLeftShift+":"+dataCenterId+":"+seq);
        return ((nowTime - twepoch) << timeLeftShift)
                | (ipId << ipIdLeftShift)
                | (dataCenterId << dataCenterIdLeftShift)
                | seq;
    }

    private long getNextTimeStamp() {
        long nowTime;
        do {
            nowTime = System.currentTimeMillis();
        } while(nowTime <= lastTime);
        return nowTime;
    }
}


/**
 * @author zcl
 * @date 2017/7/12
 **/
class ThreadSnowFlake extends Thread {

    MagicSnowFlake msf;

    int cnt = 0;

    public ThreadSnowFlake(MagicSnowFlake msf) {
        this.msf = msf;
    }

    public void run() {
        if(msf != null) {
            while(cnt < 10) {
                System.out.println(Thread.currentThread().getId() + " : " + msf.nextId());
                cnt ++;
            }
        }
    }
}

/**
 * @author zcl
 * @date 2017/7/12
 **/
class AlgorithmMain {

    public static void main(String[] args) {
        // 37292655581:1:13:1:5

        MagicSnowFlake msf = new MagicSnowFlake(1, 1);
        MagicSnowFlake msf2 = new MagicSnowFlake(1, 1);


        ThreadSnowFlake t1 = new ThreadSnowFlake(msf);
        ThreadSnowFlake t2 = new ThreadSnowFlake(msf2);

        t1.start();
        t2.start();
        System.out.println(~ (-1L << 15));
    }
}

