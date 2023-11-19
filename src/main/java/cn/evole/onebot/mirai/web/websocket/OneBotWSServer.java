package cn.evole.onebot.mirai.web.websocket;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.core.session.BotSession;
import cn.evole.onebot.mirai.util.ActionUtils;
import cn.evole.onebot.mirai.util.GsonUtils;
import cn.evole.onebot.sdk.action.ActionData;
import cn.evole.onebot.sdk.event.meta.HeartbeatMetaEvent;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Description:正向websocket服务器
 * Author: cnlimiter
 * Date: 2022/10/14 18:44
 * Version: 1.0
 */
public class OneBotWSServer extends WebSocketServer{
    public OneBotWSServer INSTANCE;
    private final BotSession botSession;
    private final int port;

    public OneBotWSServer(BotSession botSession, String host, int port){
        super(new InetSocketAddress(host, port));
        this.botSession = botSession;
        this.port = port;
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
        var json = GsonUtils.getGson().fromJson(message, JsonObject.class);
        OneBotMirai.logger.info(String.format("Bot: %s 正向Websocket服务端 收到请求", botSession.getBot().getId()));

        if (json.has("action")){
            OneBotMirai.logger.debug(String.format("Bot: %s 正向Websocket服务端 / 开始处理API请求", botSession.getBot().getId()));
            ActionUtils.handleWebSocketActions(conn, botSession.getApiImpl(), json);
        }

    }

    Executor executor = Executors.newSingleThreadExecutor();
    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (OneBotMirai.INSTANCE.isEnabled()) {
            executor.execute(() ->{
                int secs = 5;
                OneBotMirai.logger.warning(String.format("Bot: %s 正向Websocket服务端 / 出现错误， 将在 %d 秒后重试 \n %s", botSession.getBot().getId() , secs, ex.getMessage()));
                try {
                    Thread.sleep(secs*1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                start();
            });
        }
    }

    @Override
    public void onStart() {
        OneBotMirai.logger.info(String.format("Bot: %s 正向WebSocket服务端 / 正在监听端口：%s", botSession.getBot().getId(), port));
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
                var1.send(GsonUtils.getGson().toJson(data));
            }
        };
        service.scheduleAtFixedRate(runnable, 0, 3, TimeUnit.SECONDS);
    }

}
