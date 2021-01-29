package cn.qly.atguigu.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("MessageToByteEncoder encode 被调用");
        System.out.println("msg=" + msg);
        out.writeLong(msg);
    }
}
