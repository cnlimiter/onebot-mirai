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
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.PermissionDeniedException;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.MessageSource;

import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

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
                case "send_msg" -> {responseDTO=mirai.sendMessage(params);}
                case "send_private_msg" -> {responseDTO=mirai.sendPrivateMessage(params);}
                case "send_group_msg" -> {responseDTO=mirai.sendGroupMessage(params);}
                case "send_discuss_msg" -> {responseDTO=mirai.sendDiscussMessage(params);}
                case "delete_msg" -> {responseDTO=mirai.deleteMessage(params);}
                case "send_like" -> {responseDTO=mirai.sendLike(params);}
                case "set_group_kick" -> {responseDTO=mirai.setGroupKick(params);}
                case "set_group_ban" -> {responseDTO=mirai.setGroupBan(params);}
                case "set_group_anonymous_ban" -> {responseDTO=mirai.setGroupAnonymousBan(params);}
                case "set_group_whole_ban" -> {responseDTO=mirai.setWholeGroupBan(params);}
                case "set_group_admin" -> {responseDTO=mirai.setGroupAdmin(params);}
                case "set_group_anonymous" -> {responseDTO=mirai.setGroupAnonymous(params);}
                case "set_group_card" -> {responseDTO=mirai.setGroupCard(params);}
                case "set_group_leave" -> {responseDTO=mirai.setGroupLeave(params);}
                case "set_group_special_title" -> {responseDTO=mirai.setGroupSpecialTitle(params);}
                case "set_discuss_leave" -> {responseDTO=mirai.setDiscussLeave(params);}
                case "set_friend_add_request" -> {responseDTO=mirai.setFriendAddRequest(params);}
                case "set_group_add_request" -> {responseDTO=mirai.setGroupAddRequest(params);}
                case "get_login_info" -> {responseDTO=mirai.sendMessage(params);}
                case "get_stranger_info" -> {responseDTO=mirai.sendMessage(params);}
                case "get_friend_list" -> {responseDTO=mirai.sendMessage(params);}
                case "get_group_list" -> {responseDTO=mirai.sendMessage(params);}
                case "get_group_info" -> {responseDTO=mirai.sendMessage(params);}
                case "get_group_member_info" -> {responseDTO=mirai.sendMessage(params);}
                case "get_group_member_list" -> {responseDTO=mirai.sendMessage(params);}
                case "get_cookies" -> {responseDTO=mirai.sendMessage(params);}
                case "get_csrf_token" -> {responseDTO=mirai.sendMessage(params);}
                case "get_credentials" -> {responseDTO=mirai.sendMessage(params);}
                case "get_record" -> {responseDTO=mirai.sendMessage(params);}
                case "get_image" -> {responseDTO=mirai.sendMessage(params);}
                case "can_send_image" -> {responseDTO=mirai.sendMessage(params);}
                case "can_send_record" -> {responseDTO=mirai.sendMessage(params);}
                case "get_status" -> {responseDTO=mirai.sendMessage(params);}
                case "get_version_info" -> {responseDTO=mirai.sendMessage(params);}
                case "set_restart_plugin" -> {responseDTO=mirai.sendMessage(params);}
                case "clean_data_dir" -> {responseDTO=mirai.sendMessage(params);}
                case "clean_plugin_log" -> {responseDTO=mirai.sendMessage(params);}
                case ".handle_quick_operation" -> {responseDTO=mirai.sendMessage(params);}
                case "set_group_name" -> {responseDTO=mirai.sendMessage(params);}
                case "get_group_honor_info" -> {responseDTO=mirai.sendMessage(params);}
                case "get_msg" -> {responseDTO=mirai.sendMessage(params);}
                case "_set_group_announcement" -> {responseDTO=mirai.sendMessage(params);}
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

    public ActionData<?> setGroupAnonymousBan(JSONObject params){
        var groupId = params.getLong("group_id");
        String flag = "";
        var flag1 = params.getJSONObject("anonymous").getString("flag");
        var flag2 = params.getString("anonymous_flag");
        var flag3 = params.getString("flag");
        if (flag1.isEmpty()) flag = flag2.isEmpty() ? flag3 : flag2;
        var duration = params.getInteger("duration").describeConstable().orElse(30 * 60);
        var splits = flag.split("&",2);
        Mirai.getInstance().muteAnonymousMember(bot, splits[0], splits[1], groupId, duration);
        return new GeneralSuccess();
    }

    public ActionData<?> setWholeGroupBan(JSONObject params){
        var groupId = params.getLong("group_id");
        var enable = params.getBoolean("enable") != null ? params.getBoolean("enable") : true;

        bot.getGroupOrFail(groupId).getSettings().setMuteAll(enable);
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupAdmin(JSONObject params){
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        var enable = params.getBoolean("enable") != null ? params.getBoolean("enable") : true;

        bot.getGroupOrFail(groupId).getOrFail(memberId).modifyAdmin(enable);
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupCard(JSONObject params){
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        var card = params.getString("card").isEmpty() ? "" : params.getString("card");

        bot.getGroupOrFail(groupId).getOrFail(memberId).setNameCard(card);
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupLeave(JSONObject params){
        var groupId = params.getLong("group_id");
        var dismiss = params.getBoolean("is_dismiss") != null ? params.getBoolean("is_dismiss") : false;

        // Not supported
        if (dismiss) return new MiraiFailure();

        bot.getGroupOrFail(groupId).quit();
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupSpecialTitle(JSONObject params){
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        var specialTitle = params.getString("special_title").isEmpty() ? "" : params.getString("special_title");
        var duration = params.getInteger("duration").describeConstable().orElse(-1);

        bot.getGroupOrFail(groupId).getOrFail(memberId).setSpecialTitle(specialTitle);
        return new GeneralSuccess();
    }

    public ActionData<?> setFriendAddRequest(JSONObject params){
        var flag = params.getString("flag");
        var approve = params.getBoolean("approve") != null ? params.getBoolean("approve") : true;
        var remark = params.getString("remark");// unused

        var event = cacheRequestQueue.get(Long.parseLong(flag));
        if (event instanceof NewFriendRequestEvent requestEvent)
            if (approve) requestEvent.accept(); else requestEvent.reject(false);
        else return new InvalidRequest();

        return new GeneralSuccess();
    }

    public ActionData<?> setGroupAddRequest(JSONObject params){
        var flag = params.getString("flag");
        var type = params.getString("type"); // unused
        var subType = params.getString("sub_type"); // unused
        var approve = params.getBoolean("approve") != null ? params.getBoolean("approve") : true;
        var reason = params.getString("reason");
        var event = cacheRequestQueue.get(Long.parseLong(flag));
        if (event instanceof MemberJoinRequestEvent requestEvent)
            if (approve) requestEvent.accept(); else requestEvent.reject(true, reason);
        else if (event instanceof BotInvitedJoinGroupRequestEvent requestEvent) {
            if (approve) requestEvent.accept(); else requestEvent.ignore();
        }
        return new GeneralSuccess();
    }

    public ActionData<?> sendDiscussMessage(JSONObject params){
        return new MiraiFailure();
    }

    public ActionData<?> setGroupAnonymous(JSONObject params){
        var groupId = params.getLong("group_id");
        var enable = params.getBoolean("enable") != null ? params.getBoolean("enable") : true;

        // Not supported
        // bot.getGroupOrFail(groupId).settings.isAnonymousChatEnabled = enable
        return new MiraiFailure();
    }

    public ActionData<?> setDiscussLeave(JSONObject params){
        return new MiraiFailure();
    }

    public ActionData<?> sendLike(JSONObject params){
        return new MiraiFailure();
    }
}
