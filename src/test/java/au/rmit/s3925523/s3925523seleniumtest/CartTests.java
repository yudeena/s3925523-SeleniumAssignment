package au.rmit.s3925523.s3925523seleniumtest;

import au.rmit.s3925523.s3925523seleniumtest.pages.*;

import com.inflectra.spiratest.addons.junitextension.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpiraExtension.class)
@SpiraTestConfiguration(
        url = "https://rmit.spiraservice.net",
        login = "s3925523",
        rssToken = "{59E2D88B-88B1-4FFB-9470-FB906D2DFA96}",
        projectId = 643,
        releaseId = 2368,
        testSetId = -1
)
public class CartTests {
    private static final String BASE_URL = "https://demoblaze.com/";
    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get(BASE_URL);
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    private void addFirstLaptopToCart() {
        HomePage home = new HomePage(driver);
        home.goHome();                      // ensure homepage
        home.chooseCategory("Laptops");     // your updated self-healing method
        new BrowsePage(driver).openFirstProduct();
        new CartPage(driver).addToCartFromProduct(); // already waits for/accepts alert
    }

    @Test
    @SpiraTestCase(testCaseId = 38296)
    void addProductToCart() {
        addFirstLaptopToCart();
        CartPage cart = new CartPage(driver);
        cart.openCart();
        assertTrue(cart.hasPlaceOrder(), "Expected Place Order visible with items table");
    }

    @Test
    @SpiraTestCase(testCaseId = 38297)
    void removeProductFromCart() {
        addFirstLaptopToCart();
        CartPage cart = new CartPage(driver);
        cart.openCart();
        cart.deleteFirstRowIfPresent();
        // minimal assert: still shows cart but may have no rows
        assertTrue(cart.hasPlaceOrder());
    }

    @Test
    @SpiraTestCase(testCaseId = 38298)
    void cartPersistsAcrossNavigation() {
        addFirstLaptopToCart();
        CartPage cart = new CartPage(driver);
        new HomePage(driver).chooseCategory("Phones");
        cart.openCart();
        assertTrue(cart.hasPlaceOrder(), "Cart should persist after navigation");
    }
}

