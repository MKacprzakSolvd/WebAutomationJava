package com.solvd.pages;

import com.solvd.util.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPageStepThree extends AbstractPage {
    @FindBy(css = "#maincontent .action.continue")
    private WebElement continueShoppingButton;

    public CheckoutPageStepThree(WebDriver driver) {
        super(driver);
    }

    public HomePage returnToHomePage() {
        waitTillPageLoads();
        this.continueShoppingButton.click();
        return new HomePage(getDriver());
    }

    private void waitTillPageLoads() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(this.continueShoppingButton));
    }
}
