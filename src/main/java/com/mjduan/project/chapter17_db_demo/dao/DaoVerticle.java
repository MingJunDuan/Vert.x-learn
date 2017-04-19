package com.mjduan.project.chapter17_db_demo.dao;

import com.mjduan.project.chapter17_db_demo.Util;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.SQLRowStream;

import java.util.Collections;
import java.util.List;

/**
 * Created by duan on 2017/4/18.
 */
public class DaoVerticle extends AbstractVerticle {
    private JDBCClient jdbcClient;

    public DaoVerticle(Vertx vertx) {
        jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", "jdbc:mysql://127.0.0.1:3306/mybank2?useUnicode=true&characterEncoding=UTF-8")
                .put("driver_class", "com.mysql.jdbc.Driver")
                .put("user", "root")
                .put("password", "123")
                .put("max_pool_size", 30));
    }

    @Override
    public void stop() throws Exception {
        jdbcClient.close();
    }

    //增加
    public void add(String sql, JsonArray param, Handler<AsyncResult<Integer>> updateResult) {
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            if (sqlConnectionAsyncResult.failed()) {
                sqlConnectionAsyncResult.cause().printStackTrace();
                return;
            }
            SQLConnection connection = sqlConnectionAsyncResult.result();
            connection.updateWithParams(sql, param, updateResultAsyncResult -> {
                if (updateResultAsyncResult.failed()) {
                    updateResultAsyncResult.cause().printStackTrace();
                    updateResult.handle(Future.failedFuture(updateResultAsyncResult.cause()));
                } else {
                    Integer id = updateResultAsyncResult.result().getKeys().getInteger(0);
                    updateResult.handle(Future.succeededFuture(id));
                }
                connection.close();
            });
        });
    }

    public void update(String sql, JsonArray param, Handler<AsyncResult<Boolean>> updateResult) {
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            if (sqlConnectionAsyncResult.failed()) {
                sqlConnectionAsyncResult.cause().printStackTrace();
                return;
            }
            SQLConnection connection = sqlConnectionAsyncResult.result();
            connection.updateWithParams(sql, param, updateResultAsyncResult -> {
                if (updateResultAsyncResult.failed()) {
                    updateResultAsyncResult.cause().printStackTrace();
                    updateResult.handle(Future.failedFuture(updateResultAsyncResult.cause()));
                } else {
                    updateResult.handle(Future.succeededFuture(true));
                }
                connection.close();
            });
        });
    }

    public void updateBatch(String sql, List<JsonArray> params, Handler<AsyncResult<Boolean>> updateBatchResult) {
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            if (sqlConnectionAsyncResult.failed()) {
                sqlConnectionAsyncResult.cause().printStackTrace();
                return;
            }
            SQLConnection connection = sqlConnectionAsyncResult.result();
            connection.batchWithParams(sql, params, listAsyncResult -> {
                if (listAsyncResult.failed()) {
                    listAsyncResult.cause().printStackTrace();
                    updateBatchResult.handle(Future.failedFuture(listAsyncResult.cause()));
                } else {
                    updateBatchResult.handle(Future.succeededFuture(params.size() == listAsyncResult.result().stream().count()));
                }
                connection.close();
            });
        });
    }

    public <T> void queryMulti(String sql, JsonArray param, Class<T> clazz, Handler<AsyncResult<List<T>>> asyncResultHandler) {
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            if (sqlConnectionAsyncResult.failed()) {
                sqlConnectionAsyncResult.cause().printStackTrace();
                return;
            }
            SQLConnection connection = sqlConnectionAsyncResult.result();
            connection.queryWithParams(sql, param, resultSetAsyncResult -> {
                if (resultSetAsyncResult.failed()) {
                    resultSetAsyncResult.cause().printStackTrace();
                    asyncResultHandler.handle(Future.failedFuture(resultSetAsyncResult.cause()));
                } else {
                    List<JsonObject> rows = resultSetAsyncResult.result().getRows();
                    if (rows.size() == 0) {
                        asyncResultHandler.handle(Future.succeededFuture(Collections.<T>emptyList()));
                    } else {
                        asyncResultHandler.handle(Future.succeededFuture(Util.toObjects(rows, clazz)));
                    }
                }
                connection.close();
            });
        });
    }

    public <T> void queryOne(String sql, JsonArray param, Class<T> clazz, Handler<AsyncResult<T>> asyncResultHandler) {
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            if (sqlConnectionAsyncResult.failed()) {
                sqlConnectionAsyncResult.cause().printStackTrace();
                return;
            }
            SQLConnection connection = sqlConnectionAsyncResult.result();
            connection.queryWithParams(sql, param, resultSetAsyncResult -> {
                if (resultSetAsyncResult.failed()) {
                    resultSetAsyncResult.cause().printStackTrace();
                    asyncResultHandler.handle(Future.failedFuture(resultSetAsyncResult.cause()));
                } else {
                    List<JsonObject> rows = resultSetAsyncResult.result().getRows();
                    if (rows.size() == 0) {
                        asyncResultHandler.handle(Future.failedFuture("no rows"));
                    } else if (rows.size() == 1) {
                        asyncResultHandler.handle(Future.succeededFuture(rows.get(0).mapTo(clazz)));
                    } else if (rows.size() > 1) {
                        asyncResultHandler.handle(Future.failedFuture("mutil rows"));
                    }
                }
                connection.close();
            });
        });
    }

    public <T> void queryMultiStream(String sql, JsonArray param, Class<T> clazz, Handler<AsyncResult<List<T>>> asyncResultHandler) {
        jdbcClient.getConnection(sqlConnectionAsyncResult -> {
            if (sqlConnectionAsyncResult.failed()) {
                sqlConnectionAsyncResult.cause().printStackTrace();
                return;
            }
            SQLConnection connection = sqlConnectionAsyncResult.result();
            connection.queryStreamWithParams(sql, param, asyncResult -> {
                if (asyncResult.failed()) {
                    asyncResult.cause().printStackTrace();
                    asyncResultHandler.handle(Future.failedFuture(asyncResult.cause()));
                } else {
                    //there is something wrong with this method.
                    SQLRowStream rowStream = asyncResult.result();
                    //List<T> result = new LinkedList<T>();
                    rowStream.resultSetClosedHandler(handler -> {
                        rowStream.moreResults();
                    }).handler(jsonArray -> {
                        //jsonArray should map to POJO.
                        String encode = jsonArray.encode();
                        String encodePrettily = jsonArray.encodePrettily();
                        System.out.println(encode + "\t" + encodePrettily);
                    }).endHandler(handler -> {
                        rowStream.close();
                    });
                }
                connection.close();
            });
        });
    }

}
