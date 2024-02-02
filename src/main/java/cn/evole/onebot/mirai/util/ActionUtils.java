package cn.evole.onebot.mirai.util;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.ApiMap;
import cn.evole.onebot.sdk.util.json.GsonUtils;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/9 23:14
 * Version: 1.0
 */
public class ActionUtils {
    public static void handleWebSocketActions(WebSocket session, ApiMap api, JsonObject json){
        try {
            var debug = PluginConfig.INSTANCE.getDebug();
            if (debug) OneBotMirai.logger.info(String.format("WebSocket收到操作请求: %s", GsonUtils.getGson().toJson(json)));

            var echo = json.get("echo").getAsString();
            var action = json.get("action").getAsString();

            var responseDTO = api.callMiraiApi(action, json.get("params").getAsJsonObject());

            responseDTO.setEcho(echo);
            var jsonToSend = GsonUtils.getGson().toJson(responseDTO);
            session.send(jsonToSend);
            if (debug) OneBotMirai.logger.info(String.format("WebSocket将返回结果: %s" ,jsonToSend));
        } catch (Exception e) {
            OneBotMirai.logger.error(e.getMessage());
        }
    }
}
