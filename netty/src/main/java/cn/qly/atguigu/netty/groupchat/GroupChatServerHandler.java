package cn.qly.atguigu.netty.groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy--MM--dd HH:mm:ss");


    /**
     * 一旦连接被建立，该方法第一个执行
     * 1.将当前客户端上线消息推送给其他客户
     * 2.将自己加入channelGroup
     * @param ctx handler上下文
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端] " + channel.remoteAddress() + " 加入聊天 " + sdf.format(new Date()));
        channelGroup.add(channel);
    }

    /**
     * 一旦连接被断开，执行该方法
     *
     * @param ctx handler上下文
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端] " + channel.remoteAddress() + " 离开了" + sdf.format(new Date()));
        System.out.println("ChannelGroup size:" + channelGroup.size());
    }


    /**
     * channel处于活动状态，提示xx上线
     * @param ctx handler上下文
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了~~~");
    }

    /**
     * channel处于不活动状态，提示xx离线
     * @param ctx handler上下文
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了~~~");
    }

    /**
     * 将客户端发来的消息转发给其他客户端，并将自己发送的消息回显
     * @param ctx handler上下文
     * @param msg 客户端的消息
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel self = ctx.channel();
        channelGroup.forEach(c -> {
            if (c != self) {
                c.writeAndFlush("[客户]" + self.remoteAddress() + " 发送了" + msg + "\n");
            } else {
                c.writeAndFlush("[自己]发送了 " + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
