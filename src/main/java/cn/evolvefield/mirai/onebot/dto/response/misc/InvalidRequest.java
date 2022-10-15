package cn.evolvefield.mirai.onebot.dto.response.misc;

import cn.evolvefield.mirai.onebot.dto.response.ActionData;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 2:33
 * Version: 1.0
 */
public class InvalidRequest extends ActionData<String> {
    public InvalidRequest(){
        this.setStatus("failed");
        this.setRetCode(100);
        this.setData("参数错误");
    }
}
