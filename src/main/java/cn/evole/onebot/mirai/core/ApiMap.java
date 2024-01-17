package cn.evole.onebot.mirai.core;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.model.MiraiGroupMemberInfoResp;
import cn.evole.onebot.mirai.util.*;
import cn.evole.onebot.sdk.action.ActionData;
import cn.evole.onebot.sdk.response.group.GetMsgResp;
import cn.evole.onebot.sdk.response.misc.BooleanResp;
import cn.evole.onebot.sdk.response.misc.ImgInfoResp;
import cn.evole.onebot.sdk.response.misc.MiraiResp.*;
import cn.evole.onebot.sdk.response.contact.FriendInfoResp;
import cn.evole.onebot.sdk.response.contact.LoginInfoResp;
import cn.evole.onebot.sdk.response.contact.StrangerInfoResp;
import cn.evole.onebot.sdk.response.group.GroupDataResp;
import cn.evole.onebot.sdk.response.group.GroupInfoResp;
import cn.evole.onebot.sdk.response.group.GroupMemberInfoResp;
import cn.evole.onebot.mirai.web.queue.CacheRequestQueue;
import cn.evole.onebot.mirai.web.queue.CacheSourceQueue;
import cn.evole.onebot.sdk.response.misc.RecordInfoResp;
import cn.evole.onebot.sdk.util.DataBaseUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.val;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.LowLevelApi;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.contact.PermissionDeniedException;
import net.mamoe.mirai.contact.announcement.OfflineAnnouncement;
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent;
import net.mamoe.mirai.event.events.MemberJoinRequestEvent;
import net.mamoe.mirai.event.events.NewFriendRequestEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.MessageSourceKind;
import net.mamoe.mirai.utils.MiraiExperimentalApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

import static cn.evole.onebot.mirai.util.ImgUtils.*;
import static cn.evole.onebot.mirai.util.VoiceUtils.getCachedRecordFile;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 1:41
 * Version: 1.0
 */
public class ApiMap {
    Bot bot;
    @Getter
    private final LinkedHashMap<Long, Long> cachedTempContact = new LinkedHashMap<>();
    @Getter
    private final CacheRequestQueue cacheRequestQueue = new CacheRequestQueue();
    @Getter
    private final CacheSourceQueue cachedSourceQueue = new CacheSourceQueue();

    public ApiMap(Bot bot) {
        this.bot = bot;
    }

