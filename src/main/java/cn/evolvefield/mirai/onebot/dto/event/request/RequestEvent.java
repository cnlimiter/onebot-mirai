package cn.evolvefield.mirai.onebot.dto.event.request;

import cn.evolvefield.mirai.onebot.dto.event.Event;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class RequestEvent extends Event {

    @JSONField(name = "request_type")
    private String requestType;

    @JSONField(name = "user_id")
    private long userId;

    @JSONField(name = "comment")
    private String comment;

    @JSONField(name = "flag")
    private String flag;

}
