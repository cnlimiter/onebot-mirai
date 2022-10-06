package cn.evolvefield.mirai.onebot.dto.response.group;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
public class GroupInfoResp {

    @SerializedName( "group_id")
    private long groupId;

    @SerializedName( "group_name")
    private String groupName;

    @SerializedName( "group_memo")
    private String groupMemo;

    @SerializedName( "group_create_time")
    private int groupCreateTime;

    @SerializedName( "group_level")
    private int groupLevel;

    @SerializedName( "member_count")
    private Integer memberCount;

    @SerializedName( "max_member_count")
    private Integer maxMemberCount;

}
