package cn.evole.onebot.mirai.database;

import cn.evole.onebot.mirai.database.csv.Localizer;
import cn.evole.onebot.mirai.database.csv.Reflectior;
import cn.evole.onebot.mirai.database.internal.MapSerl;

import java.io.File;
import java.io.StreamCorruptedException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/19 0:44
 * @Description: 高性能、微型、伪数据库
 */

@SuppressWarnings({"unused", "BusyWait", "ResultOfMethodCallIgnored"})
public class NanoDb<K, V> {
    private final long save;
    private final int buffer;
    private final MapSerl<K, V> provider = new MapSerl<>();
    private final ConcurrentHashMap<K, V> data;
    private final String file;
    private final AtomicBoolean locked = new AtomicBoolean(false);
    private long lastSave = System.currentTimeMillis();
    private int operations = 0;
    public static final String BOM = "\ufeff";

    public NanoDb(String datafile) throws Exception {
        this(datafile, 60000L, 10, null);
    }

    public NanoDb(String datafile, long saveInterval, int bufferSize) throws Exception {
        this(datafile, saveInterval, bufferSize, null);
    }

    public NanoDb(String datafile, Class<?> classV) throws Exception {
        this(datafile, 60000L, 10, classV);
    }

    public NanoDb(String datafile, long saveInterval, int bufferSize, Class<?> classV) throws Exception {
        save = saveInterval;
        buffer = bufferSize;
        file = datafile;
        if (new File(file + "_").exists()) {
            throw new StreamCorruptedException("A unsaved backup is found!");
        }
        if (!new File(file).exists()) {
            data = new ConcurrentHashMap<>();
        } else {
            data = provider.fromFile(datafile, classV);
        }
    }

    public boolean contains(K key) {
        return data.containsKey(key);
    }

    public V query(K key) {
        return data.get(key);
    }

    public void remove(K key) {
        scheduleSave();
        data.remove(key);
    }

    public void set(K key, V value) {
        scheduleSave();
        data.put(key, value);
    }

    public int size() {
        return this.data.size();
    }

    // Attention!
    // Concurrent means that if an operation contains no more than one K-V, you may consider it thread-safe.
    // However, when you need to iterate the whole database, you need to LOCK it!
    // There is an example in toCSV.
    // Also, if you're sure that there's only one thread, you can ignore this.

    public Set<K> list() {
        return this.data.keySet();
    }

    public String toCSV(Class<?> CBase, String indexName, Localizer l) {
        StringBuilder sb = new StringBuilder();
        Reflectior<V> ref = new Reflectior<>(CBase);
        waitAndLock();
        Set<K> index = this.list();
        sb.append(BOM);
        sb.append("\"").append(indexName).append("\",");
        sb.append(ref.title(l));
        for (K now : index) {
            sb.append("\"").append(now.toString()).append("\",");
            sb.append(ref.serl(this.query(now)));
        }
        locked.set(false);
        return sb.toString();
    }

    private void scheduleSave() {
        while (locked.get()) {
            try {
                Thread.sleep(1L);
            } catch (Exception ignored) {
            }
        }
        operations++;
        if (operations >= buffer && System.currentTimeMillis() - lastSave >= save) {
            save();
        }
    }

    public void save() {
        if (!locked.get()) {
            locked.set(true);
            try {
                lastSave = System.currentTimeMillis();
                operations = 0;
                provider.toFile(file + "_", data);
                File original = new File(file);
                File temp = new File(file + "_");
                original.delete();
                temp.renameTo(original);
            } catch (Exception e) {
                e.printStackTrace();
            }
            locked.set(false);
        }
    }

    public boolean isLocked() {
        return locked.get();
    }

    public void waitAndLock() {
        while (!lock()) {
            try {
                Thread.sleep(1L);
            } catch (Exception ignored) {
            }
        }
    }

    public boolean lock() {
        if (locked.get()) {
            return false;
        }
        locked.set(true);
        return true;
    }

    public boolean unlock() {
        return locked.getAndSet(false);
    }
}
