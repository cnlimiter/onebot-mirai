package cn.evolvefield.mirai.onebot.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/14 17:39
 * Version: 1.0
 */
@Data
public class GuildMsgId {
    @JSONField(name =  "message_id")
    private String messageId;

}
