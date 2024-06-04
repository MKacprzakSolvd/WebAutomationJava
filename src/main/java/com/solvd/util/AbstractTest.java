package com.solvd.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public abstract class AbstractTest {
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        try {
            this.driver.set(
                    new RemoteWebDriver(new URL("http://localhost:4444"), chromeOptions));
            //this.driver.set(
            //        new RemoteWebDriver(new URL("http://localhost:4444"), firefoxOptions));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.get().quit();
        }
    }

    public WebDriver getDriver() {
        return driver.get();
    }
}
