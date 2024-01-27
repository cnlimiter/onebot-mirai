package cn.evole.onebot.mirai.core;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.model.MiraiClients;
import cn.evole.onebot.mirai.util.TransformUtils;
import cn.evole.onebot.sdk.event.Event;
import cn.evole.onebot.sdk.event.IgnoreEvent;
import cn.evole.onebot.sdk.event.notice.friend.FriendAddNoticeEvent;
import cn.evole.onebot.sdk.event.notice.friend.PrivatePokeNoticeEvent;
import cn.evole.onebot.sdk.event.notice.group.*;
import cn.evole.onebot.sdk.event.notice.misc.OtherClientStatusNoticeEvent;
import cn.evole.onebot.sdk.event.request.FriendAddRequestEvent;
import cn.evole.onebot.sdk.event.request.GroupAddRequestEvent;
import cn.evole.onebot.sdk.util.DataBaseUtils;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * Description:事件转换器
 * Author: cnlimiter
 * Date: 2022/10/4 21:08
 * Version: 1.0
 */
public class EventMap {
    private static final long currentTimeSeconds = System.currentTimeMillis() / 1000;

    public static final MiraiLogger logger = OneBotMirai.logger;

    public static Event toDTO(BotEvent botEvent){
        if (botEvent instanceof MessageEvent messageEvent) return MessageMap.toDTO(messageEvent);
        else if (botEvent instanceof MemberJoinEvent joinEvent){
            if (joinEvent instanceof MemberJoinEvent.Active active){
                var event = new GroupIncreaseNoticeEvent();
                event.setPostType("notice");
                event.setNoticeType("group_increase");
                event.setSelfId(active.getBot().getId());
                event.setSubType("approve");
                event.setGroupId(active.getGroupId());
                event.setOperatorId(0);
                event.setUserId(active.getMember().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
            else if (joinEvent instanceof MemberJoinEvent.Invite invite){
                var event = new GroupIncreaseNoticeEvent();
                event.setPostType("notice");
                event.setNoticeType("group_increase");
                event.setSelfId(invite.getBot().getId());
                event.setSubType("invite");
                event.setGroupId(invite.getGroupId());
                event.setOperatorId(0);
                event.setUserId(invite.getMember().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
            else return new IgnoreEvent();
        }
        else if (botEvent instanceof MemberLeaveEvent leaveEvent){
            if (leaveEvent instanceof MemberLeaveEvent.Quit quit){
                var event = new GroupDecreaseNoticeEvent();
                event.setPostType("notice");
                event.setNoticeType("group_decrease");
                event.setSelfId(quit.getBot().getId());
                event.setSubType("leave");
                event.setGroupId(quit.getGroupId());
                event.setOperatorId(event.getOperatorId());
                event.setUserId(quit.getMember().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
            else if (leaveEvent instanceof MemberLeaveEvent.Kick kick){
                var event = new GroupDecreaseNoticeEvent();
                event.setPostType("notice");
                event.setNoticeType("group_decrease");
                event.setSelfId(kick.getBot().getId());
                event.setSubType("kick");
                event.setGroupId(kick.getGroupId());
                event.setOperatorId(optOpId(kick.getOperator(), kick.getBot()));
                event.setUserId(kick.getMember().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
            else return new IgnoreEvent();
        }
        else if (botEvent instanceof BotJoinGroupEvent.Active active){
            var event = new GroupIncreaseNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_increase");
            event.setSelfId(active.getBot().getId());
            event.setSubType("approve");
            event.setGroupId(active.getGroupId());
            event.setOperatorId(0);
            event.setUserId(active.getBot().getId());
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof BotJoinGroupEvent.Invite invite){
            var event = new GroupIncreaseNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_increase");
            event.setSelfId(invite.getBot().getId());
            event.setSubType("invite");
            event.setGroupId(invite.getGroupId());
            event.setOperatorId(0);
            event.setUserId(invite.getBot().getId());
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof BotLeaveEvent leaveEvent){
            var event = new GroupDecreaseNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_decrease");
            event.setSubType("kick_me");
            if (leaveEvent instanceof BotLeaveEvent.Active active){
                event.setSelfId(active.getBot().getId());
                event.setGroupId(active.getGroupId());
                event.setOperatorId(0);
                event.setUserId(active.getBot().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
            else if (leaveEvent instanceof BotLeaveEvent.Kick kick){
                event.setSelfId(kick.getBot().getId());
                event.setGroupId(kick.getGroupId());
                event.setOperatorId(0);
                event.setUserId(kick.getBot().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
            else return new IgnoreEvent();
        }
        else if (botEvent instanceof MemberPermissionChangeEvent changeEvent){
            var event = new GroupAdminNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_admin");
            if (changeEvent.getNew().compareTo(MemberPermission.MEMBER) == 0){
                event.setSelfId(changeEvent.getBot().getId());
                event.setSubType("unset");
                event.setGroupId(changeEvent.getGroupId());
                event.setUserId(changeEvent.getMember().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
            else {
                event.setSelfId(changeEvent.getBot().getId());
                event.setSubType("set");
                event.setGroupId(changeEvent.getGroupId());
                event.setUserId(changeEvent.getMember().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
        }
        else if (botEvent instanceof MemberMuteEvent muteEvent){
            var event = new GroupBanNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_ban");
            event.setSelfId(muteEvent.getBot().getId());
            event.setSubType("ban");
            event.setGroupId(muteEvent.getGroupId());
            event.setOperatorId(optOpId(muteEvent.getOperator(), muteEvent.getBot()));
            event.setUserId(muteEvent.getMember().getId());
            event.setDuration(muteEvent.getDurationSeconds());
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof GroupMuteAllEvent muteAllEvent){
            var event = new GroupBanNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_ban");
            if (muteAllEvent.getNew()){
                event.setSelfId(muteAllEvent.getBot().getId());
                event.setSubType("ban");
                event.setGroupId(muteAllEvent.getGroupId());
                event.setOperatorId(optOpId(muteAllEvent.getOperator(), muteAllEvent.getBot()));
                event.setUserId(0);
                event.setDuration(0);
                event.setTime(currentTimeSeconds);
                return event;
            }
            else {
                event.setSelfId(muteAllEvent.getBot().getId());
                event.setSubType("lift_ban");
                event.setGroupId(muteAllEvent.getGroupId());
                event.setOperatorId(optOpId(muteAllEvent.getOperator(), muteAllEvent.getBot()));
                event.setUserId(0);
                event.setDuration(0);
                event.setTime(currentTimeSeconds);
                return event;
            }
        }
        else if (botEvent instanceof BotMuteEvent botMuteEvent){
            var event = new GroupBanNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_ban");
            event.setSelfId(botMuteEvent.getBot().getId());
            event.setSubType("ban");
            event.setGroupId(botMuteEvent.getGroupId());
            event.setUserId(botMuteEvent.getBot().getId());
            event.setDuration(botMuteEvent.getDurationSeconds());
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof MemberUnmuteEvent unmuteEvent){
            var event = new GroupBanNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_ban");
            event.setSelfId(unmuteEvent.getBot().getId());
            event.setSubType("lift_ban");
            event.setGroupId(unmuteEvent.getGroupId());
            event.setOperatorId(optOpId(unmuteEvent.getOperator(), unmuteEvent.getBot()));
            event.setUserId(unmuteEvent.getMember().getId());
            event.setDuration(0);
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof BotUnmuteEvent botUnmuteEvent){
            var event = new GroupBanNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_ban");
            event.setSelfId(botUnmuteEvent.getBot().getId());
            event.setSubType("lift_ban");
            event.setGroupId(botUnmuteEvent.getGroupId());
            event.setOperatorId(botUnmuteEvent.getOperator().getId());
            event.setUserId(botUnmuteEvent.getBot().getId());
            event.setDuration(0);
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof FriendAddEvent addEvent){
            var event = new FriendAddNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("friend_add");
            event.setSelfId(addEvent.getBot().getId());
            event.setUserId(addEvent.getFriend().getId());
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof NewFriendRequestEvent requestEvent){
            var event = new FriendAddRequestEvent();
            event.setPostType("request");
            event.setRequestType("friend");
            event.setSelfId(requestEvent.getBot().getId());
            event.setUserId(requestEvent.getFromId());
            event.setComment(requestEvent.getMessage());
            event.setFlag(String.valueOf(requestEvent.getEventId()));
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof MemberJoinRequestEvent requestEvent){
            var event = new GroupAddRequestEvent();
            event.setPostType("request");
            event.setRequestType("group");
            event.setSelfId(requestEvent.getBot().getId());
            event.setSubType("add");
            event.setGroupId(requestEvent.getGroupId());
            event.setUserId(requestEvent.getFromId());
            event.setComment(requestEvent.getMessage());
            event.setFlag(String.valueOf(requestEvent.getEventId()));
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof BotInvitedJoinGroupRequestEvent requestEvent){
            var event = new GroupAddRequestEvent();
            event.setPostType("request");
            event.setRequestType("group");
            event.setSelfId(requestEvent.getBot().getId());
            event.setSubType("invite");
            event.setGroupId(requestEvent.getGroupId());
            event.setUserId(requestEvent.getInvitorId());
            event.setComment("");
            event.setFlag(String.valueOf(requestEvent.getEventId()));
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof NudgeEvent nudgeEvent){
            if (nudgeEvent.getSubject() instanceof Group){
                var event = new GroupPokeNoticeEvent();
                event.setPostType("notice");
                event.setNoticeType("notify");
                event.setSubType("poke");
                event.setSelfId(nudgeEvent.getBot().getId());
                event.setGroupId(nudgeEvent.getSubject().getId());
                event.setUserId(nudgeEvent.getFrom().getId());
                event.setTargetId(nudgeEvent.getTarget().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
            else {
                var event = new PrivatePokeNoticeEvent();
                event.setPostType("notice");
                event.setNoticeType("notify");
                event.setSubType("poke");
                event.setSelfId(nudgeEvent.getBot().getId());
                event.setUserId(nudgeEvent.getFrom().getId());
                event.setTargetId(nudgeEvent.getTarget().getId());
                event.setTime(currentTimeSeconds);
                return event;
            }
        }
        else if (botEvent instanceof MessageRecallEvent recallEvent){
            if (recallEvent instanceof MessageRecallEvent.GroupRecall groupRecall){
                var event = new GroupMsgDeleteNoticeEvent();
                event.setPostType("notice");
                event.setNoticeType("group_recall");
                event.setSelfId(groupRecall.getBot().getId());
                event.setGroupId(groupRecall.getGroup().getId());
                event.setUserId(groupRecall.getAuthorId());
                event.setOperatorId(optOpId(groupRecall.getOperator(), groupRecall.getBot()));
                event.setMsgId(DataBaseUtils.toMessageId(groupRecall.getMessageIds(), groupRecall.getBot().getId(),
                        optOpId(groupRecall.getOperator(), groupRecall.getBot())));
                event.setTime(currentTimeSeconds);
                return event;
            }
            else if (recallEvent instanceof MessageRecallEvent.FriendRecall friendRecall){
                var event = new GroupMsgDeleteNoticeEvent();
                event.setPostType("notice");
                event.setNoticeType("friend_recall");
                event.setSelfId(friendRecall.getBot().getId());
                event.setUserId(friendRecall.getOperatorId());
                event.setMsgId(DataBaseUtils.toMessageId(friendRecall.getMessageIds(), friendRecall.getBot().getId(),
                        friendRecall.getOperatorId()));
                event.setTime(currentTimeSeconds);
                return event;
            }
            else {
                if (PluginConfig.INSTANCE.getDebug()) logger.info(String.format("发生讨论组消息撤回事件, 已被插件忽略: %s", recallEvent));
                return new IgnoreEvent(recallEvent.getBot().getId());
            }
        }
        else if (botEvent instanceof MemberHonorChangeEvent changeEvent){
            var event = new GroupHonorChangeNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("notify");
            event.setSubType("honor");
            event.setSelfId(changeEvent.getBot().getId());
            event.setGroupId(changeEvent.getGroup().getId());
            event.setUserId(changeEvent.getUser().getId());
            event.setHonorType(TransformUtils.honor(changeEvent.getHonorType()));
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof MemberCardChangeEvent changeEvent){
            var event = new GroupCardChangeNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_card");
            event.setSelfId(changeEvent.getBot().getId());
            event.setGroupId(changeEvent.getGroup().getId());
            event.setUserId(changeEvent.getUser().getId());
            event.setCardNew(changeEvent.getNew());
            event.setCardOld(changeEvent.getOrigin());
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof MemberSpecialTitleChangeEvent changeEvent){
            var event = new GroupTitleChangeNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("group_title");
            event.setSelfId(changeEvent.getBot().getId());
            event.setGroupId(changeEvent.getGroup().getId());
            event.setUserId(changeEvent.getUser().getId());
            event.setTitleNew(changeEvent.getNew());
            event.setTitleOld(changeEvent.getOrigin());
            event.setTime(currentTimeSeconds);
            return event;
        }
        else if (botEvent instanceof OtherClientEvent clientEvent){
            var event = new OtherClientStatusNoticeEvent();
            event.setPostType("notice");
            event.setNoticeType("client_status");
            if (clientEvent instanceof OtherClientOfflineEvent offlineEvent){
                event.setSelfId(offlineEvent.getBot().getId());
                event.setClient(new MiraiClients(offlineEvent.getClient().getInfo()));
                event.setOnline(false);
                return event;
            }
            else if (clientEvent instanceof OtherClientOnlineEvent onlineEvent){
                event.setSelfId(onlineEvent.getBot().getId());
                event.setClient(new MiraiClients(onlineEvent.getClient().getInfo()));
                event.setOnline(false);
                return event;
            }
            else return new IgnoreEvent();
        } else if (botEvent instanceof ImageUploadEvent) {
            logger.info("即将上传图片");
            return new IgnoreEvent(botEvent.getBot().getId());
        } else {
            if (PluginConfig.INSTANCE.getDebug()) logger.info(String.format("发生了被插件忽略的事件: %s", botEvent));
            return new IgnoreEvent(botEvent.getBot().getId());
        }
    }


    private static long optOpId(Member op, Bot bot){
        if (op != null) return op.getId();
        else return bot.getId();
    }


}
