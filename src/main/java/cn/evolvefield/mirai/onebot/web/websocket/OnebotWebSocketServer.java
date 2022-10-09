package cn.evolvefield.mirai.onebot.web.websocket;

import cn.evolvefield.mirai.onebot.config.PluginConfig;
import cn.evolvefield.mirai.onebot.core.BotSession;
import cn.evolvefield.mirai.onebot.web.websocket.common.WSServerConfig;
import cn.evolvefield.mirai.onebot.web.websocket.core.FastWSServer;
import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketSession;
import io.netty.channel.ChannelHandler;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/8 15:21
 * Version: 1.0
 */
public class OnebotWebSocketServer extends FastWSServer {
    public OnebotWebSocketServer INSTANCE = this;

    private final BotSession botSession;
    private final PluginConfig.BotConfig botConfig;

    public OnebotWebSocketServer(BotSession botSession){
        this.botSession = botSession;
        this.botConfig = botSession.getBotConfig();
    }

    public void startWS(){
        super.start(botConfig.getWs().getWsHost(), botConfig.getWs().getWsPort());
    }

    @Override
    public void onOpen(WebSocketSession session) {
        super.onOpen(session);
    }

    @Override
    public void onMessage(WebSocketSession session, String message) {
        //var listener = botSession.subscribeEvent()
    }


    @Override
    public void onClose(WebSocketSession session) {
        super.onClose(session);
    }

    @Override
    public void onError(WebSocketSession session, Throwable e) {
        super.onError(session, e);
    }
}
