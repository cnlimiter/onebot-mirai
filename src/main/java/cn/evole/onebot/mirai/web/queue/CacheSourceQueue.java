package cn.evole.onebot.mirai.web.queue;

import cn.evole.onebot.sdk.util.DataBaseUtils;
import net.mamoe.mirai.message.data.MessageSource;

import java.util.LinkedHashMap;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/2 22:29
 * Version: 1.0
 */
public class CacheSourceQueue extends LinkedHashMap<Integer, MessageSource> {
    int cacheSize = 1024;

    @Override
    public MessageSource get(Object key) {
        return super.get(key);
    }

    @Override
    public MessageSource put(Integer key, MessageSource value) {
        if (size() > cacheSize) remove(this.entrySet().stream().findFirst().get().getKey());
        return super.put(key, value);
    }

    public void add(MessageSource source){
        put(DataBaseUtils.toMessageId(source.getInternalIds(), source.getBotId(), source.getFromId()), source);
    }
}
