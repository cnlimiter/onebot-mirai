package cn.evolvefield.mirai.onebot.dto.event.message;

import cn.evolvefield.mirai.onebot.dto.event.Event;
import cn.evolvefield.mirai.onebot.dto.event.IgnoreEvent;
import cn.evolvefield.mirai.onebot.entity.Anonymous;
import cn.evolvefield.mirai.onebot.util.DataBaseUtils;
import cn.evolvefield.mirai.onebot.util.OnebotMsgParser;
import net.mamoe.mirai.contact.AnonymousMember;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;

import java.io.Serializable;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 23:48
 * Version: 1.0
 */
public class MessageMap {
    private static final long currentTimeSeconds = System.currentTimeMillis() / 1000;

    public static Event toDTO(MessageEvent botEvent) {
        final String[] rawMessage = {""};

        botEvent.getMessage().forEach(message -> {
            rawMessage[0] += OnebotMsgParser.toCQString(message);
        });


        if (botEvent instanceof GroupMessageEvent group){
            var event = new cn.evolvefield.mirai.onebot.dto.event.message.GroupMessageEvent();
            event.setSelfId(group.getBot().getId());
            if (group.getSender() instanceof AnonymousMember) event.setSubType("anonymous");
            else event.setSubType("normal");
            event.setMessageId(DataBaseUtils.toMessageId(group.getSource().getInternalIds(), group.getBot().getId(), group.getSource().getFromId()));
            event.setGroupId(group.getGroup().getId());
            event.setUserId(group.getSender().getId());
            if (group.getSender() instanceof AnonymousMember anonymous) {
                var anonymous1 = new Anonymous();
                anonymous1.setId(anonymous.getId());
                anonymous1.setFlag(anonymous.getAnonymousId() + "&" + anonymous.getNameCard());
                anonymous1.setName(anonymous.getNameCard());
                event.setAnonymous(anonymous1);;
            }
            else event.setAnonymous(null);
            event.setMessage(rawMessage[0]);
            event.setRawMessage(rawMessage[0]);
            event.setFont(0);
            event.setSender(new cn.evolvefield.mirai.onebot.dto.event.message.GroupMessageEvent.GroupSender(group.getSender()));
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof FriendMessageEvent friend){
            var event = new PrivateMessageEvent();
            event.setSelfId(friend.getBot().getId());
            event.setSubType("friend");
            event.setMessageId(DataBaseUtils.toMessageId(friend.getSource().getInternalIds(), friend.getBot().getId(), friend.getSource().getFromId()));
            event.setUserId(friend.getSender().getId());
            event.setMessage(rawMessage[0]);
            event.setRawMessage(rawMessage[0]);
            event.setFont(0);
            event.setPrivateSender(new PrivateMessageEvent.PrivateSender(friend.getSender()));
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof GroupTempMessageEvent temp){
            var event = new PrivateMessageEvent();
            event.setSelfId(temp.getBot().getId());
            event.setSubType("group");
            event.setMessageId(DataBaseUtils.toMessageId(temp.getSource().getInternalIds(), temp.getBot().getId(), temp.getSource().getFromId()));
            event.setUserId(temp.getSender().getId());
            event.setMessage(rawMessage[0]);
            event.setRawMessage(rawMessage[0]);
            event.setFont(0);
            event.setPrivateSender(new PrivateMessageEvent.PrivateSender(temp.getSender()));
            event.setTime(currentTimeSeconds);
            return event;
        }
//        else if (botEvent instanceof  guild){
//            var event = new GuildMessageEvent();
//        }
        else return new IgnoreEvent(botEvent.getSender().getId());

    }

    public static class MessageChainOrStringDTO implements Serializable{


    }
}
