package cn.evolvefield.mirai.onebot;

import cn.evolvefield.mirai.onebot.config.PluginConfig;
import cn.evolvefield.mirai.onebot.core.SessionManager;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.MiraiLogger;

public final class OneBotMirai extends JavaPlugin {
    public static final OneBotMirai INSTANCE = new OneBotMirai();

    public static MiraiLogger logger = INSTANCE.getLogger();

    public PluginConfig config;
    private Listener<? extends Event> initialSubscription = null;

    private OneBotMirai() {
        super(new JvmPluginDescriptionBuilder("cn.evolvefield.mirai.onebot", "0.1.3")
                .name("OneBot Mirai")
                .author("cnlimiter")
                .build());
        //super(JvmPluginDescription.loadFromResource("plugin.yml"));
    }

    @Override
    public void onEnable() {
        logger.info("Plugin loaded!");
        this.reloadPluginData(PluginConfig.INSTANCE);
        logger.info("Plugin loaded!");
        logger.info("插件当前Commit 版本: 0.1.3");
        Bot.getInstances().forEach(bot -> {
            if (!SessionManager.getSessions().containsKey(bot.getId())) {
                if (PluginConfig.INSTANCE.bots.get().containsKey(String.valueOf(bot.getId()))){
                    SessionManager.createBotSession(bot, PluginConfig.INSTANCE.bots.get().get(String.valueOf(bot.getId())));
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
                if (!SessionManager.containsSession(onlineEvent.getBot().getId())){
                    if (PluginConfig.INSTANCE.bots.get().containsKey(String.valueOf(event.getBot().getId()))){
                        SessionManager.createBotSession(event.getBot(), PluginConfig.INSTANCE.bots.get().get(String.valueOf(event.getBot().getId())));
                    }
                    else {
                        logger.debug(String.format("%s 未进行onebot配置", event.getBot().getId()));
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

