package com.simple8583.util;

/**
 * <p>本类用于标识终端交易流水号，每次会生成一个1~999999之间的字符串</p>
 * <p>使用方法TraceGenerator.getInstance().nextTrace();</p>
 *
 * @author Magic Joey
 * @version TraceGenerator.java 1.0 @2014-06-09 17:57$
 */
public class TraceGenerator {

    private static  TraceGenerator tg;
    private int traceValue;
    //random为false，生成值每次+1，为true，生成值为随机
    private boolean random=true;
    
    private TraceGenerator(int initValue){
        this.traceValue = initValue;
    }

    public static TraceGenerator getInstance(){
        //双检查锁
        if(tg==null){
            synchronized (TraceGenerator.class){
                if(tg==null){
                    tg = new TraceGenerator(1);
                }
            }
        }
        return tg;
    }

    public String nextTrace(){
    	if(!random){
        if(traceValue>=999999){
            traceValue=1;
        }
        return String.valueOf(traceValue++);
    	}else{
    		return String.valueOf((int)(Math.random()*1000000));
    	}
    }


}
