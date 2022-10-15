package cn.evolvefield.mirai.onebot.config

import kotlinx.serialization.Serializable

@Serializable
data class BotConfig (
    var enable: Boolean = true,
    var postMessageFormat: String = "string",
    var wsHost: String = "0.0.0.0",
    var wsPort: Int = 8080,
    var accessToken: String = ""

)

