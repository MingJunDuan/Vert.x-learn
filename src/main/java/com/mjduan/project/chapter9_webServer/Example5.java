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
public class Example5 extends AbstractVerticle {

    public static void main(String[] args) {
        Consumer<Vertx> consumer = new Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(Example5.class.getCanonicalName());
            }
        };
        consumer.accept(Vertx.vertx());
    }

    @Override
    public void start() throws Exception {
        HttpServer httpServer = super.vertx.createHttpServer();
        Router router = Router.router(super.vertx);

        router.put("/products/:productID").blockingHandler(routingContext -> {
            String productID = routingContext.request().getParam("productID");
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            System.out.println("put Http method");
            response.write("put method\n").write("productID:" + productID + "\n from server").end();
        });

        router.get("/products/:productID").blockingHandler(routingContext -> {
            String productID = routingContext.request().getParam("productID");
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            System.out.println("get Http method");
            response.write("get method\n").write("productID:" + productID + "\n from server").end();
        });

        Router rootRouter = Router.router(super.vertx);
        rootRouter.mountSubRouter("/productsAPI", router);

        httpServer.requestHandler(request -> rootRouter.accept(request)).listen(8080);
    }
}
