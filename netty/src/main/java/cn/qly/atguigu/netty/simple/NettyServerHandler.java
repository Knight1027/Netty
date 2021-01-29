package cn.qly.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;


public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 将客户端发来的消息打印到终端
     * @param ctx Handler的上下文
     * @param msg 客户端发来的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("有数据可读------------------------------------------------->");
        System.out.println("当前线程ID：" + Thread.currentThread().getId() + " 当前线程名字：" + Thread.currentThread().getName());
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送的消息：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端的地址：" + ctx.channel().remoteAddress());
        System.out.println("有数据可读-------------------------------------------------<");

    }

    /**
     * 发送一个消息到客户端
     * @param ctx Handler的上下文
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("数据读完------------------------------------------------->");
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端(>^ω^<)喵！", CharsetUtil.UTF_8));
        System.out.println("数据读完-------------------------------------------------<");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
