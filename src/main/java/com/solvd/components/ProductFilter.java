package com.solvd.components;

import com.solvd.pages.ProductsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

// TODO: consider moving to ProductsPage (as static nested class)
public class ProductFilter {
    private final WebElement productFilterElement;
    private final WebDriver driver;

    @FindBy(className = "swatch-option")
    private List<WebElement> options;

    @FindBy(className = "filter-options-title")
    private WebElement title;


    public ProductFilter(WebElement productFilterElement, WebDriver driver) {
        PageFactory.initElements(productFilterElement, this);
        // this assigment have to be after PageFactory, otherwise PageFactory will overwrite it
        this.productFilterElement = productFilterElement;
        this.driver = driver;
    }

    public List<String> getOptions() {
        return this.options.stream()
                .map(webElement -> webElement.getAttribute("option-label"))
                .toList();
    }

    /**
     * expand filter section, allowing to select option
     * does anything only if filter is not expanded
     */
    private void expand() {
        if (!isExpanded()) {
            this.title.click();
        }
    }

    private boolean isExpanded() {
        return this.productFilterElement.getAttribute("class").contains("active");
    }

    public ProductsPage filterBy(String option) {
        for (WebElement optionElement : options) {
            if (optionElement.getAttribute("option-label").equals(option)) {
                expand();
                optionElement.click();
                return new ProductsPage(driver);
            }
        }
        throw new IllegalArgumentException("Option not found: " + option);
    }
}
