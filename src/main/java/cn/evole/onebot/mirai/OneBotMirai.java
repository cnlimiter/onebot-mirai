package cn.evole.onebot.mirai;

import cn.evole.onebot.mirai.cmd.OneBotMiraiCmd;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.session.SessionManager;
import cn.evole.onebot.mirai.database.NanoDb;
import cn.evole.onebot.mirai.util.BaseUtils;
import cn.evole.onebot.mirai.util.DBUtils;
import cn.evole.onebot.mirai.util.HttpUtils;
import cn.evole.onebot.sdk.util.FileUtils;
import lombok.val;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.OnlineAudio;
import net.mamoe.mirai.utils.MiraiLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

import static cn.evole.onebot.mirai.util.ImgUtils.constructCacheImageMeta;
import static cn.evole.onebot.mirai.util.ImgUtils.getImageType;

public final class OneBotMirai extends JavaPlugin {
    public static String VERSION;
    public static final OneBotMirai INSTANCE = new OneBotMirai();

    public static MiraiLogger logger = INSTANCE.getLogger();

    private Listener<? extends Event> initialSubscription = null;

    private OneBotMirai() {
        VERSION = getDescription().getVersion().toString();
    }

    public final File imageFolder = new File(getDataFolder(), "image");
    public final File recordFolder = new File(getDataFolder(), "record");
    public final File dbFolder = new File(getDataFolder(), "db");
    public NanoDb<Long, DBUtils.MessageNode> db  = null;

