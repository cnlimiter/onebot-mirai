package cn.evolvefield.mirai.onebot.core;

import cn.evolvefield.mirai.onebot.OneBotMirai;
import cn.evolvefield.mirai.onebot.config.BotConfig;
import cn.evolvefield.mirai.onebot.dto.event.EventMap;
import cn.evolvefield.mirai.onebot.dto.event.IgnoreEvent;
import cn.evolvefield.mirai.onebot.web.websocket.OneBotWSServer;
import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.BotEvent;
import org.java_websocket.WebSocket;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Description:功能实现单元
 * Author: cnlimiter
 * Date: 2022/10/8 7:19
 * Version: 1.0
 */
public class BotSession {

    @Getter
    private final Bot bot;
    @Getter
    private final BotConfig botConfig;

    @Getter
    private final ConcurrentLinkedQueue<String> eventSubscriptionString = new ConcurrentLinkedQueue<>();

    private final OneBotWSServer websocketServer;

    @Getter
    private final MiraiApi apiImpl;

    public BotSession(Bot bot, BotConfig botConfig){
        this.bot = bot;
        this.botConfig = botConfig;
        this.apiImpl = new MiraiApi(bot);
        this.websocketServer = new OneBotWSServer(this);
        websocketServer.create();
    }

    public void close()  {
        websocketServer.close();
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
    public String subscribeEvent(WebSocket socket){
        if (eventSubscriptionString.isEmpty()) return "";
        var send = eventSubscriptionString.poll();
        socket.send(send);
        return send;



    }

    public void unsubscribeEvent(String msg){
        eventSubscriptionString.remove(msg);
    }
}
