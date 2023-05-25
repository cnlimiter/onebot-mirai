package cn.evole.onebot.mirai.core;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.BotConfig;
import cn.evole.onebot.mirai.web.websocket.OneBotWSServer;
import cn.evole.onebot.sdk.event.IgnoreEvent;
import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.BotEvent;

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

    private final OneBotWSServer websocketServer;
    @Getter
    private final ApiMap apiImpl;

    public BotSession(Bot bot, BotConfig botConfig){
        this.bot = bot;
        this.botConfig = botConfig;
        this.apiImpl = new ApiMap(bot);
        this.websocketServer = new OneBotWSServer(this);
        this.websocketServer.create();
    }

    public void close()  {
        websocketServer.close();
    }

    public void triggerEvent(BotEvent event){
        var e = EventMap.toDTO(event);
        var json = JSON.toJSONString(e);
        if (!(e instanceof IgnoreEvent)) {
            OneBotMirai.logger.info(String.format("将发送事件: %s", json));
            websocketServer.broadcast(json);
        }
    }
}
