package com.solvd.components;

import com.solvd.model.Product;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.math.BigDecimal;

public class ProductCard {
    private final WebElement productCardElement;

    // locators
    @FindBy(xpath = ".//*[contains(@class,'product-item-name')]//a[contains(@class,'product-item-link')]")
    private WebElement name;
    //@FindBy(xpath = ".//*[contains(@class,'price')]")
    @FindBy(className = "price")
    private WebElement price;

    public ProductCard(WebElement productCardElement) {
        this.productCardElement = productCardElement;
        PageFactory.initElements(productCardElement, this);
    }

    public Product getProductData() {
        return Product.builder()
                .name(this.name.getText())
                // remove $ sign from price
                .price(new BigDecimal(extractPrice(this.price.getText())))
                .build();
    }

    /**
     * extract price from string containing price
     */
    private static String extractPrice(String priceString) {
        // TODO find better way of extracting price - this might fail for different currency
        // split string with price by $, and return last part
        String[] parts = priceString.split("\\$");
        return parts[parts.length - 1];
    }
}
