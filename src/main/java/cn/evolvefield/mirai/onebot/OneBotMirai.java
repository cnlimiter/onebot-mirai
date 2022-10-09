package cn.evolvefield.mirai.onebot;

import cn.evolvefield.mirai.onebot.config.PluginConfig;
import cn.evolvefield.mirai.onebot.core.SessionManager;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.AbstractJvmPlugin;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
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
        super(new JvmPluginDescriptionBuilder("cn.evolvefield.mirai.onebot", "0.1.0")
                .name("OneBot Mirai")
                .author("cnlimiter")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
        this.config = new PluginConfig();
        this.reloadPluginData(config);
        logger.info("Plugin loaded!");
        logger.info("插件当前Commit 版本: 0.1.2 ");
        Bot.getInstances().forEach(bot -> {
            if (!SessionManager.getSessions().containsKey(bot.getId())) {
                if (PluginConfig.bots.containsKey(String.valueOf(bot.getId()))){
                    SessionManager.createBotSession(bot, PluginConfig.bots.get(String.valueOf(bot.getId())));
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
                SessionManager.getSessions().get(event.getBot().getId()).subscribeEvent(event);
            }
            if (event instanceof BotOnlineEvent onlineEvent){
                if (!SessionManager.containsSession(onlineEvent.getBot().getId())){
                    if (PluginConfig.bots.containsKey(String.valueOf(event.getBot().getId()))){
                        SessionManager.createBotSession(event.getBot(), PluginConfig.bots.get(String.valueOf(event.getBot().getId())));
                    }
                    else {
                        logger.debug(String.format("%s 未进行onebot配置", event.getBot().getId()));
                    }
                }

            }
            else if (event instanceof MessageEvent messageEvent){
                if (!SessionManager.containsSession(messageEvent.getBot().getId())) {
                    var session = SessionManager.get(messageEvent.getBot().getId());
                }
            }
            else if (event instanceof NewFriendRequestEvent requestEvent) {
                if (!SessionManager.containsSession(requestEvent.getBot().getId())) {
                    var session = SessionManager.get(requestEvent.getBot().getId());
                    session.getApiImpl().getCacheRequestQueue().add(requestEvent);
                }
            }
            else if (event instanceof MemberJoinRequestEvent requestEvent) {
                if (!SessionManager.containsSession(requestEvent.getBot().getId())) {
                    var session = SessionManager.get(requestEvent.getBot().getId());
                    session.getApiImpl().getCacheRequestQueue().add(requestEvent);
                }
            }
            else if (event instanceof BotInvitedJoinGroupRequestEvent requestEvent) {
                if (!SessionManager.containsSession(requestEvent.getBot().getId())) {
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

