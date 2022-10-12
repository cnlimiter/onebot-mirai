package cn.evolvefield.mirai.onebot.dto.response;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/9/14 17:40
 * Version: 1.0
 */
@Data
public class ActionList<T>{

    @JSONField(name = "status")
    private String status;

    @JSONField(name = "retcode")
    private int retCode;

    @JSONField(name = "data")
    private LinkedList<T> data;

}
