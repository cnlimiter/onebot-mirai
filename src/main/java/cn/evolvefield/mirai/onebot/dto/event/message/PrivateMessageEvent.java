package cn.evolvefield.mirai.onebot.dto.event.message;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;

/**
 * 私聊消息
 *
 * @author cnlimiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class PrivateMessageEvent extends MessageEvent {

    @JSONField(name = "message_id")
    private int messageId;

    @JSONField(name = "sub_type")
    private String subType;

    @JSONField(name = "sender")
    private PrivateSender privateSender;

    /**
     * sender信息
     */
    @Data
    public static class PrivateSender {
        public PrivateSender(Friend sender){
            this.userId = sender.getId();
            this.nickname = sender.getNick();
            this.age = sender.queryProfile().getAge();
            this.sex = sender.queryProfile().getSex().name().toLowerCase();
        }
        public PrivateSender(NormalMember sender){
            this.userId = sender.getId();
            this.nickname = sender.getNick();
            this.age = sender.queryProfile().getAge();
            this.sex = sender.queryProfile().getSex().name().toLowerCase();
        }
        @JSONField(name = "user_id")
        private long userId;

        @JSONField(name = "nickname")
        private String nickname;

        @JSONField(name = "sex")
        private String sex;

        @JSONField(name = "age")
        private int age;

    }

}
