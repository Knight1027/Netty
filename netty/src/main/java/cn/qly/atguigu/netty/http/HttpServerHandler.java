package cn.qly.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

//HttpObject指定了服务端和客户端交换数据的类型
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    // 读取事件发生
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        /**
         * 1.判断是否为HttpRequest
         * 2.打印一些信息
         * 3.获取URI，过滤指定资源
         * 4.回复信息给浏览器[http]
         * 5.将回复数据打包成HttpResponse格式，并设置对应的属性
         */

        if (msg instanceof HttpRequest) {
            System.out.println("pipeline hashcode:" + ctx.pipeline().hashCode() + "HttpServerHandler hashcode:" + this.hashCode());
            System.out.println("msg类型：" + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());
            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 /favicon.ico 不做响应！");
                return;
            }
            ByteBuf buf = Unpooled.copiedBuffer("hello, 我是服务器端", CharsetUtil.UTF_8);
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
            ctx.writeAndFlush(response);
        }
    }
}
