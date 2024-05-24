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
}
