package com.mjduan.project.chapter17_db_demo.dao;

import com.mjduan.project.chapter17_db_demo.entity.Mypay;
import com.mjduan.project.chapter17_db_demo.Util;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by duan on 2017/4/18.
 */
public class MypayDaoVerticle extends AbstractVerticle {
    private JDBCClient jdbcClient;

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx(new VertxOptions());
        vertx.deployVerticle(MypayDaoVerticle.class.getCanonicalName());
    }

    @Override
    public void start() throws Exception {
        jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", "jdbc:mysql://127.0.0.1:3306/mybank2?useUnicode=true&characterEncoding=UTF-8")
                .put("driver_class", "com.mysql.jdbc.Driver")
                .put("user", "root")
                .put("password", "123")
                .put("max_pool_size", 30));

        addBatch(Util.getMypay(),Future.future());
    }

    public void addBatch(List<Mypay> mypays, Future<Boolean> future) {
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            if (sqlConnectionAsyncResult.failed()) {
                sqlConnectionAsyncResult.cause().printStackTrace();
                return;
            }
            List<JsonArray> params = mypays.stream()
                    .map(mypay -> Util.toJsonArray(mypay.getMoney().floatValue(),mypay.getRemark(),mypay.getUser(),mypay.getTime()))
                    .collect(Collectors.toList());
            String sql = "insert into test5(money,remark,user,time) values(?,?,?,?)";
            SQLConnection connection = sqlConnectionAsyncResult.result();
            connection.batchWithParams(sql,params,listAsyncResult -> {
                if (listAsyncResult.failed()) {
                    listAsyncResult.cause().printStackTrace();
                    connection.close();
                    return;
                }
                long count = listAsyncResult.result().stream().count();
                System.out.println(count);
                connection.close();
            });
        });
    }

    @Override
    public void stop() throws Exception {
        jdbcClient.close();
    }

    public void test1(){
        jdbcClient.getConnection(handler -> {
            if (handler.failed()) {
                handler.cause().printStackTrace();
                return;
            }

            SQLConnection connection = handler.result();
            connection.execute("create table test(id int primary key, name varchar(255))", res -> {
                if (res.failed()) {
                    res.cause().printStackTrace();
                    return;
                }

                connection.execute("insert into test values(1, 'Hello')", insert -> {
                    connection.query("select * from test", queryResult -> {
                        for (JsonArray line : queryResult.result().getResults()) {
                            System.out.println(line.encode());
                        }

                        connection.close(close -> {
                            if (close.failed()) {
                                close.cause().printStackTrace();
                            }
                        });
                    });
                });
            });
        });

    }

}
