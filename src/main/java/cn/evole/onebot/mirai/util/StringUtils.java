package cn.evole.onebot.mirai.util;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/17 22:22
 * @Description:
 */

public class StringUtils {
    /**
     * 判断不是空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return !(str == null || str.trim().isEmpty());
    }

    /**
     * 判断是空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.trim().isEmpty());
    }

    /**
     * 判断字符数组不是空
     *
     * @param sss
     * @return
     */
    public static boolean isNotEmpty(String... sss) {
        for (String str : sss) {
            if (str == null || str.trim().isEmpty()) return false;
        }
        return true;
    }

    /**
     * 判断字符组 其中有空
     *
     * @param sss
     * @return
     */
    public static boolean isEmpty(String... sss) {
        for (String str : sss) {
            if (str == null || str.trim().isEmpty()) return true;
        }
        return false;
    }

    /**
     * 判断不是Null
     *
     * @param objects
     * @return 返回 true 代表 全部不是Null
     */
    public static boolean isNotNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) return false;
            if (o.getClass().isArray()) {
                if (!isNotNull((Object[]) o)) return false;
            }
        }
        return true;
    }


    public static String substringBeforeLast(String value, String split){
       return value.substring(value.lastIndexOf(split)+1);
    }
}
