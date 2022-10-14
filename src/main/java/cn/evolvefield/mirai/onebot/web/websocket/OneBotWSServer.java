package cn.evolvefield.mirai.onebot.web.websocket;

import cn.evolvefield.mirai.onebot.OneBotMirai;
import cn.evolvefield.mirai.onebot.core.BotSession;
import cn.evolvefield.mirai.onebot.util.ActionUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * Description:正向websocket服务器
 * Author: cnlimiter
 * Date: 2022/10/14 18:44
 * Version: 1.0
 */
public class OneBotWSServer extends WebSocketServer{
    public OneBotWSServer INSTANCE;
    private final BotSession botSession;

    public OneBotWSServer(BotSession botSession){
        super(new InetSocketAddress(botSession.getBotConfig().getWsHost(), botSession.getBotConfig().getWsPort()));
        this.botSession = botSession;
        this.INSTANCE = this;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        OneBotMirai.logger.info(String.format("Bot: %s 正向Websocket服务端 / 成功连接", botSession.getBot().getId()));

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        OneBotMirai.logger.info(String.format("Bot: %s 正向Websocket服务端 / 连接被关闭", botSession.getBot().getId()));
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        var listener = botSession.subscribeEvent(conn);//发送数据

        try {
            OneBotMirai.logger.debug(String.format("Bot: %s 正向Websocket服务端 / 开始处理API请求", botSession.getBot().getId()));

            ActionUtils.handleWebSocketActions(conn, botSession.getApiImpl(), message);

        } finally {
            OneBotMirai.logger.debug(String.format("Bot: %s 正向Websocket服务端 / 连接被关闭", botSession.getBot().getId()));
            botSession.unsubscribeEvent(listener);//错误处理
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        OneBotMirai.logger.warning(String.format("Bot: %s 正向Websocket服务端 / 出现错误 \n %s", botSession.getBot().getId(), ex.getMessage()));
    }

    @Override
    public void onStart() {
        OneBotMirai.logger.info(String.format("Bot: %s 正向WebSocket服务端 / 正在监听端口：%s", botSession.getBot().getId(), botSession.getBotConfig().getWsPort()));
    }


    public void create() {
        super.start();
    }

    public void close(){
        try {
            this.stop();
        } catch (InterruptedException e){
            OneBotMirai.logger.error(String.format("出现错误:\n %s", e));
        }
    }
}
