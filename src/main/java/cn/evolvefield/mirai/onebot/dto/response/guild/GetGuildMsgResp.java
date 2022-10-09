package cn.evolvefield.mirai.onebot.dto.response.guild;

import cn.evolvefield.mirai.onebot.dto.event.message.GuildMessageEvent;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;


@Data
public class GetGuildMsgResp {

    @JSONField(name = "guild_id")
    private String guildId;

    @JSONField(name = "channel_id")
    private String channelId;

    @JSONField(name = "message")
    private String message;

    @JSONField(name = "message_id")
    private String messageId;

    @JSONField(name = "message_seq")
    private int messageSeq;

    @JSONField(name = "message_source")
    private String messageSource;

    @JSONField(name = "sender")
    private GuildMessageEvent.Sender sender;

    @JSONField(name = "time")
    private long time;

}
