package cn.evolvefield.mirai.onebot.dto.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/14 17:40
 * Version: 1.0
 */
@Data
public class ActionList<T> {

    @SerializedName( "status")
    private String status;

    @SerializedName( "retcode")
    private int retCode;

    @SerializedName( "data")
    private List<T> data;

}
