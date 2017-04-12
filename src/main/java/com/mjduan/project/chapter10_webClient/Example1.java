package com.mjduan.project.chapter10_webClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.nio.charset.Charset;
import java.util.function.Consumer;

/**
 * Created by Duan on 2017/4/5.
 */
public class Example1 extends AbstractVerticle {

    public static void main(String[] args) {
        Consumer<Vertx> consumer = new Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(Example1.class.getCanonicalName());
            }
        };
        consumer.accept(Vertx.vertx());
    }

    @Override
    public void start() throws Exception {
        WebClient webClient = WebClient.create(super.vertx);

        webClient
                .get(8080,"localhost","/path/some")
                .send(httpResponseAsyncResult -> {
                    if (httpResponseAsyncResult.succeeded()) {
                        HttpResponse<Buffer> result = httpResponseAsyncResult.result();
                        String s = result.bodyAsBuffer().toString(Charset.forName("utf-8"));
                        System.out.println("Received response with status code:" + result.statusCode()+"\t"+s);
                    } else {
                        System.out.println("Something went wrong " + httpResponseAsyncResult.cause().getMessage());
                    }
                });
    }
}
