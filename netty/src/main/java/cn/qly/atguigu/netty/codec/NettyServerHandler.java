package cn.qly.atguigu.netty.codec;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * selector监听到读事件发生
     * 1.将客户端发来的消息打印到终端
     * 2.并打印客户端的地址
     * @param ctx Handler的上下文
     * @param msg 客户端发来的消息
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        StudentPOJO.Student student = (StudentPOJO.Student) msg;
        System.out.println("客户端发送的数据 id=" + student.getId() + " 名字=" + student.getName());
    }

    /**
     * 读取事件发生结束后调用的方法
     * 我们发送一个消息到客户端
     *
     * @param ctx Handler的上下文
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端(>^ω^<)喵！", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
