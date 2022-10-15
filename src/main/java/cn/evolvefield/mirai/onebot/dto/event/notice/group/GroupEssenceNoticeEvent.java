package cn.evolvefield.mirai.onebot.dto.event.notice.group;

import cn.evolvefield.mirai.onebot.dto.event.notice.NoticeEvent;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/1 23:30
 * Version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GroupEssenceNoticeEvent extends NoticeEvent {
    @JSONField(name = "sub_type")
    private String  subType;//add,delete	添加为add,移出为delete
    @JSONField(name = "sender_id")
    private Long  senderId	;//消息发送者ID
    @JSONField(name = "operator_id")
    private Long operatorId;//操作者ID
    @JSONField(name = "message_id")
    private Long messageId	;//消息ID
}
