package com.solvd.pages;

import com.solvd.components.ProductCard;
import com.solvd.components.ProductFilter;
import com.solvd.components.ShoppingCartPopup;
import com.solvd.model.Product;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import java.util.*;

public class ProductsPage {
    private WebDriver driver;

    @FindBy(css = ".products .product-items .product-item")
    private List<WebElement> productCardElements;
    private List<ProductCard> productCards = new ArrayList<>();

    @FindBy(xpath = "//*[contains(@class,'page-header')]//*[@data-block='minicart']")
    private WebElement shoppingCartPopupElement;
    private ShoppingCartPopup shoppingCartPopup;

    // TODO: extract string 'Size' and 'Color' from this and move it somewhere else (as constant)?
    // select filter block that have 'Size' in title
    @FindBy(xpath = "//*[@id='layered-filter-block']" +
            "//*[contains(@class,'filter-options-item')]" +
            "[.//*[contains(@class,'filter-options-title')][text()='Size']]")
    private WebElement sizeFilterElement;
    private ProductFilter sizeFilter;

    // select filter block that have 'Color' in title
    @FindBy(xpath = "//*[@id='layered-filter-block']" +
            "//*[contains(@class,'filter-options-item')]" +
            "[.//*[contains(@class,'filter-options-title')][text()='Color']]")
    private WebElement colorFilterElement;
    private ProductFilter colorFilter;

    // maps from filters enum to filter object
    private Map<Filter, ProductFilter> filtersMap = new EnumMap<>(Filter.class);

    // there are two elements with id 'sorter', so this locator is required
    @FindBy(css = "#authenticationPopup + .toolbar-products #sorter")
    private WebElement sortTypeSelector;
    @FindBy(css = "#authenticationPopup + .toolbar-products [data-role='direction-switcher']")
    private WebElement sortDirectionSelector;


    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);

        for (WebElement productCardElement : productCardElements) {
            this.productCards.add(new ProductCard(productCardElement, driver));
        }

        this.shoppingCartPopup = new ShoppingCartPopup(this.shoppingCartPopupElement, this.driver);

        this.sizeFilter = new ProductFilter(this.sizeFilterElement, this.driver);
        this.filtersMap.put(Filter.SIZE, this.sizeFilter);
        this.colorFilter = new ProductFilter(this.colorFilterElement, this.driver);
        this.filtersMap.put(Filter.COLOR, this.colorFilter);
    }


    public List<ProductCard> getProductCards() {
        return Collections.unmodifiableList(this.productCards);
    }

    /**
     * finds product card corresponding to passed product
     */
    public Optional<ProductCard> findProductCard(Product product) {
        for (ProductCard productCard : this.productCards) {
            // TODO: create method representsProduct(Product) in ProductCard and use it here
            if (productCard.getName().equals(product.getName())) {
                return Optional.of(productCard);
            }
        }
        return Optional.empty();
    }

    public List<Product> getProducts() {
        return this.productCards.stream()
                .map(productCard -> productCard.getProductData())
                .toList();
    }

    public ShoppingCartPopup getShoppingCartPopup() {
        return this.shoppingCartPopup;
    }

    //FIXME: add support for case where filter is used (and thus inaccessible)
    public List<String> getFilterOptions(Filter filter) {
        return this.filtersMap.get(filter).getOptions();
    }

    //FIXME: add support for case where filter is used (and thus inaccessible)
    public ProductsPage filterBy(Filter filter, String option) {
        return this.filtersMap.get(filter).filterBy(option);
    }

    public SortOrder getSortOrder() {
        Select sortTypeSelect = new Select(this.sortTypeSelector);
        return SortOrder.valueOf(
                sortTypeSelect.getFirstSelectedOption().getAttribute("value"),
                // here desc indicates that clicking will change direction to descending
                // so when data-value == desc, the order is ascending
                this.sortDirectionSelector.getAttribute("data-value").equals("desc")
        );
    }

    public ProductsPage setSortOrder(SortOrder sortOrder) {
        ProductsPage productsPage = this;
        SortOrder currentSortOrder = getSortOrder();

        // select correct sort type
        if (!currentSortOrder.getValue().equals(sortOrder.getValue())) {
            Select sortTypeSelect = new Select(this.sortTypeSelector);
            sortTypeSelect.selectByValue(sortOrder.getValue());
            productsPage = new ProductsPage(this.driver);
        }

        // select correct sort direction
        if (currentSortOrder.isAscending() != sortOrder.isAscending()) {
            this.sortDirectionSelector.click();
            productsPage = new ProductsPage(this.driver);
        }

        return productsPage;
    }

    // TODO improve this method (make it more readable, implement comparing inside Product)
    //      now it just compares base on price
    public boolean isSortedBy(SortOrder sortOrder) {
        List<Product> products = getProducts();
        List<Product> sortedProducts = products.stream()
                .sorted(sortOrder.comparator)
                .toList();
        for (int i = 0; i < sortedProducts.size(); i++) {
            if (!sortedProducts.get(i).getName().equals(
                    products.get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    // TODO: implement
    // isFilterApplied
    // getAppliedFilters


    public enum Filter {
        SIZE,
        COLOR;
    }

    public enum SortOrder {
        BY_NAME_A_TO_Z("name", true,
                Product::compareByNameAToZ),
        BY_NAME_Z_TO_A("name", false,
                Product::compareByNameZToA),
        BY_PRICE_ASCENDING("price", true,
                Product::compareByPriceAscending),
        BY_PRICE_DESCENDING("price", false,
                Product::compareByPriceDescending),
        BY_POSITION_ASCENDING("position", true,
                SortOrder::unimplementedComparator),
        BY_POSITION_DESCENDING("position", false,
                SortOrder::unimplementedComparator);

        @Getter
        private String value;
        @Getter
        private boolean ascending;
        @Getter
        Comparator<Product> comparator;

        private SortOrder(String value, boolean ascending, Comparator<Product> comparator) {
            this.value = value;
            this.ascending = ascending;
            this.comparator = comparator;
        }

        public static SortOrder valueOf(String value, boolean ascending) {
            for (SortOrder sortOrder : SortOrder.values()) {
                if (sortOrder.value.equals(value) && sortOrder.ascending == ascending) {
                    return sortOrder;
                }
            }
            throw new IllegalArgumentException("Cannot find enum value matching:" +
                    "value=" + value + ", ascending=" + ascending);
        }

        private static int unimplementedComparator(Product product1, Product product2) {
            throw new UnsupportedOperationException("This comparator is not implemented.");
        }
    }
}
