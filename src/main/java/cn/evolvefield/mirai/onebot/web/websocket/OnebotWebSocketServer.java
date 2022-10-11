package cn.evolvefield.mirai.onebot.web.websocket;

import cn.evolvefield.mirai.onebot.OneBotMirai;
import cn.evolvefield.mirai.onebot.config.PluginConfig;
import cn.evolvefield.mirai.onebot.core.BotSession;
import cn.evolvefield.mirai.onebot.util.ActionUtils;
import cn.evolvefield.mirai.onebot.web.websocket.core.FastWSServer;
import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketSession;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/8 15:21
 * Version: 1.0
 */
public class OnebotWebSocketServer extends FastWSServer {



    public OnebotWebSocketServer INSTANCE;

    private final BotSession botSession;
    private final PluginConfig.BotConfig botConfig;

    private String listener;

    public OnebotWebSocketServer(BotSession botSession){
        this.botSession = botSession;
        this.botConfig = botSession.getBotConfig();
        this.INSTANCE = this;
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
        try {
            OneBotMirai.logger.debug(String.format("Bot: %s 正向Websocket服务端 / 开始处理API请求", botSession.getBot().getId()));

            ActionUtils.handleWebSocketActions(session, botSession.getApiImpl(), message);

        } finally {
            OneBotMirai.logger.debug(String.format("Bot: %s 正向Websocket服务端 / 连接被关闭", botSession.getBot().getId()));
            botSession.unsubscribeEvent(listener);
        }
    }

    @Override
    public void send(WebSocketSession session, String message) {
        this.listener = botSession.subscribeEvent(message);
        super.send(session, listener);
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
