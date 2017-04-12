package com.mjduan.project.chapter10_webClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.WebClient;


/**
 * Created by Duan on 2017/4/11.
 */
public class Example2 extends AbstractVerticle {

    public static void main(String[] args){
        Vertx.vertx().deployVerticle(Example2.class.getCanonicalName());
    }

    @Override
    public void start() throws Exception {
        String s = "[{\"uri\":\"/uri3/:id\",\"method\":\"PUT\",\"ipv4\":\"127.0.0.1\",\"port\":8085,\"description\":\"desc\"},{\"uri\":\"/uri4/:name\",\"method\":\"ALL\",\"ipv4\":\"127.0.0.2\",\"port\":8085,\"description\":\"desc\"}]";
        Buffer buffer = Buffer.buffer(s);
        WebClient client = WebClient.create(vertx);
        client.post(8081,"localhost","/registerServer")
                .sendBuffer(buffer,httpResponseAsyncResult -> {
                    if (httpResponseAsyncResult.failed()) {
                        httpResponseAsyncResult.cause().printStackTrace();
                    } else {
                        System.out.println("ok");
                    }
                });
    }
}