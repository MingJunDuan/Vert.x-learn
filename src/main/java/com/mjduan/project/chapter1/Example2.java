package com.mjduan.project.chapter1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import org.junit.Test;
import org.junit.runner.Runner;

/**
 * Created by Duan on 2017/3/9.
 */
public class Example2 {

    class Server extends AbstractVerticle{
        @Override
        public void start() throws Exception {
            vertx.createHttpServer().requestHandler(new Handler<HttpServerRequest>() {
                @Override
                public void handle(HttpServerRequest request) {
                    request.response().putHeader("contect-type","text/html").end("<html><body><h1>Hello from vert.x!</h1></body></html>");
                }
            }).listen(8080);
        }
    }
    @Test
    public void test1(){
    }


}
