package com.mjduan.project.chapter16_config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * Created by Duan on 2017/4/10.
 */
public class Example1 extends AbstractVerticle {

    public static void main(String[] args){
        Vertx.vertx().deployVerticle(Example1.class.getCanonicalName());
    }

    @Override
    public void start() throws Exception {
        ConfigStoreOptions configStoreOptions = new ConfigStoreOptions().setType("file")
                .setConfig(new JsonObject().put("path", "test.json"));
        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions();
        configRetrieverOptions.setScanPeriod(4_000).addStore(configStoreOptions);;
        ConfigRetriever configRetriever = ConfigRetriever.create(super.vertx, configRetrieverOptions);

        configRetriever.getConfig(jsonObjectAsyncResult -> {
            if (jsonObjectAsyncResult.failed()) {
                jsonObjectAsyncResult.cause().printStackTrace();
            } else {
                JsonObject jsonObject = jsonObjectAsyncResult.result();
                String name = jsonObject.getString("name");
                System.out.println(name);
            }
        });

        //不行，有问题
        configRetriever.listen(change->{
            JsonObject previousConfiguration = change.getPreviousConfiguration();
            JsonObject newConfiguration = change.getNewConfiguration();
            System.out.println("old:"+previousConfiguration);
            System.out.println("new:"+newConfiguration);
        });
    }

}
