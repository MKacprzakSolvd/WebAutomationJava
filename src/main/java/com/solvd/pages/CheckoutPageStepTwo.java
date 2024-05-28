package com.solvd.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPageStepTwo {
    private WebDriver driver;

    @FindBy(css = "#checkout-payment-method-load [type='submit']")
    private WebElement placeOrderButton;

    //.checkout-billing-address
    @FindBy(css = "#checkout-payment-method-load .payment-method-content")
    private WebElement orderAddressWrapper;

    public CheckoutPageStepTwo(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        waitTillPageLoads();
    }

    public CheckoutPageStepThree placeOrder() {
        this.placeOrderButton.click();
        return new CheckoutPageStepThree(this.driver);
    }

    private void waitTillPageLoads() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("body > div.loading-mask")));
        wait.until(ExpectedConditions.elementToBeClickable(this.placeOrderButton));
        wait.until(ExpectedConditions.visibilityOf(this.orderAddressWrapper));
    }
}
