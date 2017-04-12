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
public class Example4 extends AbstractVerticle {

    public static void main(String[] args){
        Consumer<Vertx> consumer = new Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(Example4.class.getCanonicalName());
            }
        };
        consumer.accept(Vertx.vertx());
    }

    @Override
    public void start() throws Exception {
        HttpServer httpServer = super.vertx.createHttpServer();

        Router router = Router.router(super.vertx);
        Route postRoute = router.route(HttpMethod.POST, "/catalogue/products/:producttype/:productid/");
        postRoute.handler(routingContext -> {
            String producttype = routingContext.request().getParam("producttype").trim();
            String productid = routingContext.request().getParam("productid").trim();
            System.out.println("post\t"+producttype+"\t"+productid);

            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("Post method\n");
            response.write("producttype:"+producttype+"\n");
            response.write("productid:"+productid+"\n");
            response.write("from server");
            response.end();
        });

        Route getRoute = router.route(HttpMethod.GET, "/catalogue/products/:producttype/:productid/");
        getRoute.handler(routingContext -> {
            String producttype = routingContext.request().getParam("producttype").trim();
            String productid = routingContext.request().getParam("productid").trim();
            System.out.println("get\t"+producttype+"\t"+productid);

            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("Get method\n");
            response.write("producttype:"+producttype+"\n");
            response.write("productid:"+productid+"\n");
            response.write("from server");
            response.end();
        });

        httpServer.requestHandler(request ->  router.accept(request)).listen(8080);
    }
}
