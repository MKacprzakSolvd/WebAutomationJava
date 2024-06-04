package com.solvd.pages;

import com.solvd.util.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {
    @FindBy(id = "search")
    private WebElement searchInputField;
    @FindBy(xpath = "//*[@id='search_mini_form']//button[@type='submit']")
    private WebElement searchSubmitButton;

    public HomePage(WebDriver driver) {
        super(driver);
        // TODO add some verification that this is home page
    }

    public SearchPage searchForProduct(String searchTerm) {
        this.searchInputField.sendKeys(searchTerm);
        // TODO replace with searchInputField.submit()
        this.searchSubmitButton.click();
        return new SearchPage(getDriver());
    }
}
