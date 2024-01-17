package cn.evole.onebot.mirai.util;

import cn.evole.onebot.sdk.util.json.JsonsObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.val;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.Nullable;

import static cn.evole.onebot.mirai.util.RichMessageUtils.xmlMessage;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/18 1:29
 * @Description:
 */

public class MusicUtils {

    public static class QQMusic{
        public static String getPlayUrl(String mid){
            val result =HttpUtils.getStringFromHttpUrl(
                    "https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?&jsonpCallback=MusicJsonCallback&cid=205361747&songmid=" +
                            mid + "&filename=C400" + mid + ".m4a&guid=7549058080"
            );
            JsonsObject jsonObject = new JsonsObject(result);
            val json =
                    jsonObject.getJsonElement("data").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject();
            if (json.has("subcode")) {
                return "http://aqqmusic.tc.qq.com/amobile.music.tc.qq.com/C400$mid.m4a?guid=7549058080&amp;vkey=%s&amp;uin=0&amp;fromtag=38".formatted(json.get("vkey").getAsString());
            }
            return "";
        }

        public static JsonObject getSongInfo(String id, String mid){
            val result = HttpUtils.getStringFromHttpUrl(
                    "https://u.y.qq.com/cgi-bin/musicu.fcg?format=json&inCharset=utf8&outCharset=utf-8&notice=0&" +
                            "platform=yqq.json&needNewCode=0&data=" +
                            "{%22comm%22:{%22ct%22:24,%22cv%22:0},%22songinfo%22:{%22method%22:%22get_song_detail_yqq%22,%22param%22:" +
                            "{%22song_type%22:0,%22song_mid%22:%22"+mid+"%22,%22song_id%22:"+id+"},%22module%22:%22music.pf_song_detail_svr%22}}"
            );
            JsonsObject jsonObject = new JsonsObject(result);
            return jsonObject.getJsonElement("songinfo").getAsJsonObject().get("data").getAsJsonObject();

        }
        public static Message send(String id) {

            val info = getSongInfo(id, "");
            val trackInfo = info.get("track_info").getAsJsonObject();
            val url = getPlayUrl(trackInfo.get("file").getAsJsonObject().get("media_mid").getAsString());
            return new MusicShare(
                    MusicKind.QQMusic,
                    trackInfo.get("name").getAsString(),
                    trackInfo.get("singer").getAsJsonArray().get(0).getAsJsonObject().get("name").getAsString(),
                    "https://i.y.qq.com/v8/playsong.html?_wv=1&amp;songid=$id&amp;souce=qqshare&amp;source=qqshare&amp;ADTAG=qqshare",
                    "http://imgcache.qq.com/music/photo/album_500/"+id.substring(id.length() - 2)+"/500_albumpic_"+id+"_0.jpg",
                    url
            );
        }
    }

        public static Message custom(String url ,String audio , String title, @Nullable String content, @Nullable String image ){
            return xmlMessage(
                    "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>" +
                            "<msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"[分享] $title\" sourceMsgId=\"0\" " +
                            "url=\""+url+"\" " +
                            "flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\">" +
                            "<audio cover=\" "+image+" \" " +
                            "src=\""+audio+"\" /><title>"+title+"</title><summary>"+content+"</summary></item><source name=\"音乐\" " +
                            "icon=\"https://i.gtimg.cn/open/app_icon/01/07/98/56/1101079856_100_m.png\" " +
                            "url=\"http://web.p.qq.com/qqmpmobile/aio/app.html?id=1101079856\" action=\"app\" " +
                            "a_actionData=\"com.tencent.qqmusic\" i_actionData=\"tencent1101079856://\" appid=\"1101079856\" /></msg>"
            );
        }

    public static class NeteaseMusic{
        public static JsonObject getSongInfo(String id) {
            val result = HttpUtils.getStringFromHttpUrl("http://music.163.com/api/song/detail/?id="+id+"&ids=%5B"+id+"%5D");
            JsonsObject jsonObject = new JsonsObject(result);
            return jsonObject.getJsonElement("songs").getAsJsonArray().get(0).getAsJsonObject();
        }

        public static ServiceMessage toXmlMessage(String song, String  singer, String  songId, String  coverUrl){
            return xmlMessage(
                    "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>" +
                            "<msg serviceID=\"2\" templateID=\"1\" action=\"web\" brief=\"[分享] %s\" sourceMsgId=\"0\" ".formatted(song) +
                            "url=\"http://music.163.com/m/song/%s\" ".formatted(songId) +
                            "flag=\"0\" adverSign=\"0\" multiMsgFlag=\"0\"><item layout=\"2\">" +
                            "<audio cover=\"%s?param=90y90\" ".formatted(coverUrl) +
                            "src=\"https://music.163.com/song/media/outer/url?id=%s.mp3\" /><title>%s</title><summary>%s</summary></item><source name=\"网易云音乐\" ".formatted(songId,song,singer) +
                            "icon=\"https://pic.rmb.bdstatic.com/911423bee2bef937975b29b265d737b3.png\" " +
                            "url=\"http://web.p.qq.com/qqmpmobile/aio/app.html?id=100495085\" action=\"app\" " +
                            "a_actionData=\"com.netease.cloudmusic\" i_actionData=\"tencent100495085://\" appid=\"100495085\" /></msg>"

            );
        }

        public static Message send(String id) {
            val info = getSongInfo(id);
            val song = info.get("name").getAsString();
            val artists = info.get("artists").getAsJsonArray();
            val albumInfo = info.get("album").getAsJsonObject();

            return new MusicShare(
                    MusicKind.NeteaseCloudMusic,
                    song,
                    artists.get(0).getAsJsonObject().get("name").getAsString(),
                    "http://music.163.com/m/song/"+id,
                    "http://imgcache.qq.com/music/photo/album_500/"+id.substring(id.length() - 2)+"/500_albumpic_"+id+"_0.jpg",
                    albumInfo.get("picUrl").getAsString(),
                    "[分享] "+ song
            );
        }
    }

}
