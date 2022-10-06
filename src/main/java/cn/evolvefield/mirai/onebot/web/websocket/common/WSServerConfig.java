package cn.evolvefield.mirai.onebot.web.websocket.common;

import io.netty.channel.ChannelHandler;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Description:ws Server配置
 * Author: cnlimiter
 * Date: 2022/10/4 19:46
 * Version: 1.0
 */
public interface WSServerConfig {
    /**
     * 默认工作线程池等于物理核心数2被
     */
    default NioEventLoopGroup getWorkerGroup(){
        return new NioEventLoopGroup();
    }

    /**
     * 默认一个调度线程
     */
    default NioEventLoopGroup getBoosGroup(){
        return new NioEventLoopGroup(1);
    }

    ChannelHandler getChildHandler();

    /**
     * 默认端口8080
     */
    default int getPort(){ return 8080; }
}
