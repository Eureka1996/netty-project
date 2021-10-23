package com.wufuqiang.netty.base;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Wu Fuqiang
 * @date 2021/10/22 4:50 下午
 */
@Slf4j
public class EventLoopGroupDemo {

    EventLoopGroup group = new NioEventLoopGroup(2);

    @Test
    public void groupSubmit() throws IOException {
        EventLoopGroup group = new NioEventLoopGroup(2);
        // 轮询的方式
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());
        System.out.println(group.next());

        group.next().execute(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("wufuqiang:group submit");
        });

        System.out.println("wufuqiang:main");
        //System.in.read();
    }

    public void scheduleAtFixedRate(){
        // 1秒后，每5秒执行一次
        group.next().scheduleAtFixedRate(()->{
            log.debug("wufuqiang:"+System.currentTimeMillis());
        },1,5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws IOException {
        EventLoopGroupDemo groupDemo = new EventLoopGroupDemo();
        //groupDemo.groupSubmit();
        groupDemo.scheduleAtFixedRate();
    }
}
