package com.mjduan.project.chapter9_webServer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

import java.util.function.Consumer;

/**
 * Created by Duan on 2017/4/5.
 */
public class Example1 extends AbstractVerticle {

    public static void main(String[] args) {
        Consumer<Vertx> consumer = new Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(Example1.class.getCanonicalName());
            }
        };
        consumer.accept(Vertx.vertx());
    }

    @Override
    public void start() throws Exception {
        HttpServer httpServer = super.vertx.createHttpServer();

        Router router = Router.router(super.vertx);
        router.route().handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");

            // Write to the response and end it
            response.end("Hello World from Vert.x-Web!");
        });

        httpServer.requestHandler(request ->  router.accept(request)).listen(8080);
    }
}
