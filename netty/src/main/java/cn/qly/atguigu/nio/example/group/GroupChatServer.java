package cn.qly.atguigu.nio.example.group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    /**
     *
     * 群聊服务端
     *
     * 数据：
     * ServerSocketChannel
     * Selector
     * PORT
     *
     * 构造：
     * 初始化Selector和ServerSocketChannel
     *
     * 方法：
     * listen
     * 循环监听，根据对应的事件，选择对应的业务处理逻辑
     *
     * readData
     * 将从客户端读到的数据显示到终端上，并将其转发给其他用户
     *
     * sendInfoToOtherClients
     * 将从一个客服端读到的数据转发给其他用户
     */

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    public static final int PORT = 6666;

    public GroupChatServer() {

        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 循环监听Selector上发生的事件，根据事件的类型，分派给不同的逻辑处理
     *
     */
    public void listen() {
        System.out.println("服务器启动了(>^ω^<)喵");
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey key = selectionKeyIterator.next();
                        if (key.isAcceptable()) {
                            accept();
                        } else if (key.isReadable()) {
                            readData(key);
                        }
                        selectionKeyIterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理连接事件，显示客户上线
     * @throws IOException
     */
    private void accept() throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        System.out.println(socketChannel.getRemoteAddress() + "上线");
    }

    /**
     * 打印客服端发来的数据，并转发给其他客户
     * @param key 事件对象
     */
    private void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            ByteBuffer buf = ByteBuffer.allocate(1024);
            int num = socketChannel.read(buf);
            if (num > 0) {
                String msg = new String(buf.array());
                System.out.println("from 客户端：" + msg);
                sendInfoToOtherClients(msg, socketChannel);
            }
        } catch (IOException e) {
            try {
                key.cancel();
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                socketChannel.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    /**
     * 将数据转发给其他Clients
     * @param msg 消息
     * @param self 代表自己的连接通道
     */
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        Set<SelectionKey> keys = selector.keys();
        ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());

        for (SelectionKey key : keys) {
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dst = (SocketChannel) targetChannel;
                dst.write(wrap);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}
