package au.rmit.s3925523.s3925523seleniumtest;

import au.rmit.s3925523.s3925523seleniumtest.base.BaseTest;
import au.rmit.s3925523.s3925523seleniumtest.pages.*;
import com.inflectra.spiratest.addons.junitextension.SpiraExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
public class OrderTests  {
    private static final String BASE_URL = "https://demoblaze.com/";
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get(BASE_URL);
        wait = new WebDriverWait(driver, Duration.ofSeconds(6));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) driver.quit();
    }

    // In a BasePage or reuse your helper:
    private void waitForNoModal(WebDriver driver, WebDriverWait wait) {
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal.show"))); } catch (Exception ignore) {}
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-backdrop"))); } catch (Exception ignore) {}
    }


    // OrderTests.java
    private void ensureCartHasItem() {
        new HomePage(driver).goHome();
        new HomePage(driver).chooseCategory("Phones");
        new BrowsePage(driver).openFirstProduct();
        new CartPage(driver).addToCartFromProduct(); // should accept alert + wait for it to close
        new CartPage(driver).openCart();
    }


    //
    @Test
    @SpiraTestCase(testCaseId = 38300)
    void placeOrderWithRequiredFields() {
        ensureCartHasItem();
        CartPage cart = new CartPage(driver);
        cart.placeOrderWith("Test User", "4242424242424242");
        assertTrue(cart.sawPurchaseConfirmation(), "Expected purchase confirmation dialog");
    }

    @Test
    @SpiraTestCase(testCaseId = 38301)
    void blockOrderWhenRequiredFieldsMissing() {
        ensureCartHasItem();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        By orderModalVisible = By.cssSelector("#orderModal.show");
        By placeOrderBtn     = By.xpath("//button[normalize-space()='Place Order']");
        By purchaseBtnInModal= By.xpath("//div[@id='orderModal' and contains(@class,'show')]//button[normalize-space()='Purchase']");
        By closeBtnInModal   = By.xpath("//div[@id='orderModal' and contains(@class,'show')]//button[normalize-space()='Close']");
        By xBtnInModal       = By.xpath("//div[@id='orderModal' and contains(@class,'show')]//button[contains(@class,'close') or normalize-space()='×']");

        // open modal
        wait.until(ExpectedConditions.elementToBeClickable(placeOrderBtn)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(orderModalVisible));

        // click Purchase with empty fields -> should raise alert
        wait.until(ExpectedConditions.elementToBeClickable(purchaseBtnInModal)).click();

        // handle validation alert (this is the "block")
        String alertText = wait.until(ExpectedConditions.alertIsPresent()).getText();
        assertTrue(alertText.toLowerCase().contains("please fill out"), "Expected validation alert");
        driver.switchTo().alert().accept();

        // ✅ Assertion of *blocking*: modal is still open and interactive
        assertTrue(
                !driver.findElements(purchaseBtnInModal).isEmpty()
                        || !driver.findElements(closeBtnInModal).isEmpty(),
                "Order dialog should remain open after validation"
        );

        // --- Clean up (best-effort). Don't fail the test if close is finicky. ---
        try {
            // prefer Close; if not clickable, try JS click; then try [X]; finally ESC
            WebElement close = wait.until(ExpectedConditions.presenceOfElementLocated(closeBtnInModal));
            try {
                wait.until(ExpectedConditions.elementToBeClickable(close)).click();
            } catch (Exception e) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", close);
            }
        } catch (Exception ignore) {
            try {
                WebElement x = driver.findElement(xBtnInModal);
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", x);
            } catch (Exception ignore2) {
                new org.openqa.selenium.interactions.Actions(driver).sendKeys(org.openqa.selenium.Keys.ESCAPE).perform();
            }
        }

        // Wait a moment for the modal/backdrop to go—*but don’t fail if it lingers*
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(orderModalVisible));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-backdrop")));
        } catch (Exception ignore) {
            // leaving it open is fine; we already asserted the "block"
        }
    }


}

