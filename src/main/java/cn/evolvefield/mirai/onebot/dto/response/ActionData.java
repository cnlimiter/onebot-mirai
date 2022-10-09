package cn.evolvefield.mirai.onebot.dto.response;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/13 22:05
 * Version: 1.0
 */
@Data
public class ActionData<T> {

    @JSONField(name = "status")
    private String status;
    @JSONField(name = "retcode")
    private int retCode;
    @JSONField(name = "data")
    private T data;
    @JSONField(name = "echo")
    private String echo;
}
