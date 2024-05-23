package com.solvd;

import com.solvd.pages.HomePage;
import com.solvd.pages.ProductsPage;
import com.solvd.pages.SearchPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class WebTest {
    private static final Logger LOGGER = LogManager.getLogger(WebTest.class.getName());
    private WebDriver driver = null;

    // TODO for multithreading BeforeMethod might be better - experiment
    @BeforeClass
    public void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        try {
            this.driver = new RemoteWebDriver(new URL("http://localhost:4444"), chromeOptions);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }


    /**
     * Verify Product Search
     * <p>
     * Steps:
     * 1. Open https://magento.softwaretestingboard.com website
     * Result: Home page sould load
     * 2. Insert "bag" into search field and click search
     * Result: Search page should open, containing non-empty list of items
     */
    @Test
    public void verifyProductSearch() {
        // TODO ! add asserts
        // TODO: move url's to separate file
        driver.get("https://magento.softwaretestingboard.com/");
        HomePage homePage = new HomePage(driver);
        SearchPage searchPage = homePage.searchForProduct("bag");
        for (var productCard : searchPage.getProductCards()) {
            LOGGER.info("Product name: " + productCard.getProductData().getName());
        }
    }


    @Test
    public void verifySizeColorFilters() {
        driver.get("https://magento.softwaretestingboard.com/men/tops-men.html");
        ProductsPage productsPage = new ProductsPage(driver);
        for (var size : productsPage.getFilterOptions(ProductsPage.Filters.SIZE)) {
            System.out.println(size);
        }
        for (var size : productsPage.getFilterOptions(ProductsPage.Filters.COLOR)) {
            System.out.println(size);
        }
        productsPage = productsPage.filterBy(ProductsPage.Filters.SIZE, "XS");
        productsPage = productsPage.filterBy(ProductsPage.Filters.COLOR, "Blue");
        for (var productCard : productsPage.getProductCards()) {
            LOGGER.info("Product name: " + productCard.getProductData().getName());
        }
    }
}
