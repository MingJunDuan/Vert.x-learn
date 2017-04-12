package com.mjduan.project.chapter4;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Created by Duan on 2017/3/9.
 */
public class MyVerticleReceiver extends AbstractVerticle {
    private String name;

    public MyVerticleReceiver(String name) {
        this.name = name;
    }

    @Override
    public void start() throws Exception {
        System.out.println(name+ " started");
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().consumer("anAddress",message -> {
            System.out.println( name+" Rec:"+message.body());
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println(name+ " stop");
    }
}
