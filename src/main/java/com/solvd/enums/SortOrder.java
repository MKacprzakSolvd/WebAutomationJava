package com.solvd.enums;

import com.solvd.model.Product;
import lombok.Getter;

import java.util.Comparator;

public enum SortOrder {
    BY_NAME_A_TO_Z("name", true,
            Product::compareByNameAToZ),
    BY_NAME_Z_TO_A("name", false,
            Product::compareByNameZToA),
    BY_PRICE_ASCENDING("price", true,
            Product::compareByPriceAscending),
    BY_PRICE_DESCENDING("price", false,
            Product::compareByPriceDescending),
    BY_POSITION_ASCENDING("position", true,
            SortOrder::unimplementedComparator),
    BY_POSITION_DESCENDING("position", false,
            SortOrder::unimplementedComparator);

    @Getter
    private String value;
    @Getter
    private boolean ascending;
    @Getter
    Comparator<Product> comparator;

    private SortOrder(String value, boolean ascending, Comparator<Product> comparator) {
        this.value = value;
        this.ascending = ascending;
        this.comparator = comparator;
    }

    public static SortOrder valueOf(String value, boolean ascending) {
        for (SortOrder sortOrder : SortOrder.values()) {
            if (sortOrder.value.equals(value) && sortOrder.ascending == ascending) {
                return sortOrder;
            }
        }
        throw new IllegalArgumentException("Cannot find enum value matching:" +
                "value=" + value + ", ascending=" + ascending);
    }

    private static int unimplementedComparator(Product product1, Product product2) {
        throw new UnsupportedOperationException("This comparator is not implemented.");
    }
}
