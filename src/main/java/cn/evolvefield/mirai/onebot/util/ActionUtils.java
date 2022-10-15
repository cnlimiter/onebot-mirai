package cn.evolvefield.mirai.onebot.util;

import cn.evolvefield.mirai.onebot.OneBotMirai;
import cn.evolvefield.mirai.onebot.core.MiraiApi;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.java_websocket.WebSocket;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/9 23:14
 * Version: 1.0
 */
public class ActionUtils {
    public static void handleWebSocketActions(WebSocket session, MiraiApi api, JSONObject json){
        try {
            OneBotMirai.logger.info(String.format("WebSocket收到操作请求: %s", JSON.toJSONString(json)));
            var echo = json.getString("echo");
            var action = json.getString("action");

            var responseDTO = api.callMiraiApi(action, json.getJSONObject("params"), api);

            responseDTO.setEcho(echo);
            var jsonToSend = JSON.toJSONString(responseDTO);
            OneBotMirai.logger.debug(String.format("WebSocket将返回结果: %s" ,jsonToSend));
            session.send(jsonToSend);
        } catch (Exception e) {
            OneBotMirai.logger.error(e.getMessage());
        }
    }
}
