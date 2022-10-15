package cn.evolvefield.mirai.onebot.dto.event.notice.group;

import cn.evolvefield.mirai.onebot.dto.event.notice.NoticeEvent;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
public class GroupUploadNoticeEvent extends NoticeEvent {

    @JSONField(name = "group_id")
    private long groupId;

    @JSONField(name = "file")
    private File file;

    /**
     * 文件实体
     */
    @Data
    public static class File {

        @JSONField(name = "id")
        private String id;

        @JSONField(name = "name")
        private String name;

        @JSONField(name = "size")
        private long size;

        @JSONField(name = "busid")
        private long busid;

    }

}
