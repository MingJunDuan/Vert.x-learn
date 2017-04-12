package com.mjduan.project.chapter8_databaseAccess;

import com.mjduan.project.chapter8_databaseAccess.model.User;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLRowStream;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Duan on 2017/4/5.
 */
public class Example3 extends AbstractVerticle {
    private JDBCClient jdbcClient;

    public static void main(String[] args) {
        Consumer<Vertx> consumer = new Consumer<Vertx>() {
            @Override
            public void accept(Vertx vertx) {
                vertx.deployVerticle(Example3.class.getCanonicalName());
            }
        };
        consumer.accept(Vertx.vertx());
    }

    private void testAdd() {
        User user = new User();
        user.setAge(45);
        user.setName("Jack");
        add(user);
    }

    private void testAddBatch() {
        List<User> users = Arrays.asList(new User("name1", 13), new User("name2", 14), new User("name3", 15));
        batchAdd(users);
    }

    @Override
    public void start() throws Exception {
        JsonObject mySQLClientConfig = new JsonObject();
        mySQLClientConfig.put("url", "jdbc:mysql://192.168.56.101:3306/test?useUnicode=true&characterEncoding=UTF-8");
        mySQLClientConfig.put("driver_class", "com.mysql.jdbc.Driver");
        mySQLClientConfig.put("user", "root");
        mySQLClientConfig.put("password", "dmj2010");
        jdbcClient = JDBCClient.createShared(vertx, mySQLClientConfig);

        //testAdd();
        //query();
        //testAddBatch();
        queryWithStream();
    }

    public void add(User user) {
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
                System.out.println(updated + "\t" + id);

                connection.close(closeHandler -> {
                    if (closeHandler.failed()) {
                        closeHandler.cause().printStackTrace();
                    }
                });
            });
        });
        System.out.println("user'id should be 0" + user);
    }

    public void query() {
        jdbcClient.getConnection(connectionAsyncResult -> {
            if (connectionAsyncResult.failed()) {
                connectionAsyncResult.cause().printStackTrace();
                return;
            }
            SQLConnection sqlConnection = connectionAsyncResult.result();
            String sql = "select * from t_user";
            sqlConnection.query(sql, resultSetAsyncResult -> {
                if (resultSetAsyncResult.failed()) {
                    resultSetAsyncResult.cause().printStackTrace();
                    return;
                }

                ResultSet resultSet = resultSetAsyncResult.result();
                List<JsonObject> rows = resultSet.getRows();
                System.out.println("row size:" + rows.size());
                rows.stream()
                        //jsonObject的内容：{"id":1,"name":"Jack","age":15}
                        //content of jsonObject: {"id":1,"name":"Jack","age":15}
                        .map(jsonObject -> jsonObject.mapTo(User.class))
                        .forEach(user -> System.out.println(user));

                sqlConnection.close(closeHandler -> {
                    if (closeHandler.failed()) {
                        closeHandler.cause().printStackTrace();
                        return;
                    }
                });
            });
        });
    }

    public void queryWithStream() {
        jdbcClient.getConnection(connectionAsyncResult -> {
            if (connectionAsyncResult.failed()) {
                connectionAsyncResult.cause().printStackTrace();
                return;
            }
            SQLConnection sqlConnection = connectionAsyncResult.result();
            String sql = "select * from t_user";
            sqlConnection.queryStream(sql, streamAsyncResult -> {
                if (streamAsyncResult.failed()) {
                    streamAsyncResult.cause().printStackTrace();
                    return;
                }

                SQLRowStream rowStream = streamAsyncResult.result();
                rowStream.resultSetClosedHandler(handler -> {
                    System.out.println("more more");
                    rowStream.moreResults();
                }).handler(jsonArray -> {
                    String encode = jsonArray.encode();
                    String encodePrettily = jsonArray.encodePrettily();
                    System.out.println(encode + "\t" + encodePrettily);
                }).endHandler(handler -> {
                    System.out.println("close all resource");
                    rowStream.close();
                    sqlConnection.close();
                });
            });
        });
    }

    public void batchAdd(List<User> users) {
        jdbcClient.getConnection(connectionAsyncResult -> {
            if (connectionAsyncResult.failed()) {
                connectionAsyncResult.cause().printStackTrace();
                return;
            }
            List<JsonArray> jsonArrays = users.stream().map(user -> new JsonArray().add(user.getName()).add(user.getAge())).collect(Collectors.toList());

            SQLConnection sqlConnection = connectionAsyncResult.result();
            String sql = "insert into t_user(name,age) values(?,?)";
            sqlConnection.batchWithParams(sql, jsonArrays, batchAsyncResult -> {
                if (batchAsyncResult.failed()) {
                    batchAsyncResult.cause().printStackTrace();
                    return;
                }

                //返回的全是1、1、1、1，不是生成的主键
                //the return value is not expected primary key(auto_increment value by database)
                batchAsyncResult.result().stream().forEach(i -> System.out.println(i));

                sqlConnection.close();
            });
        });

        //上面的操作都是异步的，非阻塞式的
        //this statement is for testing jdbcClient.getConnection(...) is asynchronous, not blocking by jdbc operation.
        System.out.println("end");
    }


}
