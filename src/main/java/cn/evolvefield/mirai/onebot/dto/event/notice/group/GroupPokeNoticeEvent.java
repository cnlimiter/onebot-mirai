package cn.evolvefield.mirai.onebot.dto.event.notice.group;

import cn.evolvefield.mirai.onebot.dto.event.notice.NoticeEvent;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupPokeNoticeEvent extends NoticeEvent {

    @JSONField(name = "sub_type")
    private String subType;

    @JSONField(name = "target_id")
    private long targetId;

    @JSONField(name = "group_id")
    private long groupId;


}
