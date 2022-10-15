package cn.evolvefield.mirai.onebot.dto.event.notice.misc;

import cn.evolvefield.mirai.onebot.dto.event.notice.NoticeEvent;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import net.mamoe.mirai.contact.OtherClientInfo;

import java.util.Objects;

/**
 * Created on 2022/7/8.
 *
 * @author cnlimiter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
public class OtherClientStatusNoticeEvent extends NoticeEvent {

    @JSONField(name = "online")
    private boolean online;

    @JSONField(name = "client")
    private Clients client;

    /**
     * 设备对象
     */
    @Data
    public static class Clients {

        public Clients(OtherClientInfo info){
            this.platform = Objects.requireNonNull(info.getPlatform()).name().toLowerCase();
            this.appId = info.getAppId();
            this.deviceName = info.getDeviceName();
            this.deviceKind = info.getDeviceKind();
        }

        @JSONField(name = "app_id")
        private long appId;

        @JSONField(name = "platform")
        private String platform;

        @JSONField(name = "device_name")
        private String deviceName;

        @JSONField(name = "device_kind")
        private String deviceKind;

    }

}
