package com.mjduan.project.chapter6_success;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

import java.nio.charset.Charset;

/**
 * Created by Duan on 2017/3/10.
 */
public class Client2 {

    public static void main(String[] args) throws InterruptedException {
        WebClient webClient = WebClient.create(Vertx.vertx());
        HttpRequest<Buffer> localhost = webClient.get(8080, "localhost", "/hello");
        localhost.addQueryParam("dmj","Alice").addQueryParam("Duan","MM");
        localhost.send(new Handler<AsyncResult<HttpResponse<Buffer>>>() {
            @Override
            public void handle(AsyncResult<HttpResponse<Buffer>> httpResponseAsyncResult) {
                if (httpResponseAsyncResult.succeeded()) {
                    HttpResponse<Buffer> result = httpResponseAsyncResult.result();
                    int i = result.statusCode();
                    System.out.println("code:" + i);
                    String s = result.bodyAsString();
                    System.out.println(s);
                } else {
                    System.out.println(httpResponseAsyncResult.cause().getMessage());
                }
            }
        });

        Thread.sleep(3000);
    }


}
