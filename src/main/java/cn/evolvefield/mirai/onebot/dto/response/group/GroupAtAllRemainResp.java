package cn.evolvefield.mirai.onebot.dto.response.group;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
public class GroupAtAllRemainResp {

    /**
     * 是否可以 @全体成员
     */
    @JSONField(name = "can_at_all")
    private boolean canAtAll;

    /**
     * 群内所有管理当天剩余 @全体成员 次数
     */
    @JSONField(name = "remain_at_all_count_for_group")
    private int remainAtAllCountForGroup;

    /**
     * Bot 当天剩余 @全体成员 次数
     */
    @JSONField(name = "remain_at_all_count_for_uin")
    private int remainAtAllCountForUin;

}
