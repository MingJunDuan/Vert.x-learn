package com.mjduan.project.chapter14;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.HealthChecks;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;

/**
 * Created by Duan on 2017/4/7.
 */
public class Example1 extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(Example1.class.getCanonicalName());
    }


    @Override
    public void start() throws Exception {
        HttpServer httpServer = super.vertx.createHttpServer();
        Router router = Router.router(super.vertx);

        HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(super.vertx);
        healthCheckHandler.register("productsHealth",statusFuture -> {
            //if something is true
            statusFuture.complete(Status.OK(new JsonObject().put("load",99)));
        });

        router.put("/products/:productID").blockingHandler(routingContext -> {
            String productID = routingContext.request().getParam("productID");
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            System.out.println("put http method");
            response.write("put method\n").write("productID:" + productID + "\nfrom server").end();
        });

        router.get("/products/:productID").blockingHandler(routingContext -> {
            String productID = routingContext.request().getParam("productID");
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            System.out.println("get http method");
            response.write("get method\n").write("productID:" + productID + "\nfrom server").end();
        });

        Router rootRouter = Router.router(super.vertx);
        rootRouter.mountSubRouter("/productsAPI", router);

        httpServer.requestHandler(request -> rootRouter.accept(request)).listen(8080);
    }
}
