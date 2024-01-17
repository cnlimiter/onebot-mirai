<div align="center">

<h1>OneBot Mirai</h1> 
欢迎回到Mirai

_✨ 对接[OneBot](https://github.com/howmanybots/onebot/blob/master/README.md)标准的 mirai 插件✨_

</div>
<hr>
<p align="center">
    <a href="https://github.com/cnlimiter/onebot-mirai/issues"><img src="https://img.shields.io/github/issues/cnlimiter/onebot-mirai?style=flat" alt="issues" /></a>
    <a href="https://github.com/cnlimiter/onebot-mirai/blob/master/LICENSE"><img src="https://img.shields.io/badge/license-GPLV3-green" alt="License"/></a>
    <a href="https://github.com/howmanybots/onebot"><img src="https://img.shields.io/badge/OneBot-v11-blue?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABABAMAAABYR2ztAAAAIVBMVEUAAAAAAAADAwMHBwceHh4UFBQNDQ0ZGRkoKCgvLy8iIiLWSdWYAAAAAXRSTlMAQObYZgAAAQVJREFUSMftlM0RgjAQhV+0ATYK6i1Xb+iMd0qgBEqgBEuwBOxU2QDKsjvojQPvkJ/ZL5sXkgWrFirK4MibYUdE3OR2nEpuKz1/q8CdNxNQgthZCXYVLjyoDQftaKuniHHWRnPh2GCUetR2/9HsMAXyUT4/3UHwtQT2AggSCGKeSAsFnxBIOuAggdh3AKTL7pDuCyABcMb0aQP7aM4AnAbc/wHwA5D2wDHTTe56gIIOUA/4YYV2e1sg713PXdZJAuncdZMAGkAukU9OAn40O849+0ornPwT93rphWF0mgAbauUrEOthlX8Zu7P5A6kZyKCJy75hhw1Mgr9RAUvX7A3csGqZegEdniCx30c3agAAAABJRU5ErkJggg==" alt=""></a>
    <a href=""><img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/cnlimiter/onebot-mirai?style=flat&color=abcdef"></a>

</p>


## 声明

- OneBot Mirai 是完全免费且开放源代码的Mirai插件，旨在学习，请勿用于非法用途 

## 兼容性
- OneBot Mirai 兼容 [OneBot-v11](https://github.com/botuniverse/onebot-11) 大部分内容。

### 接口
- [x] 正向 WebSocket
- [x] 反向 WebSocket
- [ ] HTTP API
- [ ] 反向 HTTP POST

### 实现
<details>
<summary>已实现 CQ 码</summary>

#### 符合 OneBot 标准的 CQ 码

| CQ 码         | 功能           |
|--------------|--------------|
| [CQ:face]    | QQ 表情        |
| [CQ:record]  | 语音           |
| [CQ:video]   | 短视频          |
| [CQ:at]      | @某人          |
| [CQ:share]   | 链接分享         |
| [CQ:music]   | 音乐分享 音乐自定义分享 |
| [CQ:reply]   | 回复           |
| [CQ:forward] | 合并转发         |
| [CQ:node]    | 合并转发节点       |
| [CQ:xml]     | XML 消息       |
| [CQ:json]    | JSON 消息      |

</details>

<details>
<summary>已实现 API</summary>

#### 符合 OneBot 标准的 API

| API                      | 功能         |
|--------------------------|------------|
| /send_private_msg        | 发送私聊消息     | 
| /send_group_msg          | 发送群消息      |
| /send_msg                | 发送消息       |
| /get_msg                 | 获取消息       |
| /delete_msg              | 撤回信息       |
| /set_group_kick          | 群组踢人       |
| /set_group_ban           | 群组单人禁言     |
| /set_group_whole_ban     | 群组全员禁言     |
| /set_group_admin         | 群组设置管理员    |
| /set_group_card          | 设置群名片（群备注） |
| /set_group_name          | 设置群名       |
| /get_image               | 获取图片信息     |
| /get_record              | 获取语音       |
| /set_group_leave         | 退出群组       |
| /set_group_special_title | 设置群组专属头衔   |
| /set_friend_add_request  | 处理加好友请求    |
| /set_group_add_request   | 处理加群请求/邀请  |
| /set_essence_msg         | 设置群精华消息    |
| /get_login_info          | 获取登录号信息    |
| /get_stranger_info       | 获取陌生人信息    |
| /get_friend_list         | 获取好友列表     |
| /get_group_info          | 获取群信息      |
| /get_group_list          | 获取群列表      |
| /get_group_member_info   | 获取群成员信息    |
| /get_group_member_list   | 获取群成员列表    |
| /get_group_honor_info    | 获取群荣誉信息    |
| /get_version_info        | 获取版本信息     |
| /get_status              | 获取状态       |

[//]: # (| /can_send_image          | [检查是否可以发送图片]   |)
[//]: # (| /can_send_record         | [检查是否可以发送语音]   |)
[//]: # (| /set_restart             | [重启 go-cqhttp] |)
[//]: # (| /.handle_quick_operation | [对事件执行快速操作]    |)

</details>

<details>
<summary>已实现 Event</summary>

#### 符合 OneBot 标准的 Event（部分 Event 比 OneBot 标准多上报几个字段，不影响使用）

| 事件类型 | Event     |
|------|-----------|
| 消息事件 | 私聊信息      |
| 消息事件 | 群消息       |
| 通知事件 | 群管理员变动    |
| 通知事件 | 群成员减少     |
| 通知事件 | 群成员增加     |
| 通知事件 | 群禁言       |
| 通知事件 | 好友添加      |
| 通知事件 | 群消息撤回     |
| 通知事件 | 好友消息撤回    |
| 通知事件 | 群内戳一戳     |
| 通知事件 | 好友戳一戳     |
| 通知事件 | 群成员荣誉变更   |
| 通知事件 | 群成员名片变更   |
| 通知事件 | 群成员特殊头衔变更 |
| 通知事件 | 其他客户端在线状态 |
| 请求事件 | 加好友请求     |
| 请求事件 | 加群请求邀请    |

[//]: # (| 通知事件 | [群红包运气王]  |)
</details>


## 更新日志:
- [CHANGELOG](https://github.com/cnlimiter/onebot-mirai/blob/master/CHANGELOG.md)

## 开源协议
- [AGPL-3.0](LICENSE) © cnlimiter

## 项目引用
- [OneBot标准](https://github.com/howmanybots/onebot)  
- [onebot-kotlin](https://github.com/yyuueexxiinngg/onebot-kotlin)  -  [LICENSE](https://github.com/yyuueexxiinngg/onebot-kotlin/blob/dev/LICENSE)  
- [mirai-api-http](https://github.com/mamoe/mirai-api-http) -  [LICENSE](https://github.com/mamoe/mirai-api-http/blob/master/LICENSE)  
- [Mirai Native](https://github.com/iTXTech/mirai-native)  -  [LICENSE](https://github.com/iTXTech/mirai-native/blob/master/LICENSE)  
- [CQHTTP](https://github.com/richardchien/coolq-http-api) -  [LICENSE](https://github.com/richardchien/coolq-http-api/blob/master/LICENSE)
- [go-cqhttp](https://github.com/Mrs4s/go-cqhttp)  -  [LICENSE](https://github.com/Mrs4s/go-cqhttp/blob/master/LICENSE)

## 友情链接
兼容 [OneBot-v11 ](https://github.com/howmanybots/onebot)标准的一些项目🥰  

| 项目地址                                                                      | 核心作者           | 备注                  |
|---------------------------------------------------------------------------|----------------|---------------------|
| [MrXiaoM/Overflow](https://github.com/MrXiaoM/Overflow)                   | MrXiaoM        | 实现 mirai 的无缝迁移      |
| [LagrangeDev/Lagrange.Core](https://github.com/LagrangeDev/Lagrange.Core) | NepPure        | C#实现 By Konata.Core |
| [whitechi73/OpenShamrock](https://github.com/whitechi73/OpenShamrock)     | whitechi73     | Xposed框架hook实现      |
| [Hoshinonyaruko/Gensokyo](https://github.com/Hoshinonyaruko/Gensokyo)     | Hoshinonyaruko | 基于官方api 轻量 原生跨平台    |



## 鸣谢

> [IntelliJ IDEA](https://zh.wikipedia.org/zh-hans/IntelliJ_IDEA) 是一个在各个方面都最大程度地提高开发人员的生产力的 IDE，适用于 JVM 平台语言。  
[<img src="https://mikuac.com/images/jetbrains-variant-3.png" width="200"/>](https://www.jetbrains.com/?from=mirai)



## 统计
> 觉得不错给个star吧~  (～￣▽￣)～
 [![Stargazers over time](https://starchart.cc/cnlimiter/onebot-mirai.svg)](https://starchart.cc/cnlimiter/onebot-mirai)
