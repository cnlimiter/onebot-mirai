package cn.evole.onebot.mirai.model;

import cn.evole.onebot.sdk.event.message.PrivateMessageEvent;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.NormalMember;

/**
 * Author cnlimiter
 * CreateTime 2023/5/25 10:49
 * Name MiraiPrivateSender
 * Description
 */

public class MiraiPrivateSender extends PrivateMessageEvent.PrivateSender {
    public MiraiPrivateSender(Friend sender) {
        this.setUserId(sender.getId());
        this.setNickname(sender.getNick());
        this.setAge(sender.queryProfile().getAge());
        this.setSex(sender.queryProfile().getSex().name().toLowerCase());
    }

    public MiraiPrivateSender(NormalMember sender) {
        this.setUserId(sender.getId());
        this.setNickname(sender.getNick());
        this.setAge(sender.queryProfile().getAge());
        this.setSex(sender.queryProfile().getSex().name().toLowerCase());
    }
}
