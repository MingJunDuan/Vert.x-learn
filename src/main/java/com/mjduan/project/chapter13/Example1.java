package com.mjduan.project.chapter13;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.consul.ConsulClient;
import io.vertx.ext.consul.ConsulClientOptions;

/**
 * Created by Duan on 2017/4/7.
 */
public class Example1 extends AbstractVerticle {

    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Example1.class.getCanonicalName());
    }

    @Override
    public void start() throws Exception {
        ConsulClient consulClient = ConsulClient.create(super.vertx, new ConsulClientOptions().setHost("192.168.99.101"));
        consulClient.putValue("key","value",booleanAsyncResult -> {
            if (booleanAsyncResult.succeeded()) {
                System.out.println("ok");
            } else {
                booleanAsyncResult.cause().printStackTrace();
                System.out.println("no");
            }
        });
    }
}
