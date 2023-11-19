package cn.evole.onebot.mirai.util;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.sdk.util.DataBaseUtils;
import com.google.gson.JsonObject;
import kotlin.NotImplementedError;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 2:43
 * Version: 1.0
 */
public class OnebotMsgParser {


    private static final PlainText MSG_EMPTY = new PlainText("");

    public static MessageChain messageToMiraiMessageChains(Bot bot, Contact contact, Object message, boolean raw){
        if (message instanceof String s){
            return new MessageChainBuilder().append(new PlainText(s)).build();
        }
        else if (message instanceof JsonObject jsonObject) {
            try {
                var data = jsonObject.getAsJsonObject("data");
                if (jsonObject.has("type")){
                    if (data.asMap().containsKey("text"))
                        return new MessageChainBuilder().append(new PlainText(data.get("text").getAsString())).build();
                    else return new MessageChainBuilder().append(textToMessageInternal(bot, contact, message)).build();
                }
            } catch (NullPointerException e) {
                OneBotMirai.logger.warning("Got null when parsing CQ message object");
                return null;
            }
        }
        return null;
    }

     public static String toCQString(SingleMessage message){
        if (message instanceof PlainText text) return escape(text.getContent());

        else if (message instanceof At at) return "[CQ:at,qq=" +at.getTarget() + "]";

        else if (message instanceof Face face) return "[CQ:face,id=" + face.getId() + "]";

        else if (message instanceof VipFace vipFace) return "[CQ:vipface,id="+vipFace.getKind().getId()+",name=" +vipFace.getKind().getName()+ ",count=" +vipFace.getCount()+ "]";

        else if (message instanceof PokeMessage pokeMessage) return "[CQ:poke,id=" +pokeMessage.getId()+ ",type= "+pokeMessage.getPokeType()+" ,name="+pokeMessage.getName()+"]";

        else if (message instanceof AtAll all) return "[CQ:at,qq=all]";

        else if (message instanceof Image image) return "[CQ:image,file="+image.getImageId()+",url="+escape(Image.queryUrl(image))+"]";

        else if (message instanceof FlashImage flashImage) return "[CQ:image,file="+flashImage.getImage().getImageId()+",url="+escape(Image.queryUrl(flashImage.getImage()))+",type=flash]";

        else if (message instanceof ServiceMessage serviceMessage){
            if (serviceMessage.getContent().contains("xml version")) return "[CQ:xml,data="+escape(serviceMessage.getContent())+"]";
            else return "[CQ:json,data="+escape(serviceMessage.getContent())+"]";
        }
        else if (message instanceof LightApp app) return "[CQ:json,data="+escape(app.getContent())+"]";

        else if (message instanceof MessageSource) return "";

        else if (message instanceof QuoteReply quoteReply) return "[CQ:reply,id="+ DataBaseUtils.toMessageId(quoteReply.getSource().getInternalIds(), quoteReply.getSource().getBotId(), quoteReply.getSource().getFromId())+"]";

        else if (message instanceof OnlineAudio audio) return "[CQ:record,url="+escape(audio.getUrlForDownload())+",file="+ Arrays.toString(audio.getFileMd5()) +"]";

        else if (message instanceof Audio audio) return "[CQ:record,url=,file="+ Arrays.toString(audio.getFileMd5()) +"]";

        else return "此处消息的转义尚未被插件支持";

    }




//    private static MessageChain codeToChain(Bot bot, String message, Contact contact){
//        AtomicReference<Message> msg = new AtomicReference<>();
//        PlainText text = new PlainText("");
//        if (message.contains("[CQ:")) {
//            final boolean[] interpreting = {false};
//            var sb = new StringBuilder();
//            final int[] index = {0};
//            List.of(message).forEach(
//                    c -> {
//                        if ("[".equals(c)) {
//                            if (interpreting[0]) {
//                                OneBotMirai.logger.error(String.format("CQ消息解析失败：%s，索引：%s", message, Arrays.toString(index)));
//                                return;
//                            } else {
//                                interpreting[0] = true;
//                                if (!sb.isEmpty()) {
//                                    var lastMsg = sb.toString();
//                                    sb.delete(0, sb.length());
//                                    msg.set(textToMessageInternal(bot, contact, lastMsg));
//                                }
//                                sb.append(c);
//                            }
//                        } else if ("]".equals(c)) {
//                            if (!interpreting[0]) {
//                                OneBotMirai.logger.error(String.format("CQ消息解析失败：%s，索引：%s", message, Arrays.toString(index)));
//                                return;
//                            } else {
//                                interpreting[0] = false;
//                                sb.append(c);
//                                if (!sb.isEmpty()) {
//                                    var lastMsg = sb.toString();
//                                    sb.delete(0, sb.length());
//                                    msg.set(textToMessageInternal(bot, contact, lastMsg));
//                                }
//                            }
//                        } else {
//                            sb.append(c);
//                        }
//                        index[0]++;
//                    }
//            );
//            if (!sb.isEmpty()) {
//               msg.set(textToMessageInternal(bot, contact, sb.toString()));
//            }
//        } else {
//          text = new PlainText(unescape(message));
//        }
//        return new MessageChainBuilder()
//                .append(msg.get())
//                .append(text)
//                .build();
//    }
//

