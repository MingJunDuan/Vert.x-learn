package com.mjduan.project.chapter6_success;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;

import java.nio.charset.Charset;

/**
 * Created by Duan on 2017/3/10.
 */
public class Client{

    public static void main(String[] args) throws InterruptedException {
        /*Vertx vertx = Vertx.vertx();
        HttpClient httpClient = vertx.createHttpClient();

        httpClient.getNow(8080, "localhost", "/hello", new Handler<HttpClientResponse>() {
            @Override
            public void handle(HttpClientResponse httpClientResponse) {
                int statusCode = httpClientResponse.statusCode();
                httpClientResponse.bodyHandler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        String s = buffer.toString(Charset.forName("utf-8"));
                        System.out.println(s);
                    }
                });
            }
        });
        HttpClientRequest request = httpClient.get(8080, "localhost", "/hello");
        request.w*/
        Buffer buffer = Buffer.buffer("dmj=Jack", "UTF-8");

        Vertx vertx = Vertx.vertx();
        HttpClient httpClient = vertx.createHttpClient();
        HttpClientRequest request = httpClient.get(8080, "localhost", "/hello", new Handler<HttpClientResponse>() {
            @Override
            public void handle(HttpClientResponse httpClientResponse) {
                httpClientResponse.bodyHandler(new Handler<Buffer>() {
                    @Override
                    public void handle(Buffer buffer) {
                        String s = buffer.toString(Charset.forName("utf-8"));
                        System.out.println("client REC:"+s);
                    }
                });
            }
        });
        request.putHeader("content-length", buffer.length()+"");
        request.putHeader("content-type", "text/plain;charset=utf-8");

        request.end(buffer);

        Thread.sleep(3000);
    }

}
