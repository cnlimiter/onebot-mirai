package cn.evolvefield.mirai.onebot.dto.response.group;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
public class GroupInfoResp {

    @JSONField(name = "group_id")
    private long groupId;

    @JSONField(name = "group_name")
    private String groupName;

    @JSONField(name = "group_memo")
    private String groupMemo;

    @JSONField(name = "group_create_time")
    private int groupCreateTime;

    @JSONField(name = "group_level")
    private int groupLevel;

    @JSONField(name = "member_count")
    private Integer memberCount;

    @JSONField(name = "max_member_count")
    private Integer maxMemberCount;

}
