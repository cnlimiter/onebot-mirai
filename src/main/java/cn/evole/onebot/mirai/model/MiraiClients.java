package cn.evole.onebot.mirai.model;

import cn.evole.onebot.sdk.event.notice.misc.OtherClientStatusNoticeEvent;
import net.mamoe.mirai.contact.OtherClientInfo;
import net.mamoe.mirai.contact.Platform;

import java.util.Objects;

/**
 * Author cnlimiter
 * CreateTime 2023/5/25 10:56
 * Name MiraiClient
 * Description
 */

public class MiraiClients extends OtherClientStatusNoticeEvent.Clients {
    public MiraiClients(OtherClientInfo info) {
        this.setPlatform(((Platform) Objects.requireNonNull(info.getPlatform())).name().toLowerCase());
        this.setAppId((long) info.getAppId());
        this.setDeviceName(info.getDeviceName());
        this.setDeviceKind(info.getDeviceKind());
    }
}
