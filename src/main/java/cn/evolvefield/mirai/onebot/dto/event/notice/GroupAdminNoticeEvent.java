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
public class GroupAdminNoticeEvent extends NoticeEvent {

    /**
     * set、unset
     * 事件子类型, 分别表示设置和取消管理
     */
    @SerializedName( "sub_type")
    private String subType;

    /**
     * 群号
     */
    @SerializedName( "group_id")
    private long groupId;

    /**
     * 管理员 QQ 号
     */
    @SerializedName( "user_id")
    private long userId;

}
