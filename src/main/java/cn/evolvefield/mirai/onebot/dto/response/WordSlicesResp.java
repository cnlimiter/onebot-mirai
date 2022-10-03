package cn.evolvefield.mirai.onebot.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * @author cnlimiter
 */
@Data
public class WordSlicesResp {

    @SerializedName( "slices")
    private List<String> slices;

}
