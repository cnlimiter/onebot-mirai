package cn.evolvefield.mirai.onebot.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/13 22:06
 * Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgId {
    @JSONField(name =  "message_id")
    private int messageId;
}
