package cn.evole.onebot.mirai.util;

import net.mamoe.mirai.data.GroupHonorType;

/**
 * Author cnlimiter
 * CreateTime 2023/5/25 13:49
 * Name TransformUtils
 * Description
 */

public class TransformUtils {


    public static String honor(int honorType){
        switch (honorType){
            case GroupHonorType.TALKATIVE_ID -> {
                return "talkative";
            }
            case GroupHonorType.PERFORMER_ID -> {
                return "performer";
            }
            case GroupHonorType.LEGEND_ID -> {
                return "legend";
            }
            case GroupHonorType.STRONG_NEWBIE_ID -> {
                return "strong_newbie";
            }
            case GroupHonorType.EMOTION_ID -> {
                return "emotion";
            }
            case GroupHonorType.BRONZE_ID -> {
                return "bronz";
            }
            case GroupHonorType.SILVER_ID -> {
                return "silver";
            }
            case GroupHonorType.GOLDEN_ID -> {
                return "golden";
            }
            case GroupHonorType.WHIRLWIND_ID -> {
                return "whirlwind";
            }
            case GroupHonorType.RICHER_ID -> {
                return "richer";
            }
            case GroupHonorType.RED_PACKET_ID -> {
                return "red_packet";
            }
            default -> {
                return "";
            }
        }
    }
}
