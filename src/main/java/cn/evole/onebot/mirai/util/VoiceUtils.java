package cn.evole.onebot.mirai.util;

import lombok.val;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Audio;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import static cn.evole.onebot.mirai.OneBotMirai.logger;
import static cn.evole.onebot.mirai.util.BaseUtils.getDataFile;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/17 22:27
 * @Description:
 */

public class VoiceUtils {

    public static File getCachedRecordFile(String name){
        return name.endsWith(".cqrecord") ? getDataFile("record", name) : getDataFile("record", "$name.cqrecord");
    }

    public static Audio tryResolveCachedRecord(String name, @Nullable Contact contact){
       return BaseUtils.safeRun(() -> {
            Audio audio1 = null;
            val cacheFile = getCachedRecordFile(name);
            if (cacheFile != null) {
                if (cacheFile.canRead()) {
                    logger.info("此语音已缓存, 如需删除缓存请至 "+ cacheFile.getAbsolutePath());
                    if (contact instanceof Group group){
                        audio1 = group.uploadAudio(ExternalResource.create(cacheFile));
                    }
                } else {
                    logger.error("Record $name cache file cannot read.");
                }
            } else {
                logger.info("Record $name cache file cannot found.");
            }
            return audio1;
        }, null);
    }
}
