package com.mjduan.project.chapter4;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Created by Duan on 2017/3/9.
 */
public class MyVerticleSender extends AbstractVerticle {

    private String address="anAddress";

    @Override
    public void start(Future<Void> startFuture) throws Exception {
       /* publish()方法发送的消息会被监听在该地址的所有verticle接收，注意是所有。
        send()方法发送的消息只会被监听在该地址的其中一个verticle接收*/
        vertx.eventBus().publish(address,"message 1");
        vertx.eventBus().send(address,"message 2");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
