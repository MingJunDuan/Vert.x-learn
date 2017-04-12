package com.mjduan.project.chapter9_webServer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

import java.util.function.Consumer;

/**
 * Created by Duan on 2017/4/5.
 */
public class Example3 extends AbstractVerticle {

    public static void main(String[] args){
        Consumer<Vertx> consumer = new Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(Example3.class.getCanonicalName());
            }
        };
        consumer.accept(Vertx.vertx());
    }

    @Override
    public void start() throws Exception {
        HttpServer httpServer = super.vertx.createHttpServer();

        Router router = Router.router(super.vertx);
        Route route = router.route(HttpMethod.POST, "/catalogue/products/:producttype/:productid/");
        route.handler(routingContext -> {
            String producttype = routingContext.request().getParam("producttype");
            String productid = routingContext.request().getParam("productid");
            System.out.println(producttype+"\t"+productid);

            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("producttype:"+producttype+"\n");
            response.write("productid:"+productid+"\n");
            response.write("from server");
            response.end();
        });

        httpServer.requestHandler(request ->  router.accept(request)).listen(8080);
    }
}
