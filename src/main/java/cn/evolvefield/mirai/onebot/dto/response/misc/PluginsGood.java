package cn.evolvefield.mirai.onebot.dto.response.misc;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/12 14:04
 * Version: 1.0
 */
@Data
public class PluginsGood {
   @JSONField
   boolean websocket  = true;
   @JSONField
   boolean eventDataPatcher  = true;

}
