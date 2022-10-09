package cn.evolvefield.mirai.onebot.core;

import cn.evolvefield.mirai.onebot.config.PluginConfig;
import net.mamoe.mirai.Bot;

import java.util.LinkedHashMap;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/8 19:34
 * Version: 1.0
 */
public class SessionManager{


    private static final LinkedHashMap<Long, BotSession> sessions = new LinkedHashMap<>();

    public static LinkedHashMap<Long, BotSession> getSessions() {
        return sessions;
    }

    public static BotSession get(long botId){
        return sessions.get(botId);
    }

    public static boolean containsSession(long botId){
        return sessions.containsKey(botId);
    }

    public static void closeSession(long botId){
        sessions.get(botId).close();
        sessions.remove(botId);

    }


    public static BotSession createBotSession(Bot bot, PluginConfig.BotConfig botConfig){
        var session = new BotSession(bot, botConfig);
        sessions.put(bot.getId(), session);
        return session;
    }
}
