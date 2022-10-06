package cn.evolvefield.mirai.onebot.web.websocket.impl;

import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.websocketx.Utf8FrameValidator;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * Description:自定义url路由
 * Author: cnlimiter
 * Date: 2022/10/4 19:41
 * Version: 1.0
 */
public class WebSocketServerAuthProtocolHandler extends WebSocketServerProtocolHandler {

    private final WebSocketServer webSocketServer;

    public WebSocketServerAuthProtocolHandler(String websocketPath, WebSocketServer webSocketServer) {
        this(websocketPath, null, false,webSocketServer);
    }

    public WebSocketServerAuthProtocolHandler(String websocketPath, String subprotocols, WebSocketServer webSocketServer) {
        this(websocketPath, subprotocols, false,webSocketServer);
    }

    public WebSocketServerAuthProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, WebSocketServer webSocketServer) {
        this(websocketPath, subprotocols, allowExtensions, 65536,webSocketServer);
    }

    public WebSocketServerAuthProtocolHandler(String websocketPath, String subprotocols,
                                              boolean allowExtensions, int maxFrameSize, WebSocketServer webSocketServer) {
        this(websocketPath, subprotocols, allowExtensions, maxFrameSize, false,webSocketServer);
    }

    public WebSocketServerAuthProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch, WebSocketServer webSocketServer) {
        super(websocketPath, subprotocols, allowExtensions, maxFrameSize, allowMaskMismatch);
        this._webSocketPathPrefix = websocketPath;
        this._subprotocols =subprotocols;
        this._allowExtensions = allowExtensions;
        this._maxFrameSize = maxFrameSize;
        this._allowMaskMismatch = allowMaskMismatch;
        this.webSocketServer =webSocketServer;
    }

    String _webSocketPathPrefix;
    String _subprotocols;
    boolean _allowExtensions;
    int _maxFrameSize;
    boolean _allowMaskMismatch;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        ChannelPipeline cp = ctx.pipeline();
        if (cp.get(WebSocketServerAuthHandshakeHandler.class) == null) {
            // Add the WebSocketHandshakeHandler before this one.
            ctx.pipeline().addBefore(ctx.name(), WebSocketServerAuthHandshakeHandler.class.getName(),
                    new WebSocketServerAuthHandshakeHandler(_webSocketPathPrefix, _subprotocols,
                            _allowExtensions, _maxFrameSize, _allowMaskMismatch, webSocketServer));
        }
        if (cp.get(Utf8FrameValidator.class) == null) {
            // Add the UFT8 checking before this one.
            ctx.pipeline().addBefore(ctx.name(), Utf8FrameValidator.class.getName(),
                    new Utf8FrameValidator());
        }
    }

}