    private static String escape(String msg){
        return msg.replace("&", "&amp;")
                .replace("[", "&#91;")
                .replace("]", "&#93;")
                .replace(",", "&#44;");
    }

    private static String unescape(String msg){
        return msg.replace("&amp;", "&")
                .replace("&#91;", "[")
                .replace("&#93;", "]")
                .replace("&#44;", ",");
    }

    private static HashMap<String, String> toMap(String msg){
        var map = new HashMap<String, String>();
        Arrays.stream(msg.split(",")).forEach(
                s -> {
                    var parts = s.split("=",  2);
                    map.put(parts[0].trim(), unescape(parts[1]));
                }

        );
        return map;
    }

    private static Message textToMessageInternal(Bot bot, Contact contact, Object message){
        if (message instanceof String msg){
            if (msg.startsWith("[CQ:") && msg.endsWith("]")) {
                var parts = msg.substring(4, msg.length() - 1).split(",",  2);

                HashMap<String, String> args;
                if (parts.length == 2) {
                    args = toMap(parts[1]);
                } else {
                   args = new HashMap<>();
                }
                return convertToMiraiMessage(bot, contact, parts[0], args);
            }
            return new PlainText(unescape(msg));
        }
        else if (message instanceof JsonObject jsonObject){
            var type = jsonObject.get("type").getAsString();
            JsonObject data = jsonObject.getAsJsonObject("data");
            Map<String, String> args = new HashMap<>();
            data.asMap().forEach((s, o) -> args.put(s, o.isJsonNull()? "": o.getAsString()));
            return convertToMiraiMessage(bot, contact, type, args);
        }
        else return MSG_EMPTY;
    }


