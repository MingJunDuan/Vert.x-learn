package com.mjduan.project.chapter6_success;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by Duan on 2017/3/10.
 */
public class App2 extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/hello").handler(new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                HttpServerResponse httpServerResponse = routingContext.response()
                        .putHeader("content-type", "text/html;charset=utf-8");
                String dmj = routingContext.request().getParam("dmj");
                System.out.println("app2 dmj:"+dmj);
                Buffer buffer=Buffer.buffer(dmj+" 你好! from app2","UTF-8");
                httpServerResponse.putHeader("Content-Length",buffer.length()+"");
                httpServerResponse.write(buffer);
                httpServerResponse.end();
            }
        });
        HttpServer httpServer = vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest request) {
                router.accept(request);
            }
        });
        httpServer.listen(8080);
    }

}
