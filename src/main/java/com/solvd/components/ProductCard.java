package com.solvd.components;

import com.solvd.model.Product;
import com.solvd.pages.ProductDetailsPage;
import com.solvd.pages.ProductsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.math.BigDecimal;
import java.util.List;

public class ProductCard {
    private final WebElement productCardElement;
    private final WebDriver driver;

    // locators
    @FindBy(xpath = ".//*[contains(@class,'product-item-name')]//a[contains(@class,'product-item-link')]")
    private WebElement name;
    @FindBy(className = "price")
    private WebElement price;
    @FindBy(className = "product-image-photo")
    private WebElement image;

    @FindBy(xpath = ".//*[@attribute-code='size']//*[@option-label]")
    private List<WebElement> avaliableSizes;
    @FindBy(xpath = ".//*[@attribute-code='color']//*[@option-label]")
    private List<WebElement> avaliableColors;

    @FindBy(css = ".product-item-details .tocart")
    private WebElement addToCartButton;

    public ProductCard(WebElement productCardElement, WebDriver driver) {
        PageFactory.initElements(productCardElement, this);
        // have to be after PageFactory init, otherwise will be overwritten
        this.productCardElement = productCardElement;
        this.driver = driver;
    }

    // FIXME: add available colors and sizes to product data
    public Product getProductData() {
        return Product.builder()
                .name(getName())
                .price(getPrice())
                .build();
    }

    // TODO: remove, only for debugging
    public WebElement getRoot() {
        return this.productCardElement;
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

    // TODO: check if returning new page is necessary
    // TODO: add option to specify size and color
    public ProductsPage addToCart() {
        // hover over product cart to show add to cart button
        Actions action = new Actions(this.driver);
        action.moveToElement(this.productCardElement).perform();
        // select first size and color (if colors avaliable)
        if (!this.avaliableSizes.isEmpty()) {
            this.avaliableSizes.getFirst().click();
        }
        if (!this.avaliableColors.isEmpty()) {
            this.avaliableColors.getFirst().click();
        }
        // click it
        this.addToCartButton.click();
        return new ProductsPage(this.driver);
    }

    public ProductDetailsPage goToProductDetailsPage() {
        this.image.click();
        return new ProductDetailsPage(this.driver);
    }

    /**
     * extract price from string containing price
     */
    private static String extractPrice(String priceString) {
        // TODO find better way of extracting price - this might fail for different currency
        //      tip: you can extract price from data-price-amount property (of price wrapper)
        // split string with price by $, and return last part
        String[] parts = priceString.split("\\$");
        return parts[parts.length - 1];
    }
}
