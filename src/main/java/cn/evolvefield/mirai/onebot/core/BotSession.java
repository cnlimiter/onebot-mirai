package cn.evolvefield.mirai.onebot.core;

import cn.evolvefield.mirai.onebot.OneBotMirai;
import cn.evolvefield.mirai.onebot.config.PluginConfig;
import cn.evolvefield.mirai.onebot.dto.event.EventMap;
import cn.evolvefield.mirai.onebot.dto.event.IgnoreEvent;
import cn.evolvefield.mirai.onebot.web.websocket.OnebotWebSocketServer;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.BotEvent;

import java.util.LinkedList;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/8 7:19
 * Version: 1.0
 */
public class BotSession {

    @Getter
    public Bot bot;
    @Getter
    public PluginConfig.BotConfig botConfig;

    private LinkedList<String> eventSubscriptionString;

    private final OnebotWebSocketServer websocketServer;

    @Getter
    private final MiraiApi apiImpl;

    public BotSession(Bot bot, PluginConfig.BotConfig botConfig){
        this.bot = bot;
        this.botConfig = botConfig;
        this.apiImpl = new MiraiApi(bot);
        this.websocketServer = new OnebotWebSocketServer(this);
    }

    public void close(){
        websocketServer.stop();
    }

    public String subscribeEvent(BotEvent event){
        var e = EventMap.toDTO(event, true);
        if (!(e instanceof IgnoreEvent)) {
            var json = JSON.toJSONString(e);
            OneBotMirai.logger.debug(String.format("将发送事件: %s", json));
            this.eventSubscriptionString.add(json);
            return json;
        }
        return "";
    }

    public void unsubscribeEvent(String msg){
        eventSubscriptionString.remove(msg);
    }
}
