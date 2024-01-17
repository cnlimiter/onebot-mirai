package cn.evole.onebot.mirai.util;

import java.io.*;
import java.net.URL;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.evole.onebot.mirai.util.StringUtils.isEmpty;


/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/17 22:20
 * @Description:
 */

public class HttpUtils {
    public static ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(8, 8,
            10L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>());

    public static ExecutorService EXECUTOR_SERVICE1 = new ThreadPoolExecutor(1, 1,
            10L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    /**
     * 从网络上获取字符
     *
     * @param k   忽略 #开头的文字
     * @param url 网址
     * @return 字符
     */
    public static String getStringFromHttpUrl(boolean k, String url) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openConnection().getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                if (k && line.trim().startsWith("#"))
                    continue;
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从网络上获取资源
     *
     * @param url
     * @return
     */
    public static String getStringFromHttpUrl(String url) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从网络上获取bytes
     *
     * @param url 网址
     * @return byte数组
     */
    public static byte[] getBytesFromHttpUrl(String url) {
        try {
            InputStream is = new URL(url).openStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024 * 1024];
            int len = -1;
            while ((len = is.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 下载文件
     *
     * @param urlStr   网络链接
     * @param fileName 文件名
     */
    public static void downloadFile(String urlStr, String fileName) {
        EXECUTOR_SERVICE.execute(() -> {
            try {
                if (isEmpty(urlStr) || isEmpty(fileName)) return;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                startDownload(urlStr, baos);
                File file = new File(fileName);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 下载文件
     *
     * @param urlStr 网络链接
     * @param file   文件
     */
    public static void downloadFile(String urlStr, File file) {
        EXECUTOR_SERVICE.execute(() -> {
            try {
                if (file == null || isEmpty(urlStr)) return;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                startDownload(urlStr, baos);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baos.toByteArray());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void startDownload(String urlStr, ByteArrayOutputStream baos) throws IOException {
        InputStream is = new URL(urlStr).openStream();
        byte[] bytes = new byte[1024 * 1024];
        int len = -1;
        while ((len = is.read(bytes)) != -1) {
            baos.write(bytes, 0, len);
        }
        is.close();
    }

    private static final Pattern PATTERN = Pattern.compile("<title>.+</title>");

    /**
     * @param url
     * @param title default value
     * @return
     * @throws IOException
     */
    public static String getTitle(String url, String title) throws IOException {
        String s = getStringFromHttpUrl(url);
        Matcher matcher = PATTERN.matcher(s);
        if (matcher.find()) {
            s = matcher.group();
            s = s.substring(7, s.length() - 8);
            return s;
        }
        return title;
    }
}
