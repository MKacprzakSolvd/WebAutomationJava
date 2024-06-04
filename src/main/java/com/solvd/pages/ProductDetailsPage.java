package com.solvd.pages;

import com.solvd.components.ShoppingCart;
import com.solvd.model.Product;
import com.solvd.model.Review;
import com.solvd.util.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.math.BigDecimal;
import java.util.List;

public class ProductDetailsPage extends AbstractPage {
    @FindBy(xpath = "//*[contains(@class,'page-header')]//*[@data-block='minicart']")
    private WebElement shoppingCartElement;
    private ShoppingCart shoppingCart;

    @FindBy(xpath = "//*[@role='alert']/*/*")
    private List<WebElement> alerts;

    @FindBy(xpath = "//*[contains(@class,'product-info-main')]//*[@itemprop='name']")
    private WebElement productName;
    @FindBy(xpath = "//*[contains(@class,'product-info-main')]//*[contains(@class,'price-wrapper')]")
    private WebElement productPrice;

    @FindBy(xpath = "//*[@id='product-options-wrapper']" +
            "//*[@attribute-code='size']//*[contains(@class,'swatch-option')]")
    private List<WebElement> productSizes;
    @FindBy(xpath = "//*[@id='product-options-wrapper']" +
            "//*[@attribute-code='color']//*[contains(@class,'swatch-option')]")
    private List<WebElement> productColors;

    @FindBy(css = ".product-add-form [type='submit']")
    private WebElement addToCartButton;

    @FindBy(id = "tab-label-reviews")
    private WebElement reviewsTab;
    @FindBy(id = "tab-label-reviews-title")
    private WebElement reviewsTabTitle;

    @FindBy(xpath = "//*[contains(@class,'review-control-vote')]//*[contains(@class,'rating-')]")
    private List<WebElement> reviewRatings;
    @FindBy(id = "nickname_field")
    private WebElement reviewNickname;
    @FindBy(id = "summary_field")
    private WebElement reviewSummary;
    @FindBy(id = "review_field")
    private WebElement reviewReviewText;
    @FindBy(xpath = "//*[@id='review-form']//*[@type='submit']")
    private WebElement reviewSubmitButton;


    public ProductDetailsPage(WebDriver driver) {
        super(driver);

        this.shoppingCart = new ShoppingCart(
                this.shoppingCartElement, getDriver());
    }


    public ShoppingCart getShoppingCart() {
        return this.shoppingCart;
    }

    public String getProductName() {
        return this.productName.getText();
    }

    public BigDecimal getProductPrice() {
        return new BigDecimal(
                this.productPrice.getAttribute("data-price-amount"));
    }

    public boolean isForElement(Product product) {
        // TODO improve (move comparison to Product class)
        return product.getName().equals(getProductName())
                // warning: you cannot replace compareTo with equals
                //          because for equals scale have to be the same
                && product.getPrice().compareTo(getProductPrice()) == 0;
    }

    // TODO add option to select color and size
    public void addToCart() {
        // select first color and size
        this.productSizes.getFirst().click();
        this.productColors.getFirst().click();
        this.addToCartButton.click();
    }

    public ProductDetailsPage addReview(Review review) {
        // open ratings tab
        this.reviewsTabTitle.click();

        // fill in the form
        // click on star corresponding to selected rating
        WebElement selectedRating = this.reviewRatings.get(review.getRating() - 1);
        int starsHeigth = selectedRating.getSize().getHeight();
        int starsWidth = selectedRating.getSize().getWidth();
        Actions actions = new Actions(getDriver());
        // click on the last star (assumes starWidth == starHeigth)
        actions.moveToElement(selectedRating, starsWidth / 2 - starsHeigth / 2, 0)
                .click()
                .perform();

        this.reviewNickname.sendKeys(review.getUserNickname());
        this.reviewSummary.sendKeys(review.getSummary());
        this.reviewReviewText.sendKeys(review.getReviewContent());
        this.reviewSubmitButton.click();

        return new ProductDetailsPage(getDriver());
    }

    /**
     * informs whether alert that review was added successfully is shown
     */
    public boolean isReviewAddedSuccessfullyAlertShown() {
        for (WebElement message : this.alerts) {
            if (message.getText().equals("You submitted your review for moderation.")) {
                return true;
            }
        }
        return false;
    }

    // add goToDetailsPage to ProductCard class
    // goToCheckout()
    // boolean addReview()
}