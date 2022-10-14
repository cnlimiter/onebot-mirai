package cn.evolvefield.mirai.onebot.core;

import cn.evolvefield.mirai.onebot.OneBotMirai;
import cn.evolvefield.mirai.onebot.dto.response.ActionData;
import cn.evolvefield.mirai.onebot.dto.response.BooleanResp;
import cn.evolvefield.mirai.onebot.dto.response.MessageResponse;
import cn.evolvefield.mirai.onebot.dto.response.contact.FriendInfoResp;
import cn.evolvefield.mirai.onebot.dto.response.contact.LoginInfoResp;
import cn.evolvefield.mirai.onebot.dto.response.contact.StrangerInfoResp;
import cn.evolvefield.mirai.onebot.dto.response.group.GroupDataResp;
import cn.evolvefield.mirai.onebot.dto.response.group.GroupHonorInfoResp;
import cn.evolvefield.mirai.onebot.dto.response.group.GroupInfoResp;
import cn.evolvefield.mirai.onebot.dto.response.group.GroupMemberInfoResp;
import cn.evolvefield.mirai.onebot.dto.response.msic.*;
import cn.evolvefield.mirai.onebot.util.BaseUtils;
import cn.evolvefield.mirai.onebot.util.DataBaseUtils;
import cn.evolvefield.mirai.onebot.util.OnebotMsgParser;
import cn.evolvefield.mirai.onebot.web.queue.CacheRequestQueue;
import cn.evolvefield.mirai.onebot.web.queue.CacheSourceQueue;
import com.alibaba.fastjson2.JSONObject;
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

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 1:41
 * Version: 1.0
 */
public class MiraiApi {
    Bot bot;
    @Getter
    private final LinkedHashMap<Long, Long> cachedTempContact = new LinkedHashMap<>();
    @Getter
    private final CacheRequestQueue cacheRequestQueue = new CacheRequestQueue();
    @Getter
    private final CacheSourceQueue cachedSourceQueue = new CacheSourceQueue();

    public MiraiApi(Bot bot) {
        this.bot = bot;
    }

