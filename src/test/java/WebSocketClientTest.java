import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.session.BotSession;
import cn.evole.onebot.mirai.web.websocket.OneBotWSClient;
import cn.evole.onebot.sdk.action.ActionData;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.MiraiLogger;
import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.impl.SimpleWsEchoServer;

import java.net.InetSocketAddress;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/8 0:14
 * Version: 1.0
 */
public class WebSocketClientTest{
    static WebSocketServer simpleServer;

    @BeforeAll
    public static void construct(){
        simpleServer = new SimpleWsEchoServer(new InetSocketAddress(2342));
        simpleServer.start();
    }

    @Test
    public void testConnect() throws InterruptedException {
        BotSession bsm = Mockito.mock(BotSession.class);
        Bot bot = Mockito.mock(Bot.class);
        Mockito.when(bsm.getBot()).thenReturn(bot);
        Mockito.when(bot.getId()).thenReturn(1L);
        Mockito.when(bot.getLogger()).thenReturn(MiraiLogger.Factory.INSTANCE.create(this.getClass()));
        PluginConfig.WSReverseConfig reverseConfig = new PluginConfig.WSReverseConfig();
        reverseConfig.setReverseHost("localhost");
        reverseConfig.setReversePort(2342);
        reverseConfig.setUseUniversal(true);
        reverseConfig.setAccessToken("testToken");


        OneBotWSClient oneBotWSClient = new OneBotWSClient(bsm, reverseConfig);
        oneBotWSClient.connectBlocking();
        //oneBotWSClient.send("hello world");
        simpleServer.broadcast("""
                {
                    "action": "send_group_msg",
                    "params": {
                        "参数名": "参数值",
                        "参数名2": "参数值"
                    }
                }""");


    }
}
