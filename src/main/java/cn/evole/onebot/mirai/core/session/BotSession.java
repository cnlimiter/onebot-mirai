package cn.evole.onebot.mirai.core.session;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.config.PluginConfig.BotConfig;
import cn.evole.onebot.mirai.core.ApiMap;
import cn.evole.onebot.mirai.core.EventMap;
import cn.evole.onebot.mirai.util.GsonUtils;
import cn.evole.onebot.mirai.web.websocket.OneBotWSClient;
import cn.evole.onebot.mirai.web.websocket.OneBotWSServer;
import cn.evole.onebot.sdk.event.IgnoreEvent;
import com.google.gson.Gson;
import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.utils.MiraiLogger;

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
    private final MiraiLogger miraiLogger = MiraiLogger.Factory.INSTANCE.create(BotSession.class);


    @Override
    public String toString() {
        return """
                Bot Id: %s,
                Bot Config: %s,
                OneBot Clients: %s,
                OneBot Server: %s
                """.formatted(bot.getBot().getId(), botConfig.toString(), websocketClient.size(), websocketServer.getAddress())
                ;
    }

    public BotSession(Bot bot, BotConfig botConfig){
        this.bot = bot;
        this.apiImpl = new ApiMap(bot);
        this.botConfig = botConfig;
        this.websocketServer = new OneBotWSServer(
                this, botConfig.getWs().getWsHost(), botConfig.getWs().getWsPort()
        );
        if (this.botConfig.getWs().getEnable()){
            miraiLogger.info(String.format("创建正向服务器：%s, %s", botConfig.getWs().getWsHost(), botConfig.getWs().getWsPort()));
            websocketServer.create();
        }


        for(PluginConfig.WSReverseConfig ws_re : botConfig.getWsReverse()){
            if (ws_re.getEnable()){
                OneBotWSClient client = new OneBotWSClient(
                        this, ws_re
                );
                client.connect();
                this.websocketClient.add(client);
            }
        }
    }

    public void close()  {
        websocketServer.close();
        websocketClient.forEach(OneBotWSClient::close);
    }

    private ThreadLocal<Gson> gsonTl = new ThreadLocal<Gson>();
    public void triggerEvent(BotEvent event){
        var e = EventMap.toDTO(event);
        var json = GsonUtils.getGson().toJson(e);
        if (!(e instanceof IgnoreEvent)) {
            var debug = PluginConfig.INSTANCE.getDebug();
            if (this.botConfig.getWs().getEnable()){
                if (debug) OneBotMirai.logger.info("将广播正向websocket事件");
                websocketServer.broadcast(json);
            }

            for(PluginConfig.WSReverseConfig ws_re : botConfig.getWsReverse()){
                if (ws_re.getEnable()){
                    long sendCount = websocketClient.stream().filter(client -> {
                        try{
                            if (client.isOpen()) {
                                client.send(json);
                            }
                        }catch (Exception ex){
                            OneBotMirai.logger.warning("error sending msg", ex);
                        }
                        return client.isOpen();
                    }).count();
                    if (debug)  OneBotMirai.logger.info(String.format("将广播反向websocket事件, 共计发送 :%d", sendCount));
                }
            }

        }
    }


}
