package cn.qly.atguigu.nio.example.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    /**
     *
     * 接收客户端发来的消息，并显示到终端上。
     *
     * 1.两个创建三次配置
     * 2.循环监听
     * 3.根据不同的网络事件，分派不同的逻辑
     *
     * 1.创建ServerSocketChannel对象
     * 2.创建Selector对象
     * 3.ServerSocketChannel绑定端口
     * 4.设置ServerSocketChannel为非阻塞
     * 5.ServerSocketChannel注册到Selector上
     * 6.循环监听连接
     * 7.若Selector上有事件发生，获取所有事件对象set，则根据对应事件类型，选择不同的处理逻辑
     * 8.对于网络连接事件，创建一个新的SocketChannel，并将其注册到Selector上
     * 9.对于读写事件，对应处理逻辑
     */


    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 6666));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器启动了(>^ω^<)喵");

        while (true) {


            if (selector.select(1000) == 0) {
                System.out.println("1s没有事件发生");
                continue;
            }


            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个SocketChannel: " + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }

                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    channel.read(buf);
                    System.out.printf("来自客户端的数据：" + new String(buf.array()));
                }
                iterator.remove();
            }
        }
    }

}
