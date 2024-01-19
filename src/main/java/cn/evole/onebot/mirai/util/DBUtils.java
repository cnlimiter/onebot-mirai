package cn.evole.onebot.mirai.util;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.database.csv.Localizer;
import cn.evole.onebot.sdk.util.DataBaseUtils;
import lombok.val;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/18 0:44
 * @Description:
 */

public class DBUtils {
    public static class MessageNode implements Serializable {
        @Serial
        private static final long serialVersionUID = 114514114514L;
        long contactId;
        int messageId;
        String content;
        public MessageNode(long contactId, int messageId, String content) {
            this.contactId = contactId;
            this.messageId = messageId;
            this.content = content;
        }
    }

    public static class Locates {
        private static Localizer l = null;

        public static Localizer getLocalizer() {
            if (l == null) {
                l = new Localizer();
                l.add("contactId", "群或者陌生人或者好友id");
                l.add("messageId", "消息id");
                l.add("content", "内容");
            }
            return l;
        }
    }
    public static void saveMessageToDB(MessageEvent event) {
        if (PluginConfig.INSTANCE.getDb().getEnable()){
            val messageId = DataBaseUtils.toMessageId(event.getSource().getInternalIds(), event.getBot().getId(), event.getSource().getFromId());
            if (OneBotMirai.INSTANCE.db!=null){
                OneBotMirai.INSTANCE.db.set(
                        event.getBot().getId(),
                        new MessageNode(
                                event.getSubject().getId(),
                                messageId,
                                MessageChain.serializeToJsonString(event.getMessage()))
                        );
            }
        }
    }
}
