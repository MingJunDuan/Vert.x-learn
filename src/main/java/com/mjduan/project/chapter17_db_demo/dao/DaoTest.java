package com.mjduan.project.chapter17_db_demo.dao;

import com.mjduan.project.chapter17_db_demo.Util;
import com.mjduan.project.chapter17_db_demo.entity.TestModel;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Duan on 2017/4/19.
 */
public class DaoTest {

    public static void main(String[] args) {
        //testQueryOne();
        //testQueryMutil();
        //testQueryUpdateForDelete();
        //testQueryUpdateForUpdate();
        testQueryMutilStream();
    }

    public static void testQueryMutilStream() {
        Vertx vertx = Vertx.vertx();
        DaoVerticle daoVerticle = new DaoVerticle(vertx);
        vertx.deployVerticle(daoVerticle);

        String sql = "select * from test where id>?";
        JsonArray jsonArray = new JsonArray().add(1);
        daoVerticle.queryMultiStream(sql,jsonArray,TestModel.class,listAsyncResult -> {
            listAsyncResult.result();
            System.out.println("end");
        });

    }

    public static void testQueryUpdateForUpdate() {
        Vertx vertx = Vertx.vertx();
        DaoVerticle daoVerticle = new DaoVerticle(vertx);
        vertx.deployVerticle(daoVerticle);

        String sql = "update test set balance=? where id=?";
        JsonArray jsonArray = new JsonArray().add(100).add(10);
        daoVerticle.update(sql,jsonArray,booleanAsyncResult -> {
            Boolean result = booleanAsyncResult.result();
            System.out.println(result);
        });
    }

    public static void testQueryUpdateForDelete() {
        Vertx vertx = Vertx.vertx();
        DaoVerticle daoVerticle = new DaoVerticle(vertx);
        vertx.deployVerticle(daoVerticle);

        String sql = "delete from test where id=?";
        JsonArray jsonArray = new JsonArray().add(11);
        daoVerticle.update(sql,jsonArray,booleanAsyncResult -> {
            Boolean result = booleanAsyncResult.result();
            System.out.println(result);
        });
    }

    public static void testQueryMutil() {
        Vertx vertx = Vertx.vertx();
        DaoVerticle daoVerticle = new DaoVerticle(vertx);
        vertx.deployVerticle(daoVerticle);

        String sql = "select * from test where id>?";
        JsonArray jsonArray = new JsonArray().add(15);
        daoVerticle.queryMulti(sql, jsonArray, TestModel.class, listAsyncResult -> {
            List<TestModel> testModels = listAsyncResult.result();
            testModels.stream()
                    .forEach(System.out::println);
        });
    }

    public static void testQueryOne() {
        Vertx vertx = Vertx.vertx();
        DaoVerticle daoVerticle = new DaoVerticle(vertx);
        vertx.deployVerticle(daoVerticle);

        String sql = "select * from test where id=?";
        JsonArray jsonArray = new JsonArray().add(1);
        daoVerticle.queryOne(sql, jsonArray, TestModel.class, asyncResult -> {
            TestModel result = asyncResult.result();
            System.out.println(result);
        });
    }

    public static void testAdd() {
        Vertx vertx = Vertx.vertx();
        DaoVerticle daoVerticle = new DaoVerticle(vertx);
        vertx.deployVerticle(daoVerticle);

        TestModel testModel = new TestModel("remark", new BigDecimal(12.56));
        String sql = "insert into test(remark,balance) values(?,?)";
        JsonArray jsonArray = Util.toJsonArray(testModel.getRemark(), testModel.getBalance().floatValue());
        daoVerticle.add(sql, jsonArray, integerAsyncResult -> {
            Integer result = integerAsyncResult.result();
            System.out.println(result);
        });
    }

}
