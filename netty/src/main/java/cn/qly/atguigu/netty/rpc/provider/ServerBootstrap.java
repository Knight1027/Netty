package cn.qly.atguigu.netty.rpc.provider;


import cn.qly.atguigu.netty.rpc.netty.NettyServer;

/**
 * 会启动一个服务器，就是NettyServer
 */
public class ServerBootstrap {

    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1", 6668);
    }

}
