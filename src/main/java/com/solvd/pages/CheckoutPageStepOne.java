package com.solvd.pages;

import com.solvd.model.Product;
import com.solvd.model.ShippingInfo;
import com.solvd.util.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CheckoutPageStepOne extends AbstractPage {
    @FindBy(id = "customer-email")
    private WebElement emailField;
    @FindBy(xpath = "//*[@name='shippingAddress.firstname']//*[@name='firstname']")
    private WebElement firstNameField;
    @FindBy(xpath = "//*[@name='shippingAddress.lastname']//*[@name='lastname']")
    private WebElement lastNameField;
    @FindBy(xpath = "//*[@name='shippingAddress.company']//*[@name='company']")
    private WebElement companyField;

    @FindBy(xpath = "//*[@name='shippingAddress.street.0']//*[@name='street[0]']")
    private WebElement addressLine1Field;
    @FindBy(xpath = "//*[@name='shippingAddress.street.1']//*[@name='street[1]']")
    private WebElement addressLine2Field;
    @FindBy(xpath = "//*[@name='shippingAddress.street.2']//*[@name='street[2]']")
    private WebElement addressLine3Field;

    @FindBy(xpath = "//*[@name='shippingAddress.city']//*[@name='city']")
    private WebElement cityField;
    // state/province is either dropdown or field, depending on country
    @FindBy(xpath = "//*[@name='shippingAddress.region_id']//*[@name='region_id']")
    private WebElement provinceDropdown;
    @FindBy(xpath = "//*[@name='shippingAddress.region']//*[@name='region']")
    private WebElement provinceField;
    @FindBy(xpath = "//*[@name='shippingAddress.postcode']//*[@name='postcode']")
    private WebElement postalCodeField;
    @FindBy(xpath = "//*[@name='shippingAddress.country_id']//*[@name='country_id']")
    private WebElement countryDropdown;

    @FindBy(xpath = "//*[@name='shippingAddress.telephone']//*[@name='telephone']")
    private WebElement phoneNumberField;

    @FindBy(xpath = "//*[@id='checkout-shipping-method-load']//*[@value='flatrate_flatrate']")
    private WebElement fixedRateShippingMethodRadio;
    @FindBy(xpath = "//*[@id='checkout-shipping-method-load']//*[@value='tablerate_bestway']")
    private WebElement tableRateShippingMethodRadio;

    @FindBy(xpath = "//*[@id='shipping-method-buttons-container']//*[@type='submit']")
    private WebElement goToNextStepButton;

    @FindBy(css = "#opc-sidebar .title[data-role='title']")
    private WebElement toggleProductListButton;
    @FindBy(css = "#opc-sidebar .items-in-cart [role='heading'] > span:first-child")
    private WebElement productsInCartCount;
    @FindBy(css = "#opc-sidebar .minicart-items-wrapper")
    private WebElement productsWrapper;
    // TODO replace with custom element
    @FindBy(css = "#opc-sidebar .product-item-name")
    private List<WebElement> productNames;

    public CheckoutPageStepOne(WebDriver driver) {
        super(driver);
        waitTillCheckoutLoaded();
    }

    // TODO create class for address with builder and just pass it in
    public CheckoutPageStepTwo goToNextStep(
            ShippingInfo shippingInfo
    ) {
        this.emailField.sendKeys(shippingInfo.getEmail());
        this.firstNameField.sendKeys(shippingInfo.getFirstName());
        this.lastNameField.sendKeys(shippingInfo.getLastName());
        this.companyField.sendKeys(shippingInfo.getCompany());
        this.addressLine1Field.sendKeys(shippingInfo.getAddressLine1());
        this.addressLine2Field.sendKeys(shippingInfo.getAddressLine2());
        this.addressLine3Field.sendKeys(shippingInfo.getAddressLine3());
        this.cityField.sendKeys(shippingInfo.getCity());
        this.postalCodeField.sendKeys(shippingInfo.getPostalCode());
        this.phoneNumberField.sendKeys(shippingInfo.getPhoneNumber());

        // select country
        Select selectCountry = new Select(this.countryDropdown);
        // TODO change this to select by value instead of selected text
        selectCountry.selectByVisibleText(shippingInfo.getCountry());

        // select province
        // FIXME add support for case when you need to insert province
        //       instead selecting from dropdown (when provinceField is visible)
        //       this depends on selected country
        // TODO maybe change this to select province by value??
        Select selectProvince = new Select(this.provinceDropdown);
        selectProvince.selectByVisibleText(shippingInfo.getProvince());

        // FIXME add checking whether shipping method is avaliable
        switch (shippingInfo.getShippingMethod()) {
            case FIXED -> this.fixedRateShippingMethodRadio.click();
            case TABLE_RATE -> this.tableRateShippingMethodRadio.click();
        }

        // go to next step
        this.goToNextStepButton.click();

        return new CheckoutPageStepTwo(getDriver());
    }

    public int getProductsCount() {
        return Integer.parseInt(this.productsInCartCount.getText());
    }

    public boolean isProductInCart(Product product) {
        // TODO implement comparison based on all avaliable data (price, color, etc)
        openProductsList();
        return productNames.stream()
                .map(webElement -> webElement.getText())
                .anyMatch(title -> title.equals(product.getName()));
    }

    private boolean isProductsListOpened() {
        return this.productsWrapper.isDisplayed();
    }

    private void openProductsList() {
        if (!isProductsListOpened()) {
            this.toggleProductListButton.click();
        }
    }

    private void waitTillCheckoutLoaded() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(this.emailField));
        wait.until(ExpectedConditions.elementToBeClickable(this.firstNameField));
    }

}
