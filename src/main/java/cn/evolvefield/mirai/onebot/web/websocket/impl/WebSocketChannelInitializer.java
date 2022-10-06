package cn.evolvefield.mirai.onebot.web.websocket.impl;

import cn.evolvefield.mirai.onebot.web.websocket.core.WebSocketServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Description:通道处理
 * Author: cnlimiter
 * Date: 2022/10/4 19:40
 * Version: 1.0
 */
public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final WebSocketServer server;


    public WebSocketChannelInitializer(WebSocketServer server){
        this.server = server;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        //二进制流在通道中被处理
        ChannelPipeline pipeline = ch.pipeline();
        // HttpRequestDecoder和HttpResponseEncoder的一个组合，针对http协议进行编解码
        pipeline.addLast("httpServerCodec", new HttpServerCodec());//设置解码器
        //分块向客户端写数据，防止发送大文件时导致内存溢出， channel.write(new ChunkedFile(new File("bigFile.mkv")))
        pipeline.addLast(new ChunkedWriteHandler());//用于大数据的分区传输
        // 将HttpMessage和HttpContents聚合到一个完成的 FullHttpRequest或FullHttpResponse中
        // 具体是FullHttpRequest对象还是FullHttpResponse对象取决于是请求还是响应
        // 需要放到HttpServerCodec这个处理器后面
        pipeline.addLast(new HttpObjectAggregator(1024 * 2));//聚合器,使用websocket会用到
        // webSocket 数据压缩扩展，当添加这个的时候WebSocketServerProtocolHandler的第三个参数需要设置成true
        pipeline.addLast(new WebSocketServerCompressionHandler());
        // 服务器端向外暴露的 web socket 端点，当客户端传递比较大的对象时，maxFrameSize参数的值需要调大
        pipeline.addLast(new WebSocketServerAuthProtocolHandler("/ws/onebot/", null, true, 65536, server));
        pipeline.addLast(new LengthFieldPrepender(4));
        // 业务代码
        pipeline.addLast(new WebSocketServerChannelInboundHandler(server));
    }
}
