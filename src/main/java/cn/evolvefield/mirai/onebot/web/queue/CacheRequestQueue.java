package cn.evolvefield.mirai.onebot.web.queue;

import net.mamoe.mirai.event.events.BotEvent;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;

import java.util.LinkedHashMap;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/2 22:21
 * Version: 1.0
 */
public class CacheRequestQueue extends LinkedHashMap<Long, BotEvent>{
    int cacheSize = 512;

    @Override
    public BotEvent get(Object key) {
         return super.get(key);
    }

    @Override
    public BotEvent put(Long key, BotEvent value) {
        if (size() > cacheSize) remove(this.entrySet().stream().findFirst().get().getKey());
        return super.put(key, value);
    }

    public void add(NewFriendRequestEvent event){
        put(event.getEventId(), event);
    }
    public void add(MemberJoinRequestEvent event){
        put(event.getEventId(), event);
    }
    public void add(BotInvitedJoinGroupRequestEvent event){
        put(event.getEventId(), event);
    }
}
