package com.mjduan.project.chapter7;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Duan on 2017/4/1.
 */
public class chapter_7 extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        System.out.println("start method");
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stop method");
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        //Asynchronous Verticle start
        Thread.sleep(3000);
        startFuture.complete();
    }

    public static void main(String[] args){
        //test1();
        //test2();
        test3();
    }

    private static void test3(){
        VertxOptions vertxOptions = new VertxOptions();
        MessagePassingQueue.Consumer<Vertx> consumer = new MessagePassingQueue.Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(chapter_7.class.getCanonicalName());
            }
        };
        Vertx vertx = Vertx.vertx(vertxOptions);
        consumer.accept(vertx);

        WorkerExecutor executor = vertx.createSharedWorkerExecutor("my-worker-pool");
        executor.executeBlocking(future -> {
            try {
                System.out.println("start sleep");
                Thread.sleep(800);
                System.out.println("wake up");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.complete("OK");
        },asyncResult -> {
            System.out.println("result:"+asyncResult.result());
        });

        try {
            //sleep 3s for executor execute complete
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //The worker executor must be closed when it’s not necessary anymore
        executor.close();
        System.out.println("executor closed");
    }

    private static void test2(){
        VertxOptions vertxOptions = new VertxOptions();
        MessagePassingQueue.Consumer<Vertx> consumer = new MessagePassingQueue.Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(chapter_7.class.getCanonicalName());
            }
        };
        Vertx vertx = Vertx.vertx(vertxOptions);
        consumer.accept(vertx);

        vertx.executeBlocking(future -> {
            try {
                System.out.println("start sleep");
                Thread.sleep(400);
                System.out.println("wake up");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.complete();
        },asyncResult -> {
            System.out.println("asyncResult");
        });
        System.out.println("end");
    }

    private static void test1(){
        VertxOptions vertxOptions = new VertxOptions();
        MessagePassingQueue.Consumer<Vertx> consumer = new MessagePassingQueue.Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(chapter_7.class.getCanonicalName());
            }
        };
        Vertx vertx = Vertx.vertx(vertxOptions);
        consumer.accept(vertx);

        //每过1秒就会调用里面的内容
        vertx.setPeriodic(1000,id->{
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
            System.out.println("Time fired\t"+time);
        });

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
        vertx.setTimer(2_000,handler->{
            //过2秒之后会调用这里面的内容，即这个里面的内容只会被调用一次，不会像setPeriodic那样周期性的调用
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
        });
    }


}
