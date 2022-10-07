package cn.evolvefield.mirai.onebot.dto.event.message;

import com.google.gson.annotations.SerializedName;
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

    @SerializedName( "message_id")
    private int messageId;

    @SerializedName( "sub_type")
    private String subType;

    @SerializedName( "sender")
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
        @SerializedName( "user_id")
        private long userId;

        @SerializedName( "nickname")
        private String nickname;

        @SerializedName( "sex")
        private String sex;

        @SerializedName( "age")
        private int age;

    }

}
