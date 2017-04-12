package com.mjduan.project.chapter12_withSpring.dao;

import com.mjduan.project.chapter12_withSpring.entity.Product;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Duan on 2017/4/6.
 */
@Component("ProductDaoImpl")
public class ProductDaoImpl {

    public List<Product> products(){
        return Arrays.asList(
                new Product(1,"p1"),
                new Product(2,"p2"),
                new Product(3,"p3"),
                new Product(4,"p4"));
    }

}
