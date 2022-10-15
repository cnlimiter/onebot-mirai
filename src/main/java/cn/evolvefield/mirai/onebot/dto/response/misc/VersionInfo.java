package cn.evolvefield.mirai.onebot.dto.response.misc;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/12 13:59
 * Version: 1.0
 */
@Data
public class VersionInfo {

    @JSONField String coolq_directory = "";
    @JSONField String coolq_edition = "pro";
    @JSONField String plugin_version = "4.15.0";
    @JSONField int plugin_build_number = 99;
    @JSONField String plugin_build_configuration = "release";
    @JSONField String app_name = "onebot-mirai";
    @JSONField String app_version = JvmPluginDescription.loadFromResource().getVersion().toString();
    @JSONField String app_build_version = JvmPluginDescription.loadFromResource().getVersion().toString();
    @JSONField String protocol_version = "v11";
}
