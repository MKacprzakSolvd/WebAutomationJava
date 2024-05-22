package com.solvd.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    private WebDriver driver;

    @FindBy(id = "search")
    private WebElement searchInputField;
    @FindBy(xpath = "//*[@id='search_mini_form']//button[@type='submit']")
    private WebElement searchSubmitButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        // TODO add some verification that this is home page
        PageFactory.initElements(driver, this);
    }

    public void searchForProduct(String searchTerm) {
        this.searchInputField.sendKeys(searchTerm);
        this.searchSubmitButton.click();
    }
}
