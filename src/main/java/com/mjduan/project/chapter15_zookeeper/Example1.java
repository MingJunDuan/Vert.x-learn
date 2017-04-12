package com.mjduan.project.chapter15_zookeeper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager;

/**
 * Created by Duan on 2017/4/10.
 */
public class Example1 extends AbstractVerticle {

    public static void main(String[] args){
        Vertx.vertx().deployVerticle(Example1.class.getCanonicalName());
    }

    @Override
    public void start() throws Exception {
        JsonObject zkConfig = new JsonObject();
        zkConfig.put("zookeeperHosts", "192.168.56.101");
        zkConfig.put("rootPath", "io.vertx");
        zkConfig.put("retry", new JsonObject()
                .put("initialSleepTime", 3000)
                .put("maxTimes", 3));

        ZookeeperClusterManager clusterManager = new ZookeeperClusterManager(zkConfig);
        VertxOptions vertxOptions = new VertxOptions().setClusterManager(clusterManager);
        Vertx.clusteredVertx(vertxOptions,vertxAsyncResult -> {
            if (vertxAsyncResult.failed()) {
                vertxAsyncResult.cause().printStackTrace();
            } else {
                System.out.println("success");
            }
        });

    }
}
