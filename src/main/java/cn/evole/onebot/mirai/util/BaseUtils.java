package cn.evole.onebot.mirai.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/11 18:46
 * Version: 1.0
 */
public class BaseUtils {

    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }
}
