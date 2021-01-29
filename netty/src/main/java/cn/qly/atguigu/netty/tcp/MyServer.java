package cn.qly.atguigu.netty.tcp;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup acceptGroup = new NioEventLoopGroup();
        NioEventLoopGroup IOGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(acceptGroup, IOGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            ChannelFuture cf = serverBootstrap.bind(6668).sync();
            cf.channel().closeFuture().sync();
        }finally {
            acceptGroup.shutdownGracefully();
            IOGroup.shutdownGracefully();
        }
    }

}
