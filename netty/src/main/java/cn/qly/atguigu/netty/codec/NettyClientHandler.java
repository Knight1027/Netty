package cn.qly.atguigu.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 通道准备就绪，即客户端connect，服务端accept成功时，调用该方法
     *
     * @param ctx handler上下文
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("张飞").build();
        ctx.writeAndFlush(student);
    }

    /**
     * selector有读事件发生，调用该方法
     *
     * @param ctx handler上下文
     * @param msg 服务端发来的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("from 服务端" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务端地址：" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
