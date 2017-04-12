package com.mjduan.project.chapter8_databaseAccess;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Duan on 2017/4/5.
 */
public class Example1 extends AbstractVerticle {

    /**
     * en: maven dependency
     * cn: 需要的依赖是
     * <dependency>
         <groupId>io.vertx</groupId>
         <artifactId>vertx-mysql-postgresql-client</artifactId>
         <version>3.4.1</version>
       </dependency>
       <dependency>
         <groupId>org.slf4j</groupId>
         <artifactId>slf4j-api</artifactId>
         <version>1.7.22</version>
       </dependency>
     *
     * @param args
     */
    public static void main(String[] args){
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
        JsonObject mySQLClientConfig = new JsonObject();
        mySQLClientConfig.put("host","localhost");
        mySQLClientConfig.put("port",3306);
        mySQLClientConfig.put("username","root");
        mySQLClientConfig.put("password","123");
        mySQLClientConfig.put("database","test");
        AsyncSQLClient client = MySQLClient.createShared(vertx, mySQLClientConfig);

        client.getConnection(handler->{
            if (handler.failed()) {
                handler.cause().printStackTrace();
                return;
            }
            SQLConnection sqlConnection = handler.result();
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

            if (handler.failed()) {
                System.out.println("failed");
            }
        });
    }
}
