package cn.qly.atguigu.nio.example.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class NIOClient {

    /**
     *
     * 从终端接受用户输入，把数据发送给服务端
     *
     * 1.创建SocketChannel
     * 2.设置SocketChannel非阻塞
     * 3.提供服务端IP和Port
     * 4.连接服务端
     * 5.循环发送数据到服务端
     */

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("如果连接时间过长，客户端不会阻塞，可以去做其他事！！！");
            }
        }
        while (true) {
            Scanner in = new Scanner(System.in);
            System.out.println("输入要发送到服务端的数据：");
            String str = in.next();
            ByteBuffer buf = ByteBuffer.wrap(str.getBytes());
            socketChannel.write(buf);
        }


    }

}
