package cn.evole.onebot.mirai.model;


import cn.evole.onebot.sdk.response.group.GroupMemberInfoResp;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.NormalMember;

/**
 * Author cnlimiter
 * CreateTime 2023/5/25 2:38
 * Name MiraiGroupMemberInfoResp
 * Description
 */

public class MiraiGroupMemberInfoResp extends GroupMemberInfoResp {
    public MiraiGroupMemberInfoResp(NormalMember member) {
        this.setGroupId(member.getGroup().getId());
        this.setUserId(member.getId());
        this.setNickname(member.getNick());
        this.setCard(member.getNameCard());
        this.setSex(member.queryProfile().getSex().name().toLowerCase());
        this.setAge(member.queryProfile().getAge());
        this.setJoinTime(member.getJoinTimestamp());
        this.setLastSentTime(member.getLastSpeakTimestamp());
        this.setLevel(member.queryProfile().getQLevel());
        if (member.getPermission() == MemberPermission.ADMINISTRATOR) {
            this.setRole("admin");
        } else {
            this.setRole(member.getPermission().name().toLowerCase());
        }

        this.setUnfriendly(false);
        this.setTitle(member.getSpecialTitle());
        this.setTitleExpireTime(0L);
        this.setCardChangeable(member.getGroup().getBotPermission() == MemberPermission.ADMINISTRATOR || member.getGroup().getBotPermission() == MemberPermission.OWNER);
    }
}
