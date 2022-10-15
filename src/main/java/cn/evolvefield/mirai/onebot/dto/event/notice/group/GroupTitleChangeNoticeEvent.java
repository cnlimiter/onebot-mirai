package cn.evolvefield.mirai.onebot.dto.event.notice.group;

import cn.evolvefield.mirai.onebot.dto.event.notice.NoticeEvent;
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
public class GroupTitleChangeNoticeEvent extends NoticeEvent {

    @JSONField(name = "title_new")
    private String titleNew;

    @JSONField(name = "group_id")
    private long groupId;

    @JSONField(name = "title_old")
    private String titleOld;

    @JSONField(name = "user_id")
    private long userId;

}
