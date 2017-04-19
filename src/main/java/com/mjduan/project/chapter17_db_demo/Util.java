package com.mjduan.project.chapter17_db_demo;

import com.mjduan.project.chapter17_db_demo.entity.Mypay;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by duan on 2017/4/18.
 */
public final class Util {

    private Util() {
    }

    public static List<Mypay> getMypay() throws IOException {
        String filepath = "F:\\test2.txt";
        return Files.lines(Paths.get(filepath), StandardCharsets.UTF_8)
                .skip(1)
                .map(line -> {
                    String[] items = line.split(",");
                    return new Mypay(new BigDecimal(items[0]), items[1], 5, getDateTime(items[2]));
                }).collect(Collectors.toList());
    }

    private static String getDateTime(String item) {
        String[] split = item.split("/");
        String t1 = split[0];
        String t2 = split[1].length() == 1 ? "0" + split[1] : split[1];
        String t3 = split[2].length() == 1 ? "0" + split[2] : split[2];
        return t1 + "-" + t2 + "-" + t3 + " " + getTime();
    }

    private static String getTime() {
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH-mm"));
    }

    public static JsonArray toJsonArray(Object... objs) {
        JsonArray jsonArray = new JsonArray();
        for (Object obj : objs) {
            if (null == obj) {
                jsonArray.addNull();
            } else {
                jsonArray.add(obj);
            }
        }
        return jsonArray;
    }

    public static <T> List<T> toObjects(List<JsonObject> jsonObjects,Class<T> clazz) {
        return jsonObjects.stream()
                .map(jsonObject-> jsonObject.mapTo(clazz))
                .collect(Collectors.toList());
    }

}
