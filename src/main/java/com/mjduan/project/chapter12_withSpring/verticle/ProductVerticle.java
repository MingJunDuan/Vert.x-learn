package com.mjduan.project.chapter12_withSpring.verticle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mjduan.project.chapter12_withSpring.entity.Product;
import com.mjduan.project.chapter12_withSpring.service.ProductServiceImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by Duan on 2017/4/6.
 */
public class ProductVerticle extends AbstractVerticle {
    public static final String PRODUCTS_ADDRESS = "products_address";

    private final ProductServiceImpl productService;

    public ProductVerticle(final ApplicationContext context) {
        productService = context.getBean(ProductServiceImpl.class);
    }

    @Override
    public void start() throws Exception {
        super.vertx.eventBus().consumer(PRODUCTS_ADDRESS,productsHandler(productService));
    }

    private Handler<Message<String>> productsHandler(ProductServiceImpl productService){
        return msg -> vertx.<String>executeBlocking(future -> {
            List<Product> products = productService.products();
            try {
                String s = Json.mapper.writeValueAsString(products);
                future.complete(s);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                future.fail(e);
            }
        }, asyncResult -> {
            if (asyncResult.succeeded()) {
                msg.reply(asyncResult.result());
            } else {
                msg.reply(asyncResult.cause().toString());
            }
        });
    }

}
