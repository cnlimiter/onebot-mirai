package cn.evole.onebot.mirai;

import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.SessionManager;
import cn.evole.onebot.mirai.util.BaseUtils;
import cn.evole.onebot.mirai.util.DBUtils;
import cn.evole.onebot.sdk.util.FileUtils;
import lombok.val;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.MiraiLogger;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

public final class OneBotMirai extends JavaPlugin {
    public static String VERSION;
    public static final OneBotMirai INSTANCE = new OneBotMirai();

    public static MiraiLogger logger = INSTANCE.getLogger();

    private Listener<? extends Event> initialSubscription = null;

    private OneBotMirai() {
        VERSION = getDescription().getVersion().toString();
    }

    private File imageFolder = new File(getDataFolder(), "image");
    private File recordFolder = new File(getDataFolder(), "record");
    public DB db  = null;

    @Override
    public void onEnable() {

        this.reloadPluginConfig(PluginConfig.INSTANCE);
        FileUtils.checkFolder(imageFolder.toPath());
        FileUtils.checkFolder(recordFolder.toPath());
        if (PluginConfig.INSTANCE.getDb().getEnable()) {
            val options = new Options().createIfMissing(true);
            try {
                db = new Iq80DBFactory().open(new File(getDataFolderPath() + "/db"), options);
            } catch (IOException ignored) {
            }
        }

        logger.info("Plugin loaded!");
        logger.info("插件当前版本: " + VERSION);
        logger.info("开发交流群: 720975019");



        List<Bot> instances = Bot.getInstances();
        logger.info(String.format("当前存在的机器人数量: %d", instances.size()));
        instances.forEach(bot -> {
            logger.info(String.format("bot : %d", bot.getId()));
            if (!SessionManager.getSessions().containsKey(bot.getId())) {
                var botId = String.valueOf(bot.getId());
                if (Objects.requireNonNull(PluginConfig.INSTANCE.getBots()).containsKey(botId)){
                    var mapConfig = PluginConfig.INSTANCE.getBots().get(botId);
                    SessionManager.createBotSession(bot, mapConfig);
                        logger.info(String.format("创建配置: %d", bot.getId()));
                }
                else {
                    logger.debug(String.format("%s 未进行OneBot配置,请在setting.yml中进行配置", bot.getId()));
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
                    var botId = String.valueOf(onlineEvent.getBot().getId());
                    if (Objects.requireNonNull(PluginConfig.INSTANCE.getBots()).containsKey(String.valueOf(event.getBot().getId()))){
                        var mapConfig = PluginConfig.INSTANCE.getBots().get(botId);
                        SessionManager.createBotSession(onlineEvent.getBot(), mapConfig);
                        logger.warning(String.format("%s 创建OneBot Session", event.getBot().getId()));

                    }
                    else {
                        logger.warning(String.format("%s 未进行OneBot配置,请在setting.yml中进行配置", event.getBot().getId()));
                    }
                }

            }
            else if (event instanceof MessageEvent messageEvent){
                if (SessionManager.containsSession(messageEvent.getBot().getId())) {
                    DBUtils.saveMessageToDB(messageEvent);//存入数据库
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
        SessionManager.getSessions().forEach((aLong, botSession) -> SessionManager.closeSession(aLong));
        logger.info("OneBot 已关闭");
    }


    private File image(String imageName){
        return new File(this.imageFolder, imageName);
    }

    private File record(String recordName){
        return new File(this.recordFolder, recordName);
    }

    public void saveImageOrRecord(String name, byte[] data, boolean img){
        BaseUtils.safeRun(() -> {
            if(data != null) {
                try {
                    Files.write(img ? image(name).toPath() : record(name).toPath(), data, StandardOpenOption.WRITE);
                } catch (IOException ignored) {}
            }
        });
    }
}

