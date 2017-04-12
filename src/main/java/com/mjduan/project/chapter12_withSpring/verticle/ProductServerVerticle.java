package com.mjduan.project.chapter12_withSpring.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * Created by Duan on 2017/4/6.
 */
public class ProductServerVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        HttpServer server = super.vertx.createHttpServer();
        Router router = Router.router(super.vertx);

        router.route(HttpMethod.GET,"/products")
            .handler(routingContext -> {
                routingContext.response().setChunked(true);
                super.vertx.eventBus().<String>send(ProductVerticle.PRODUCTS_ADDRESS,"",messageAsyncResult -> {
                    if (messageAsyncResult.succeeded()) {
                        routingContext.response().setStatusCode(200).write(messageAsyncResult.result().body()).end();
                    } else {
                        routingContext.response().setStatusCode(500).write(messageAsyncResult.cause().toString()).end();
                    }
                });
            });
        server.requestHandler(request -> router.accept(request)).listen(8080);
    }

}
