package cn.qly.atguigu.bio.frame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServerFrame {


    /**
     * 本例为BIO服务器框架代码，使用者需要
     * 1.配置线程池，配置端口
     * 2.实现Handler接口，完成自己的业务逻辑
     */

    private ExecutorService executorService;

    private int port;

    private BIOHandler BIOHandler;

    public BIOServerFrame() {
        this(6666);
    }

    public BIOServerFrame(int port) {
        this(Executors.newCachedThreadPool(), port, new BIOHandler() {
            @Override
            public void handler(Socket socket) {
                System.out.println("do your handler o(╥﹏╥)o");
            }
        });
    }

    public BIOServerFrame(int port, BIOHandler BIOHandler) {
        this(Executors.newCachedThreadPool(), port, BIOHandler);
    }

    public BIOServerFrame(ExecutorService executorService, int port, BIOHandler handler) {
        this.executorService = executorService;
        this.port = port;
        this.BIOHandler = handler;
    }

    public void run() {
        /**
         * 1.创建ServerSocket，并绑定端口
         * 2.循环监听
         * 3.若有连接，则使用线程池中的线程执行对应的I/O操作
         */

        try {
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        BIOHandler.handler(socket);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
