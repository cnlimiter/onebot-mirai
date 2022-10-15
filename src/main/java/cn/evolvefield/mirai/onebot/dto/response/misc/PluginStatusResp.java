package cn.evolvefield.mirai.onebot.dto.response.misc;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/11 19:58
 * Version: 1.0
 */
@Data
public class PluginStatusResp {
    @JSONField
    boolean app_initialized =  true;
    @JSONField boolean app_enabled =  true;
    PluginsGood plugins_good = new PluginsGood();
    @JSONField boolean app_good =  true;
    @JSONField boolean online =  true;
    @JSONField boolean good = true;
}
