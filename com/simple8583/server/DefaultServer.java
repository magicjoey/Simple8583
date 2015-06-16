package com.simple8583.server;

import com.simple8583.key.SimpleConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>默认服务类.</p>
 *
 * @author Magic Joey
 * @version DefaultServer.java 1.0 Created@2015-06-16 17:14 $
 */
public class DefaultServer extends AbstractServer{


    //此处实际可以用Decorate针对不同类型的MTI进行处理
    @Override
    protected Map<String, String> response(Map<String, String> receiveMap) {
        Map<String,String> responseMap = new HashMap<String,String>(30);
        responseMap.put(SimpleConstants.MTI,"0430");
        responseMap.put("2","470000");
        responseMap.put("3","12M01041");
        responseMap.put("4","01041          ");
        responseMap.put("5","04");
        responseMap.put("6","6228210250013865710");
        responseMap.put("7","000000492500");
        responseMap.put("8","SP99515061500104387 ");
        responseMap.put("9","000653");
        responseMap.put("10","20150615");
        responseMap.put("11","20150615");
        responseMap.put("12","01030000041");
        responseMap.put("13","04061415000000048098");
        responseMap.put("14","156");
        responseMap.put("15","00");
        responseMap.put("16","01030000041              ");
        return responseMap;
    }



}
