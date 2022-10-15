package cn.evolvefield.mirai.onebot.web.websocket.impl;

import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:session管理器
 * Author: cnlimiter
 * Date: 2022/10/4 19:39
 * Version: 1.0
 */
@Slf4j
public class Sessions {
    private static final ConcurrentHashMap<Channel, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public static WebSocketSession createSession(ChannelHandlerContext ctx){
        WebSocketSession session = new WebSocketSession();
        sessions.put(ctx.channel(),session);

        log.debug("session size = {}",sessions.size());
        return session;
    }

    public static WebSocketSession getSession(ChannelHandlerContext ctx){
        return sessions.get(ctx.channel());
    }

    public static WebSocketSession getSession(ChannelHandlerContext ctx, boolean newSession){
        if(newSession){
            return sessions.computeIfAbsent(ctx.channel(),e -> new WebSocketSession());
        }else {
            return sessions.get(ctx.channel());
        }
    }

    public static WebSocketSession destroy(ChannelHandlerContext ctx){
        return sessions.remove(ctx.channel());
    }
}
