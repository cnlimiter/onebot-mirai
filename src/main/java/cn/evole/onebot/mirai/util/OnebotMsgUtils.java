package cn.evole.onebot.mirai.util;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.config.PluginConfig;
import cn.evole.onebot.mirai.core.session.SessionManager;
import cn.evole.onebot.mirai.web.queue.CacheSourceQueue;
import cn.evole.onebot.sdk.util.DataBaseUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.val;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static cn.evole.onebot.mirai.OneBotMirai.logger;
import static cn.evole.onebot.mirai.util.RichMessageUtils.jsonMessage;
import static cn.evole.onebot.mirai.util.RichMessageUtils.xmlMessage;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 2:43
 * Version: 1.0
 */
public class OnebotMsgUtils {


    private static final PlainText MSG_EMPTY = new PlainText("");

    public static MessageChain messageToChains(Bot bot, Contact contact, JsonElement message, boolean escape){
        if (message.isJsonPrimitive() && message.getAsJsonPrimitive().isString()){
            return escape ? new MessageChainBuilder().append(new PlainText(message.getAsString())).build():
                    codeToChain(bot, message.getAsString(), contact)
                    ;
        }
        else if (message.isJsonObject()) {
            var jsonObject = message.getAsJsonObject();
            var messageChain = new MessageChainBuilder();
            try {
                var data = jsonObject.getAsJsonObject("data");
                if (jsonObject.has("type")){
                    if (data.asMap().containsKey("text"))
                         messageChain.append(new PlainText(data.get("text").getAsString()));
                    else messageChain.append(textToMessageInternal(bot, contact, message));
                }
            } catch (NullPointerException e) {
                logger.warning("Got null when parsing CQ message object");
                return null;
            }
            return messageChain.build();
        }
        else if (message.isJsonArray()) {
            var jsonArray = message.getAsJsonArray();
            var messageChain = new MessageChainBuilder();
            for (var msg : jsonArray){
                try {
                    var data = msg.getAsJsonObject().get("data");
                    if (msg.getAsJsonObject().has("type")){
                        if (data.getAsJsonObject().asMap().containsKey("text"))
                             messageChain.append(new PlainText(data.getAsJsonObject().get("text").getAsString()));
                        else messageChain.append(textToMessageInternal(bot, contact, msg));
                    }
                } catch (NullPointerException e) {
                    logger.warning("Got null when parsing Chain message object");
                }
            }
            return messageChain.build();
        }
        else {
            logger.warning("Cannot determine type of " + message.toString());
            return null;
        }
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

        //else return "此处消息的转义尚未被插件支持";
        else {
            logger.warning("此处消息的转义尚未被插件支持: " + message.toString());
            return "";
        }
    }

