package cn.qly.atguigu.netty.protocolcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyMessageDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyMessageDecoder decode 被调用");
        // 需要将得到的二进制字节码 转换为 MessageProtocol 数据包（对象）
        int len = in.readInt();
        byte[] content = new byte[len];
        in.readBytes(content);

        // 封装成MessageProtocol对象， 放入out中， 传递给下一个handler处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(len);
        messageProtocol.setContent(content);

        out.add(messageProtocol);
    }
}
