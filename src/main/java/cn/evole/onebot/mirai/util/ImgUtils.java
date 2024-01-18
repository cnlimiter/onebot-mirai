package cn.evole.onebot.mirai.util;

import lombok.val;
import net.mamoe.mirai.message.data.Image;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;

import static cn.evole.onebot.mirai.OneBotMirai.logger;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/17 12:55
 * @Description:
 */

public class ImgUtils {

    public static String constructCacheImageMeta(String md5, long size, String url, String imageType){
        return """
                [image]
                md5=%s
                size=%s
                url=%s
                addtime=%s
                type=%s
            """.formatted(md5, size, url, System.currentTimeMillis(), imageType);
    }


    public static String getImageType(Image image){
        val parts = image.getImageId().split("\\.", 2);
        if (parts.length == 2){
            return parts[1];
        }
        else {
            return "unknown";
        }
    }

    public static String getImageType(byte[] bytes){
        byte[] b1 = new byte[8];
        System.arraycopy(bytes, 0, b1, 0, 8);
        switch (BaseUtils.bytesToString(b1)){
            case ("FFD8") -> {
                return  "jpg";
            }
            case("89504E47") ->  {
                return  "png";
            }
            case("47494638") -> {
                return  "gif";
            }
            case("424D") -> {
                return  "bmp";
            }
            case("52494646") -> {
                return  "webp";
            }
            default -> {
                return  "unknown";
            }
        }
    }



    public record CachedImage(File file, String fileName, String path,
                       String md5, int size, String url,
                       long addTime, @Nullable String imageType){}


    public static CachedImage getCachedImageFile(String name){
        CachedImage cacheImage = null;
        File cacheFile = null;
        if (name.endsWith(".cqimg")){
            cacheFile = BaseUtils.getDataFile("image", name);
        }
        else if (name.endsWith(".image")){
            cacheFile = BaseUtils.getDataFile("image", name.toLowerCase(Locale.ROOT));
        }
        else {
            cacheFile = BaseUtils.getDataFile("image", name + ".cqimg") == null ?
            BaseUtils.getDataFile(
                    "image",
                    name.toLowerCase(Locale.ROOT) + ".image"
            ) : BaseUtils.getDataFile("image", name + ".cqimg");
        }

        if (cacheFile != null){
            if (cacheFile.canRead()) {
                logger.info("此链接图片已缓存, 如需删除缓存请至 " + cacheFile.getAbsolutePath());
                final String[] md5 = {""};
                final int[] size = {0};
                final String[] url = {""};
                final long[] addTime = {0L};
                final String[] imageType = {null};

                switch (
                        StringUtils.substringBeforeLast(cacheFile.getName(), ".")
                        ){
                    case "cqimg" -> {
                        try {
                            val cacheMediaContent = Files.readAllLines(cacheFile.toPath());
                            cacheMediaContent.forEach(
                                    s -> {
                                        val parts = s.trim().split("=", 2);
                                        if (parts.length == 2) {
                                            switch (parts[0]){
                                                case "md5" -> md5[0] = parts[1];
                                                case "size" -> size[0] = parts[1] != null ? Integer.parseInt(parts[1]) : 0;
                                                case "url" -> url[0] = parts[1];
                                                case "addtime" -> addTime[0] = parts[1] != null ? Long.parseLong(parts[1]) : 0L;
                                                case "type" -> imageType[0] = parts[1];
                                            }

                                        }
                                    }

                            );
                        }
                        catch (IOException ignored){}

                    }
                    case "image" -> {
                        try {
                            val bytes = Files.readAllBytes(cacheFile.toPath());
                            byte[] b1 = new byte[16];
                            System.arraycopy(bytes, 0, b1, 0, 16);
                            md5[0] = BaseUtils.bytesToHexString(b1);

                            byte[] b2 = new byte[4];
                            System.arraycopy(bytes, 16, b2, 0, 4);
                            size[0] = BaseUtils.byteToInt(b2);

                            url[0] = "https://c2cpicdw.qpic.cn/offpic_new//0/0-00-$md5/0?term=2";
                        }
                        catch (IOException ignored){}
                    }
                }


                if (md5[0] != "" && size[0] != 0) {
                    cacheImage = new CachedImage(
                            cacheFile,
                            name,
                            cacheFile.getAbsolutePath(),
                            md5[0],
                            size[0],
                            url[0],
                            addTime[0],
                            imageType[0]
                    );
                } else { // If cache file corrupted
                    cacheFile.delete();
                }
            } else {
                logger.error("Image "+ name +" cache file cannot read.");
            }
        } else {
            logger.info("Image "+ name +" cache file cannot be found.");
        }
        return cacheImage;
    }


}
