package cn.evole.onebot.mirai.model;

import cn.evole.onebot.sdk.response.group.GroupHonorInfoResp;
import net.mamoe.mirai.contact.active.ActiveHonorInfo;

/**
 * Author cnlimiter
 * CreateTime 2023/5/25 2:57
 * Name MiraiCurrentTalkative
 * Description
 */

public class MiraiCurrentTalkative extends GroupHonorInfoResp.CurrentTalkative {

    public MiraiCurrentTalkative(ActiveHonorInfo active) {
        this.setUserId(active.getMemberId());
        this.setNickname(active.getMemberName());
        this.setAvatar(active.getAvatar());
        this.setDayCount(active.getTermDays());
    }
}
