package com.mjduan.project.chapter9_webServer.example6;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Created by Duan on 2017/4/7.
 */
public class Example6_2 extends AbstractVerticle{
    private HttpServer server;

    public Example6_2(HttpServer server) {
        this.server = server;
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(super.vertx);
        router.get("/products2").handler(requestHandler->{
            HttpServerResponse response = requestHandler.response();
            response.setChunked(true);
            response.write("products\n").write("product1\n").write("from example6_2");
            response.end();
        });

        server.requestHandler(request -> router.accept(request));
    }
}
