package cn.evole.onebot.mirai.cmd;

import cn.evole.onebot.mirai.OneBotMirai;
import cn.evole.onebot.mirai.util.BaseUtils;
import cn.evole.onebot.mirai.util.DBUtils;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JCompositeCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Project: onebot-mirai
 * @Author: cnlimiter
 * @CreateTime: 2024/1/19 12:04
 * @Description:
 */

public class OneBotMiraiCmd extends JCompositeCommand {

    public static OneBotMiraiCmd INSTANCE = new OneBotMiraiCmd();
    public OneBotMiraiCmd() {
        super(OneBotMirai.INSTANCE, "onebot", "ob");
    }

    @SubCommand
    public void dbToCsv(CommandSender sender) {
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        var csv= OneBotMirai.INSTANCE.db.toCSV(DBUtils.MessageNode.class, "机器人Id", DBUtils.Locates.getLocalizer());
        var file = OneBotMirai.INSTANCE.dbFolder.toPath().resolve(dateFormat.format(date) + ".csv");
        BaseUtils.safeRun(() -> {
            try {
                Files.writeString(file, csv, StandardOpenOption.CREATE_NEW);
            } catch (IOException ignored) {
                sender.sendMessage("导出csv失败");
            }
        });

        sender.sendMessage("导出csv成功:" + file.toFile().getAbsolutePath());
    }




}
