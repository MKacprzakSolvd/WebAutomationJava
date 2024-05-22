package com.solvd.pages;

import com.solvd.components.ProductCard;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class SearchPage {
    private WebDriver driver;

    // TODO make ProductCard implement WebElement
    @FindBy(css = ".product-items .product-item")
    private List<WebElement> productCardElements;
    @Getter
    private List<ProductCard> productCards = new ArrayList<>();

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        // TODO check if page loaded (?and add waiting for loading?)
        PageFactory.initElements(driver, this);

        for (WebElement productCardElement : productCardElements) {
            this.productCards.add(new ProductCard(productCardElement));
        }
    }
}