    @Override
    public void onEnable() {

        this.reloadPluginConfig(PluginConfig.INSTANCE);
        FileUtils.checkFolder(imageFolder.toPath());
        FileUtils.checkFolder(recordFolder.toPath());
        if (PluginConfig.INSTANCE.getDb().getEnable()) {
            try {
                db = new NanoDb<>(dbFolder + "/db.obm");
            } catch (Exception ignored) {
            }
        }

        CommandManager.INSTANCE.registerCommand(OneBotMiraiCmd.INSTANCE, false);

        logger.info("OneBot Loaded!");
        logger.info("插件当前版本: " + VERSION);
        logger.info("开发交流群: 720975019");



        List<Bot> instances = Bot.getInstances();
        logger.info(String.format("当前存在的机器人数量: %d", instances.size()));
        instances.forEach(bot -> {
            logger.info(String.format("Bot : %s 开始创建", bot.getId()));
            if (!SessionManager.getSessions().containsKey(bot.getId())) {
                val botId = String.valueOf(bot.getId());
                if (Objects.requireNonNull(PluginConfig.INSTANCE.getBots()).containsKey(botId)){
                    val mapConfig = PluginConfig.INSTANCE.getBots().get(botId);
                    SessionManager.createBotSession(bot, mapConfig);
                        logger.info(String.format("Bot: %s 创建Session", bot.getId()));
                }
                else {
                    logger.debug(String.format("Bot: %s 未进行OneBot配置,请在setting.yml中进行配置", bot.getId()));
                }
            }
            else {
                logger.debug(String.format("Bot: %s 已经存在", bot.getId()));
            }
        });

        initialSubscription = GlobalEventChannel.INSTANCE.subscribeAlways(BotEvent.class, event -> {
            if (SessionManager.getSessions().containsKey(event.getBot().getId())){
                SessionManager.getSessions().get(event.getBot().getId()).triggerEvent(event);
            }
            if (event instanceof BotOnlineEvent onlineEvent){
                if (!SessionManager.containsSession(onlineEvent.getBot().getId())){
                    val botId = String.valueOf(onlineEvent.getBot().getId());
                    if (Objects.requireNonNull(PluginConfig.INSTANCE.getBots()).containsKey(String.valueOf(event.getBot().getId()))){
                        val mapConfig = PluginConfig.INSTANCE.getBots().get(botId);
                        val session = SessionManager.createBotSession(onlineEvent.getBot(), mapConfig);
                        logger.info(String.format("Bot: %s 创建Session", event.getBot().getId()));
                        if (PluginConfig.INSTANCE.getDebug()) logger.info("OneBot Session: " + session);
                    }
                    else {
                        logger.warning(String.format("Bot: %s 未进行OneBot配置,请在setting.yml中进行配置", event.getBot().getId()));
                    }
                }

            }
            else if (event instanceof MessageEvent messageEvent){
                val bot = messageEvent.getBot();
                val botId = bot.getId();
                if (SessionManager.containsSession(botId)) {
                    DBUtils.saveMessageToDB(messageEvent);//存入数据库
                    val session = SessionManager.get(botId);
                    if (messageEvent instanceof GroupMessageEvent groupMessageEvent){
                        session.getApiImpl().getCachedSourceQueue().add(groupMessageEvent.getSource());
                    }

                    if (messageEvent instanceof GroupTempMessageEvent groupTempMessageEvent){
                        session.getApiImpl().getCachedTempContact()
                                .put(groupTempMessageEvent.getSender().getId(), groupTempMessageEvent.getGroup().getId());
                    }

                    if (session.getBotConfig().getCacheImage()){
                        messageEvent.getMessage().stream()
                                .filter(singleMessage -> singleMessage instanceof Image)
                                .forEach(singleMessage -> {
                                    val img = (Image) singleMessage;
                                    long imageSize;
                                    val imageMD5 = BaseUtils.bytesToHexString(img.getMd5());
                                    val subject = messageEvent.getSubject();
                                    if (subject instanceof Member || subject instanceof Friend){
                                        val imageHeight = img.getHeight();
                                        val imageWidth = img.getWidth();
                                        imageSize = (long) imageHeight * imageWidth;
                                    }
                                    else if (subject instanceof Group){
                                        imageSize = img.getSize();
                                    }
                                    else imageSize = 0;

                                    val imgMetaContent = constructCacheImageMeta(
                                            imageMD5,
                                            imageSize,
                                            Mirai.getInstance().queryImageUrl(bot, img),
                                            getImageType(img)
                                    );
                                    saveImageOrRecord(
                                            img.getImageId() + ".cqimg",
                                            imgMetaContent,
                                            true
                                    );

                                });
                    }
                    if (session.getBotConfig().getCacheRecord()){
                        messageEvent.getMessage().stream()
                                .filter(singleMessage -> singleMessage instanceof OnlineAudio)
                                .forEach(singleMessage -> {
                                    val audio = (OnlineAudio) singleMessage;
                                    val voiceUrl = audio.getUrlForDownload();
                                    val voiceBytes = HttpUtils.getBytesFromHttpUrl(voiceUrl);
                                    if (voiceBytes != null) {
                                        saveImageOrRecord(
                                                BaseUtils.bytesToHexString(audio.getFileMd5()) + ".cqrecord",
                                                voiceBytes,
                                                false
                                        );
                                    }
                                });
                    }


                }
            }
            else if (event instanceof NewFriendRequestEvent requestEvent) {
                if (SessionManager.containsSession(requestEvent.getBot().getId())) {
                    val session = SessionManager.get(requestEvent.getBot().getId());
                    session.getApiImpl().getCacheRequestQueue().add(requestEvent);
                }
            }
            else if (event instanceof MemberJoinRequestEvent requestEvent) {
                if (SessionManager.containsSession(requestEvent.getBot().getId())) {
                    val session = SessionManager.get(requestEvent.getBot().getId());
                    session.getApiImpl().getCacheRequestQueue().add(requestEvent);
                }
            }
            else if (event instanceof BotInvitedJoinGroupRequestEvent requestEvent) {
                if (SessionManager.containsSession(requestEvent.getBot().getId())) {
                    val session = SessionManager.get(requestEvent.getBot().getId());
                    session.getApiImpl().getCacheRequestQueue().add(requestEvent);
                }
            }
        });
    }

    @Override
    public void onDisable() {
        this.db.save();
        initialSubscription.complete();
        SessionManager.getSessions().forEach((aLong, botSession) -> SessionManager.closeSession(aLong));
        logger.info("OneBot Stopped!");
    }


    private File image(String imageName){
        return new File(this.imageFolder, imageName);
    }

    private File record(String recordName){
        return new File(this.recordFolder, recordName);
    }

    public void saveImageOrRecord(String name, String data, boolean img){
        BaseUtils.safeRun(() -> {
            if(data != null) {
                try {
                    Files.writeString(img ? image(name).toPath() : record(name).toPath(), data, StandardOpenOption.CREATE_NEW);
                } catch (IOException ignored) {}
            }
        });
    }

    public void saveImageOrRecord(String name, byte[] data, boolean img){
        BaseUtils.safeRun(() -> {
            if(data != null) {
                try {
                    Files.write(img ? image(name).toPath() : record(name).toPath(), data, StandardOpenOption.CREATE_NEW);
                } catch (IOException ignored) {}
            }
        });
    }
}

