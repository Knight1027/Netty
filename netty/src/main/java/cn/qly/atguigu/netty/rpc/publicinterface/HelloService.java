package cn.qly.atguigu.netty.rpc.publicinterface;

/**
 * 这个是接口，是服务提供方和消费方都需要的
 */
public interface HelloService {

    String hello(String msg);

}
