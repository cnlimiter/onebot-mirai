package cn.evole.onebot.mirai.core;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.model.MiraiGroupMemberInfoResp;
import cn.evole.onebot.sdk.action.ActionData;
import cn.evole.onebot.sdk.response.common.*;
import cn.evole.onebot.sdk.response.misc.BooleanResp;
import cn.evole.onebot.sdk.response.common.MessageResponse;
import cn.evole.onebot.sdk.response.contact.FriendInfoResp;
import cn.evole.onebot.sdk.response.contact.LoginInfoResp;
import cn.evole.onebot.sdk.response.contact.StrangerInfoResp;
import cn.evole.onebot.sdk.response.group.GroupDataResp;
import cn.evole.onebot.sdk.response.group.GroupInfoResp;
import cn.evole.onebot.sdk.response.group.GroupMemberInfoResp;
import cn.evole.onebot.mirai.util.BaseUtils;
import cn.evole.onebot.mirai.util.OnebotMsgParser;
import cn.evole.onebot.mirai.web.queue.CacheRequestQueue;
import cn.evole.onebot.mirai.web.queue.CacheSourceQueue;
import cn.evole.onebot.sdk.util.DataBaseUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import lombok.Getter;
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
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.utils.MiraiExperimentalApi;

import javax.swing.text.html.Option;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
                    break;
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
                    responseDTO =sendMessage(params);
                }
                case "get_image" -> {
                    responseDTO =sendMessage(params);
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
                case "get_msg" -> {
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
            responseDTO = new InvalidRequest();
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
        return new InvalidRequest();
    }

    public ActionData<?> sendGroupMessage(JsonObject params) {
        var targetGroupId = params.get("group_id").getAsLong();
        var raw = params.has("auto_escape") && params.get("auto_escape").getAsBoolean();
        var messages = params.get("message").getAsJsonArray();

        MessageResponse r = new MessageResponse(-1);;

        for (JsonElement message : messages.asList()) {
            var group = bot.getGroupOrFail(targetGroupId);
            var messageChain = OnebotMsgParser.messageToMiraiMessageChains(bot, group, message, raw);
            if (messageChain != null && !messageChain.contentToString().isEmpty()) {
                var receipt = group.sendMessage(messageChain);
                cachedSourceQueue.add(receipt.getSource());
                r = new MessageResponse(DataBaseUtils.toMessageId(receipt.getSource().getInternalIds(), bot.getId(), receipt.getSource().getFromId()));
            }
        }
        return r;
    }

    public ActionData<?> sendPrivateMessage(JsonObject params) {
        var targetQQId = params.get("user_id").getAsLong();
        var raw = params.has("auto_escape") || params.get("auto_escape").getAsBoolean();
        var messages = params.get("message");
        Contact contact;


        try {
            contact = bot.getFriendOrFail(targetQQId);
        } catch (NoSuchElementException e) {
            var fromGroupId = cachedTempContact.get(targetQQId);
            //?: bot.groups.find { group -> group.members.contains(targetQQId) }?.id
            contact = bot.getGroupOrFail(fromGroupId).getOrFail(targetQQId);
        }
        var messageChain = OnebotMsgParser.messageToMiraiMessageChains(bot, contact, messages, raw);
        if (messageChain != null && !messageChain.contentToString().isEmpty()) {
            var receipt = contact.sendMessage(messageChain);
            cachedSourceQueue.add(receipt.getSource());
            return new MessageResponse(DataBaseUtils.toMessageId(receipt.getSource().getInternalIds(), bot.getId(), receipt.getSource().getFromId()));
        } else {
            return new MessageResponse(-1);
        }

    }

    //delete
    public ActionData<?> deleteMessage(JsonObject params) {
        var messageId = params.get("message_id").getAsInt();
        MessageSource.recall(cachedSourceQueue.get(messageId));
        return new GeneralSuccess();
    }


    public ActionData<?> setGroupKick(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var memberId = params.get("user_id").getAsLong();
        var rejectAddRequest = params.has("reject_add_request") && params.get("reject_add_request").getAsBoolean();
        bot.getGroupOrFail(groupId).getOrFail(memberId).kick("", rejectAddRequest);
        return new GeneralSuccess();
    }

    public ActionData<?> sendLike(JsonObject params) {
        return new MiraiFailure();
    }


    //set
    public ActionData<?> setGroupBan(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var memberId = params.get("user_id").getAsLong();
        var duration = params.get("duration").getAsInt();
        if (duration == 0) {
            bot.getGroupOrFail(groupId).getOrFail(memberId).unmute();
        } else {
            bot.getGroupOrFail(groupId).getOrFail(memberId).mute(duration);
        }
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupAnonymousBan(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        String flag = "";
        var flag1 = params.get("anonymous").getAsJsonObject().get("flag").getAsString();
        var flag2 = params.get("anonymous_flag").getAsString();
        var flag3 = params.get("flag").getAsString();
        if (flag1.isEmpty()) flag = flag2.isEmpty() ? flag3 : flag2;
        var duration = params.has("duration") ? params.get("duration").getAsInt() :30 * 60;

        var splits = flag.split("&", 2);
        Mirai.getInstance().muteAnonymousMember(bot, splits[0], splits[1], groupId, duration);
        return new GeneralSuccess();
    }

    public ActionData<?> setWholeGroupBan(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var enable = params.has("enable") && params.get("enable").getAsBoolean();

        bot.getGroupOrFail(groupId).getSettings().setMuteAll(enable);
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupAdmin(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var memberId = params.get("user_id").getAsLong();
        var enable = params.has("enable") && params.get("enable").getAsBoolean();

        bot.getGroupOrFail(groupId).getOrFail(memberId).modifyAdmin(enable);
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupCard(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var memberId = params.get("user_id").getAsLong();
        var card = params.get("card").getAsString().isEmpty() ? "" : params.get("card").getAsString();

        bot.getGroupOrFail(groupId).getOrFail(memberId).setNameCard(card);
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupLeave(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var dismiss = params.has("is_dismiss") && params.get("is_dismiss").getAsBoolean();

        // Not supported
        if (dismiss) return new MiraiFailure();

        bot.getGroupOrFail(groupId).quit();
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupSpecialTitle(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var memberId = params.get("user_id").getAsLong();
        var specialTitle = params.get("special_title").getAsString().isEmpty() ? "" : params.get("special_title").getAsString();
        var duration = params.has("duration") ? params.get("duration"):-1;

        bot.getGroupOrFail(groupId).getOrFail(memberId).setSpecialTitle(specialTitle);
        return new GeneralSuccess();
    }

    public ActionData<?> setFriendAddRequest(JsonObject params) {
        var flag = params.get("flag").getAsString();
        var approve = params.has("approve") && params.get("approve").getAsBoolean();
        var remark = params.get("remark");// unuse.getAsString()d

        var event = cacheRequestQueue.get(Long.parseLong(flag));
        if (event instanceof NewFriendRequestEvent requestEvent)
            if (approve) requestEvent.accept();
            else requestEvent.reject(false);
        else return new InvalidRequest();

        return new GeneralSuccess();
    }

    public ActionData<?> setGroupAddRequest(JsonObject params) {
        var flag = params.get("flag").getAsString();
        var type = params.get("type"); // unuse.getAsString()d
        var subType = params.get("sub_type"); // unuse.getAsString()d
        var approve = params.has("approve") && params.get("approve").getAsBoolean();
        var reason = params.get("reason").getAsString();
        var event = cacheRequestQueue.get(Long.parseLong(flag));
        if (event instanceof MemberJoinRequestEvent requestEvent)
            if (approve) requestEvent.accept();
            else requestEvent.reject(true, reason);
        else if (event instanceof BotInvitedJoinGroupRequestEvent requestEvent) {
            if (approve) requestEvent.accept();
            else requestEvent.ignore();
        }
        return new GeneralSuccess();
    }

    public ActionData<?> sendDiscussMessage(JsonObject params) {
        return new MiraiFailure();
    }

    public ActionData<?> setGroupAnonymous(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var enable = params.get("enable") != null ? params.get("enable") : true;

        //todo
        // Not supported
        // bot.getGroupOrFail(groupId).settings.isAnonymousChatEnabled = enable
        return new MiraiFailure();
    }

    public ActionData<?> setDiscussLeave(JsonObject params) {
        return new MiraiFailure();
    }


    //get
    public ActionData<?> getLoginInfo(JsonObject params) {
        LoginInfoResp loginInfo = new LoginInfoResp(bot.getId(), bot.getNick());
        var data = new ActionData<LoginInfoResp>();
        data.setData(loginInfo);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> sendQQProfile(JsonObject params) {
        var nick = params.get("nickname").getAsString();
        var company = params.get("company").getAsString();
        var email = params.get("email").getAsString();
        var college = params.get("college").getAsString();
        var personalNote = params.get("personal_note").getAsString();

        return new MiraiFailure();
    }


    public ActionData<?> getStrangerInfo(JsonObject params) {
        var userId = params.get("user_id").getAsLong();

        var profile = Mirai.getInstance().queryProfile(bot, userId);
        StrangerInfoResp loginInfo = new StrangerInfoResp();
        loginInfo.setUserId(userId);
        loginInfo.setNickname(profile.getNickname());
        loginInfo.setSex(profile.getSex().name().toLowerCase());
        loginInfo.setAge(profile.getAge());
        loginInfo.setLevel(profile.getQLevel());
        var data = new ActionData<StrangerInfoResp>();
        data.setData(loginInfo);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> getFriendList(JsonObject params) {
        var friendList = new LinkedList<FriendInfoResp>();
        bot.getFriends().forEach(friend -> {
            friendList.add(new FriendInfoResp(friend.getId(), friend.getNick(), friend.getRemark()));
        });
        var data = new ActionData<>();
        data.setData(friendList);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> getGroupList(JsonObject params) {
        var groupList = new LinkedList<GroupDataResp>();
        bot.getGroups().forEach(group ->
                groupList.add(new GroupDataResp(group.getId(), group.getName())));
        {
        }
        var data = new ActionData<>();
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
        var groupId = params.get("group_id").getAsLong();
        var noCache = params.has("no_cache") && params.get("no_cache").getAsBoolean();

        var group = bot.getGroupOrFail(groupId);
        var groupInfo = new GroupInfoResp();
        groupInfo.setGroupId(group.getId());
        groupInfo.setGroupName(group.getName());
        groupInfo.setMemberCount(group.getMembers().size() + 1);//加上机器人本身
        groupInfo.setMaxMemberCount(0);
        groupInfo.setGroupCreateTime(group.getOwner().getJoinTimestamp());//mirai没有直接接口，采用群主加入群的时间，一般情况下均可以实现，除了转让群
        var data = new ActionData<GroupInfoResp>();
        data.setData(groupInfo);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    @MiraiExperimentalApi
    @LowLevelApi
    public ActionData<?> getGroupMemberInfo(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var memberId = params.get("user_id").getAsLong();
        var noCache = params.has("no_cache") && params.get("no_cache").getAsBoolean();

        var group = bot.getGroupOrFail(groupId);
        var data = new ActionData<GroupMemberInfoResp>();
        if (noCache) {
            var groupUin = Mirai.getInstance().getUin(group);
            var members = Mirai.getInstance().getRawGroupMemberList(bot, groupUin, groupId, group.getOwner().getId());

            var groupMemberInfo = new GroupMemberInfoResp();
            var member = BaseUtils.copyIterator(members.iterator())
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
            var member = bot.getGroupOrFail(groupId).getOrFail(memberId);

            data.setData(new MiraiGroupMemberInfoResp(member));
            data.setStatus("ok");
            data.setRetCode(0);
            return data;
        }
    }

    public ActionData<?> getGroupMemberList(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var groupMemberListData = new LinkedList<GroupMemberInfoResp>();
        var data = new ActionData<>();

        AtomicBoolean isBotIncluded = new AtomicBoolean(false);
        var group = bot.getGroupOrFail(groupId);
        var members = group.getMembers();
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


//    public ActionData<?> getRecord(JsonObject params){
//        var file = params.get("file").getAsString();
//        var outFormat = params.get("out_format").getAsString().isEmpty() ? "" : params.get("out_format");// Currently not supporte.getAsString()d
//        var cachedFile = getCachedRecordFile(file)
//        cachedFile?.let {
//            var fileType = with(it.readBytes().copyOfRange(0, 10).toUHexString("")) {
//                when {
//                    startsWith("2321414D52") -> "amr"
//                    startsWith("02232153494C4B5F5633") -> "silk"
//                    else -> "unknown"
//                }
//            }
//            return ResponseDTO.RecordInfo(
//                    RecordInfoData(
//                            it.absolutePath,
//                            it.nameWithoutExtension,
//                            it.nameWithoutExtension,
//                            fileType
//                    )
//            )
//        } ?: return ResponseDTO.PluginFailure()
//    }

//    public ActionData<?> getImage(JsonObject params){
//        var file = params["file"].string
//
//        var image = getCachedImageFile(file)
//        image?.let { cachedImageMeta ->
//                var cachedFile = getDataFile("image", cachedImageMeta.fileName)
//            if (cachedFile == null) {
//                HttpClient.getBytes(cachedImageMeta.url)?.let { saveImage(cachedImageMeta.fileName, it) }
//            }
//            cachedFile = getDataFile("image", cachedImageMeta.fileName)
//
//            cachedFile?.let {
//                var fileType = getImageType(it.readBytes())
//                return ResponseDTO.ImageInfo(
//                        ImageInfoData(
//                                it.absolutePath,
//                                cachedImageMeta.fileName,
//                                cachedImageMeta.md5,
//                                cachedImageMeta.size,
//                                cachedImageMeta.url,
//                                cachedImageMeta.addTime,
//                                fileType
//                        )
//                )
//            }
//        } ?: return ResponseDTO.PluginFailure()
//    }

    public ActionData<?> canSendImage(JsonObject params) {
        var data = new ActionData<BooleanResp>();
        data.setData(new BooleanResp(true));
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> canSendRecord(JsonObject params) {
        var data = new ActionData<BooleanResp>();
        data.setData(new BooleanResp(true));
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> getStatus(JsonObject params) {
        var data = new ActionData<PluginStatusResp>();
        data.setStatus("ok");
        data.setRetCode(0);
        data.setData(new PluginStatusResp());
        return data;
    }


    public ActionData<?> getVersionInfo(JsonObject params) {
        var data = new ActionData<VersionInfo>();
        data.setData(new VersionInfo());
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> setGroupName(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var name = params.get("group_name").getAsString();
        if (!"".equals(name)) {
            bot.getGroupOrFail(groupId).setName(name);
            return new GeneralSuccess();
        } else {
            return new InvalidRequest();
        }
    }

//    @MiraiExperimentalApi
//    @LowLevelApi
//    public ActionData<?> getGroupHonorInfo(JsonObject params) {
//        var groupId = params.get("group_id").getAsLong();
//        var type = params.get("type").getAsString();
//
//        GroupHonorInfoResp finalData = new GroupHonorInfoResp(bot, groupId, type);
//
//        var data = new ActionData<GroupHonorInfoResp>();
//        data.setStatus("ok");
//        data.setRetCode(0);
//        data.setData(finalData);
//        return finalData != null ? data : new MiraiFailure();
//    }


    public ActionData<?> setGroupNotice(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var content = params.get("content").getAsString();
        if (!"".equals(content)) {
            bot.getGroupOrFail(groupId).getAnnouncements().publish(OfflineAnnouncement.create(content));
            return new GeneralSuccess();
        } else {
            return new InvalidRequest();
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
        return new GeneralSuccess();
    }

    public ActionData<?> cleanPluginLog(JsonObject params) {
        return new GeneralSuccess();
    }

    public ActionData<?> setRestartPlugin(JsonObject params) {
        var delay = params.get("delay");// unuse.getAsInt()d
        return new GeneralSuccess();
    }


    /////////////////
    //// hidden ////
    ///////////////
    @MiraiExperimentalApi
    @LowLevelApi
    public ActionData<?> getWordSlice(JsonObject params) {
        var content = params.get("content").getAsString();

        return new GeneralSuccess();

    }

    /////////////////
    ////addition ///
    ///////////////
    public ActionData<?> getGroupRootFiles(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();

        //Mirai.getInstance().getFileCacheStrategy()
        //bot.getGroupOrFail(groupId).setEssenceMessage(cachedSourceQueue.get(messageId));
        return new GeneralSuccess();

    }

    public ActionData<?> setEssenceMsg(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var messageId = params.get("message_id").getAsInt();
        bot.getGroupOrFail(groupId).setEssenceMessage(cachedSourceQueue.get(messageId));
        return new GeneralSuccess();

    }

    public ActionData<?> deleteEssenceMsg(JsonObject params) {
        var groupId = params.get("group_id").getAsLong();
        var messageId = params.get("message_id").getAsInt();
        //bot.getGroupOrFail(groupId).setEssenceMessage(cachedSourceQueue.get(messageId));
        return new GeneralSuccess();//todo 等待mirai的api

    }





}
