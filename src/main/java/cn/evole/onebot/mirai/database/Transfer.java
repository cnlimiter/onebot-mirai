package cn.evole.onebot.mirai.database;

import java.lang.reflect.Field;

public class Transfer {
    public static Object copy(Object from, Object to) {
        Class<?> fc = from.getClass();
        Class<?> tc = to.getClass();
        Field[] ff = fc.getDeclaredFields();
        for (Field f : ff) {
            try {
                Field tf;
                tf = tc.getDeclaredField(f.getName());
                f.setAccessible(true);
                tf.setAccessible(true);
                //noinspection ConstantValue
                if (tf.getClass().isAssignableFrom(f.getClass())) {
                    tf.set(to, f.get(from));
                } else {
                    throw new RuntimeException("Convert error:" + f.getName());
                }
            } catch (Exception e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
            }
        }
        return to;
    }
}
