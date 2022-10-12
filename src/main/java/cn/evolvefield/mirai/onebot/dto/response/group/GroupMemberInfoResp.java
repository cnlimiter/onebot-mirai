package cn.evolvefield.mirai.onebot.dto.response.group;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberInfoResp {

    @JSONField(name = "group_id")
    private long groupId;

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

    @JSONField(name = "join_time")
    private int joinTime;

    @JSONField(name = "last_sent_time")
    private int lastSentTime;

    @JSONField(name = "level")
    private int level;

    @JSONField(name = "role")
    private String role;

    @JSONField(name = "unfriendly")
    private boolean unfriendly;

    @JSONField(name = "title")
    private String title;

    @JSONField(name = "title_expire_time")
    private long titleExpireTime;

    @JSONField(name = "card_changeable")
    private boolean cardChangeable;

    public GroupMemberInfoResp(NormalMember member){
        this.setGroupId(member.getGroup().getId());
        this.setUserId(member.getId());
        this.setNickname(member.getNick());
        this.setCard(member.getNameCard());
        this.setSex(member.queryProfile().getSex().name().toLowerCase());
        this.setAge(member.queryProfile().getAge());
        this.setJoinTime(member.getJoinTimestamp());
        this.setLastSentTime(member.getLastSpeakTimestamp());
        this.setLevel(member.queryProfile().getQLevel());
        if (member.getPermission() == MemberPermission.ADMINISTRATOR) this.setRole("admin"); else this.setRole(member.getPermission().name().toLowerCase());
        this.setUnfriendly(false);
        this.setTitle(member.getSpecialTitle());
        this.setTitleExpireTime(0);//没有接口
        this.setCardChangeable(member.getGroup().getBotPermission() == MemberPermission.ADMINISTRATOR || member.getGroup().getBotPermission() == MemberPermission.OWNER);

    }

}
