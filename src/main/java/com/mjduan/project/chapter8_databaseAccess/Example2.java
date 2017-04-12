package com.mjduan.project.chapter8_databaseAccess;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Duan on 2017/4/5.
 */
public class Example2 extends AbstractVerticle {

    public static void main(String[] args){
        Consumer<Vertx> consumer = new Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(Example2.class.getCanonicalName());
            }
        };
        consumer.accept(Vertx.vertx());
    }

    @Override
    public void start() throws Exception {
        JsonObject mySQLClientConfig = new JsonObject();
        mySQLClientConfig.put("url","jdbc:mysql://192.168.56.101:3306/test?useUnicode=true&characterEncoding=UTF-8");
        mySQLClientConfig.put("driver_class","com.mysql.jdbc.Driver");
        mySQLClientConfig.put("user","root");
        mySQLClientConfig.put("password","dmj2010");

        JDBCClient jdbcClient = JDBCClient.createShared(vertx, mySQLClientConfig);
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            SQLConnection sqlConnection = sqlConnectionAsyncResult.result();
            sqlConnection.execute("insert into t_user(name,age) values('Jack',15)",voidAsyncResult -> {
                if (voidAsyncResult.failed()) {
                    System.out.println("insert into failed");
                    return;
                }

                sqlConnection.query("select * from t_user",queryResult->{
                    List<JsonArray> arrays = queryResult.result().getResults();
                    System.out.println("size:"+arrays.size());
                    arrays.stream().forEach(jsonArray->{
                        String line = jsonArray.encode();
                        System.out.println(line);
                    });

                    sqlConnection.close(closeHandler->{
                        if (closeHandler.failed()) {
                            System.out.println("close failed");
                            closeHandler.cause().printStackTrace();
                        }
                    });
                });
            });

            if (sqlConnectionAsyncResult.failed()) {
                System.out.println("failed");
            }
        });
    }
}
