package cn.evole.onebot.mirai.core.session;

import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.session.BotSession;
import lombok.Getter;
import net.mamoe.mirai.Bot;

import java.util.LinkedHashMap;

/**
 * Description:客户端连接管理器
 * Author: cnlimiter
 * Date: 2022/10/8 19:34
 * Version: 1.0
 */
public class SessionManager {


    @Getter
    private static final LinkedHashMap<Long, BotSession> sessions = new LinkedHashMap<>();//一个机器人对应一个session

    public static BotSession get(long botId) {
        return sessions.get(botId);
    }

    public static boolean containsSession(long botId) {
        return sessions.containsKey(botId);
    }

    public static void closeSession(long botId) {
        sessions.get(botId).close();
        sessions.remove(botId);

    }

    public static void closeAllSessions() {
        sessions.forEach((id, bs) -> bs.close());
        sessions.clear();
    }

    public static BotSession createBotSession(Bot bot, PluginConfig.BotConfig botConfig) {
        var session = new BotSession(bot, botConfig);
        sessions.put(bot.getId(), session);
        return session;
    }
}
