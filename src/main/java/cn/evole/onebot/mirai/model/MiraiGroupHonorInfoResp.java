//package cn.evole.mirai.onebot.model;
//
//import cn.evole.onebot.sdk.response.group.GroupHonorInfoResp;
//import net.mamoe.mirai.Bot;
//import net.mamoe.mirai.Mirai;
//import net.mamoe.mirai.contact.active.ActiveHonorList;
//import net.mamoe.mirai.data.GroupHonorType;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Author cnlimiter
// * CreateTime 2023/5/25 2:45
// * Name MiraiGroupHonorInfoResp
// * Description
// */
//
//public class MiraiGroupHonorInfoResp extends GroupHonorInfoResp {
//    public MiraiGroupHonorInfoResp(Bot bot, long groupId, String type) {
//        this.setGroupId(groupId);
//        List<OtherHonor> emotionLists = new ArrayList();
//        ActiveHonorList data;
//        if ("emotion".equals(type) || "all".equals(type)) {
//            data = bot.getGroup(groupId).getActive().queryHonorHistory(GroupHonorType.EMOTION_ID, bot.getGroup(groupId).getActive());
//            if (data != null) {
//                data.getRecords().forEach((info) -> {
//                    emotionLists.add(new MiraiOtherHonor(info));
//                });
//                this.setCurrentTalkative(new MiraiCurrentTalkative(data.getCurrent() != null ? data.getCurrent() : new GroupHonorListData.CurrentTalkative()));
//            }
//
//            this.setEmotionList(emotionLists);
//        }
//
//        List<OtherHonor> strongNewbieLists = new ArrayList();
//        if ("strong_newbie".equals(type) || "all".equals(type)) {
//            data = Mirai.getInstance().getRawGroupHonorListData(bot, groupId, GroupHonorType.STRONG_NEWBIE);
//            if (data != null && data.getStrongNewbieList() != null) {
//                data.getStrongNewbieList().forEach((strongNewbieList) -> {
//                    strongNewbieLists.add(new OtherHonor(strongNewbieList));
//                });
//                this.setCurrentTalkative(new MiraiCurrentTalkative(data.getCurrentTalkative() != null ? data.getCurrentTalkative() : new GroupHonorListData.CurrentTalkative()));
//            }
//
//            this.setStrongNewbieList(strongNewbieLists);
//        }
//
//        List<OtherHonor> talkActiveLists = new ArrayList();
//        if ("talkative".equals(type) || "all".equals(type)) {
//            data = Mirai.getInstance().getRawGroupHonorListData(bot, groupId, GroupHonorType.TALKATIVE);
//            if (data != null && data.getTalkativeList() != null) {
//                data.getTalkativeList().forEach((talkActiveList) -> {
//                    talkActiveLists.add(new OtherHonor(talkActiveList));
//                });
//                this.currentTalkative = new CurrentTalkative(data.getCurrentTalkative() != null ? data.getCurrentTalkative() : new GroupHonorListData.CurrentTalkative());
//            }
//
//            this.talkativeList = talkActiveLists;
//        }
//
//        List<OtherHonor> actorLists = new ArrayList();
//        if ("performer".equals(type) || "all".equals(type)) {
//            data = Mirai.getInstance().getRawGroupHonorListData(bot, groupId, GroupHonorType.ACTIVE);
//            if (data != null && data.getActorList() != null) {
//                data.getActorList().forEach((actor) -> {
//                    actorLists.add(new OtherHonor(actor));
//                });
//                this.currentTalkative = new CurrentTalkative(data.getCurrentTalkative() != null ? data.getCurrentTalkative() : new GroupHonorListData.CurrentTalkative());
//            }
//
//            this.performerList = actorLists;
//        }
//
//        List<OtherHonor> legendLists = new ArrayList();
//        if ("legend".equals(type) || "all".equals(type)) {
//            data = Mirai.getInstance().getRawGroupHonorListData(bot, groupId, GroupHonorType.LEGEND);
//            if (data != null && data.getLegendList() != null) {
//                data.getLegendList().forEach((legendList) -> {
//                    legendLists.add(new OtherHonor(legendList));
//                });
//                this.currentTalkative = new CurrentTalkative(data.getCurrentTalkative() != null ? data.getCurrentTalkative() : new GroupHonorListData.CurrentTalkative());
//            }
//
//            this.legendList = legendLists;
//        }
//
//    }
//}
