package cn.evolvefield.mirai.onebot.web.websocket.common;

import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketSession;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:session管理器
 * Author: cnlimiter
 * Date: 2022/10/4 20:37
 * Version: 1.0
 */
@Slf4j
public class SessionManager {
    private static final ConcurrentHashMap<Object, WebSocketSession> map = new ConcurrentHashMap<>();


    public static WebSocketSession get(Object id){
        return map.get(id.toString());
    }

    public static void put(Object id, WebSocketSession session){
        map.put(id,session);
        log.debug("Sessions size = {}",map.size());
    }

    public static WebSocketSession remove(Object id){
        return map.remove(id);
    }
}
