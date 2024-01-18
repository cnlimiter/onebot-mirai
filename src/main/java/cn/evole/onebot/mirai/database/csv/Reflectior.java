package cn.evole.onebot.mirai.database.csv;

import java.lang.reflect.Field;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/19 0:44
 * @Description:
 */
public class Reflectior<T> {
    private final Field[] allFields;

    public Reflectior(Class<?> clazz) {
        try {
            allFields = clazz.getDeclaredFields();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String serl(T o) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("\"");
            for (Field f : allFields) {
                if (f.getName().equals("serialVersionUID")) {
                    continue;
                }
                f.setAccessible(true);
                sb.append(f.get(o).toString().replaceAll("\"", "^"));
                sb.append("\",\"");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
            return sb.toString();
        } catch (Exception e) {
            return e.getMessage() + "\n";
        }
    }

    public String title(Localizer l) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("\"");
            for (Field f : allFields) {
                if (f.getName().equals("serialVersionUID")) {
                    continue;
                }
                sb.append(l.transform(f.getName()));
                sb.append("\",\"");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            sb.append("\n");
            return sb.toString();
        } catch (Exception e) {
            return e.getMessage() + "\n";
        }
    }
}
