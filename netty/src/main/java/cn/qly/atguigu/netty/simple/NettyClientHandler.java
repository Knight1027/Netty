package cn.qly.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 连接建立，发送数据给服务端
     * @param ctx handler上下文
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接建立------------------------------------------------->");
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，服务端！", CharsetUtil.UTF_8));
        System.out.println("连接建立-------------------------------------------------<");
    }

    /**
     * 打印服务端发送的数据
     * @param ctx handler上下文
     * @param msg 服务端发来的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("有数据可读------------------------------------------------->");
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("from 服务端" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务端地址：" + ctx.channel().remoteAddress());
        System.out.println("有数据可读-------------------------------------------------<");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
