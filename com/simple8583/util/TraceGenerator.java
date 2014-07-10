package com.simple8583.util;

/**
 * Description: TraceGenerator
 * Author: joey
 * Update: joey(2014-06-09 17:57)
 * 单例实现,本类用于标识终端交易流水号(Field 11)
 */

public class TraceGenerator {

    private static  TraceGenerator tg;
    private int traceValue;
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
