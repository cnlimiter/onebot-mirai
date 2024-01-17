package cn.evole.onebot.mirai.util;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.sdk.util.DataBaseUtils;
import lombok.val;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/18 0:44
 * @Description:
 */

public class DBUtils {
    public static void saveMessageToDB(MessageEvent event) {
        if (PluginConfig.INSTANCE.getDb().getEnable()){
            val messageId = DataBaseUtils.toMessageId(event.getSource().getInternalIds(), event.getBot().getId(), event.getSource().getFromId());
            if (OneBotMirai.INSTANCE.db!=null){
                OneBotMirai.INSTANCE.db.put(
                        DataBaseUtils.toByteArray(messageId),
                        MessageChain.serializeToJsonString(event.getMessage()).getBytes());
            }
        }
    }
}
