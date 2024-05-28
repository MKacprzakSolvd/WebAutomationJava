package com.solvd;

import com.solvd.components.ProductCard;
import com.solvd.model.Product;
import com.solvd.pages.*;
import com.solvd.util.RandomPicker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

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

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
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
     * Result: Home page should load
     * 2. Insert "bag" into search field and click search
     * Result: Search page should open, containing non-empty list of items
     */
    @Test
    public void verifyProductSearchTest() {
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
    // TODO: add logging
    // TODO: add test case description (steps, etc)
    public void verifySizeColorFiltersTest() {
        // open products page
        driver.get("https://magento.softwaretestingboard.com/women/tops-women.html");
        ProductsPage productsPage = new ProductsPage(driver);

        // filter by random size
        String randomSizeOption = RandomPicker.getRandomElement(
                productsPage.getFilterOptions(ProductsPage.Filter.SIZE)
        );
        productsPage = productsPage.filterBy(ProductsPage.Filter.SIZE, randomSizeOption);

        // make sure every element is avaliable in given size
        // FIXME: check it on all pages
        SoftAssert softAssert = new SoftAssert();
        for (ProductCard productCard : productsPage.getProductCards()) {
            softAssert.assertTrue(productCard.isAvailableInSize(randomSizeOption),
                    "Product: %s is not available in size '%s'".formatted(productCard.getName(), randomSizeOption));
        }

        // filter by random color
        String randomColorOption = RandomPicker.getRandomElement(
                productsPage.getFilterOptions(ProductsPage.Filter.COLOR)
        );
        productsPage = productsPage.filterBy(ProductsPage.Filter.COLOR, randomColorOption);

        // make sure every element is avaliable in given color and size
        // FIXME: check it on all pages
        // TODO: test for case when no elements found with selected filters
        for (ProductCard productCard : productsPage.getProductCards()) {
            softAssert.assertTrue(productCard.isAvailableInSize(randomSizeOption),
                    "Product: %s is not available in size '%s;".formatted(productCard.getName(), randomSizeOption));
            softAssert.assertTrue(productCard.isAvailableInColor(randomColorOption),
                    "Product: %s is not available in color '%s'".formatted(productCard.getName(), randomColorOption));
        }

        softAssert.assertAll();
    }

    @Test
    public void verifyAddRemoveFromShoppingCartTest() {
        // open products page
        driver.get("https://magento.softwaretestingboard.com/men/tops-men.html");
        ProductsPage productsPage = new ProductsPage(driver);
        final int PRODUCTS_TO_ADD_TO_CART = 2;

        // select two random products
        List<Product> selectedProducts = RandomPicker.getRandomElements(
                productsPage.getProducts(), PRODUCTS_TO_ADD_TO_CART);

        // add them to cart & check if they were added
        for (Product product : selectedProducts) {
            Optional<ProductCard> productCard = productsPage.findProductCard(product);
            Assert.assertTrue(productCard.isPresent(),
                    "Unable to find product card corresponding to product '%s' in products page"
                            .formatted(product.getName()));
            productsPage = productCard.get().addToCart();
        }
        int itemsInShoppingCart = productsPage.getShoppingCartPopup().getProductsCount();
        Assert.assertEquals(itemsInShoppingCart, PRODUCTS_TO_ADD_TO_CART,
                "Number of products in shopping cart (%d) doesn't match expected number (%d)."
                        .formatted(itemsInShoppingCart, PRODUCTS_TO_ADD_TO_CART));
        for (Product product : selectedProducts) {
            Assert.assertTrue(
                    productsPage.getShoppingCartPopup().isProductInCart(product),
                    "Product '%s' was not in the shopping cart".formatted(product.getName()));
        }

        // remove products from card & check if they were removed
        for (Product product : selectedProducts) {
            productsPage = productsPage.getShoppingCartPopup().removeFromCart(product);
        }

        Assert.assertTrue(productsPage.getShoppingCartPopup().isEmpty(),
                "Shopping cart is not empty.");
    }


    @Test
    public void verifyCheckoutProcessFromProductsPageTest() {
        // open products page
        driver.get("https://magento.softwaretestingboard.com/gear/bags.html");
        ProductsPage productsPage = new ProductsPage(driver);

        // select random item, add it to the cart and go to checkout
        ProductCard selectedProductCard = RandomPicker.getRandomElement(productsPage.getProductCards());
        Product selectedProduct = selectedProductCard.getProductData();
        productsPage = selectedProductCard.addToCart();
        // TODO: check if cart contains exactly one product
        Assert.assertTrue(productsPage.getShoppingCartPopup().isProductInCart(selectedProduct),
                "Selected product (%s) was not in the cart (on products page)."
                        .formatted(selectedProduct.getName()));
        CheckoutPageStepOne checkoutPageStepOne = productsPage.getShoppingCartPopup().goToCheckout();

        // complete first step of checkout
        int productsInShoppingCart = checkoutPageStepOne.getProductsCount();
        Assert.assertEquals(productsInShoppingCart, 1,
                "%d products in shopping car, while expecting only one, during the first step of checkout."
                        .formatted(productsInShoppingCart));
        Assert.assertTrue(checkoutPageStepOne.isProductInCart(selectedProduct),
                "The selected product ('%s') is not in the shopping cart, during the first step of checkout."
                        .formatted(selectedProduct.getName()));

        // TODO add data provider / read data from config
        CheckoutPageStepTwo checkoutPageStepTwo = checkoutPageStepOne.goToNextStep(
                "a@b.com",
                "John",
                "Smith",
                "Postal Inc.",
                "ul. Wielopole 2",
                "",
                "",
                "Kraków",
                "małopolskie",
                "12-345",
                "Poland",
                "123456789",
                CheckoutPageStepOne.ShippingMethod.FIXED
        );

        CheckoutPageStepThree checkoutPageStepThree = checkoutPageStepTwo.placeOrder();
        HomePage homePage = checkoutPageStepThree.returnToHomePage();
    }

    @Test
    public void verifyItemSortingTest() {
        driver.get("https://magento.softwaretestingboard.com/women/bottoms-women.html");
        ProductsPage productsPage = new ProductsPage(driver);
        SoftAssert softAssert = new SoftAssert();

        ProductsPage.SortOrder[] sortOrdersToCheck = new ProductsPage.SortOrder[]{
                ProductsPage.SortOrder.BY_NAME_A_TO_Z,
                ProductsPage.SortOrder.BY_NAME_Z_TO_A,
                ProductsPage.SortOrder.BY_PRICE_ASCENDING,
                ProductsPage.SortOrder.BY_PRICE_DESCENDING};

        for (ProductsPage.SortOrder sortOrder : sortOrdersToCheck) {
            productsPage = productsPage.setSortOrder(sortOrder);
            softAssert.assertTrue(productsPage.isSortedBy(sortOrder),
                    "Products sorted incorrectly according to sort order '%s'"
                            .formatted(sortOrder));
        }
        // TODO: go to the next page and check sorting (step 5)

        /*
            sorting by price (ascending and descending)
            will fail, page sorts incorrectly (usually last
            items are in the wrong order)
         */
        softAssert.assertAll();
    }
}
