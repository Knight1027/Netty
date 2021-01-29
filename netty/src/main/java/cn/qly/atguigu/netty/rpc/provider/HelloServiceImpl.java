package cn.qly.atguigu.netty.rpc.provider;

import cn.qly.atguigu.netty.rpc.publicinterface.HelloService;

public class HelloServiceImpl implements HelloService {


    private int count = 0;


    /**
     * 当消费方，调用该方法时，就返回一个结果
     * @param msg
     * @return
     */
    @Override
    public String hello(String msg) {
        System.out.println("收到客户端消息=" + msg);
        if (msg != null) {
            return "你好客户端，我已经收到你的消息 [" + msg + "] 第" + (++this.count) + "次";
        } else {
            return "你好客户端，我已经收到你的消息";
        }

    }
}
