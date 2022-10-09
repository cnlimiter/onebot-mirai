package cn.evolvefield.mirai.onebot.dto.event.meta;

import cn.evolvefield.mirai.onebot.dto.event.Event;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/3 13:28
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class HeartbeatMetaEvent extends MetaEvent {
    @JSONField(name = "status")
    private Object status;

    @JSONField(name = "interval")
    private long interval;


    public HeartbeatMetaEvent(long selfId, long time, Object status, long interval){
        this.setSelfId(selfId);
        this.setTime(time);
        this.status = status;
        this.interval = interval;
    }

}
