package cn.evolvefield.mirai.onebot.dto.response.guild;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * @author cnlimiter
 */
@Data
public class GuildListResp {

    @JSONField(name = "guild_id")
    private String guildId;

    @JSONField(name = "guild_name")
    private String guildName;

    @JSONField(name = "guild_display_id")
    private String guildDisplayId;

}
