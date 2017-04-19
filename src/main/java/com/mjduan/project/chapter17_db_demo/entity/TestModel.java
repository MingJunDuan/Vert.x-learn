package com.mjduan.project.chapter17_db_demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * Created by Duan on 2017/4/19.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TestModel {
    private long id;
    private String remark;
    private BigDecimal balance;

    public TestModel(String remark, BigDecimal balance) {
        this.remark = remark;
        this.balance = balance;
    }
}
