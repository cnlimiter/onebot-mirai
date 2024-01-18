package cn.evole.onebot.mirai.database.internal;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.database.Transfer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/19 0:44
 * @Description:
 */
@SuppressWarnings("IOStreamConstructor")
public class MapSerl<K, V> {
    public void toFile(String f, Map<K, V> m) throws Exception {
        Set<K> Entities = m.keySet();
        Object[] KeyStorage = new Object[m.size()];
        Object[] ValueStorage = new Object[m.size()];
        int i = 0;
        for (K now : Entities) {
            KeyStorage[i] = now;
            ValueStorage[i++] = m.get(now);
        }
        SerlItem item = new SerlItem(KeyStorage, ValueStorage);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        oos.writeObject(item);
        oos.close();
    }

    @SuppressWarnings("unchecked")
    public ConcurrentHashMap<K, V> fromFile(String f, Class<?> clazz) throws Exception {
        boolean changed = false;
        ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
        SerlItem item = (SerlItem) ois.readObject();
        for (int i = 0; i < item.key.length; i++) {
            if (clazz != null) {
                if (item.value[i].getClass().getName().equals(clazz.getName())) {
                    map.put((K) item.key[i], (V) item.value[i]);
                } else {
                    if (!changed) {
                        changed = true;
                        OneBotMirai.logger.warning("Convert the database.");
                        OneBotMirai.logger.warning("This operation is not reversible.");
                        OneBotMirai.logger.warning("Cast " + item.value[i].getClass().getName() + " -> " + clazz.getName());
                    }
                    map.put((K) item.key[i], (V) Transfer.copy(item.value[i], clazz.getConstructor().newInstance()));
                }
            } else {
                map.put((K) item.key[i], (V) item.value[i]);
            }
        }
        ois.close();
        return map;
    }
}