    private static MessageChain codeToChain(Bot bot, String message, Contact contact){
        AtomicReference<Message> msg = new AtomicReference<>();
        PlainText text = new PlainText("");
        if (message.contains("[CQ:")) {
            final boolean[] interpreting = {false};
            var sb = new StringBuilder();
            final int[] index = {0};
            List.of(message).forEach(
                    c -> {
                        if ("[".equals(c)) {
                            if (interpreting[0]) {
                                logger.error(String.format("CQ消息解析失败：%s，索引：%s", message, Arrays.toString(index)));
                                return;
                            } else {
                                interpreting[0] = true;
                                if (!sb.isEmpty()) {
                                    var lastMsg = sb.toString();
                                    sb.delete(0, sb.length());
                                    msg.set(textToMessageInternal(bot, contact, lastMsg));
                                }
                                sb.append(c);
                            }
                        } else if ("]".equals(c)) {
                            if (!interpreting[0]) {
                                logger.error(String.format("CQ消息解析失败：%s，索引：%s", message, Arrays.toString(index)));
                                return;
                            } else {
                                interpreting[0] = false;
                                sb.append(c);
                                if (!sb.isEmpty()) {
                                    var lastMsg = sb.toString();
                                    sb.delete(0, sb.length());
                                    msg.set(textToMessageInternal(bot, contact, lastMsg));
                                }
                            }
                        } else {
                            sb.append(c);
                        }
                        index[0]++;
                    }
            );
            if (!sb.isEmpty()) {
               msg.set(textToMessageInternal(bot, contact, sb.toString()));
            }
        } else {
          text = new PlainText(unescape(message));
        }
        return msg.get() != null
                ?
                new MessageChainBuilder()
                .append(msg.get())
                .append(text)
                .build()
                :
                new MessageChainBuilder()
                .append(text)
                .build();
    }


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
                        logger.debug("不能在私聊中发送 At。");
                        return MSG_EMPTY;
                    } else {
                        var member = contact.getBot().getFriend(Long.parseLong(args.get("qq")));
                        if (member == null) {
                            logger.debug(String.format("无法找到群员：%s", args.get("qq")));
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
                Message message = constructImageMsg(file, contact);
                logger.info(String.format("Image: %s", message));
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
            case "record" -> {
                // 语音
                String file = args.get("file");
                Message message = VoiceUtils.sendRecordMsg(file, contact);
                logger.info(String.format("Record: %s", message));
                return message;
            }
            case "contact" -> {
                if ("qq".equals(args.get("type"))) {
                    return  RichMessageUtils.contactQQ(bot, Long.parseLong(args.get("id")));
                } else {
                    //return RichMessageHelper.contactGroup(bot, args["id"]!!.toLong())
                }

            }
            case "music" -> {
                switch (args.get("type")){
                    case "qq" -> { return MusicUtils.QQMusic.send(args.get("id")); }
                    case "163" -> { return MusicUtils.NeteaseMusic.send(args.get("id")); }
                    case "custom" -> { return MusicUtils.custom(
                            args.get("url"),
                            args.get("audio"),
                            args.get("title"),
                            args.get("content"),
                            args.get("image")
                    ); }
                    default -> throw new IllegalArgumentException("Custom music share not supported anymore");
                }
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
            case "xml" -> {
                return xmlMessage(args.get("data"));
            }
            case "json" -> {
                return args.get("data")!= null && args.get("data").contains("\"app\":") ? new LightApp(args.get("data")) : jsonMessage(args.get("data"));
            }
            case "reply" -> {
                if (PluginConfig.INSTANCE.getDb().getEnable() && OneBotMirai.INSTANCE.db != null) {
                    if (SessionManager.containsSession(bot.getId())) {
                        val session = SessionManager.get(bot.getId());
                        CacheSourceQueue cachedSources = session.getApiImpl().getCachedSourceQueue();
                        MessageSource messageSource = cachedSources.get(Integer.parseInt(args.get("id")));
                        return MessageSource.quote(messageSource);
                    } else {
                        logger.warning("CacheSourceQueue has not found reply messageId -> " + args.get("id"));
                    }

                    //return MessageSource.quote(MessageChain.deserializeFromJsonString(new String(DataBaseUtils.toByteArray(Integer.parseInt(args.get("id"))))));
                }
            }
            default -> {
                logger.debug("不支持的 CQ码:" + type);
            }
        }
        return MSG_EMPTY;

    }

    private static Message constructImageMsg(String fileStr, Contact contact) {
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
            case "file" -> {
                File file = new File(content);
                return ExternalResource.uploadAsImage(file, contact);
            }
            case "http", "https", "base64" -> {
                byte[] decode;
                if (protocol.equals("base64")) {
                    decode = Base64.getDecoder().decode(content);
                } else {
                    decode = HttpUtils.getBytesFromHttpUrl(fileStr);
                }

                if (decode == null || decode.length == 0) {
                    throw new IllegalStateException("Unexpected value: " + protocol);
                }

                ImageType t = ImageType.PNG;
                if ((decode[0] & 0xff) == 0x47 && (decode[1] & 0xff) == 0x49) {
                    t = ImageType.GIF;
                }
                if ((decode[0] & 0xff) == 0x89 && (decode[1] & 0xff) == 0x50) {
                    t = ImageType.PNG;
                }
                if ((decode[0] & 0xff) == 0xff && (decode[1] & 0xff) == 0xd8) {
                    t = ImageType.JPG;
                }

                InputStream bio = new ByteArrayInputStream(decode);
                try {
                    return ExternalResource.uploadAsImage(ExternalResource.create(bio, t.getFormatName()).toAutoCloseable(), contact);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + protocol);
        }

    }
}
