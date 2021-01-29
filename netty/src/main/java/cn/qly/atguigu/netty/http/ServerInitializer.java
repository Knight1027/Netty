package cn.qly.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        /**
         * 1.获取pipeline
         * 2.增加HttpServerCodec，编解码Http数据
         *   指定格式字节流数据 ---> HttpServerCodec ---> HttpRequest ---> text
         *   text ---> HttpResponse ---> HttpServerCodec ---> 指定格式字节流数据
         * 3.增加自己的业务出来handler
         */

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        pipeline.addLast(new HttpServerHandler());
    }
}
