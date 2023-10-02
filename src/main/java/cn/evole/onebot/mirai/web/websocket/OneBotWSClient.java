package cn.evole.onebot.mirai.web.websocket;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.core.session.BotSession;
import cn.evole.onebot.mirai.util.ActionUtils;
import cn.evole.onebot.sdk.action.ActionData;
import cn.evole.onebot.sdk.event.meta.HeartbeatMetaEvent;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description:正向websocket服务器
 * Author: cnlimiter
 * Date: 2022/10/14 18:44
 * Version: 1.0
 */
public class OneBotWSClient extends WebSocketClient {
    public OneBotWSClient INSTANCE;
    private final BotSession botSession;

    public OneBotWSClient(BotSession botSession, String host, int port){
        super( URI.create("ws://"+ host +":" + port));
        this.botSession = botSession;
        this.INSTANCE = this;
    }

    public void close(){
        try {
            this.close(0);
        } catch (Exception e){
            OneBotMirai.logger.error(String.format("出现错误:\n %s", e));
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        OneBotMirai.logger.info(String.format("Bot: %s 正向Websocket服务端 / 成功连接", botSession.getBot().getId()));
    }

    @Override
    public void onMessage(String message) {
        var json = JSONObject.parseObject(message);

        if (json.containsKey("action")){
            OneBotMirai.logger.debug(String.format("Bot: %s 正向Websocket服务端 / 开始处理API请求", botSession.getBot().getId()));
            ActionUtils.handleWebSocketActions(this, botSession.getApiImpl(), json);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        OneBotMirai.logger.info(String.format("Bot: %s 正向Websocket服务端 / 连接被关闭", botSession.getBot().getId()));

    }

    @Override
    public void onError(Exception ex) {
        OneBotMirai.logger.warning(String.format("Bot: %s 正向Websocket服务端 / 出现错误 \n %s", botSession.getBot().getId(), ex.getMessage()));
    }


    /**
     * 心跳保活
     * @param var1
     */
    private void heartbeat(WebSocket var1){
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        Runnable runnable = () -> {
            if(var1 != null) {
                var data = new ActionData<>();
                var event = new HeartbeatMetaEvent();
                data.setData(event);
                var1.send(JSON.toJSONString(data));
            }
        };
        service.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.SECONDS);
    }

}
