package cn.evole.onebot.mirai.web.http;

import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.session.BotSession;
import cn.evole.onebot.mirai.util.GsonUtils;
import cn.evole.onebot.sdk.enums.ActionPathEnum;
import cn.evole.onebot.sdk.util.json.JsonsObject;
import io.github.kongweiguang.khttp.KHTTP;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/19 0:30
 * @Description:
 */

public class OneBotHttpServer {

    private final BotSession botSession;
    private final String host;
    private final int port;
    private final KHTTP khttp;

    public OneBotHttpServer(BotSession botSession, PluginConfig.BotConfig botConfig) {
        this.botSession = botSession;
        this.host = botConfig.getHttp().getHost();
        this.port = botConfig.getHttp().getPort();
        this.khttp = KHTTP.of().executor(Executors.newCachedThreadPool());
    }

    public String getAddress(){
        return khttp.server().getAddress().toString();
    }

    public void create(){
        AtomicReference<KHTTP> inner = new AtomicReference<>(KHTTP.of());
        Arrays.stream(ActionPathEnum.values()).forEach(actionPathEnum -> inner.set(oneBotApi(actionPathEnum, this.khttp)));
        inner.get().ok(new InetSocketAddress(host, port));
    }

    public void close(){
        khttp.stop(2);
    }

    private KHTTP oneBotApi(ActionPathEnum path, KHTTP khttp){
       return khttp.post("/" + path.getPath(), (req, res) -> {
           if (req.contentType().equals("application/json")){
               final String str = req.str();
               JsonsObject json = new JsonsObject(str);
               var echo = json.optString("echo");
               var action = json.optString("action");
               var actionPath = path.getPath();
               if (actionPath.equals(action)){
                   var responseDTO = this.botSession.getApiImpl().callMiraiApi(action, json.optJSONObject("params"));
                   responseDTO.setEcho(echo);
                   res.getHeaders().set("Content-Type", "application/json");
                   res.send(GsonUtils.getGson().toJson(responseDTO));
               }
               else
                   res.write(404, "API 不存在".getBytes(StandardCharsets.UTF_8));
           }
           else
               res.write(406, "Content-Type 不支持".getBytes(StandardCharsets.UTF_8));


       });
    }

}
