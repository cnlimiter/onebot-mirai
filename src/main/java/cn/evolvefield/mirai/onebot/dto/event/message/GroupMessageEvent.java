package cn.evolvefield.mirai.onebot.dto.event.message;

import cn.evolvefield.mirai.onebot.entity.Anonymous;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.mamoe.mirai.contact.Member;

/**
 * @author cnlimiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class GroupMessageEvent extends MessageEvent {

    @JSONField(name = "message_id")
    private int messageId;

    @JSONField(name = "sub_type")
    private String subType;

    @JSONField(name = "group_id")
    private long groupId;

    @JSONField(name = "anonymous")
    private Anonymous anonymous;

    @JSONField(name = "sender")
    private GroupSender sender;

    /**
     * sender信息
     */
    @Data
    public static class GroupSender {
        public GroupSender(Member sender){
            this.userId = sender.getId();
            this.nickname = sender.getNick();
            this.card = sender.getNameCard();
            this.age = sender.queryProfile().getAge();
            this.area = "";
            this.sex = sender.queryProfile().getSex().name().toLowerCase();
            this.role = sender.getPermission().name().toLowerCase();
            this.level = String.valueOf(sender.queryProfile().getQLevel());
            this.title = sender.getSpecialTitle();

        }


        @JSONField(name = "user_id")
        private long userId;

        @JSONField(name = "nickname")
        private String nickname;

        @JSONField(name = "card")
        private String card;

        @JSONField(name = "sex")
        private String sex;

        @JSONField(name = "age")
        private int age;

        @JSONField(name = "area")
        private String area;

        @JSONField(name = "level")
        private String level;

        @JSONField(name = "role")
        private String role;

        @JSONField(name = "title")
        private String title;

    }

}
