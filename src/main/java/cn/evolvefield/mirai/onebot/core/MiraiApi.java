package cn.evolvefield.mirai.onebot.core;

import cn.evolvefield.mirai.onebot.web.queue.CacheRequestQueue;
import cn.evolvefield.mirai.onebot.web.queue.CacheSourceQueue;
import lombok.Getter;
import net.mamoe.mirai.Bot;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 1:41
 * Version: 1.0
 */
public class MiraiApi {
    Bot bot;
    Map<Long, Long>  cachedTempContact = new HashMap<>();
   @Getter
   public CacheRequestQueue cacheRequestQueue = new CacheRequestQueue();
   @Getter
   public CacheSourceQueue cachedSourceQueue = new CacheSourceQueue();
    public MiraiApi(Bot bot){
        this.bot = bot;
    }

//    public ActionData<?> sendMessage(ApiParams params){
//        if (params.containsKey("message_type")) {
//            switch (params.get("message_type").getAsString()){
//                case "private" -> {
//                    return sendPrivateMessage(params);
//                }
//                case "group" -> {
//                    return sendGroupMessage(params);
//                }
//            }
//        } else {
//            if (params.get("group_id") != null)
//                return sendGroupMessage(params);
//            if (params.get("discuss_id") != null)
//                return sendGroupMessage(params);
//            if (params.get("user_id") != null)
//                return sendPrivateMessage(params);
//
//        }
//        return new InvalidRequest();
//    }

//    public ActionData<?> sendGroupMessage(ApiParams params){
//        var targetGroupId = params.get("group_id").getAsLong();
//        var raw = params.booleanOrNull("auto_escape");
//        var messages = params.get("message");
//
//        var group = bot.getGroupOrFail(targetGroupId);
//        messageToMiraiMessageChains(bot, group, messages, raw)?.let {
//            return if (it.content.isNotEmpty()) {
//                val receipt = group.sendMessage(it)
//                cachedSourceQueue.add(receipt.source)
//                ResponseDTO.MessageResponse(receipt.source.internalIds.toMessageId(bot.id, receipt.source.fromId))
//            } else {
//                ResponseDTO.MessageResponse(-1)
//            }
//        }
//        return new InvalidRequest();
//    }
//
//    public ActionData<?> sendPrivateMessage(ApiParams params){
//        val targetQQId = params["user_id"].long
//        val raw = params["auto_escape"].booleanOrNull ?: false
//        val messages = params["message"]
//
//        val contact = try {
//            bot.getFriendOrFail(targetQQId)
//        } catch (e: NoSuchElementException) {
//            val fromGroupId = cachedTempContact[targetQQId]
//                    ?: bot.groups.find { group -> group.members.contains(targetQQId) }?.id
//            bot.getGroupOrFail(fromGroupId!!).getMemberOrFail(targetQQId)
//        }
//
//        messageToMiraiMessageChains(bot, contact, messages, raw)?.let {
//            return if (it.content.isNotEmpty()) {
//                val receipt = contact.sendMessage(it)
//                cachedSourceQueue.add(receipt.source)
//                ResponseDTO.MessageResponse(receipt.source.internalIds.toMessageId(bot.id, receipt.source.fromId))
//            } else {
//                ResponseDTO.MessageResponse(-1)
//            }
//        }
//        return ResponseDTO.InvalidRequest()
//    }

}