    private static Message convertToMiraiMessage(Bot bot, Contact contact, String type, Map<String, String> args){
        switch (type) {
            case "at" -> {
                if ("all".equals(args.get("qq"))) {
                    return AtAll.INSTANCE;
                } else {
                    if (contact instanceof Group) {
                        OneBotMirai.logger.debug("不能在私聊中发送 At。");
                        return MSG_EMPTY;
                    } else {
                        var member = contact.getBot().getFriend(Long.parseLong(args.get("qq")));
                        if (member == null) {
                            OneBotMirai.logger.debug(String.format("无法找到群员：%s", args.get("qq")));
                            return MSG_EMPTY;
                        } else {
                           return new At(member.getId());
                        }
                    }
                }
            }
            case "face" -> {
                return new Face(Integer.parseInt(args.get("id")));
            }
            case "emoji" -> {
                return new PlainText(new String(Character.toChars(Integer.parseInt(args.get("id")))));
            }
            case "image" -> {
                String file = args.get("file");
                Message message = constuctImageMsg(file, contact);
                OneBotMirai.logger.info(String.format("Image: %s", message));
                return message;
            }
            case "share" -> {
                return RichMessage.share(
                        args.get("url"),
                        args.get("title"),
                        args.get("content"),
                        args.get("image")
                );
            }
            case "record" -> {}
            case "contact" -> {
                if ("qq".equals(args.get("type"))) {
                    //return  RichMessageHelper.contactQQ(bot, args["id"]!!.toLong())
                } else {
                    //return RichMessageHelper.contactGroup(bot, args["id"]!!.toLong())
                }

            }
            case "music" -> {
//                switch (args.get("type")){
//                    case "qq" -> { }
//                }
//                return when (args["type"]) {
//                    "qq" -> QQMusic.send(args["id"]!!)
//                    "163" -> NeteaseMusic.send(args["id"]!!)
//                    "custom" -> Music.custom(
//                            args["url"]!!,
//                            args["audio"]!!,
//                            args["title"]!!,
//                            args["content"],
//                            args["image"]
//                )
//                else -> throw IllegalArgumentException("Custom music share not supported anymore")
//                }
            }
            case "shake" -> {return PokeMessage.ChuoYiChuo;}
            case "poke" -> {
                return Arrays.stream(PokeMessage.values).filter(
                        pokeMessage -> pokeMessage.getPokeType() == Integer.parseInt(args.get("type")) && pokeMessage.getId() == Integer.parseInt(args.get("id"))
                ).findFirst().orElseThrow();
            }
            case "nudge" -> {
                var target = Optional.of(args.get("qq")).orElseThrow();
                if (contact instanceof Group c) {
                    Optional.ofNullable(c.get(Long.parseLong(target))).orElseThrow().nudge().sendTo(c);
                } else {
                    Optional.ofNullable(contact).ifPresent( contact1 -> Optional.ofNullable(bot.getFriend(Long.parseLong(target))).orElseThrow().nudge().sendTo(contact) );
                }
                return MSG_EMPTY;
            }
            case "xml" -> {}
            case "json" -> {}
            case "reply" -> {

            }
            default -> {
                OneBotMirai.logger.debug("不支持的 CQ码：${type}");
            }
        }
        return MSG_EMPTY;

    }

    private static Message constuctImageMsg(String fileStr, Contact contact) {
        int index = fileStr.indexOf(":");
        String protocol;
        String content;
        if (index == -1){
            protocol = "file";
            content = fileStr;
        }else{
            protocol = fileStr.substring(0, index);
            content = fileStr.substring(index + 3);
        }
        switch (protocol) {
            case "http" -> {
                throw new NotImplementedError("Not Implemented" );
            }
            case "file" -> {
                File file = new File(content);
                return ExternalResource.uploadAsImage(file, contact);
            }
            case "base64" -> {
                byte[] decode = Base64.getDecoder().decode(content);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decode);
                Image image = null;
                try {
                    image = ExternalResource.uploadAsImage(ExternalResource.create(byteArrayInputStream, ImageType.PNG.getFormatName()).toAutoCloseable(), contact);
                    return image;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + protocol);
        }

    }


//    File getDataFile(String type,String name){
//        arrayOf(
//                File(PluginBase.dataFolder, type).absolutePath + File.separatorChar,
//                "data" + File.separatorChar + type + File.separatorChar,
//                System.getProperty("java.library.path")
//                        .substringBefore(";") + File.separatorChar + "data" + File.separatorChar + type + File.separatorChar,
//                ""
//        ).forEach {
//            var f = File(it + name).absoluteFile;
//            if (f.exists()) {
//                return f;
//            }
//        }
//        return null;
//    }
}
