package cn.evolvefield.mirai.onebot.dto.event.message;

import cn.evolvefield.mirai.onebot.dto.event.Event;
import cn.evolvefield.mirai.onebot.util.OnebotMsgParser;
import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.MessageEvent;

import java.io.Serializable;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 23:48
 * Version: 1.0
 */
public class MessageMap {

    public Event toDTO(MessageEvent botEvent, boolean isRawMessage) {
        final String[] rawMessage = {""};

        botEvent.getMessage().forEach(message -> {
            rawMessage[0] += OnebotMsgParser.toCQString(message);
        });
    }

    public static class MessageChainOrStringDTO implements Serializable{


    }
}
