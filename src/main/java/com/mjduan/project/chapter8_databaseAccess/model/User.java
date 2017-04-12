package com.mjduan.project.chapter8_databaseAccess.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * Created by Duan on 2017/4/5.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class User {
    int id;
    String name;
    int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
