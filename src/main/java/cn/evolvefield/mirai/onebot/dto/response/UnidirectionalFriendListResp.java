package cn.evolvefield.mirai.onebot.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author cnlimiter
 */
@Data
public class UnidirectionalFriendListResp {

    @SerializedName( "user_id")
    private long userId;

    @SerializedName( "nickname")
    private String nickname;

    @SerializedName( "source")
    private String source;

}
