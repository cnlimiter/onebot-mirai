package cn.evole.onebot.mirai.web.http;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.session.BotSession;
import cn.evole.onebot.sdk.enums.ActionPathEnum;
import cn.evole.onebot.sdk.util.json.GsonUtils;
import com.google.gson.JsonObject;
import io.github.kongweiguang.khttp.KHTTP;
import io.github.kongweiguang.khttp.core.Req;
import io.github.kongweiguang.khttp.core.Res;
import net.mamoe.mirai.utils.MiraiLogger;

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
    private final MiraiLogger miraiLogger = OneBotMirai.logger;
    private final BotSession botSession;
    private final PluginConfig.HTTPConfig config;
    private final KHTTP khttp;

    public OneBotHttpServer(BotSession botSession, PluginConfig.BotConfig botConfig) {
        this.botSession = botSession;
        this.config = botConfig.getHttp();
        this.khttp = KHTTP.of().executor(Executors.newCachedThreadPool());
    }

    public String getAddress(){
        return khttp.server().getAddress().toString();
    }

    public void create(){
        AtomicReference<KHTTP> inner = new AtomicReference<>(KHTTP.of());
        Arrays.stream(ActionPathEnum.values()).forEach(actionPathEnum -> inner.set(oneBotApi(actionPathEnum, this.khttp)));
        inner.get().ok(new InetSocketAddress(config.getHost(), config.getPort()));
    }

    public void close(){
        khttp.stop(2);
    }

    private KHTTP oneBotApi(ActionPathEnum path, KHTTP khttp){
        miraiLogger.debug("创建/"+path.getPath()+"接口");

        return khttp.post("/" + path.getPath(), (req, res) -> {
           if (!config.getAccessToken().isBlank()){
               if (req.header("Authorization").equals("Bearer " + config.getAccessToken())) apiAction(path, req, res);
               else res.write(406, "鉴权失败".getBytes(StandardCharsets.UTF_8));
           }
           else apiAction(path, req, res);
       });
    }

    private void apiAction(ActionPathEnum path, Req req, Res res) {
        if (req.contentType().equals("application/json")){
            final String str = req.str();
            JsonObject json = GsonUtils.parse(str);
            var echo = GsonUtils.getAsString(json,"echo");
            var action = GsonUtils.getAsString(json,"action");
            if (action.equals(path.getPath())){
                var responseDTO = this.botSession.getApiImpl().callMiraiApi(action, GsonUtils.getAsJsonObject(json,"params"));
                responseDTO.setEcho(echo);
                res.getHeaders().set("Content-Type", "application/json");
                res.send(GsonUtils.getGson().toJson(responseDTO));
            }
            else
                res.write(404, "API 不存在".getBytes(StandardCharsets.UTF_8));
        }
        else
            res.write(406, "Content-Type 不支持".getBytes(StandardCharsets.UTF_8));
    }

}
