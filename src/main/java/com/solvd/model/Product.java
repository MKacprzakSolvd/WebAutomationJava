package com.solvd.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

// TODO consider making it into record
@Getter
@Builder
public class Product {
    // TODO add sensible equals
    private String name;
    private BigDecimal price;
    private Integer rating;
    private Integer reviewNumber;
    private Boolean inStock;
    // TODO add avaliable colors and sizes


    public static int compareByNameAToZ(Product product1, Product product2) {
        return product1.getName().compareTo(product2.getName());
    }

    public static int compareByNameZToA(Product product1, Product product2) {
        return product2.getName().compareTo(product1.getName());
    }

    public static int compareByPriceAscending(Product product1, Product product2) {
        return product1.getPrice().compareTo(product2.getPrice());
    }

    public static int compareByPriceDescending(Product product1, Product product2) {
        return product2.getPrice().compareTo(product1.getPrice());
    }
}
