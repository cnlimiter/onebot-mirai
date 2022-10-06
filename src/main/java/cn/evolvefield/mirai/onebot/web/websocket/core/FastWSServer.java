package cn.evolvefield.mirai.onebot.web.websocket.core;

import cn.evolvefield.mirai.onebot.web.websocket.common.WSServerConfig;
import cn.evolvefield.mirai.onebot.web.websocket.impl.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/4 19:51
 * Version: 1.0
 */
@Slf4j
public abstract class FastWSServer implements WebSocketServer {
    private Channel serverChannel;
    private WSServerConfig config;

    public void start(WSServerConfig config) {
        this.config = config;
        ServerBootstrap server = new ServerBootstrap();
        server.group(config.getBoosGroup(), config.getWorkerGroup());
        server.channel(NioServerSocketChannel.class);
        server.childHandler(config.getChildHandler());

        ChannelFuture future = server.bind(config.getPort());
        future.addListener(f -> {
            if (f.isDone() && f.isSuccess()) {
                this.serverChannel = future.channel();
                log.info("启动WS服务器成功");
                log.info("主线程id为 {}", config.getBoosGroup().executorCount());
                log.info("工作线程id为 {}", config.getWorkerGroup().executorCount());
            }
            if (f.isDone() && f.cause() != null) {
                log.error("启动WS服务器失败,抛出={}", f.cause().getMessage());
                future.channel().close();
            }
        });
    }

    public void start(final int port) {
        start(new WSServerConfig() {
            @Override
            public ChannelHandler getChildHandler() {
                return new WebSocketChannelInitializer(FastWSServer.this);
            }

            @Override
            public int getPort() {
                return port;
            }
        });
    }

    public void start() {
        start(8080);
    }

    public void stop() {
        if (serverChannel != null && serverChannel.isOpen()) {
            final int waitSec = 10;
            CountDownLatch latch = new CountDownLatch(1);
            serverChannel.close().addListener(f -> {
                config.getWorkerGroup().schedule(() -> {
                    log.info("停止处理器成功...");
                    config.getWorkerGroup().shutdownGracefully();
                    latch.countDown();
                }, waitSec - 2, TimeUnit.SECONDS);

                log.info("关闭WS服务器套接字成功={}", f.isSuccess());
                config.getBoosGroup().shutdownGracefully();
            });

            try {
                boolean flag = latch.await(waitSec, TimeUnit.SECONDS);
                if(!flag){
                    log.warn("关闭WS服务器超时");
                }
            } catch (InterruptedException e) {
                log.warn("关闭WS服务器异常={}", e.getMessage());
            }
        }
    }
}
