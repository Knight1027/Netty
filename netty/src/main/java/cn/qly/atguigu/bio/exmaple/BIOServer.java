package cn.qly.atguigu.bio.exmaple;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {

    /**
     *
     * 一个BIOServer例子
     *
     * main:
     * 1.创建线程池
     * 2.创建ServerSocket
     * 3.循环监听连接
     * 4.一旦有连接，将任务则提交给线程池，线程池执行任务
     *
     * handler:
     * 1.通过socket获取输入、输出流
     * 2.循环读取client发送的数据，若没有数据可读，则退出
     *
     */

    public static void main(String[] args) throws IOException {

        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        final ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了！！！");

        while (true) {

            System.out.println("等待连接...的线程的ID：" + Thread.currentThread().getId() + " 线程的名字：" + Thread.currentThread().getName());
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端(>^ω^<)喵");


            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    try {
                        handler(socket);
                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            socket.close();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            });

        }
    }

    public static void handler(Socket socket) throws IOException {

        System.out.println("执行业务逻辑！！！的线程ID：" + Thread.currentThread().getId() + " 线程的名字：" + Thread.currentThread().getName());

        byte[] buf = new byte[1024];

        InputStream is = socket.getInputStream();

        while (true) {

            int num = is.read(buf);
            if (num != -1) {
                System.out.println("从client读出的数据：" + new String(buf, 0, num));
            } else {
                break;
            }
        }
    }

}
