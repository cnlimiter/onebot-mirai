package cn.evole.onebot.mirai.config;

import kotlinx.serialization.SerialName;
import kotlinx.serialization.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: onebot-mirai / BotConfig
 * Author: cnlimiter
 * CreateTime: 2023/10/2 12:43
 * Description:
 */

@Serializable
@Getter
public class BotConfig {

    boolean cacheImage = false;
    boolean cacheRecord = false;
    HeartbeatConfig heartbeat = new HeartbeatConfig();
    WSConfig ws = new WSConfig();
    List<WSReverseConfig> wsReverse = new ArrayList<>();


    @Data
    @Serializable
    @SerialName("ws")
    public static class WSConfig{
        boolean enable = true;
        String postMessageFormat = "string";
        String wsHost = "0.0.0.0";
        int wsPort = 8080;
        String accessToken  = "";
    }

    @Data
    @Serializable
    @SerialName("ws_reverse")
    public static class WSReverseConfig{
        boolean enable = false;
        String postMessageFormat = "string";
        String reverseHost = "0.0.0.0";
        int reversePort = 8080;
        String accessToken  = "";
        long reconnectInterval = 3000L;
    }

    @Data
    @Serializable
    @SerialName("db")
    public static class DBConfig{
        boolean enable = true;
    }

    @Data
    @NoArgsConstructor
    @Serializable()
    @SerialName("heartbeat")
    public static class HeartbeatConfig {
        boolean enable = false;
        long interval = 1500L;
    }
}
