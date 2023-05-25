package cn.evole.onebot.mirai.model;

import cn.evole.onebot.sdk.event.message.GroupMessageEvent;
import net.mamoe.mirai.contact.Member;

/**
 * Author cnlimiter
 * CreateTime 2023/5/25 10:50
 * Name PrivateGroupSender
 * Description
 */

public class MiraiGroupSender extends GroupMessageEvent.GroupSender {
    public MiraiGroupSender(Member sender) {
        this.setUserId(String.valueOf(sender.getId()));
        this.setNickname(sender.getNick());
        this.setCard(sender.getNameCard());
        this.setAge(sender.queryProfile().getAge());
        this.setArea("");
        this.setSex(sender.queryProfile().getSex().name().toLowerCase());
        this.setRole(sender.getPermission().name().toLowerCase());
        this.setLevel(String.valueOf(sender.queryProfile().getQLevel()));
        this.setTitle(sender.getSpecialTitle());
    }
}
