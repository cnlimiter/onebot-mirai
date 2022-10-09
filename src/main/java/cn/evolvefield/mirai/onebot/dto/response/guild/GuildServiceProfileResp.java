package cn.evolvefield.mirai.onebot.dto.response.guild;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * @author cnlimiter
 */
@Data
public class GuildServiceProfileResp {

    @JSONField(name = "nickname")
    private String nickname;

    @JSONField(name = "tiny_id")
    private String tinyId;

    @JSONField(name = "avatar_url")
    private String avatarUrl;

}
