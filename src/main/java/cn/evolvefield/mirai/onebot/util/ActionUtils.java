package cn.evolvefield.mirai.onebot.util;

import cn.evolvefield.mirai.onebot.OneBotMirai;
import cn.evolvefield.mirai.onebot.core.MiraiApi;
import cn.evolvefield.mirai.onebot.dto.response.ActionData;
import cn.evolvefield.mirai.onebot.dto.response.msic.AsyncStarted;
import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketSession;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/9 23:14
 * Version: 1.0
 */
public class ActionUtils {
    public static void handleWebSocketActions(WebSocketSession session, MiraiApi api, String text){
        try {
            OneBotMirai.logger.debug(String.format("WebSocket收到操作请求: %s", text));
            var json = JSONObject.parseObject(text);
            var echo = json.getString("echo");
            var action = json.getJSONObject("action").toString();
            ActionData<?> responseDTO;
            if (action.endsWith("_async")) {
                responseDTO = new AsyncStarted();
                action = action.replace("_async", "");
                api.callMiraiApi(action, json.getJSONObject("params"), api);

            } else {
                responseDTO = api.callMiraiApi(action, json.getJSONObject("params"), api);
            }
            responseDTO.setEcho(echo);
            var jsonToSend = JSON.toJSONString(responseDTO);
            OneBotMirai.logger.debug(String.format("WebSocket将返回结果: %s" ,jsonToSend));
            session.getChannel().writeAndFlush(jsonToSend);
        } catch (Exception e) {
            OneBotMirai.logger.error(e);
        }
    }
}
