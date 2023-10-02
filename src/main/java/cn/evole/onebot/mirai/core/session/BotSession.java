package cn.evole.onebot.mirai.core.session;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.config.PluginConfig.BotConfig;
import cn.evole.onebot.mirai.core.ApiMap;
import cn.evole.onebot.mirai.core.EventMap;
import cn.evole.onebot.mirai.web.websocket.OneBotWSClient;
import cn.evole.onebot.mirai.web.websocket.OneBotWSServer;
import cn.evole.onebot.sdk.event.IgnoreEvent;
import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.BotEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:功能实现单元
 * Author: cnlimiter
 * Date: 2022/10/8 7:19
 * Version: 1.0
 */
@Getter
public class BotSession {

    private final Bot bot;
    private final ApiMap apiImpl;
    private final BotConfig botConfig;
    private final OneBotWSServer websocketServer;
    private final List<OneBotWSClient> websocketClient = new ArrayList<>();

    public BotSession(Bot bot, BotConfig botConfig){
        this.bot = bot;
        this.apiImpl = new ApiMap(bot);
        this.botConfig = botConfig;

        this.websocketServer = new OneBotWSServer(
                this, botConfig.getWs().getWsHost(), botConfig.getWs().getWsPort()
        );
        websocketServer.create();


        for(PluginConfig.WSReverseConfig ws_re : botConfig.getWsReverse()){
            OneBotWSClient client = new OneBotWSClient(
                    this, ws_re.getReverseHost(), ws_re.getReversePort()
            );
            client.connect();
            this.websocketClient.add(client);
        }

    }

    public void close()  {
        websocketServer.close();
        websocketClient.forEach(OneBotWSClient::close);
    }

    public void triggerEvent(BotEvent event){
        var e = EventMap.toDTO(event);
        var json = JSON.toJSONString(e);
        if (!(e instanceof IgnoreEvent)) {
            OneBotMirai.logger.info(String.format("将发送事件: %s", json));
            websocketServer.broadcast(json);
            websocketClient.forEach(client -> {
                if (client.isOpen()) client.send(json);
            });
        }
    }
}
