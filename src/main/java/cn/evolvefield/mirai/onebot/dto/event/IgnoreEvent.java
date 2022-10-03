package cn.evolvefield.mirai.onebot.dto.event;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/3 13:20
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class IgnoreEvent {
    @SerializedName("post_type")
    private String postType = "IGNORED";

    @SerializedName( "time")
    private long time = System.currentTimeMillis();

}
