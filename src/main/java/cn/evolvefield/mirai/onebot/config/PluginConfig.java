package cn.evolvefield.mirai.onebot.config;

import com.google.gson.annotations.Expose;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/3 20:25
 * Version: 1.0
 */
public class PluginConfig extends ReadOnlyPluginConfig {
    public static Map<String, BotConfig> bots = new HashMap<>();


    public PluginConfig() {
        super("settings");
        bots.put("12345654321", new BotConfig());

    }

    @Data
    public static class DBConfig{
        @Expose
        boolean enable = true;
    }

    @Data
    public static class BotConfig{
        @Expose boolean cacheImage = false;
        @Expose boolean cacheRecord = false;
        @Expose HeartbeatSettings heartbeat = new HeartbeatSettings();
        @Expose WebsocketServerSettings ws = new WebsocketServerSettings();
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
