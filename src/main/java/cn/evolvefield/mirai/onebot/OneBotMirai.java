package cn.evolvefield.mirai.onebot;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.utils.MiraiLogger;

public final class OneBotMirai extends JavaPlugin {
    public static final OneBotMirai INSTANCE = new OneBotMirai();

    public static MiraiLogger logger = INSTANCE.getLogger();
    private OneBotMirai() {
        super(new JvmPluginDescriptionBuilder("cn.evolvefield.mirai.onebot", "0.1.0")
                .name("OneBot Mirai")
                .author("cnlimiter")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
    }
}
