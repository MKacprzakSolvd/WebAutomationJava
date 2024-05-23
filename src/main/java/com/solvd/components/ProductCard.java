package com.solvd.components;

import com.solvd.model.Product;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.math.BigDecimal;
import java.util.List;

public class ProductCard {
    private final WebElement productCardElement;

    // locators
    @FindBy(xpath = ".//*[contains(@class,'product-item-name')]//a[contains(@class,'product-item-link')]")
    private WebElement name;
    @FindBy(className = "price")
    private WebElement price;

    @FindBy(xpath = ".//*[@attribute-code='size']//*[@option-label]")
    private List<WebElement> avaliableSizes;
    @FindBy(xpath = ".//*[@attribute-code='color']//*[@option-label]")
    private List<WebElement> avaliableColors;

    public ProductCard(WebElement productCardElement) {
        this.productCardElement = productCardElement;
        PageFactory.initElements(this.productCardElement, this);
    }

    // FIXME: add available colors and sizes to product data
    public Product getProductData() {
        return Product.builder()
                .name(getName())
                .price(getPrice())
                .build();
    }

    public String getName() {
        return this.name.getText();
    }

    public BigDecimal getPrice() {
        return new BigDecimal(extractPrice(this.price.getText()));
    }

    public List<String> getAvaliableSizes() {
        return this.avaliableSizes.stream()
                .map(webElement -> webElement.getAttribute("option-label"))
                .toList();
    }

    public boolean isAvailableInSize(String size) {
        return getAvaliableSizes().contains(size);
    }

    public List<String> getAvaliableColors() {
        return this.avaliableColors.stream()
                .map(webElement -> webElement.getAttribute("option-label"))
                .toList();
    }

    public boolean isAvailableInColor(String color) {
        return getAvaliableColors().contains(color);
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
