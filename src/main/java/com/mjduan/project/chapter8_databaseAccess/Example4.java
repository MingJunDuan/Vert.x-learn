package com.mjduan.project.chapter8_databaseAccess;

import com.mjduan.project.chapter8_databaseAccess.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

/**
 * Created by Duan on 2017/4/5.
 */
public class Example4 extends AbstractVerticle {
    private JDBCClient jdbcClient;

    public static void main(String[] args) {
        Consumer<Vertx> consumer = new Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(Example4.class.getCanonicalName());
            }
        };
        consumer.accept(Vertx.vertx());
    }

    @Override
    public void start() throws Exception {
        JsonObject mySQLClientConfig = new JsonObject();
        mySQLClientConfig.put("url", "jdbc:mysql://192.168.56.101:3306/test?useUnicode=true&characterEncoding=UTF-8");
        mySQLClientConfig.put("driver_class", "com.mysql.jdbc.Driver");
        mySQLClientConfig.put("user", "root");
        mySQLClientConfig.put("password", "123");
        jdbcClient = JDBCClient.createShared(vertx, mySQLClientConfig);

        testAdd();
        //query();
        //testAddBatch();
    }

    private void testAdd() {
        User user = new User();
        user.setAge(23);
        user.setName("Jack");

        super.vertx.executeBlocking((Future<User> future) -> {
            System.out.println("before add method:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
            add(user, future);
        }, asyncResult -> {
            User u = asyncResult.result();
            System.out.println("asyncResult method:" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
            System.out.println("asyncResult:" + u);
        });

    }

    public void add(User user, Future<User> future) {
        jdbcClient.getConnection(connectionAsyncResult -> {
            if (connectionAsyncResult.failed()) {
                connectionAsyncResult.cause().printStackTrace();
                return;
            }
            String sql = "insert into t_user(name,age) values(?,?)";
            JsonArray jsonArray = new JsonArray().add(user.getName()).add(user.getAge());
            SQLConnection connection = connectionAsyncResult.result();
            connection.updateWithParams(sql, jsonArray, updateResultAsyncResult -> {
                if (updateResultAsyncResult.failed()) {
                    updateResultAsyncResult.cause().printStackTrace();
                    return;
                }

                int updated = updateResultAsyncResult.result().getUpdated();
                Integer id = updateResultAsyncResult.result().getKeys().getInteger(0);
                user.setId(id);
                System.out.println("after set id value");
                future.complete(user);
                System.out.println(updated + "\t" + id);

                connection.close(closeHandler -> {
                    if (closeHandler.failed()) {
                        closeHandler.cause().printStackTrace();
                    }
                    System.out.println("close connection " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")));
                });
            });
        });
        System.out.println("user'id should be 0" + user);
    }

}
