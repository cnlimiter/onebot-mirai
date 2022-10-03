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
 * Date: 2022/10/3 13:21
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class LifecycleMetaEventDTO extends Event {

    public LifecycleMetaEventDTO(long selfId, String subType, long time){
        this.setSelfId(selfId);
        this.subType= subType;
        this.setTime(time);
        this.setPostType("meta_event");
    }

    @SerializedName("sub_type")
    private String subType;// enable、disable、connect

}
