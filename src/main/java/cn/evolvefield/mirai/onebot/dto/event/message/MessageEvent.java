package cn.evolvefield.mirai.onebot.dto.event.message;

import cn.evolvefield.mirai.onebot.entity.MsgChainBean;
import cn.evolvefield.mirai.onebot.dto.event.Event;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author cnlimiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class MessageEvent extends Event {

    @JSONField(name = "message_type")
    private String messageType;

    @JSONField(name = "user_id")
    private long userId;

    @JSONField(name = "message")
    private String message;

    @JSONField(name = "raw_message")
    private String rawMessage;

    @JSONField(name = "font")
    private int font;

    private List<MsgChainBean> arrayMsg;

}
