package cn.evolvefield.mirai.onebot.dto.event.connect;

import cn.evolvefield.mirai.onebot.dto.event.Event;
import com.google.gson.annotations.SerializedName;
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
public class HeartbeatMetaEventDTO extends Event {
    @SerializedName("status")
    private PluginStatusData status;

    @SerializedName("interval")
    private long interval;


    public HeartbeatMetaEventDTO(long selfId, long time, PluginStatusData status, long interval){
        this.setSelfId(selfId);
        this.setTime(time);
        this.status = status;
         this.interval = interval;
        this.setPostType("meta_event");
    }

}
