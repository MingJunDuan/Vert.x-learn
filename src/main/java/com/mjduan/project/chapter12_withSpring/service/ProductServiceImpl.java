package com.mjduan.project.chapter12_withSpring.service;

import com.mjduan.project.chapter12_withSpring.dao.ProductDaoImpl;
import com.mjduan.project.chapter12_withSpring.entity.Product;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Duan on 2017/4/6.
 */
@Service("ProductServiceImpl")
public class ProductServiceImpl {
    @Resource(name = "ProductDaoImpl")
    private ProductDaoImpl productDao;

    public List<Product> products(){
        return productDao.products();
    }
}
