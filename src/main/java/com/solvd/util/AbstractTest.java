package com.solvd.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Properties;

public abstract class AbstractTest {
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @BeforeMethod
    public void setUp() {
        // TODO: REFACTOR INTO CONFIG CLASS!
        String webPropertiesPath = Thread.currentThread().getContextClassLoader().getResource("web.properties").getPath();
        Properties webProperties = new Properties();
        try {
            webProperties.load(new FileInputStream(webPropertiesPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String webdriver = "";
        if (System.getProperties().containsKey("webdriver")) {
            webdriver = System.getProperty("webdriver");
        } else if (webProperties.containsKey("webdriver")) {
            webdriver = webProperties.getProperty("webdriver");
        }

        String seleniumUrlText = "http://localhost:4444";
        URL seleniumUrl = null;
        try {
            seleniumUrl = new URL(seleniumUrlText);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    "Malformed selenium url (%s)".formatted(seleniumUrlText), e);
        }

        // TODO: add enum for webdriver
        switch (webdriver) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                this.driver.set(
                        new RemoteWebDriver(seleniumUrl, chromeOptions));
                break;
            case "gecko":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                this.driver.set(
                        new RemoteWebDriver(seleniumUrl, firefoxOptions));
                break;
            default:
                throw new IllegalStateException("Unexpected webdriver: '" + webdriver + "'");
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
