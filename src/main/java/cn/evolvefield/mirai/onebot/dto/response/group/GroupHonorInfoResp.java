package cn.evolvefield.mirai.onebot.dto.response.group;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.data.GroupHonorListData;
import net.mamoe.mirai.data.GroupHonorType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
public class GroupHonorInfoResp {

    @JSONField(name = "group_id")
    private long groupId;

    @JSONField(name = "current_talkative")
    private CurrentTalkative currentTalkative;

    @JSONField(name = "talkative_list")
    private List<OtherHonor> talkativeList;

    @JSONField(name = "performer_list")
    private List<OtherHonor> performerList;

    @JSONField(name = "legend_list")
    private List<OtherHonor> legendList;

    @JSONField(name = "strong_newbie_list")
    private List<OtherHonor> strongNewbieList;

    @JSONField(name = "emotion_list")
    private List<OtherHonor> emotionList;

    /**
     * 活跃天数
     */
    @Data
    @NoArgsConstructor
    public static class CurrentTalkative {

        @JSONField(name = "user_id")
        private long userId;

        @JSONField(name = "nickname")
        private String nickname;

        @JSONField(name = "avatar")
        private String avatar;

        @JSONField(name = "day_count")
        private int dayCount;

        public CurrentTalkative(GroupHonorListData.CurrentTalkative active) {
            this.userId = active.getUin() != null ? active.getUin() : 0;
            this.nickname = active.getNick();
            this.avatar = active.getAvatar();
            this.dayCount = active.getDayCount() != null ? active.getDayCount() : 0;
        }

    }

    /**
     * 其它荣耀
     */
    @Data
    @NoArgsConstructor
    public static class OtherHonor {

        @JSONField(name = "user_id")
        private long userId;

        @JSONField(name = "nickname")
        private String nickname;

        @JSONField(name = "avatar")
        private String avatar;

        @JSONField(name = "description")
        private String description;

        public OtherHonor(GroupHonorListData.Actor actor) {
            this.userId = actor.getUin() != null ? actor.getUin() : 0;
            this.nickname = actor.getName();
            this.avatar = actor.getAvatar();
            this.description = actor.getDesc();
        }

        public OtherHonor(GroupHonorListData.Talkative talkative) {
            this.userId = talkative.getUin() != null ? talkative.getUin() : 0;
            this.nickname = talkative.getName();
            this.avatar = talkative.getAvatar();
            this.description = talkative.getDesc();
        }

    }

    public GroupHonorInfoResp(Bot bot, long groupId, String type) {
        this.groupId = groupId;
        GroupHonorListData data;
        List<OtherHonor> emotionLists = new ArrayList<>();
        if (("emotion".equals(type) ||  "all".equals(type))){
            data = Mirai.getInstance().getRawGroupHonorListData(bot, groupId, GroupHonorType.EMOTION);
            if (data != null && data.getEmotionList() != null){
                data.getEmotionList().forEach(emotionList -> emotionLists.add(new OtherHonor(emotionList)));
                this.currentTalkative = new CurrentTalkative(data.getCurrentTalkative() != null ? data.getCurrentTalkative() : new GroupHonorListData.CurrentTalkative());
            }
             this.emotionList = emotionLists;
        }

        List<OtherHonor> strongNewbieLists = new ArrayList<>();
        if (("strong_newbie".equals(type) ||  "all".equals(type)) ){
            data = Mirai.getInstance().getRawGroupHonorListData(bot, groupId, GroupHonorType.STRONG_NEWBIE);
            if (data != null && data.getStrongNewbieList() != null) {
                data.getStrongNewbieList().forEach(strongNewbieList -> strongNewbieLists.add(new OtherHonor(strongNewbieList)));
                this.currentTalkative = new CurrentTalkative(data.getCurrentTalkative() != null ? data.getCurrentTalkative() : new GroupHonorListData.CurrentTalkative());
            }
            this.strongNewbieList = strongNewbieLists;
        }

        List<OtherHonor> talkActiveLists = new ArrayList<>();
        if (("talkative".equals(type) || "all".equals(type))) {
            data = Mirai.getInstance().getRawGroupHonorListData(bot, groupId, GroupHonorType.TALKATIVE);
            if (data != null && data.getTalkativeList() != null) {
                data.getTalkativeList().forEach(talkActiveList -> talkActiveLists.add(new OtherHonor(talkActiveList)));
                this.currentTalkative = new CurrentTalkative(data.getCurrentTalkative() != null ? data.getCurrentTalkative() : new GroupHonorListData.CurrentTalkative());
            }
            this.talkativeList = talkActiveLists;
        }
        List<OtherHonor> actorLists = new ArrayList<>();
        if (("performer".equals(type) ||  "all".equals(type)) ) {
            data = Mirai.getInstance().getRawGroupHonorListData(bot, groupId, GroupHonorType.ACTIVE);
            if (data != null && data.getActorList() != null) {
                data.getActorList().forEach(actor -> actorLists.add(new OtherHonor(actor)));
                this.currentTalkative = new CurrentTalkative(data.getCurrentTalkative() != null ? data.getCurrentTalkative() : new GroupHonorListData.CurrentTalkative());
            }
            this.performerList = actorLists;
        }
        List<OtherHonor> legendLists = new ArrayList<>();
        if (("legend".equals(type) ||  "all".equals(type)) ) {
            data = Mirai.getInstance().getRawGroupHonorListData(bot, groupId, GroupHonorType.LEGEND);
            if (data != null && data.getLegendList() != null) {
                data.getLegendList().forEach(legendList -> legendLists.add(new OtherHonor(legendList)));
                this.currentTalkative = new CurrentTalkative(data.getCurrentTalkative() != null ? data.getCurrentTalkative() : new GroupHonorListData.CurrentTalkative());
            }
            this.legendList = legendLists;
        }
    }

}
