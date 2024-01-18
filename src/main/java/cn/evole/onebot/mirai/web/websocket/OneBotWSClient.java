package cn.evole.onebot.mirai.web.websocket;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.session.BotSession;
import cn.evole.onebot.mirai.util.ActionUtils;
import cn.evole.onebot.mirai.util.GsonUtils;
import cn.evole.onebot.sdk.action.ActionData;
import cn.evole.onebot.sdk.enums.ActionPathEnum;
import cn.evole.onebot.sdk.event.meta.HeartbeatMetaEvent;
import com.google.gson.JsonObject;
import io.github.kongweiguang.khttp.KHTTP;
import kotlin.NotImplementedError;
import net.mamoe.mirai.utils.MiraiLogger;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Description:正向websocket服务器
 * Author: cnlimiter
 * Date: 2022/10/14 18:44
 * Version: 1.0
 */
public class OneBotWSClient extends WebSocketClient {
    public OneBotWSClient INSTANCE;
    private final BotSession botSession;

    private static final MiraiLogger miraiLogger = MiraiLogger.Factory.INSTANCE
            .create(OneBotWSClient.class, "OneBotWsClient");


    public OneBotWSClient(BotSession botSession, PluginConfig.WSReverseConfig wsRe) {
        super(URI.create(constructUri(wsRe)));
        miraiLogger.info(String.format("初始化连接目标: %s", constructUri(wsRe)));
        this.botSession = botSession;
        this.INSTANCE = this;
        addHeader("x-self-id", String.valueOf(botSession.getBot().getId()));
        addHeader("x-client-role", "Universal");
        String authStr = wsRe.getAccessToken();
        if (!authStr.trim().isEmpty()){
            addHeader("authorization", "Bearer " + authStr);
        }
    }

    private static String constructUri(PluginConfig.WSReverseConfig wsRe) {
        if (!wsRe.getUseUniversal()){
            throw new NotImplementedError("当前尚未实现非Universal模式，请设置反向代理的universal=true");
        }
        // universal path
        String reversePath = wsRe.getReversePath();

        return "ws://"+ wsRe.getReverseHost() +":" + wsRe.getReversePort() +"/" + reversePath;
    }

    public void create(){
        this.connect();
    }

    public void close(){
        try {
            this.close(0);
        } catch (Exception e){
            miraiLogger.error(String.format("出现错误:\n %s", e));
        }
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        miraiLogger.info(String.format("Bot: %s 反向Websocket服务端 / 成功连接", botSession.getBot().getId()));
    }

    @Override
    public void onMessage(String message) {
        miraiLogger.info(String.format("收到原始消息： %s", message));
        var json = GsonUtils.getGson().fromJson(message, JsonObject.class);
        //throw new NotImplementedError("尚不支持ws反向链接");
        if (json.has("action")){
            miraiLogger.debug(String.format("收到json消息： %s", json));
            miraiLogger.debug(String.format("Bot: %s 反向Websocket服务端 / 开始处理API请求", botSession.getBot().getId()));
            ActionUtils.handleWebSocketActions(this, botSession.getApiImpl(), json);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        miraiLogger.info(String.format("Bot: %s 反向Websocket服务端 / 连接被关闭 , 状态码： %d, 信息: %s", botSession.getBot().getId(), code, reason));

    }

    @Override
    public void onError(Exception ex) {
        miraiLogger.warning(String.format("Bot: %d 反向Websocket服务端 / 出现错误 \n %s", botSession.getBot().getId(), ex.getMessage()), ex);
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
