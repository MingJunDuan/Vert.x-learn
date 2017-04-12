package com.mjduan.project.chapter4;

import io.vertx.core.Vertx;
import org.junit.Test;

/**
 * Created by Duan on 2017/3/9.
 */
public class Example1 {

    @Test
    public void test1() throws InterruptedException {

    }

    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MyVerticleReceiver("R1"));
        vertx.deployVerticle(new MyVerticleReceiver("R2"));

        Thread.sleep(4000);
        vertx.deployVerticle(new MyVerticleSender());
    }

}
