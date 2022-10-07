package cn.evolvefield.mirai.onebot.util;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.CRC32;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/2 22:31
 * Version: 1.0
 */
public class DataBaseUtils {

    public static int toMessageId(int[] from, long botId, long contactId){
        var crc = new CRC32();
        String messageId = botId + "-" + contactId;
        crc.update(messageId.getBytes());
        return (int) crc.getValue();
    }

//    public static String toUHexString(byte[] array, String separator){
//        int offset = 0;
//        int length = array.length -offset;
//        if (length == 0) {
//            return "";
//        }
//        var lastIndex = offset + length;
//        Stream.of(array)..forEach(array1 -> {
//            if ( in offset until lastIndex) {
//                var ret = it.toUByte().toString(16).uppercase()
//                if (ret.length == 1) ret = "0$ret"
//                append(ret)
//                if (index < lastIndex - 1) append(separator)
//            }
//        });
//        return new StringBuilder(length * 2) {
//
//            this@toUHexString.forEachIndexed { index, it ->
//                if (index in offset until lastIndex) {
//                    var ret = it.toUByte().toString(16).uppercase()
//                    if (ret.length == 1) ret = "0$ret"
//                    append(ret)
//                    if (index < lastIndex - 1) append(separator)
//                }
//            }
//        }
//    }
}
