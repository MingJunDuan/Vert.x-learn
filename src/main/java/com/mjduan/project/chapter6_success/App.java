package com.mjduan.project.chapter6_success;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * Created by Duan on 2017/3/9.
 */
public class App extends AbstractVerticle{

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
                System.out.println("dmj:"+dmj);
                Buffer buffer=Buffer.buffer(dmj+" 你好!","UTF-8");
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

    public static void runExample(String verticleID){
        VertxOptions vertxOptions = new VertxOptions();
        MessagePassingQueue.Consumer<Vertx> consumer = new MessagePassingQueue.Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(verticleID);
            }
        };
        Vertx vertx = Vertx.vertx(vertxOptions);
        consumer.accept(vertx);
    }

    public static void main(String[] args){
        runExample(App.class.getCanonicalName());
        runExample(App2.class.getCanonicalName());
    }
}
