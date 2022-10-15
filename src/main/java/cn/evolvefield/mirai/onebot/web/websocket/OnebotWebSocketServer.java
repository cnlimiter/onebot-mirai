//package cn.evolvefield.mirai.onebot.web.websocket;
//
//import cn.evolvefield.mirai.onebot.OneBotMirai;
//import cn.evolvefield.mirai.onebot.core.BotSession;
//import cn.evolvefield.mirai.onebot.util.ActionUtils;
//import cn.evolvefield.mirai.onebot.web.websocket.core.FastWSServer;
//import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketServer;
//import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketSession;
//
///**
// * Description:
// * Author: cnlimiter
// * Date: 2022/10/8 15:21
// * Version: 1.0
// */
//public class OnebotWebSocketServer extends FastWSServer implements WebSocketServer {
//
//    private final BotSession botSession;
//
//    public OnebotWebSocketServer(BotSession botSession){
//        this.botSession = botSession;
//    }
//
//    public void create(){
//        super.start(botSession.getBotConfig().getWsHost(), botSession.getBotConfig().getWsPort());
//    }
//
//    @Override
//    public void onOpen(WebSocketSession session) {
//        super.onOpen(session);
//        OneBotMirai.logger.info(String.format("Bot: %s 正向Websocket服务端 / 成功连接", botSession.getBot().getId()));
//    }
//
//    @Override
//    public void onMessage(WebSocketSession session, String message) {
//        OneBotMirai.logger.debug(String.format("Bot: %s 正向Websocket服务端 / 开始处理API请求", botSession.getBot().getId()));
//        ActionUtils.handleWebSocketActions(botSession, botSession.getApiImpl(), message);
//        botSession.subscribeEvent(session);
//    }
//
//
//    @Override
//    public void onClose(WebSocketSession session) {
//        super.onClose(session);
//        OneBotMirai.logger.info(String.format("Bot: %s 正向Websocket服务端 / 连接被关闭", botSession.getBot().getId()));
//    }
//
//    @Override
//    public void onError(WebSocketSession session, Throwable e) {
//        super.onError(session, e);
//        OneBotMirai.logger.warning(String.format("Bot: %s 正向Websocket服务端 / 出现错误 \n %s", botSession.getBot().getId(), e.getMessage()));
//    }
//
//
//    public void close(){
//        super.stop();
//    }
//
//}
