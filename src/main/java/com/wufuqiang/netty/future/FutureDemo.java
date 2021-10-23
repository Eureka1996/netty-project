package com.wufuqiang.netty.future;

import com.wufuqiang.netty.base.EventLoopGroupDemo;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author Wu Fuqiang
 * @date 2021/10/23 12:41 上午
 */
@Slf4j
public class FutureDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        jdkFuture();
        log.debug("--------------------");
        nettyFuture();
        log.debug("--------------------");
        nettyFutureAsync();
        log.debug("--------------------");
        nettyPromise();
        
    }
    
    public static void jdkFuture() throws ExecutionException, InterruptedException {
        // 线程池
        final ExecutorService executor = Executors.newFixedThreadPool(2);
        // 提交任务
        final Future<Integer> future = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(10000);
                return 100;
            }
        });
        // 主线程通过future来获取结果
        final Integer res = future.get();
        log.debug("jdkFuture获取到的结果：{}",res);
        
        executor.shutdown();
    }
    
    public static void nettyFuture() throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        final EventLoop eventLoop = group.next();

        final io.netty.util.concurrent.Future<Integer> nettyFuture = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("netty Future开始执行");
                Thread.sleep(5000);
                return 300;
            }
        });
        
        log.debug("等待结果");
        log.debug("结果是：{}",nettyFuture.get());
    }
    
    public static void nettyFutureAsync() throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        final EventLoop eventLoop = group.next();

        final io.netty.util.concurrent.Future<Integer> nettyFuture = eventLoop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("netty Future Async开始执行");
                Thread.sleep(5000);
                return 200;
            }
        });

        nettyFuture.addListener((future -> {
            if(future.isSuccess()){
                log.debug("future成功。");
            }
            if(future.isDone()){
                log.debug("future结束");
            }
            log.debug("netty future async获取结果：{}",future.getNow());
        }));
    }
    
    // netty promise样例
    public static void nettyPromise() throws ExecutionException, InterruptedException {
        final EventLoop eventLoop = new NioEventLoopGroup().next();
        final DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
        
        new Thread(()->{
            log.debug("netty promise计算线程，开始计算。");
            try {
                int i = 10/0;
                Thread.sleep(5000);
                promise.setSuccess(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);            
            }
        }).start();
        
        log.debug("netty promise等待结果。");
        log.debug("netty promise结果是：{}",promise.get());
    }
}
