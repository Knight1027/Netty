package cn.qly.atguigu.netty.tcp;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class MyClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup IOGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(IOGroup).
                    channel(NioSocketChannel.class).
                    handler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new MyClientHandler());
                }
            });
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 6668).sync();
            sync.channel().closeFuture().sync();

        } finally {
            IOGroup.shutdownGracefully();
        }
    }

}