    public ActionData<?> callMiraiApi(String action, JsonObject params) {
        ActionData<?> responseDTO = new PluginFailure();

        try {
            switch (action) {
                case "send_msg" -> {
                    responseDTO = sendMessage(params);
                }
                case "send_private_msg" -> {
                    responseDTO = sendPrivateMessage(params);
                }
                case "send_group_msg" -> {
                    responseDTO =sendGroupMessage(params);
                }
                case "send_discuss_msg" -> {
                    responseDTO =sendDiscussMessage(params);
                }
                case "delete_msg" -> {
                    responseDTO =deleteMessage(params);
                }
                case "send_like" -> {
                    responseDTO =sendLike(params);
                }
                case "get_msg" -> {
                    responseDTO =getMessage(params);
                }


                case "set_group_kick" -> {
                    responseDTO =setGroupKick(params);
                }
                case "set_group_ban" -> {
                    responseDTO =setGroupBan(params);
                }
                case "set_group_anonymous_ban" -> {
                    responseDTO =setGroupAnonymousBan(params);
                }
                case "set_group_whole_ban" -> {
                    responseDTO =setWholeGroupBan(params);
                }
                case "set_group_admin" -> {
                    responseDTO =setGroupAdmin(params);
                }
                case "set_group_anonymous" -> {
                    responseDTO =setGroupAnonymous(params);
                }
                case "set_group_card" -> {
                    responseDTO =setGroupCard(params);
                }
                case "set_group_leave" -> {
                    responseDTO =setGroupLeave(params);
                }
                case "set_group_special_title" -> {
                    responseDTO =setGroupSpecialTitle(params);
                }
                case "set_group_portrait" -> {
                    responseDTO =setGroupPortrait(params);
                }

                case "set_discuss_leave" -> {
                    responseDTO =setDiscussLeave(params);
                }
                case "set_friend_add_request" -> {
                    responseDTO =setFriendAddRequest(params);
                }
                case "set_group_add_request" -> {
                    responseDTO =setGroupAddRequest(params);
                }
                case "get_login_info" -> {
                    responseDTO =getLoginInfo(params);
                }
                case "set_qq_profile" -> {
                    responseDTO =sendQQProfile(params);
                }
                case "get_stranger_info" -> {
                    responseDTO =getStrangerInfo(params);
                }
                case "get_friend_list" -> {
                    responseDTO =getFriendList(params);
                }
                case "get_group_list" -> {
                    responseDTO =getGroupList(params);
                }
                case "get_group_info" -> {
                    responseDTO =getGroupInfo(params);
                }
                case "get_group_member_info" -> {
                    responseDTO =getGroupMemberInfo(params);
                }
                case "get_group_member_list" -> {
                    responseDTO =getGroupMemberList(params);
                }
                case "get_cookies" -> {
                    responseDTO =getCookies(params);
                }
                case "get_csrf_token" -> {
                    responseDTO =getCSRFToken(params);
                }
                case "get_credentials" -> {
                    responseDTO =getCredentials(params);
                }
                case "get_record" -> {
                    responseDTO =getRecord(params);
                }
                case "get_image" -> {
                    responseDTO =getImage(params);
                }
                case "can_send_image" -> {
                    responseDTO =canSendImage(params);
                }
                case "can_send_record" -> {
                    responseDTO =canSendRecord(params);
                }
                case "get_status" -> {
                    responseDTO =getStatus(params);
                }
                case "get_version_info" -> {
                    responseDTO =getVersionInfo(params);
                }
                case "set_restart_plugin" -> {
                    responseDTO =setRestartPlugin(params);
                }
                case "clean_data_dir" -> {
                    responseDTO =cleanDataDir(params);
                }
                case "clean_plugin_log" -> {
                    responseDTO =cleanPluginLog(params);
                }
                case "set_group_name" -> {
                    responseDTO =setGroupName(params);
                }
                case "get_group_honor_info" -> {
                    responseDTO =sendMessage(params);
                }
                case "_set_group_notice" -> {
                    responseDTO =setGroupNotice(params);
                }
                case ".get_word_slices" -> {
                    responseDTO =getWordSlice(params);
                }
                case "set_essence_msg" -> {
                    responseDTO =setEssenceMsg(params);
                }
                case "get_group_root_files" -> {
                    responseDTO =setEssenceMsg(params);
                }
                default -> OneBotMirai.logger.error(String.format("未知OneBot API: %s", action));
            }
        } catch (IllegalArgumentException e) {
            OneBotMirai.logger.info(e);
            responseDTO = new InvalidFailure();
        } catch (PermissionDeniedException e) {
            OneBotMirai.logger.info(String.format("机器人无操作权限, 调用的API: / %s", action));
            responseDTO = new MiraiFailure();
        } catch (Exception e) {
            OneBotMirai.logger.error(e);
            responseDTO = new PluginFailure();
        }
        return responseDTO;
    }
    ////////////////
    ////  v11  ////
    //////////////