    public ActionData<?> callMiraiApi(String action, JSONObject params, MiraiApi mirai) {
        ActionData<?> responseDTO = new PluginFailure();

        try {
            switch (action) {
                case "send_msg" -> {
                    responseDTO = mirai.sendMessage(params);
                }
                case "send_private_msg" -> {
                    responseDTO = mirai.sendPrivateMessage(params);
                }
                case "send_group_msg" -> {
                    responseDTO = mirai.sendGroupMessage(params);
                }
                case "send_discuss_msg" -> {
                    responseDTO = mirai.sendDiscussMessage(params);
                }
                case "delete_msg" -> {
                    responseDTO = mirai.deleteMessage(params);
                }
                case "send_like" -> {
                    responseDTO = mirai.sendLike(params);
                }
                case "set_group_kick" -> {
                    responseDTO = mirai.setGroupKick(params);
                }
                case "set_group_ban" -> {
                    responseDTO = mirai.setGroupBan(params);
                }
                case "set_group_anonymous_ban" -> {
                    responseDTO = mirai.setGroupAnonymousBan(params);
                }
                case "set_group_whole_ban" -> {
                    responseDTO = mirai.setWholeGroupBan(params);
                }
                case "set_group_admin" -> {
                    responseDTO = mirai.setGroupAdmin(params);
                }
                case "set_group_anonymous" -> {
                    responseDTO = mirai.setGroupAnonymous(params);
                }
                case "set_group_card" -> {
                    responseDTO = mirai.setGroupCard(params);
                }
                case "set_group_leave" -> {
                    responseDTO = mirai.setGroupLeave(params);
                }
                case "set_group_special_title" -> {
                    responseDTO = mirai.setGroupSpecialTitle(params);
                }
                case "set_discuss_leave" -> {
                    responseDTO = mirai.setDiscussLeave(params);
                }
                case "set_friend_add_request" -> {
                    responseDTO = mirai.setFriendAddRequest(params);
                }
                case "set_group_add_request" -> {
                    responseDTO = mirai.setGroupAddRequest(params);
                }
                case "get_login_info" -> {
                    responseDTO = mirai.getLoginInfo(params);
                }
                case "set_qq_profile" -> {
                    responseDTO = mirai.sendQQProfile(params);
                }
                case "get_stranger_info" -> {
                    responseDTO = mirai.getStrangerInfo(params);
                }
                case "get_friend_list" -> {
                    responseDTO = mirai.getFriendList(params);
                }
                case "get_group_list" -> {
                    responseDTO = mirai.getGroupList(params);
                }
                case "get_group_info" -> {
                    responseDTO = mirai.getGroupInfo(params);
                }
                case "get_group_member_info" -> {
                    responseDTO = mirai.getGroupMemberInfo(params);
                }
                case "get_group_member_list" -> {
                    responseDTO = mirai.getGroupMemberList(params);
                }
                case "get_cookies" -> {
                    responseDTO = mirai.getCookies(params);
                }
                case "get_csrf_token" -> {
                    responseDTO = mirai.getCSRFToken(params);
                }
                case "get_credentials" -> {
                    responseDTO = mirai.getCredentials(params);
                }
                case "get_record" -> {
                    responseDTO = mirai.sendMessage(params);
                }
                case "get_image" -> {
                    responseDTO = mirai.sendMessage(params);
                }
                case "can_send_image" -> {
                    responseDTO = mirai.canSendImage(params);
                }
                case "can_send_record" -> {
                    responseDTO = mirai.canSendRecord(params);
                }
                case "get_status" -> {
                    responseDTO = mirai.getStatus(params);
                }
                case "get_version_info" -> {
                    responseDTO = mirai.getVersionInfo(params);
                }
                case "set_restart_plugin" -> {
                    responseDTO = mirai.setRestartPlugin(params);
                }
                case "clean_data_dir" -> {
                    responseDTO = mirai.cleanDataDir(params);
                }
                case "clean_plugin_log" -> {
                    responseDTO = mirai.cleanPluginLog(params);
                }
                case "set_group_name" -> {
                    responseDTO = mirai.setGroupName(params);
                }
                case "get_group_honor_info" -> {
                    responseDTO = mirai.getGroupHonorInfo(params);
                }
                case "get_msg" -> {
                    responseDTO = mirai.sendMessage(params);
                }
                case "_set_group_notice" -> {
                    responseDTO = mirai.setGroupNotice(params);
                }
                case ".get_word_slices" -> {
                    responseDTO = mirai.getWordSlice(params);
                }
                case "set_essence_msg" -> {
                    responseDTO = mirai.setEssenceMsg(params);
                }
                default -> OneBotMirai.logger.error(String.format("未知OneBot API: %s", action));
            }
        } catch (IllegalArgumentException e) {
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


    //send
    public ActionData<?> sendMessage(JSONObject params) {
        if (params.containsKey("message_type")) {
            switch (params.getString("message_type")) {
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

    public ActionData<?> sendGroupMessage(JSONObject params) {
        var targetGroupId = params.getLong("group_id");
        var raw = params.getBooleanValue("auto_escape", false);
        var messages = params.get("message");

        var group = bot.getGroupOrFail(targetGroupId);
        var messageChain = OnebotMsgParser.messageToMiraiMessageChains(bot, group, messages, raw);
        if (messageChain != null && !messageChain.contentToString().isEmpty()) {
            var send = messageChain.contentToString();
            var receipt = group.sendMessage(send);
            cachedSourceQueue.add(receipt.getSource());
            return new MessageResponse(DataBaseUtils.toMessageId(receipt.getSource().getInternalIds(), bot.getId(), receipt.getSource().getFromId()));
        } else {
            return new MessageResponse(-1);
        }
    }

    public ActionData<?> sendPrivateMessage(JSONObject params) {
        var targetQQId = params.getLong("user_id");
        var raw = params.getBooleanValue("auto_escape", false);
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
            var send = messageChain.contentToString();
            var receipt = contact.sendMessage(send);
            cachedSourceQueue.add(receipt.getSource());
            return new MessageResponse(DataBaseUtils.toMessageId(receipt.getSource().getInternalIds(), bot.getId(), receipt.getSource().getFromId()));
        } else {
            return new MessageResponse(-1);
        }

    }

    //delete
    public ActionData<?> deleteMessage(JSONObject params) {
        var messageId = params.getInteger("message_id");
        MessageSource.recall(cachedSourceQueue.get(messageId));
        return new GeneralSuccess();
    }


    public ActionData<?> setGroupKick(JSONObject params) {
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        var rejectAddRequest = params.getBooleanValue("reject_add_request", false);
        bot.getGroupOrFail(groupId).getOrFail(memberId).kick("", rejectAddRequest);
        return new GeneralSuccess();
    }

    public ActionData<?> sendLike(JSONObject params) {
        return new MiraiFailure();
    }


    //set
    public ActionData<?> setGroupBan(JSONObject params) {
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

    public ActionData<?> setGroupAnonymousBan(JSONObject params) {
        var groupId = params.getLong("group_id");
        String flag = "";
        var flag1 = params.getJSONObject("anonymous").getString("flag");
        var flag2 = params.getString("anonymous_flag");
        var flag3 = params.getString("flag");
        if (flag1.isEmpty()) flag = flag2.isEmpty() ? flag3 : flag2;
        var duration = params.getInteger("duration").describeConstable().orElse(30 * 60);
        var splits = flag.split("&", 2);
        Mirai.getInstance().muteAnonymousMember(bot, splits[0], splits[1], groupId, duration);
        return new GeneralSuccess();
    }

    public ActionData<?> setWholeGroupBan(JSONObject params) {
        var groupId = params.getLong("group_id");
        var enable = params.getBooleanValue("enable", true);

        bot.getGroupOrFail(groupId).getSettings().setMuteAll(enable);
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupAdmin(JSONObject params) {
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        var enable = params.getBooleanValue("enable", true);

        bot.getGroupOrFail(groupId).getOrFail(memberId).modifyAdmin(enable);
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupCard(JSONObject params) {
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        var card = params.getString("card").isEmpty() ? "" : params.getString("card");

        bot.getGroupOrFail(groupId).getOrFail(memberId).setNameCard(card);
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupLeave(JSONObject params) {
        var groupId = params.getLong("group_id");
        var dismiss = params.getBooleanValue("is_dismiss", false);

        // Not supported
        if (dismiss) return new MiraiFailure();

        bot.getGroupOrFail(groupId).quit();
        return new GeneralSuccess();
    }

    public ActionData<?> setGroupSpecialTitle(JSONObject params) {
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        var specialTitle = params.getString("special_title").isEmpty() ? "" : params.getString("special_title");
        var duration = params.getIntValue("duration", -1);

        bot.getGroupOrFail(groupId).getOrFail(memberId).setSpecialTitle(specialTitle);
        return new GeneralSuccess();
    }

    public ActionData<?> setFriendAddRequest(JSONObject params) {
        var flag = params.getString("flag");
        var approve = params.getBooleanValue("approve", true);
        var remark = params.getString("remark");// unused

        var event = cacheRequestQueue.get(Long.parseLong(flag));
        if (event instanceof NewFriendRequestEvent requestEvent)
            if (approve) requestEvent.accept();
            else requestEvent.reject(false);
        else return new InvalidRequest();

        return new GeneralSuccess();
    }

    public ActionData<?> setGroupAddRequest(JSONObject params) {
        var flag = params.getString("flag");
        var type = params.getString("type"); // unused
        var subType = params.getString("sub_type"); // unused
        var approve = params.getBooleanValue("approve", true);
        var reason = params.getString("reason");
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

    public ActionData<?> sendDiscussMessage(JSONObject params) {
        return new MiraiFailure();
    }

    public ActionData<?> setGroupAnonymous(JSONObject params) {
        var groupId = params.getLong("group_id");
        var enable = params.getBoolean("enable") != null ? params.getBoolean("enable") : true;

        //todo
        // Not supported
        // bot.getGroupOrFail(groupId).settings.isAnonymousChatEnabled = enable
        return new MiraiFailure();
    }

    public ActionData<?> setDiscussLeave(JSONObject params) {
        return new MiraiFailure();
    }


    //get
    public ActionData<?> getLoginInfo(JSONObject params) {
        LoginInfoResp loginInfo = new LoginInfoResp(bot.getId(), bot.getNick());
        var data = new ActionData<LoginInfoResp>();
        data.setData(loginInfo);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> sendQQProfile(JSONObject params) {
        var nick = params.getString("nickname");
        var company = params.getString("company");
        var email = params.getString("email");
        var college = params.getString("college");
        var personalNote = params.getString("personal_note");

        return new MiraiFailure();
    }


    public ActionData<?> getStrangerInfo(JSONObject params) {
        var userId = params.getLong("user_id");

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

    public ActionData<?> getFriendList(JSONObject params) {
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

    public ActionData<?> getGroupList(JSONObject params) {
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
    public ActionData<?> getGroupInfo(JSONObject params) {
        var groupId = params.getLong("group_id");
        var noCache = params.getBooleanValue("no_cache", false);// unused

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
    public ActionData<?> getGroupMemberInfo(JSONObject params) {
        var groupId = params.getLong("group_id");
        var memberId = params.getLong("user_id");
        var noCache = params.getBooleanValue("no_cache", false);

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

            data.setData(new GroupMemberInfoResp(member));
            data.setStatus("ok");
            data.setRetCode(0);
            return data;
        }
    }

    public ActionData<?> getGroupMemberList(JSONObject params) {
        var groupId = params.getLong("group_id");
        var groupMemberListData = new LinkedList<GroupMemberInfoResp>();
        var data = new ActionData<>();

        AtomicBoolean isBotIncluded = new AtomicBoolean(false);
        var group = bot.getGroupOrFail(groupId);
        var members = group.getMembers();
        members.forEach(member -> {
            if (member.getId() == bot.getId()) isBotIncluded.set(true);
            groupMemberListData.add(new GroupMemberInfoResp(member));
        });
        if (!isBotIncluded.get()) groupMemberListData.add(new GroupMemberInfoResp(group.getBotAsMember()));
        data.setData(groupMemberListData);
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }


//    public ActionData<?> getRecord(JSONObject params){
//        var file = params.getString("file");
//        var outFormat = params.getString("out_format").isEmpty() ? "" : params.getString("out_format");// Currently not supported
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

//    public ActionData<?> getImage(JSONObject params){
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

    public ActionData<?> canSendImage(JSONObject params) {
        var data = new ActionData<BooleanResp>();
        data.setData(new BooleanResp(true));
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> canSendRecord(JSONObject params) {
        var data = new ActionData<BooleanResp>();
        data.setData(new BooleanResp(true));
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    public ActionData<?> getStatus(JSONObject params) {
        var data = new ActionData<PluginStatusResp>();
        data.setStatus("ok");
        data.setRetCode(0);
        data.setData(new PluginStatusResp());
        return data;
    }


    public ActionData<?> getVersionInfo(JSONObject params) {
        var data = new ActionData<VersionInfo>();
        data.setData(new VersionInfo());
        data.setStatus("ok");
        data.setRetCode(0);
        return data;
    }

    ////////////////
    ////  v11  ////
    //////////////

    public ActionData<?> setGroupName(JSONObject params) {
        var groupId = params.getLong("group_id");
        var name = params.getString("group_name");
        if (!"".equals(name)) {
            bot.getGroupOrFail(groupId).setName(name);
            return new GeneralSuccess();
        } else {
            return new InvalidRequest();
        }
    }

    @MiraiExperimentalApi
    @LowLevelApi
    public ActionData<?> getGroupHonorInfo(JSONObject params) {
        var groupId = params.getLong("group_id");
        var type = params.getString("type");

        GroupHonorInfoResp finalData = new GroupHonorInfoResp(bot, groupId, type);

        var data = new ActionData<GroupHonorInfoResp>();
        data.setStatus("ok");
        data.setRetCode(0);
        data.setData(finalData);
        return finalData != null ? data : new MiraiFailure();
    }


    public ActionData<?> setGroupNotice(JSONObject params) {
        var groupId = params.getLong("group_id");
        var content = params.getString("content");
        if (!"".equals(content)) {
            bot.getGroupOrFail(groupId).getAnnouncements().publish(OfflineAnnouncement.create(content));
            return new GeneralSuccess();
        } else {
            return new InvalidRequest();
        }
    }

    public ActionData<?> setEssenceMsg(JSONObject params) {
        var groupId = params.getLong("group_id");
        var messageId = params.getInteger("message_id");
        bot.getGroupOrFail(groupId).setEssenceMessage(cachedSourceQueue.get(messageId));
        return new GeneralSuccess();

    }

    public ActionData<?> deleteEssenceMsg(JSONObject params) {
        var groupId = params.getLong("group_id");
        var messageId = params.getInteger("message_id");
        bot.getGroupOrFail(groupId).setEssenceMessage(cachedSourceQueue.get(messageId));
        return new GeneralSuccess();//todo 等待mirai的api

    }

    /////////////////
    //// hidden ////
    ///////////////



    @MiraiExperimentalApi
    @LowLevelApi
    public ActionData<?> getWordSlice(JSONObject params) {
        var content = params.getString("content");

        return new GeneralSuccess();

    }

    //todo
    ////////////////////////////////
    //// currently unsupported ////
    //////////////////////////////
    public ActionData<?> getCookies(JSONObject params) {
        return new MiraiFailure();
    }

    public ActionData<?> getCSRFToken(JSONObject params) {
        return new MiraiFailure();
    }

    public ActionData<?> getCredentials(JSONObject params) {
        return new MiraiFailure();
    }

    public ActionData<?> cleanDataDir(JSONObject params) {
        return new GeneralSuccess();
    }

    public ActionData<?> cleanPluginLog(JSONObject params) {
        return new GeneralSuccess();
    }

    public ActionData<?> setRestartPlugin(JSONObject params) {
        var delay = params.getInteger("delay");// unused
        return new GeneralSuccess();
    }


}
