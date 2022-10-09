package cn.evolvefield.mirai.onebot.dto.response;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
public class LoginInfoResp {

    @JSONField(name = "user_id")
    private long userId;

    @JSONField(name = "nickname")
    private String nickname;

}