    public ActionData<?> sendMessage(JsonObject params) {
        if (params.has("message_type")) {
            switch (params.get("message_type").getAsString()) {
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
        return new InvalidFailure();
    }

    public ActionData<?> getMessage(JsonObject params) {
        val messageId = params.get("message_id").getAsInt();
        if (PluginConfig.INSTANCE.getDb().getEnable()){
            if (OneBotMirai.INSTANCE.db != null){
                val message = MessageChain.deserializeFromJsonString(new String(DataBaseUtils.toByteArray(messageId)));
                StringBuilder rawMessage = new StringBuilder();
                for (var chain : message){
                    rawMessage.append(OnebotMsgUtils.toCQString(chain));
                }
                MessageSource source = message.get(MessageSource.Key);
                if (source!=null ){
                    val data = new ActionData<GetMsgResp>();
                    data.setStatus("ok");
                    data.setRetCode(0);
                    val resp = new GetMsgResp();
                    resp.setTime(source.getTime());
                    resp.setMessageId(messageId);
                    resp.setRealId(messageId);
                    resp.setRawMessage(rawMessage.toString());
                    GetMsgResp.Sender sender = new GetMsgResp.Sender();
                    sender.setUserId(String.valueOf(source.getFromId()));
                    sender.setNickname("UNKNOWN");
                    resp.setSender(sender);
                    if (source.getKind() == MessageSourceKind.GROUP){
                        resp.setMsgType("group");
                        data.setData(resp);
                        return data;
                    }
                    else if (source.getKind() == MessageSourceKind.FRIEND || source.getKind() == MessageSourceKind.STRANGER || source.getKind() == MessageSourceKind.TEMP){
                        resp.setMsgType("private");
                        data.setData(resp);
                        return data;
                    }
                    else  {
                        var failure = new PluginFailure();
                        failure.setData("未知消息类型");
                        return failure;
                    }
                }
                else {
                    var failure = new PluginFailure();
                    failure.setData("数据库未正常初始化");
                    return failure;
                }

            }
            else {
                var failure = new PluginFailure();
                failure.setData("消息为空");
                return failure;
            }
        }
        else {
            var failure = new InvalidFailure();
            failure.setData("请配置开启数据库");
            return failure;
        }
    }

    public ActionData<?> sendGroupMessage(JsonObject params) {
        val targetGroupId = params.get("group_id").getAsLong();
        val raw = params.has("auto_escape") && params.get("auto_escape").getAsBoolean();
        val messages = params.get("message").getAsJsonArray();

        MessageSuccess r = new MessageSuccess(-1);;

        for (JsonElement message : messages.asList()) {
            val group = bot.getGroupOrFail(targetGroupId);
            val messageChain = OnebotMsgUtils.messageToMiraiMessageChains(bot, group, message, raw);
            if (messageChain != null && !messageChain.contentToString().isEmpty()) {
                val receipt = group.sendMessage(messageChain);
                cachedSourceQueue.add(receipt.getSource());
                r = new MessageSuccess(DataBaseUtils.toMessageId(receipt.getSource().getInternalIds(), bot.getId(), receipt.getSource().getFromId()));
            }
        }
        return r;
    }

    public ActionData<?> sendPrivateMessage(JsonObject params) {
        val targetQQId = params.get("user_id").getAsLong();
        val raw = params.has("auto_escape") || params.get("auto_escape").getAsBoolean();
        val messages = params.get("message");
        Contact contact;


        try {
            contact = bot.getFriendOrFail(targetQQId);
        } catch (NoSuchElementException e) {
            val fromGroupId = cachedTempContact.get(targetQQId);
            //?: bot.groups.find { group -> group.members.contains(targetQQId) }?.id
            contact = bot.getGroupOrFail(fromGroupId).getOrFail(targetQQId);
        }
        val messageChain = OnebotMsgUtils.messageToMiraiMessageChains(bot, contact, messages, raw);
        if (messageChain != null && !messageChain.contentToString().isEmpty()) {
            val receipt = contact.sendMessage(messageChain);
            cachedSourceQueue.add(receipt.getSource());
            return new MessageSuccess(DataBaseUtils.toMessageId(receipt.getSource().getInternalIds(), bot.getId(), receipt.getSource().getFromId()));
        } else {
            return new MessageSuccess(-1);
        }

    }

    //delete
    public ActionData<?> deleteMessage(JsonObject params) {
        val messageId = params.get("message_id").getAsInt();
        MessageSource.recall(cachedSourceQueue.get(messageId));
        return new Success();
    }


    public ActionData<?> setGroupKick(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val memberId = params.get("user_id").getAsLong();
        val rejectAddRequest = params.has("reject_add_request") && params.get("reject_add_request").getAsBoolean();
        bot.getGroupOrFail(groupId).getOrFail(memberId).kick("", rejectAddRequest);
        return new Success();
    }

    public ActionData<?> sendLike(JsonObject params) {
        return new MiraiFailure();
    }


    //set
    public ActionData<?> setGroupBan(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val memberId = params.get("user_id").getAsLong();
        val duration = params.get("duration").getAsInt();
        if (duration == 0) {
            bot.getGroupOrFail(groupId).getOrFail(memberId).unmute();
        } else {
            bot.getGroupOrFail(groupId).getOrFail(memberId).mute(duration);
        }
        return new Success();
    }

    public ActionData<?> setGroupAnonymousBan(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        String flag = "";
        val flag1 = params.get("anonymous").getAsJsonObject().get("flag").getAsString();
        val flag2 = params.get("anonymous_flag").getAsString();
        val flag3 = params.get("flag").getAsString();
        if (flag1.isEmpty()) flag = flag2.isEmpty() ? flag3 : flag2;
        val duration = params.has("duration") ? params.get("duration").getAsInt() :30 * 60;

        val splits = flag.split("&", 2);
        Mirai.getInstance().muteAnonymousMember(bot, splits[0], splits[1], groupId, duration);
        return new Success();
    }

    public ActionData<?> setWholeGroupBan(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val enable = params.has("enable") && params.get("enable").getAsBoolean();

        bot.getGroupOrFail(groupId).getSettings().setMuteAll(enable);
        return new Success();
    }

    public ActionData<?> setGroupAdmin(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val memberId = params.get("user_id").getAsLong();
        val enable = params.has("enable") && params.get("enable").getAsBoolean();

        bot.getGroupOrFail(groupId).getOrFail(memberId).modifyAdmin(enable);
        return new Success();
    }

    public ActionData<?> setGroupCard(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val memberId = params.get("user_id").getAsLong();
        val card = params.get("card").getAsString().isEmpty() ? "" : params.get("card").getAsString();

        bot.getGroupOrFail(groupId).getOrFail(memberId).setNameCard(card);
        return new Success();
    }

    public ActionData<?> setGroupLeave(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val dismiss = params.has("is_dismiss") && params.get("is_dismiss").getAsBoolean();

        // Not supported
        if (dismiss) return new MiraiFailure();

        bot.getGroupOrFail(groupId).quit();
        return new Success();
    }

    public ActionData<?> setGroupSpecialTitle(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val memberId = params.get("user_id").getAsLong();
        val specialTitle = params.get("special_title").getAsString().isEmpty() ? "" : params.get("special_title").getAsString();
        val duration = params.has("duration") ? params.get("duration"):-1;

        bot.getGroupOrFail(groupId).getOrFail(memberId).setSpecialTitle(specialTitle);
        return new Success();
    }

    public ActionData<?> setGroupPortrait(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val file = params.get("file").getAsString();
        val cache = params.has("cache") ? params.get("cache").getAsInt() : 1;

        //bot.getGroupOrFail(groupId).avatarUrl();
        return new MiraiFailure();
    }

    public ActionData<?> setFriendAddRequest(JsonObject params) {
        val flag = params.get("flag").getAsString();
        val approve = params.has("approve") && params.get("approve").getAsBoolean();
        val remark = params.get("remark");// unuse.getAsString()d

        val event = cacheRequestQueue.get(Long.parseLong(flag));
        if (event instanceof NewFriendRequestEvent requestEvent)
            if (approve) requestEvent.accept();
            else requestEvent.reject(false);
        else return new InvalidFailure();

        return new Success();
    }

    public ActionData<?> setGroupAddRequest(JsonObject params) {
        val flag = params.get("flag").getAsString();
        val type = params.get("type"); // unuse.getAsString()d
        val subType = params.get("sub_type"); // unuse.getAsString()d
        val approve = params.has("approve") && params.get("approve").getAsBoolean();
        val reason = params.get("reason").getAsString();
        val event = cacheRequestQueue.get(Long.parseLong(flag));
        if (event instanceof MemberJoinRequestEvent requestEvent)
            if (approve) requestEvent.accept();
            else requestEvent.reject(true, reason);
        else if (event instanceof BotInvitedJoinGroupRequestEvent requestEvent) {
            if (approve) requestEvent.accept();
            else requestEvent.ignore();
        }
        return new Success();
    }

    public ActionData<?> sendDiscussMessage(JsonObject params) {
        return new MiraiFailure();
    }

    public ActionData<?> setGroupAnonymous(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val enable = params.has("enable") && params.get("enable").getAsBoolean();
        bot.getGroupOrFail(groupId).getSettings().setAnonymousChatEnabled(enable);
        return new MiraiFailure();
    }

    public ActionData<?> setDiscussLeave(JsonObject params) {
        return new MiraiFailure();
    }


    //get
    public ActionData<?> getLoginInfo(JsonObject params) {
        LoginInfoResp loginInfo = new LoginInfoResp(bot.getId(), bot.getNick());
        val data = new ActionData<LoginInfoResp>();
        data.setData(loginInfo);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> sendQQProfile(JsonObject params) {
        val nick = params.get("nickname").getAsString();
        val company = params.get("company").getAsString();
        val email = params.get("email").getAsString();
        val college = params.get("college").getAsString();
        val personalNote = params.get("personal_note").getAsString();

        return new MiraiFailure();
    }


    public ActionData<?> getStrangerInfo(JsonObject params) {
        val userId = params.get("user_id").getAsLong();

        val profile = Mirai.getInstance().queryProfile(bot, userId);
        StrangerInfoResp loginInfo = new StrangerInfoResp();
        loginInfo.setUserId(userId);
        loginInfo.setNickname(profile.getNickname());
        loginInfo.setSex(profile.getSex().name().toLowerCase());
        loginInfo.setAge(profile.getAge());
        loginInfo.setLevel(profile.getQLevel());
        val data = new ActionData<StrangerInfoResp>();
        data.setData(loginInfo);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> getFriendList(JsonObject params) {
        val friendList = new LinkedList<FriendInfoResp>();
        bot.getFriends().forEach(friend -> {
            friendList.add(new FriendInfoResp(friend.getId(), friend.getNick(), friend.getRemark()));
        });
        val data = new ActionData<>();
        data.setData(friendList);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> getGroupList(JsonObject params) {
        val groupList = new LinkedList<GroupDataResp>();
        bot.getGroups().forEach(group ->
                groupList.add(new GroupDataResp(group.getId(), group.getName())));
        {
        }
        val data = new ActionData<>();
        data.setData(groupList);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    /**
     * 获取群信息
     * 不支持获取群容量, 返回0
     */
    public ActionData<?> getGroupInfo(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val noCache = params.has("no_cache") && params.get("no_cache").getAsBoolean();

        val group = bot.getGroupOrFail(groupId);
        val groupInfo = new GroupInfoResp();
        groupInfo.setGroupId(group.getId());
        groupInfo.setGroupName(group.getName());
        groupInfo.setMemberCount(group.getMembers().size() + 1);//加上机器人本身
        groupInfo.setMaxMemberCount(0);
        groupInfo.setGroupCreateTime(group.getOwner().getJoinTimestamp());//mirai没有直接接口，采用群主加入群的时间，一般情况下均可以实现，除了转让群
        val data = new ActionData<GroupInfoResp>();
        data.setData(groupInfo);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    @MiraiExperimentalApi
    @LowLevelApi
    public ActionData<?> getGroupMemberInfo(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val memberId = params.get("user_id").getAsLong();
        val noCache = params.has("no_cache") && params.get("no_cache").getAsBoolean();

        val group = bot.getGroupOrFail(groupId);
        val data = new ActionData<GroupMemberInfoResp>();
        if (noCache) {
            val groupUin = Mirai.getInstance().getUin(group);
            val members = Mirai.getInstance().getRawGroupMemberList(bot, groupUin, groupId, group.getOwner().getId());

            val groupMemberInfo = new GroupMemberInfoResp();
            val member = BaseUtils.copyIterator(members.iterator())
                    .stream()
                    .filter(memberInfo -> memberInfo.getUin() == memberId)
                    .findFirst();
            if (member.isPresent()) {
                member.ifPresent(memberInfo -> {
                    groupMemberInfo.setGroupId(group.getId());
                    groupMemberInfo.setUserId(memberInfo.getUin());
                    groupMemberInfo.setNickname(memberInfo.getNick());
                    groupMemberInfo.setCard(memberInfo.getNameCard());
                    groupMemberInfo.setJoinTime(member.get().getJoinTimestamp());
                    groupMemberInfo.setLastSentTime(member.get().getLastSpeakTimestamp());
                    if (member.get().getPermission() == MemberPermission.ADMINISTRATOR)
                        groupMemberInfo.setRole("admin");
                    else groupMemberInfo.setRole(member.get().getPermission().name().toLowerCase());
                    groupMemberInfo.setTitle(memberInfo.getSpecialTitle());
                    groupMemberInfo.setCardChangeable(group.getBotPermission() == MemberPermission.OWNER);

                });
                data.setData(groupMemberInfo);
                data.setStatus("ok");
                data.setRetCode(0);
                return data;
            } else {
                return new MiraiFailure();
            }

        } else {
            val member = bot.getGroupOrFail(groupId).getOrFail(memberId);

            data.setData(new MiraiGroupMemberInfoResp(member));
            data.setStatus("ok");
            data.setRetCode(0);
            return data;
        }
    }

    public ActionData<?> getGroupMemberList(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val groupMemberListData = new LinkedList<GroupMemberInfoResp>();
        val data = new ActionData<>();

        AtomicBoolean isBotIncluded = new AtomicBoolean(false);
        val group = bot.getGroupOrFail(groupId);
        val members = group.getMembers();
        members.forEach(member -> {
            if (member.getId() == bot.getId()) isBotIncluded.set(true);
            groupMemberListData.add(new MiraiGroupMemberInfoResp(member));
        });
        if (!isBotIncluded.get()) groupMemberListData.add(new MiraiGroupMemberInfoResp(group.getBotAsMember()));
        data.setData(groupMemberListData);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }


    public ActionData<?> getRecord(JsonObject params){
        val file = params.get("file").getAsString();
        val outFormat = params.get("out_format").getAsString().isEmpty() ? "" : params.get("out_format");// Currently not support
        val cachedFile = getCachedRecordFile(file);
        if (cachedFile!=null){
            byte[] b1 = new byte[10];
            try {
                System.arraycopy(Files.readAllBytes(cachedFile.toPath()), 0, b1, 0, 10);
                String fileType = BaseUtils.bytesToString(b1);
                if (fileType.startsWith("2321414D52")) fileType = "amr";
                else if (fileType.startsWith("02232153494C4B5F5633")) fileType = "silk";
                else fileType = "unknown";
                val data = new ActionData<RecordInfoResp>();
                data.setStatus("ok");
                data.setRetCode(0);
                data.setData(
                        new RecordInfoResp(
                                cachedFile.getAbsolutePath(),
                                StringUtils.substringBeforeLast(cachedFile.getName(), "."),
                                StringUtils.substringBeforeLast(cachedFile.getName(), "."),
                                fileType
                ));
                return data;
            }catch (Exception ignored){}
        }
        return new PluginFailure();
    }

    public ActionData<?> getImage(JsonObject params){
        val file = params.get("file").getAsString();

        val image = getCachedImageFile(file);
        if (image!=null){
            File cachedFile = BaseUtils.getDataFile("image", image.fileName());
            if (cachedFile == null && HttpUtils.getBytesFromHttpUrl(image.url()) != null) {
                OneBotMirai.INSTANCE.saveImageOrRecord(image.fileName(), HttpUtils.getBytesFromHttpUrl(image.url()), true);
            }
            cachedFile = BaseUtils.getDataFile("image", image.fileName());
            if (cachedFile!= null){
                try {
                    val fileType = getImageType(Files.readAllBytes(cachedFile.toPath()));
                    val data = new ActionData<ImgInfoResp>();
                    data.setStatus("ok");
                    data.setRetCode(0);
                    data.setData(new ImgInfoResp(
                            cachedFile.getAbsolutePath(),
                            image.fileName(),
                            image.md5(),
                            image.size(),
                            image.url(),
                            image.addTime(),
                            fileType
                    ));
                    return data;
                }
                catch (IOException ignored){}
            }
            else {
                return new PluginFailure();
            }
        }
        return new PluginFailure();
    }

    public ActionData<?> canSendImage(JsonObject params) {
        val data = new ActionData<BooleanResp>();
        data.setData(new BooleanResp(true));
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> canSendRecord(JsonObject params) {
        val data = new ActionData<BooleanResp>();
        data.setData(new BooleanResp(true));
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> getStatus(JsonObject params) {
        val data = new ActionData<PluginStatus>();
        data.setStatus("ok");
        data.setRetCode(0);
        val status = new PluginStatus();
        status.setOnline(bot.isOnline());
        data.setData(status);
        return data;
    }


    public ActionData<?> getVersionInfo(JsonObject params) {
        val data = new ActionData<VersionInfo>();
        data.setStatus("ok");
        data.setRetCode(0);
        val versionInfo = new VersionInfo();
        versionInfo.setCoolq_directory(OneBotMirai.INSTANCE.getDataFolder().getAbsolutePath());
        data.setData(versionInfo);
        return data;
    }

    public ActionData<?> setGroupName(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val name = params.get("group_name").getAsString();
        if (!"".equals(name)) {
            bot.getGroupOrFail(groupId).setName(name);
            return new Success();
        } else {
            return new InvalidFailure();
        }
    }

//    @MiraiExperimentalApi
//    @LowLevelApi
//    public ActionData<?> getGroupHonorInfo(JsonObject params) {
//        val groupId = params.get("group_id").getAsLong();
//        val type = params.get("type").getAsString();
//
//        GroupHonorInfoResp finalData = new GroupHonorInfoResp(bot, groupId, type);
//
//        val data = new ActionData<GroupHonorInfoResp>();
//        data.setStatus("ok");
//        data.setRetCode(0);
//        data.setData(finalData);
//        return finalData != null ? data : new MiraiFailure();
//    }


    public ActionData<?> setGroupNotice(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val content = params.get("content").getAsString();
        if (!"".equals(content)) {
            bot.getGroupOrFail(groupId).getAnnouncements().publish(OfflineAnnouncement.create(content));
            return new Success();
        } else {
            return new InvalidFailure();
        }
    }
    //todo
    ////////////////////////////////
    //// currently unsupported ////
    //////////////////////////////
    public ActionData<?> getCookies(JsonObject params) {
        return new MiraiFailure();
    }

    public ActionData<?> getCSRFToken(JsonObject params) {
        return new MiraiFailure();
    }

    public ActionData<?> getCredentials(JsonObject params) {
        return new MiraiFailure();
    }

    public ActionData<?> cleanDataDir(JsonObject params) {
        return new Success();
    }

    public ActionData<?> cleanPluginLog(JsonObject params) {
        return new Success();
    }

    public ActionData<?> setRestartPlugin(JsonObject params) {
        val delay = params.get("delay");// unuse.getAsInt()d
        return new Success();
    }


    /////////////////
    //// hidden ////
    ///////////////
    @MiraiExperimentalApi
    @LowLevelApi
    public ActionData<?> getWordSlice(JsonObject params) {
        val content = params.get("content").getAsString();

        return new Success();

    }

    /////////////////
    ////addition ///
    ///////////////
    public ActionData<?> getGroupRootFiles(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();

        //Mirai.getInstance().getFileCacheStrategy()
        //bot.getGroupOrFail(groupId).setEssenceMessage(cachedSourceQueue.get(messageId));
        return new Success();

    }

    public ActionData<?> setEssenceMsg(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val messageId = params.get("message_id").getAsInt();
        bot.getGroupOrFail(groupId).setEssenceMessage(cachedSourceQueue.get(messageId));
        return new Success();

    }

    public ActionData<?> deleteEssenceMsg(JsonObject params) {
        val groupId = params.get("group_id").getAsLong();
        val messageId = params.get("message_id").getAsInt();
        //bot.getGroupOrFail(groupId).setEssenceMessage(cachedSourceQueue.get(messageId));
        return new Success();//todo 等待mirai的api

    }





}
