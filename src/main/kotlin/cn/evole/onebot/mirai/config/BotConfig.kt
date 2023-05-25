package cn.evole.onebot.mirai.config

import kotlinx.serialization.Serializable

@Serializable
data class BotConfig (
    val enable: Boolean = true,
    val postMessageFormat: String = "string",
    val wsHost: String = "0.0.0.0",
    val wsPort: Int = 8080,
    val accessToken: String = ""

)

