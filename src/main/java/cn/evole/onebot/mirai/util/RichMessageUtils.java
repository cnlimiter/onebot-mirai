package cn.evole.onebot.mirai.util;

import kotlin.OptIn;
import lombok.val;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.RichMessage;
import net.mamoe.mirai.message.data.ServiceMessage;
import net.mamoe.mirai.message.data.SimpleServiceMessage;
import net.mamoe.mirai.message.data.XmlMessageBuilder;
import net.mamoe.mirai.utils.MiraiExperimentalApi;
import org.jetbrains.annotations.Nullable;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 14:41
 * Version: 1.0
 */
public class RichMessageUtils {


    @OptIn(markerClass = MiraiExperimentalApi.class)
    public static ServiceMessage xmlMessage(String content) {
        return new SimpleServiceMessage(60, content);
    }
    @OptIn(markerClass = MiraiExperimentalApi.class)
    public static ServiceMessage jsonMessage(String content) {
        return new SimpleServiceMessage(1, content);
    }

    public static ServiceMessage contactQQ(Bot bot , long id){
        val nick = bot.getFriendOrFail(id).getNick();
        return xmlMessage(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<msg templateID=\"12345\" action=\"plugin\" p_actionData=\"AppCmd://OpenContactInfo/?uin=%s\" ".formatted(id) +
                        "brief=\"推荐了%s\" serviceID=\"14\" ".formatted(nick) +
                        "i_actionData=\"mqqapi://card/show_pslcard?src_type=internal&amp;source=sharecard&amp;version=1&amp;uin=%s\" " .formatted(id)+
                        "a_actionData=\"mqqapi://card/show_pslcard?src_type=internal&amp;source=sharecard&amp;version=1&amp;uin=%s\">".formatted(id) +
                        "<item layout=\"0\" mode=\"1\"><summary>推荐联系人</summary><hr/></item>" +
                        "<item layout=\"2\" mode=\"1\">" +
                        "<picture cover=\"mqqapi://card/show_pslcard?src_type=internal&amp;source=sharecard&amp;version=1&amp;uin=%s\"/>".formatted(id) +
                        "<title>%s</title><summary>帐号：%s</summary></item><source/></msg>".formatted(nick, id)
        );
    }

}
