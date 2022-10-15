package cn.evolvefield.mirai.onebot.web.websocket.core;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Description:server消息处理器
 * Author: cnlimiter
 * Date: 2022/10/4 19:27
 * Version: 1.0
 */
public interface WebSocketServer {

    /**
     * 连接
     * 本方法调用时还未创建websocket连接
     */
    default void onOpen(WebSocketSession session){

    }

    /**
     * 收到消息
     */
    void onMessage(WebSocketSession session, String message);

    /**
     * 收到消息
     */
    default void onMessage(WebSocketSession session, byte[] message) {

    }

    /**
     * 发送消息
     */
    default void send(WebSocketSession session, String message){
        session.getChannel().writeAndFlush(
                new TextWebSocketFrame(message)
        );
    }

    /**
     * 发送消息
     */
    default void send(WebSocketSession session, byte[] message){
        session.getChannel().writeAndFlush(new BinaryWebSocketFrame(
                Unpooled.buffer().writeBytes(message)
        ));
    }

    /**
     * 关闭连接
     */
    default void onClose(WebSocketSession session) {

    }

    /**
     * 发生错误
     */
    default void onError(WebSocketSession session, Throwable e){

    }
}
