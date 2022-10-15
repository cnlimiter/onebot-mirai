package cn.evolvefield.mirai.onebot.web.websocket.impl;

import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketServer;
import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketSession;
import cn.evolvefield.mirai.onebot.web.websocket.common.UrlEntity;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.AttributeKey;

import static io.netty.handler.codec.http.HttpUtil.*;
import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

/**
 * Description:握手处理
 * Author: cnlimiter
 * Date: 2022/10/4 19:42
 * Version: 1.0
 */
public class WebSocketServerAuthHandshakeHandler extends ChannelInboundHandlerAdapter {

    private final String websocketPath;
    private final String subprotocols;
    private final boolean allowExtensions;
    private final int maxFramePayloadSize;
    private final boolean allowMaskMismatch;
    private final WebSocketServer webSocketServer;

    public WebSocketServerAuthHandshakeHandler(String websocketPath, String subprotocols,
                                               boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch, WebSocketServer webSocketServer) {
        this.websocketPath = websocketPath;
        this.subprotocols = subprotocols;
        this.allowExtensions = allowExtensions;
        this.maxFramePayloadSize = maxFrameSize;
        this.allowMaskMismatch = allowMaskMismatch;
        this.webSocketServer = webSocketServer;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
        FullHttpRequest req = (FullHttpRequest) msg;
        if (req.uri().indexOf(websocketPath) != 0) {
            ctx.fireChannelRead(msg);
            return;
        }

        try {
            if (req.method() != GET) {
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
                return;
            }
            //这里不再使用路由分发，太懒了，全部用一个接口吧qwq

            WebSocketSession session = Sessions.createSession(ctx);
            session.setChannel(ctx.channel());
            session.setId(ctx.channel().hashCode());
            session.setUri(req.uri());
            UrlEntity entity = UrlEntity.parse(req.uri());
            session.setUriBase(entity.getBaseUrl());
            session.setParams(entity.getParams());

            webSocketServer.onOpen(session);
            final WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                    getWebSocketLocation(ctx.pipeline(), req, websocketPath), subprotocols,
                    allowExtensions, maxFramePayloadSize, allowMaskMismatch);
            final WebSocketServerHandshaker handshaker = wsFactory.newHandshaker(req);
            if (handshaker == null) {
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            } else {
                final ChannelFuture handshakeFuture = handshaker.handshake(ctx.channel(), req);
                handshakeFuture.addListener((ChannelFutureListener) future -> {
                    if (!future.isSuccess()) {
                        ctx.fireExceptionCaught(future.cause());
                    } else {
                        ctx.fireUserEventTriggered(
                                WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE);
                    }
                });
                setHandshaker(ctx.channel(), handshaker);
                ctx.pipeline().replace(this, "WS403Responder",
                        forbiddenHttpRequestResponder());
            }
        } finally {
            req.release();
        }
    }

    private static final AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY =
            AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");

    static void setHandshaker(Channel channel, WebSocketServerHandshaker handshaker) {
        channel.attr(HANDSHAKER_ATTR_KEY).set(handshaker);
    }

    static ChannelHandler forbiddenHttpRequestResponder() {
        return new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                if (msg instanceof FullHttpRequest) {
                    ((FullHttpRequest) msg).release();
                    FullHttpResponse response =
                            new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.FORBIDDEN);
                    ctx.channel().writeAndFlush(response);
                } else {
                    ctx.fireChannelRead(msg);
                }
            }
        };
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, HttpRequest req, HttpResponse res) {
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (!isKeepAlive(req) || res.status().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private static String getWebSocketLocation(ChannelPipeline cp, HttpRequest req, String path) {
        String protocol = "ws";
        if (cp.get(SslHandler.class) != null) {
            // SSL in use so use Secure WebSockets
            protocol = "wss";
        }
        return protocol + "://" + req.headers().get(HttpHeaderNames.HOST) + path;
    }
}
