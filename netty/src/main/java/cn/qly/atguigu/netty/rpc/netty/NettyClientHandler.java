package cn.qly.atguigu.netty.rpc.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context; // 上下文

    private String result; // 返回的结果

    private String para; // 客户端调用方法时，传入的参数

    private int rank;

    //2
    public void setPara(String para) {
        System.out.println("rank= " + (rank++) + " setPara");
        this.para = para;
    }

    //1
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("rank= " + (rank++) + " channelActive");

        this.context = ctx;
    }


    //4
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        synchronized (this) {
            System.out.println("rank= " + (rank++) + " channelRead");
            this.result = msg.toString();
            this.notify();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    //3
    // 被代理对象调用，发送数据给服务器， --> wait等待被唤醒 --> 返回结果
    @Override
    public Object call() throws Exception {
        synchronized (this) {
            System.out.println("rank= " + (rank++) + " call1");
            this.context.writeAndFlush(this.para);
            this.wait();
            System.out.println("rank= " + (rank++) + " call2");
            return result;
        }
    }

}
