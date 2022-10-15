package cn.evolvefield.mirai.onebot.web.websocket.core;

import io.netty.channel.Channel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description:储存连接数据
 * Author: cnlimiter
 * Date: 2022/10/4 19:36
 * Version: 1.0
 */
public class WebSocketSession extends LinkedHashMap<String,Object> {
    public static final String ID_KEY = "id";
    public static final String URI_KEY = "uri";
    public static final String URI_BASE_KEY = "uriBase";
    public static final String PARAMS_KEY = "params";
    public static final String CHANNEL_KEY = "channel";

    public WebSocketSession(){}

    public Object getId() {
        return get(ID_KEY);
    }

    public void setId(Object id) {
        put(ID_KEY,id);
    }

    public void setChannel(Channel channel){
        put(CHANNEL_KEY,channel);
    }

    public Channel getChannel(){
        return (Channel) get(CHANNEL_KEY);
    }

    public void setUri(String uri) {
        put(URI_KEY,uri);
    }

    public String getUri(){
        return get(URI_KEY).toString();
    }

    public void setUriBase(String uriBase){
        put(URI_BASE_KEY,uriBase);
    }

    public String getUriBase(){
        return get(URI_BASE_KEY).toString();
    }

    public void setParams(Map<String,String> params){
        put(PARAMS_KEY,params);
    }

    public Map<String,String> getParams(){
        return (Map<String, String>) get(PARAMS_KEY);
    }

    public String getParam(String key){
        return getParams().get(key);
    }

}
