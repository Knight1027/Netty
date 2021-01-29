package cn.qly.atguigu.nio.frame;

import java.nio.channels.SelectionKey;

public interface NIOHandler {

    void handler(SelectionKey key);

}
