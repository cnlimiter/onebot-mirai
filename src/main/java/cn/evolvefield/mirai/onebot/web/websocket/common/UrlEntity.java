package cn.evolvefield.mirai.onebot.web.websocket.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 19:44
 * Version: 1.0
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
/*
  处理url
 */
public class UrlEntity {
    String baseUrl;
    Map<String, String> params;
    String url;

    /**
     * 解析url
     */
    public static UrlEntity parse(String url) {
        UrlEntity entity = new UrlEntity();
        entity.setUrl(url);

        if (url == null  || url.trim().length() == 0) {
            return entity;
        }

        String[] urlParts = url.split("\\?");
        entity.baseUrl = urlParts[0];
        //没有参数
        if (urlParts.length == 1) {
            return entity;
        }
        //有参数
        String[] params = urlParts[urlParts.length - 1].split("&");//自动适配参数
        HashMap<String,String> map = new HashMap<>();
        for (String param : params) {
            String[] keyValue = param.split("=");
            map.put(keyValue[0], keyValue[1]);
        }
        entity.params = Collections.unmodifiableMap(map);
        return entity;
    }
}
