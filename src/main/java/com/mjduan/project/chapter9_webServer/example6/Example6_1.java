package com.mjduan.project.chapter9_webServer.example6;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Created by Duan on 2017/4/7.
 */
public class Example6_1 extends AbstractVerticle {

    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Example6_1.class.getCanonicalName());
        vertx.deployVerticle(Example6_2.class.getCanonicalName());
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(super.vertx);
        router.get("/products1").handler(requestHandler->{
            HttpServerResponse response = requestHandler.response();
            response.setChunked(true);
            response.write("products\n").write("product1\n").write("from example6_1");
            response.end();
        });

        HttpServer server = super.vertx.createHttpServer();
        server.requestHandler(request -> router.accept(request)).listen(8081);
    }
}
