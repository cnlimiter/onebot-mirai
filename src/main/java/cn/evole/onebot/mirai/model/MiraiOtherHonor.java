package cn.evole.onebot.mirai.model;

import cn.evole.onebot.sdk.response.group.GroupHonorInfoResp;
import net.mamoe.mirai.contact.active.ActiveHonorInfo;

/**
 * Author cnlimiter
 * CreateTime 2023/5/25 2:53
 * Name MiraiOtherHonor
 * Description
 */

public class MiraiOtherHonor extends GroupHonorInfoResp.OtherHonor {
    public MiraiOtherHonor(ActiveHonorInfo actor) {
        this.setUserId(actor.getMemberId());
        this.setNickname(actor.getMemberName());
        this.setAvatar(actor.getAvatar());
        this.setDescription("");
    }

}
