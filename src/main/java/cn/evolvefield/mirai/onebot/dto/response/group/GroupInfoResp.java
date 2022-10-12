package cn.evolvefield.mirai.onebot.dto.response.group;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupInfoResp extends GroupDataResp{

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
