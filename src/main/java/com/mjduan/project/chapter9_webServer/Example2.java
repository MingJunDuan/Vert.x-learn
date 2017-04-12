package com.mjduan.project.chapter9_webServer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.util.function.Consumer;

/**
 * Created by Duan on 2017/4/5.
 */
public class Example2 extends AbstractVerticle {

    public static void main(String[] args){
        Consumer<Vertx> consumer = new Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(Example2.class.getCanonicalName());
            }
        };
        consumer.accept(Vertx.vertx());
    }

    @Override
    public void start() throws Exception {
        HttpServer httpServer = super.vertx.createHttpServer();

        Router router = Router.router(super.vertx);
        Route route1 = router.route("/path/some").blockingHandler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("router1\n");
            routingContext.next();
        });

        Route route2 = router.route("/path/some").blockingHandler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("router2\n");
            routingContext.next();
        });

        Route route3 = router.route("/path/some").blockingHandler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("router3\n");
            response.end();
        });


        httpServer.requestHandler(request ->  router.accept(request)).listen(8080);
    }
}
