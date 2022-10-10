package cn.evolvefield.mirai.onebot.core;

import cn.evolvefield.mirai.onebot.OneBotMirai;
import cn.evolvefield.mirai.onebot.dto.response.ActionData;
import cn.evolvefield.mirai.onebot.dto.response.MessageResponse;
import cn.evolvefield.mirai.onebot.dto.response.msic.GeneralSuccess;
import cn.evolvefield.mirai.onebot.dto.response.msic.InvalidRequest;
import cn.evolvefield.mirai.onebot.dto.response.msic.MiraiFailure;
import cn.evolvefield.mirai.onebot.dto.response.msic.PluginFailure;
import cn.evolvefield.mirai.onebot.util.DataBaseUtils;
import cn.evolvefield.mirai.onebot.util.OnebotMsgParser;
import cn.evolvefield.mirai.onebot.web.queue.CacheRequestQueue;
import cn.evolvefield.mirai.onebot.web.queue.CacheSourceQueue;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.PermissionDeniedException;
import net.mamoe.mirai.message.data.MessageSource;

import java.util.LinkedHashMap;
import java.util.NoSuchElementException;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 1:41
 * Version: 1.0
 */
public class MiraiApi {
    Bot bot;
    @Getter
    private LinkedHashMap<Long, Long> cachedTempContact = new LinkedHashMap<>();
    @Getter
    private CacheRequestQueue cacheRequestQueue = new CacheRequestQueue();
    @Getter
    private CacheSourceQueue cachedSourceQueue = new CacheSourceQueue();
    public MiraiApi(Bot bot){
        this.bot = bot;
    }

    public ActionData<?> callMiraiApi(String action, JSONObject params, MiraiApi mirai){
        ActionData<?> responseDTO = new PluginFailure();

        try {
            switch (action) {
                case "send_msg" -> {mirai.sendMessage(params);}
                case "send_private_msg" -> {mirai.sendPrivateMessage(params);}
                case "send_group_msg" -> {mirai.sendGroupMessage(params);}
                case "send_discuss_msg" -> {mirai.sendMessage(params);}
                case "delete_msg" -> {mirai.deleteMessage(params);}
                case "send_like" -> {mirai.sendMessage(params);}
                case "set_group_kick" -> {mirai.setGroupKick(params);}
                case "set_group_ban" -> {mirai.setGroupBan(params);}
                case "set_group_anonymous_ban" -> {mirai.sendMessage(params);}
                case "set_group_whole_ban" -> {mirai.sendMessage(params);}
                case "set_group_admin" -> {mirai.sendMessage(params);}
                case "set_group_anonymous" -> {mirai.sendMessage(params);}
                case "set_group_card" -> {mirai.sendMessage(params);}
                case "set_group_leave" -> {mirai.sendMessage(params);}
                case "set_group_special_title" -> {mirai.sendMessage(params);}
                case "set_discuss_leave" -> {mirai.sendMessage(params);}
                case "set_friend_add_request" -> {mirai.sendMessage(params);}
                case "set_group_add_request" -> {mirai.sendMessage(params);}
                case "get_login_info" -> {mirai.sendMessage(params);}
                case "get_stranger_info" -> {mirai.sendMessage(params);}
                case "get_friend_list" -> {mirai.sendMessage(params);}
                case "get_group_list" -> {mirai.sendMessage(params);}
                case "get_group_info" -> {mirai.sendMessage(params);}
                case "get_group_member_info" -> {mirai.sendMessage(params);}
                case "get_group_member_list" -> {mirai.sendMessage(params);}
                case "get_cookies" -> {mirai.sendMessage(params);}
                case "get_csrf_token" -> {mirai.sendMessage(params);}
                case "get_credentials" -> {mirai.sendMessage(params);}
                case "get_record" -> {mirai.sendMessage(params);}
                case "get_image" -> {mirai.sendMessage(params);}
                case "can_send_image" -> {mirai.sendMessage(params);}
                case "can_send_record" -> {mirai.sendMessage(params);}
                case "get_status" -> {mirai.sendMessage(params);}
                case "get_version_info" -> {mirai.sendMessage(params);}
                case "set_restart_plugin" -> {mirai.sendMessage(params);}
                case "clean_data_dir" -> {mirai.sendMessage(params);}
                case "clean_plugin_log" -> {mirai.sendMessage(params);}
                case ".handle_quick_operation" -> {mirai.sendMessage(params);}
                case "set_group_name" -> {mirai.sendMessage(params);}
                case "get_group_honor_info" -> {mirai.sendMessage(params);}
                case "get_msg" -> {mirai.sendMessage(params);}
                case "_set_group_announcement" -> {mirai.sendMessage(params);}
                default -> OneBotMirai.logger.error(String.format("未知OneBot API: %s", action));
            }
        }catch (IllegalArgumentException e) {
            OneBotMirai.logger.info(e);
            responseDTO = new InvalidRequest();
        } catch (PermissionDeniedException e) {
            OneBotMirai.logger.debug(String.format("机器人无操作权限, 调用的API: / %s", action));
            responseDTO = new MiraiFailure();
        } catch (Exception e) {
            OneBotMirai.logger.error(e);
            responseDTO = new PluginFailure();
        }
        return responseDTO;
    }


