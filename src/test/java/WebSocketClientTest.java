import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/8 0:14
 * Version: 1.0
 */
public class WebSocketClientTest{
    public static WebSocketClient client = null;


    private static final Logger log = LoggerFactory.getLogger("client");
    public static void main(String[] args) {
        if (WebSocketClientTest.client != null) {
            WebSocketClientTest.client.close();
        }
        try {
            client = new WebSocketClient(new URI("ws://127.0.0.1:8080")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("启用框架");
                }

                @Override
                public void onMessage(String message) {//执行接收到消息体后的相应操作
                    log.info(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    log.warn("出现错误!");
                    ex.printStackTrace();
                }
            };
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
