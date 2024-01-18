package cn.evole.onebot.mirai.util;

import cn.evole.onebot.mirai.OneBotMirai;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/11 18:46
 * Version: 1.0
 */
public class BaseUtils {

    public static void safeRun(Runnable runnable){
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.schedule(runnable, 0,  TimeUnit.SECONDS);
    }
    public static <T> T safeRun(Callable<T> callable, T defaultValue){
        try {
            ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
            service.schedule(callable, 0,  TimeUnit.SECONDS);
            return callable.call();
        }
        catch (Exception ignored){}
        return defaultValue;
    }



    public static <T> List<T> copyIterator(Iterator<T> iter) {
        List<T> copy = new ArrayList<T>();
        while (iter.hasNext())
            copy.add(iter.next());
        return copy;
    }

    public static File getDataFile(String type, String name){
        val miraiDataFolder = new File(OneBotMirai.INSTANCE.getDataFolder(), type).getAbsolutePath() + File.separatorChar;
        val localDataFolder = "data" + File.separatorChar + type + File.separatorChar;
        val systemDataFolder = System.getProperty("java.library.path")
                .split(";")[0] + File.separatorChar + "data" + File.separatorChar + type + File.separatorChar;
        if (new File(miraiDataFolder + name).getAbsoluteFile().exists()){
            return new File(miraiDataFolder + name).getAbsoluteFile();
        }
        else if ( new File(localDataFolder + name).getAbsoluteFile().exists()){
            return new File(localDataFolder + name).getAbsoluteFile();

        }
        else if (new File(systemDataFolder + name).getAbsoluteFile().exists()){
            return new File(systemDataFolder + name).getAbsoluteFile();

        }
        else {
            return null;
        }
    }

    /**
     * 更加高效
     * @param bytes 数据
     * @return 字符
     */
    public static String bytesToHexString(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();//通过对一个只读数组hexArray进行读取来实现
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String bytesToString(byte[] str) {
        String keyword = null;
        try {
            keyword = new String(str, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return keyword;
    }


    public static int byteToInt(byte[] b) {
        int s = 0;
        int s0 = b[0] & 0xff;// 最低位
        int s1 = b[1] & 0xff;
        int s2 = b[2] & 0xff;
        int s3 = b[3] & 0xff;
        s3 <<= 24;
        s2 <<= 16;
        s1 <<= 8;
        s = s0 | s1 | s2 | s3;
        return s;
    }


}
