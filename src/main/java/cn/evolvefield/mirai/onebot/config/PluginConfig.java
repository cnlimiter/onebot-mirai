package cn.evolvefield.mirai.onebot.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.console.data.Value;
import net.mamoe.mirai.console.data.java.JavaAutoSavePluginConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/3 20:25
 * Version: 1.0
 */
public class PluginConfig extends JavaAutoSavePluginConfig {
    public static final PluginConfig INSTANCE = new PluginConfig();

    public PluginConfig() {
        super("settings");

    }

    public final Value<Map<String, BotConfig>> bots = typedValue("bots",
            createKType(Map.class, createKType(String.class), createKType(BotConfig.class)),
            new HashMap<>() {{
                put("123454321", new BotConfig());
            }}
    );

    @Data
    public static class DBConfig{
        boolean enable = true;
    }



    @Data
    @NoArgsConstructor
    public static class HeartbeatSettings{
        boolean enable = false;
        long interval = 1500L;
    }

    @Data
    @NoArgsConstructor
    public static class WebsocketServerSettings{
        boolean enable = false;
        String postMessageFormat = "string";
        String wsHost = "0.0.0.0";
        int wsPort = 8080;
        String accessToken = "";
    }
}
