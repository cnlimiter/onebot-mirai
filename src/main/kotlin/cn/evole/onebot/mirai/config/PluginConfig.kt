package cn.evole.onebot.mirai.config

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object PluginConfig : AutoSavePluginConfig("setting"){
    val bots: MutableMap<String, BotConfig>? by value(mutableMapOf("12345654321" to BotConfig()))

    @Serializable
    data class BotConfig(
        var cacheImage: Boolean = false,
        var cacheRecord: Boolean = false,
        var heartbeat: HeartbeatConfig = HeartbeatConfig(),
        var http: HTTPConfig = HTTPConfig(),
        @SerialName("ws_reverse")
        var wsReverse: MutableList<WSReverseConfig> = mutableListOf(WSReverseConfig()),
        var ws: WSConfig = WSConfig()
    )

    @Serializable
    data class DBSettings(
        var enable: Boolean = true,
    )

    @Serializable
    data class HeartbeatConfig(
        var enable: Boolean = false,
        var interval: Long = 1500L
    )

    @Serializable
    data class HTTPConfig(
        var enable: Boolean = false,
        var host: String = "0.0.0.0",
        var port: Int = 6700,
        var accessToken: String = "",
        var postUrl: String = "",
        var postMessageFormat: String = "string",
        var secret: String = "",
        var timeout: Long = 0L
    )

    @Serializable
    data class WSReverseConfig(
        var enable: Boolean = false,
        var postMessageFormat: String = "string",
        var reverseHost: String = "127.0.0.1",
        var reversePort: Int = 8090,
        var accessToken: String = "",
        var reversePath: String = "/ws",
        var reverseApiPath: String = "/api",
        var reverseEventPath: String = "/event",
        var useUniversal: Boolean = true,
        var useTLS: Boolean = false,
        var reconnectInterval: Long = 3000L,
    )

    @Serializable
    data class WSConfig(
        var enable: Boolean = false,
        var postMessageFormat: String = "string",
        var wsHost: String = "0.0.0.0",
        var wsPort: Int = 8080,
        var accessToken: String = ""
    )
}