package cn.evolvefield.mirai.onebot.util;

import com.google.gson.JsonElement;

import java.util.HashMap;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 1:01
 * Version: 1.0
 */
public class ApiParams extends HashMap<String, JsonElement> {
    public ApiParams(){
        super();
    }

    public boolean booleanOrNull(String key){
        return this.get(key).isJsonNull() && this.get(key).getAsBoolean();
    }
}
