package cn.evolvefield.mirai.onebot.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class Anonymous {

    @JSONField(name =  "id")
    private long id;

    @JSONField(name =  "name")
    private String name;

    @JSONField(name =  "flag")
    private String flag;

}
