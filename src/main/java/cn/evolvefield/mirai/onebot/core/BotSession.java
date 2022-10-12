package cn.evolvefield.mirai.onebot.core;

import cn.evolvefield.mirai.onebot.OneBotMirai;
import cn.evolvefield.mirai.onebot.config.BotConfig;
import cn.evolvefield.mirai.onebot.config.PluginConfig;
import cn.evolvefield.mirai.onebot.dto.event.EventMap;
import cn.evolvefield.mirai.onebot.dto.event.IgnoreEvent;
import cn.evolvefield.mirai.onebot.web.websocket.OnebotWebSocketServer;
import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.BotEvent;

import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
    public BotConfig botConfig;

    private final LinkedList<String> eventSubscriptionString = new LinkedList<>();

    private final OnebotWebSocketServer websocketServer;

    @Getter
    private final MiraiApi apiImpl;

    public BotSession(Bot bot, BotConfig botConfig){
        this.bot = bot;
        this.botConfig = botConfig;
        this.apiImpl = new MiraiApi(bot);
        this.websocketServer = new OnebotWebSocketServer(this);
        websocketServer.startWS();
    }

    public void close(){
        websocketServer.stop();
    }

    public void triggerEvent(BotEvent event){
        var e = EventMap.toDTO(event, true);
        var json = JSON.toJSONString(e);
        OneBotMirai.logger.info(String.format("将发送事件: %s", json));
        if (!(e instanceof IgnoreEvent)) {
            OneBotMirai.logger.debug(String.format("将发送事件: %s", json));
            this.eventSubscriptionString.add(json);
        }
    }
    public String subscribeEvent(String listener){
        this.eventSubscriptionString.add(listener);
        return listener;
    }

    public void unsubscribeEvent(String msg){
        eventSubscriptionString.remove(msg);
    }
}
