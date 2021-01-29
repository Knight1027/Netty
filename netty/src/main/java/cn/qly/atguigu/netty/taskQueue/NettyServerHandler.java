package cn.qly.atguigu.netty.taskQueue;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /**
     * 读事件发生时调用的handler
     *
     * @param ctx handler上下文
     * @param msg 客户端的数据
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        /**
         * 非常耗时的业务-->异步执行-->提交给该channel对应的EventLoop中的taskQueue执行
         */

        System.out.println("当前线程ID：" + Thread.currentThread().getId() + " 当前线程名字：" + Thread.currentThread().getName());


        //解决方案一：用户自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            public void run() {
                try {
                    System.out.println("任务1线程ID：" + Thread.currentThread().getId() + " 当前线程名字：" + Thread.currentThread().getName());
                    Thread.sleep(10000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端！(>^ω^<)喵2", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("任务2线程ID：" + Thread.currentThread().getId() + " 当前线程名字：" + Thread.currentThread().getName());
                    Thread.sleep(5000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端！(>^ω^<)喵3", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 解决方案二：提交到scheduleTaskQueue执行
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("任务3线程ID：" + Thread.currentThread().getId() + " 当前线程名字：" + Thread.currentThread().getName());
                    Thread.sleep(5000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端！(>^ω^<)喵4", CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);


        System.out.println("异步执行handler线程ID：" + Thread.currentThread().getId() + " 当前线程名字：" + Thread.currentThread().getName());


    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("你好，客户端！(>^ω^<)喵1", CharsetUtil.UTF_8));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