    public ActionData<?> sendMessage(JSONObject params){
        if (params.containsKey("message_type")) {
            switch (params.getString("message_type")){
                case "private" -> {
                    return sendPrivateMessage(params);
                }
                case "group" -> {
                    return sendGroupMessage(params);
                }
            }
        } else {
            if (params.get("group_id") != null)
                return sendGroupMessage(params);
            if (params.get("discuss_id") != null)
                return sendGroupMessage(params);
            if (params.get("user_id") != null)
                return sendPrivateMessage(params);

        }
        return new InvalidRequest();
    }

    public ActionData<?> sendGroupMessage(JSONObject params){
        var targetGroupId = params.getLong("group_id");
        var raw = params.getBoolean("auto_escape");
        var messages = params.get("message");

        var group = bot.getGroupOrFail(targetGroupId);
        var messageChain = OnebotMsgParser.messageToMiraiMessageChains(bot, group, messages, raw);
        if (messageChain != null && !messageChain.contentToString().isEmpty()) {
            var send = messageChain.contentToString();
            var receipt = group.sendMessage(send);
            cachedSourceQueue.add(receipt.getSource());
            return new MessageResponse(DataBaseUtils.toMessageId(receipt.getSource().getInternalIds(), bot.getId(), receipt.getSource().getFromId()));
        }
        else {
            return new MessageResponse(-1);
        }
    }

    public ActionData<?> sendPrivateMessage(JSONObject params){
        var targetQQId = params.getLong("user_id");
        var raw = params.getBoolean("auto_escape");
        var messages = params.get("message");
        Contact contact ;


        try {
            contact = bot.getFriendOrFail(targetQQId);
        } catch (NoSuchElementException e) {
            var fromGroupId = cachedTempContact.get(targetQQId);
                    //?: bot.groups.find { group -> group.members.contains(targetQQId) }?.id
            contact = bot.getGroupOrFail(fromGroupId).getOrFail(targetQQId);
        }
        var messageChain = OnebotMsgParser.messageToMiraiMessageChains(bot, contact, messages, raw);
        if (messageChain != null && !messageChain.contentToString().isEmpty()) {
            var send = messageChain.contentToString();
            var receipt = contact.sendMessage(send);
            cachedSourceQueue.add(receipt.getSource());
            return new MessageResponse(DataBaseUtils.toMessageId(receipt.getSource().getInternalIds(), bot.getId(), receipt.getSource().getFromId()));
        }
        else {
            return new MessageResponse(-1);
        }

    }

    public ActionData<?> deleteMessage(JSONObject params){
        var messageId = params.getInteger("message_id");
        MessageSource.recall(cachedSourceQueue.get(messageId));
        return new GeneralSuccess();
    }


    public ActionData<?> setGroupKick(JSONObject params){
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        bot.getGroupOrFail(groupId).getOrFail(memberId).kick("");
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupBan(JSONObject params){
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        var duration = params.getInteger("duration");
        if (duration == 0) {
            bot.getGroupOrFail(groupId).getOrFail(memberId).unmute();
        } else {
            bot.getGroupOrFail(groupId).getOrFail(memberId).mute(duration);
        }
        return new GeneralSuccess();
    }

}
