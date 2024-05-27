package com.solvd.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPageStepThree {
    private WebDriver driver;

    @FindBy(css = "#maincontent .action.continue")
    private WebElement continueShoppingButton;

    public CheckoutPageStepThree(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public HomePage returnToHomePage() {
        waitTillPageLoads();
        this.continueShoppingButton.click();
        return new HomePage(this.driver);
    }

    private void waitTillPageLoads() {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(this.continueShoppingButton));
    }
}
