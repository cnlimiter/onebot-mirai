package cn.evolvefield.mirai.onebot.dto.response.common;

import cn.evolvefield.mirai.onebot.dto.response.ActionData;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/10 15:02
 * Version: 1.0
 */
public class GeneralSuccess extends ActionData<Object>{
    public GeneralSuccess(){
        this.setStatus("ok");
        this.setRetCode(0);
        this.setData(null);
    }
}
