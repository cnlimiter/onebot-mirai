package cn.evolvefield.mirai.onebot.web.websocket.impl;

import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketServer;
import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/**
 * Description:业务处理层
 * Author: cnlimiter
 * Date: 2022/10/4 19:43
 * Version: 1.0
 */
public class WebSocketServerChannelInboundHandler extends SimpleChannelInboundHandler<Object> {

    private final WebSocketServer webSocketServer;

    public WebSocketServerChannelInboundHandler(WebSocketServer webSocketServer){
        this.webSocketServer = webSocketServer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        WebSocketSession session = Sessions.getSession(ctx);

        if(msg instanceof TextWebSocketFrame){
            String message = ((TextWebSocketFrame) msg).text();

            try {
                webSocketServer.onMessage(session,message);
            }catch (Throwable e){
                webSocketServer.onError(session,e);
            }
        }else if(msg instanceof BinaryWebSocketFrame){
            byte[] bytes = ((BinaryWebSocketFrame) msg).content().array();

            try {
                webSocketServer.onMessage(session,bytes);
            }catch (Throwable e){
                webSocketServer.onError(session,e);
            }
        }else {
            System.out.println("未知消息类型:" + msg.getClass().getName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        webSocketServer.onError(Sessions.getSession(ctx),cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        WebSocketSession destroy = Sessions.destroy(ctx);
        webSocketServer.onClose(destroy);
    }
}
