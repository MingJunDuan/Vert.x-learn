package com.mjduan.project.chapter17_db_demo.entity;

import lombok.*;

import java.math.BigDecimal;

/**
 * Created by duan on 2017/4/18.
 */
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mypay {
    private long id;
    private BigDecimal money;
    private String remark;
    private long user;
    private String time;

    public Mypay(BigDecimal money, String remark, long user, String time) {
        this.money = money;
        this.remark = remark;
        this.user = user;
        this.time = time;
    }

    public BigDecimal getMoney(){
        return money.setScale(2,BigDecimal.ROUND_HALF_DOWN);
    }

}
