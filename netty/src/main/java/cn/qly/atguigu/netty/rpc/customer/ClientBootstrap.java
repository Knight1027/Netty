package cn.qly.atguigu.netty.rpc.customer;

import cn.qly.atguigu.netty.rpc.netty.NettyClient;
import cn.qly.atguigu.netty.rpc.publicinterface.HelloService;

public class ClientBootstrap {

    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws InterruptedException {

        // 创建一个消费者
        NettyClient customer = new NettyClient();

        // 创建代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);

        // 通过代理对象调用服务提供者的方法
        for (; ; ) {
            Thread.sleep(1000);

            String res = service.hello("你好 dubbo~");

            System.out.println("调用的结果 res= " + res);
        }

    }

}
