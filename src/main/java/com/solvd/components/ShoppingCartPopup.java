package com.solvd.components;

import com.solvd.model.Product;
import com.solvd.pages.ProductsPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.not;

public class ShoppingCartPopup {
    // class (added to cartCounterWrapper)  indicating that cart content is updating
    private static final String CART_UPDATE_INDICATING_CLASS = "_block-content-loading";

    WebElement shopingCartRootElement;
    WebDriver driver;

    @FindBy(css = ".showcart .counter")
    private WebElement cartCounterWrapper;

    // counter displayed on the cart icon, always visible
    @FindBy(css = ".showcart .counter-number")
    private WebElement cartCounter;

    @FindBy(className = "showcart")
    private WebElement openButton;

    @FindBy(id = "ui-id-1")
    private WebElement contentWrapper;

    @FindBy(css = ".subtotal .price")
    private WebElement itemsPriceIndicator;

    // TODO: replace with custom element
    @FindBy(css = ".product-item-details .product-item-name a")
    private List<WebElement> productNamesElements;
    @FindBy(css = ".product-item-details .delete")
    private List<WebElement> productRemoveButtons;

    // confirmation button from modal. It it outside of shopping cart root element
    private By removeProductConfirmationButton = By.cssSelector(".modals-wrapper .action-accept");

    public ShoppingCartPopup(WebElement shopingCartRootElement, WebDriver driver) {
        PageFactory.initElements(shopingCartRootElement, this);
        this.shopingCartRootElement = shopingCartRootElement;
        this.driver = driver;
    }

    public boolean isOpened() {
        return this.contentWrapper.isDisplayed();
    }

    public void open() {
        if (!isOpened()) {
            this.openButton.click();
        }
    }

    public boolean isEmpty() {
        waitTillCartUpdates();
        return !this.cartCounter.isDisplayed();
    }

    public int getProductsCount() {
        waitTillCartUpdates();
        if (isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(this.cartCounter.getText());
        }
    }

    public boolean isProductInCart(Product product) {
        waitTillCartUpdates();
        open();
        if (isEmpty()) {
            return false;
        }
        // TODO: implement Optional<ProductCartCard> findProductCard() method
        //       and use it here and in remove from cart (and find better name for it)
        for (WebElement productNameElement : this.productNamesElements) {
            if (productNameElement.getText().equals(product.getName())) {
                return true;
            }
        }
        return false;
    }

    public ProductsPage removeFromCart(Product product) {
        // TODO: rewrite with findProductCard when implemented
        if (!isProductInCart(product)) {
            throw new IllegalArgumentException("Product '%s' is not in the shopping cart."
                    .formatted(product.getName()));
        }
        for (int i = 0; i < this.productNamesElements.size(); i++) {
            if (product.getName().equals(this.productNamesElements.get(i).getText())) {
                this.productRemoveButtons.get(i).click();
                break;
            }
        }

        // Confirm removal in modal
        WebElement removalConfirmationButton = this.driver.findElement(removeProductConfirmationButton);
        removalConfirmationButton.click();
        return new ProductsPage(this.driver);
    }

    protected void waitTillCartUpdates() {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        wait.until(not(attributeContains(this.cartCounterWrapper, "class", CART_UPDATE_INDICATING_CLASS)));
    }

    // toCheckout
}

