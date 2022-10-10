package cn.evolvefield.mirai.onebot.dto.response;

import cn.evolvefield.mirai.onebot.entity.MsgId;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/10 0:04
 * Version: 1.0
 */
public class MessageResponse extends ActionData<MsgId>{
    public MessageResponse(int messageId){
        this.setStatus("ok");
        this.setRetCode(0);
        this.setData(new MsgId(messageId));
    }
}
