package cn.evolvefield.mirai.onebot.dto.event.message;

import cn.evolvefield.mirai.onebot.entity.Anonymous;
import com.google.gson.annotations.SerializedName;
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

    @SerializedName( "message_id")
    private int messageId;

    @SerializedName( "sub_type")
    private String subType;

    @SerializedName( "group_id")
    private long groupId;

    @SerializedName( "anonymous")
    private Anonymous anonymous;

    @SerializedName( "sender")
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


        @SerializedName( "user_id")
        private long userId;

        @SerializedName( "nickname")
        private String nickname;

        @SerializedName( "card")
        private String card;

        @SerializedName( "sex")
        private String sex;

        @SerializedName( "age")
        private int age;

        @SerializedName( "area")
        private String area;

        @SerializedName( "level")
        private String level;

        @SerializedName( "role")
        private String role;

        @SerializedName( "title")
        private String title;

    }

}
