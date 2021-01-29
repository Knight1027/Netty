package cn.qly.atguigu.nio.example.group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {

    /**
     * 群聊客户端：
     * 主线程启动客户端，用户从终端输入数据，发送给服务端
     * 子线程循环监听网络连接事件，读取服务端数据，并显示在终端上
     *
     * 数据：
     * SocketChannel
     * Selector
     * IP PORT
     * username
     *
     * 构造器
     * 初始化SocketChannel和Selector
     *
     * 方法：
     * sendInfo：向服务器发送消息
     * readInfo：从服务器读取消息
     */

    private SocketChannel socketChannel;
    private Selector selector;
    public static final int PORT = 6666;
    public static final String HOST = "127.0.0.1";
    private String username;
    public GroupChatClient() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
            selector = Selector.open();
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println("username:" + username + " is ok!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(String info) {
        String msg = username + "说：" + info;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            int count = selector.select();
            if (count > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    if (key.isReadable()) {
                        //将数据显示到终端
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buf = ByteBuffer.allocate(1024);
                        channel.read(buf);
                        System.out.println("from server: " + new String(buf.array()));
                    }
                    selectionKeyIterator.remove();
                }
            } else {
                System.out.printf("没有事件发生(>^ω^<)喵");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        /**
         * 1.启动客户端
         * 2.创建一个新的线程，每个3s从服务器读取数据
         * 3.main线程，读取从用户输入的数据，发送到服务器
         */
        GroupChatClient groupChatClient = new GroupChatClient();
        new Thread(()->{
            while (true) {
                groupChatClient.readInfo();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String str = sc.nextLine();
            groupChatClient.sendInfo(str);
        }
    }
}
