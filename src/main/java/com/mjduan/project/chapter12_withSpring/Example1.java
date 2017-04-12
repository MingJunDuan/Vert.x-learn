package com.mjduan.project.chapter12_withSpring;

import com.mjduan.project.chapter12_withSpring.config.SpringConfig;
import com.mjduan.project.chapter12_withSpring.verticle.ProductServerVerticle;
import com.mjduan.project.chapter12_withSpring.verticle.ProductVerticle;
import io.vertx.core.Vertx;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Duan on 2017/4/6.
 */
public class Example1 {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(ProductServerVerticle.class.getCanonicalName());
        vertx.deployVerticle(new ProductVerticle(context));
        System.out.println("server up!");
    }

}
