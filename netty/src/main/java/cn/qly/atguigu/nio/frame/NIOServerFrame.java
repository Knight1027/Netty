package cn.qly.atguigu.nio.frame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServerFrame {

    /**
     * 本例为NIO服务器框架代码，使用单Reactor单线程模型
     * 使用者需要:
     * 1.实现BIOHandler接口
     */

    private int port;

    private NIOHandler handler;

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    public NIOServerFrame() {
        this(6666);
    }

    public NIOServerFrame(int port) {
        this.port = port;
        handler = new NIOHandler() {
            @Override
            public void handler(SelectionKey key) {
                System.out.println("do your handler o(╥﹏╥)o");
            }
        };
    }

    public NIOServerFrame(int port, NIOHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    /**
     * 1.两个创建三次配置
     * 2.循环监听
     * 3.根据不同的网络事件，分派不同的逻辑
     */
    public void run() {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
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
                        handlerAccept();
                    }
                    if (key.isReadable()) {
                        handler.handler(key);
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlerAccept() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("客户端连接成功，生成了一个SocketChannel: " + socketChannel.hashCode());
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }
}
