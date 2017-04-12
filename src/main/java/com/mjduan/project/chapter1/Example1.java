package com.mjduan.project.chapter1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;

/**
 * Created by Duan on 2017/3/9.
 */
public class Example1 extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServer httpServer = vertx.createHttpServer()
                .requestHandler(new Handler<HttpServerRequest>() {
                    @Override
                    public void handle(HttpServerRequest request) {
                        request.response().end("<h1>Hello from my first " +
                                "Vert.x 3 application</h1>");
                    }
                }).listen(8080, new Handler<AsyncResult<HttpServer>>() {
                    @Override
                    public void handle(AsyncResult<HttpServer> httpServerAsyncResult) {
                        if (httpServerAsyncResult.succeeded()) {
                            startFuture.complete();
                        } else {
                            startFuture.fail(httpServerAsyncResult.cause());
                        }
                    }
                });
    }
}
