package cn.evolvefield.mirai.onebot.dto.event.notice;

import com.google.gson.annotations.SerializedName;
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
public class PokeNoticeEvent extends NoticeEvent {

    @SerializedName( "sub_type")
    private String subType;

    @SerializedName( "self_id")
    private long selfId;

    @SerializedName( "sender_id")
    private long senderId;

    @SerializedName( "user_id")
    private long userId;

    @SerializedName( "target_id")
    private long targetId;

    @SerializedName( "group_id")
    private long groupId;

    @SerializedName( "time")
    private long time;

}
