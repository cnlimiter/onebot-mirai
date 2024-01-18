package cn.evole.onebot.mirai.database.csv;

import java.util.HashMap;
import java.util.Map;
/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/19 0:44
 * @Description:
 */
@SuppressWarnings("unused")
public class Localizer {
    private final Map<String, String> map = new HashMap<>();

    public void add(String from, String to) {
        map.put(from, to);
    }

    public String transform(String str) {
        if (map.containsKey(str)) {
            return map.get(str);
        }
        return str;
    }
}
