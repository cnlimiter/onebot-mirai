package cn.evole.onebot.mirai;

import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.SessionManager;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.MiraiLogger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public final class OneBotMirai extends JavaPlugin {
    public static final OneBotMirai INSTANCE = new OneBotMirai();

    public static MiraiLogger logger = INSTANCE.getLogger();

    public PluginConfig config;
    private Listener<? extends Event> initialSubscription = null;

    private OneBotMirai() {
        super(new JvmPluginDescriptionBuilder("cn.evole.mirai.onebot", "0.1.6")
                .name("OneBot Mirai")
                .author("cnlimiter")
                .build());
        //super(JvmPluginDescription.loadFromResource("plugin.yml"));
    }

    @Override
    public void onEnable() {
        logger.info("Plugin loaded!");
        this.reloadPluginConfig(PluginConfig.INSTANCE);
        logger.info("Plugin loaded!");
        logger.info("插件当前Commit 版本: 0.1.5");

        List<Bot> instances = Bot.getInstances();
        logger.info(String.format("instances length: %d", instances.size()));
        instances.forEach(bot -> {
            logger.info(String.format("bot : %d", bot.getId()));
            if (!SessionManager.getSessions().containsKey(bot.getId())) {
                var botId = String.valueOf(bot.getId());
                if (Objects.requireNonNull(PluginConfig.INSTANCE.getBots()).containsKey(botId)){
                    var mapConfig = PluginConfig.INSTANCE.getBots().get(botId);
                    //for (String name : PluginConfig.INSTANCE.getBots().get(botId).keySet()){
                    //    if (mapConfig.get(name).getEnable())
                            SessionManager.createBotSession(bot, mapConfig);
                        logger.info(String.format("创建配置: %d", bot.getId()));

                    //}
                }
                else {
                    logger.debug(String.format("%s 未进行onebot配置", bot.getId()));
                }
            }
            else {
                logger.debug(String.format("%s 已经存在", bot.getId()));
            }
        });

        initialSubscription = GlobalEventChannel.INSTANCE.subscribeAlways(BotEvent.class, event -> {
            if (SessionManager.getSessions().containsKey(event.getBot().getId())){
                SessionManager.getSessions().get(event.getBot().getId()).triggerEvent(event);
            }
            if (event instanceof BotOnlineEvent onlineEvent){
                logger.warning(String.format("SessionManager当前session长度： %d", SessionManager.getSessions().size()));
                if (!SessionManager.containsSession(onlineEvent.getBot().getId())){
                    var botId = String.valueOf(onlineEvent.getBot().getId());
                    if (Objects.requireNonNull(PluginConfig.INSTANCE.getBots()).containsKey(String.valueOf(event.getBot().getId()))){
                        var mapConfig = PluginConfig.INSTANCE.getBots().get(botId);
//                        for (String name : PluginConfig.INSTANCE.bots.get().get(botId).keySet()){
//                            if (mapConfig.get(name).getEnable())
//                                SessionManager.createBotSession(onlineEvent.getBot(), PluginConfig.INSTANCE.bots.get().get(botId).get(name));
//
//                        }
                        SessionManager.createBotSession(onlineEvent.getBot(), mapConfig);
                        logger.warning(String.format("%s 创建反向ws监听", event.getBot().getId()));

                    }
                    else {
                        logger.warning(String.format("%s 未进行onebot配置", event.getBot().getId()));
                    }
                }

            }
            else if (event instanceof MessageEvent messageEvent){
                if (SessionManager.containsSession(messageEvent.getBot().getId())) {
                    var session = SessionManager.get(messageEvent.getBot().getId());
                    if (messageEvent instanceof GroupMessageEvent groupMessageEvent){
                        session.getApiImpl().getCachedSourceQueue().add(groupMessageEvent.getSource());
                    }
                    else if (messageEvent instanceof GroupTempMessageEvent groupTempMessageEvent){
                        session.getApiImpl().getCachedTempContact()
                                .put(groupTempMessageEvent.getSender().getId(), groupTempMessageEvent.getGroup().getId());
                    }
                }
            }
            else if (event instanceof NewFriendRequestEvent requestEvent) {
                if (SessionManager.containsSession(requestEvent.getBot().getId())) {
                    var session = SessionManager.get(requestEvent.getBot().getId());
                    session.getApiImpl().getCacheRequestQueue().add(requestEvent);
                }
            }
            else if (event instanceof MemberJoinRequestEvent requestEvent) {
                if (SessionManager.containsSession(requestEvent.getBot().getId())) {
                    var session = SessionManager.get(requestEvent.getBot().getId());
                    session.getApiImpl().getCacheRequestQueue().add(requestEvent);
                }
            }
            else if (event instanceof BotInvitedJoinGroupRequestEvent requestEvent) {
                if (SessionManager.containsSession(requestEvent.getBot().getId())) {
                    var session = SessionManager.get(requestEvent.getBot().getId());
                    session.getApiImpl().getCacheRequestQueue().add(requestEvent);
                }
            }
        });
    }

    @Override
    public void onDisable() {
        initialSubscription.complete();
        SessionManager.getSessions().forEach((aLong, botSession) -> {
            SessionManager.closeSession(aLong);
        });
        logger.info("OneBot 已关闭");
    }
}

