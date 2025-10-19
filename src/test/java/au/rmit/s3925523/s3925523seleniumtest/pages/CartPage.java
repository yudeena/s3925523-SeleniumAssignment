package au.rmit.s3925523.s3925523seleniumtest.pages;
/**
 * This Page Object class was inspired by the "Main" example from the
 * SeleniumSpiraTut10 tutorial code, adapted for modular Page-based design
 * within this project.
 */

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final By CART_TABLE = By.id("tbodyid");
    private static final By DELETE_LINKS = By.xpath("//table//a[text()='Delete']");

    public CartPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    // In a BasePage or reuse your helper:
    private void waitForNoModal(WebDriver driver, WebDriverWait wait) {
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal.show"))); } catch (Exception ignore) {}
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-backdrop"))); } catch (Exception ignore) {}
    }


    public void addToCartFromProduct(){
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Add to cart"))).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        // ensure alert is closed before moving on
        wait.until(ExpectedConditions.not(ExpectedConditions.alertIsPresent()));
    }


    // CartPage.java
    public void openCart(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
        By cartLink = By.id("cartur");

        if (driver.findElements(cartLink).isEmpty()) {
            // not on a page with navbar rendered — go home
            new HomePage(driver).goHome();
        }

        waitForNoModal(driver, wait);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        } catch (org.openqa.selenium.ElementNotInteractableException e) {
            WebElement el = driver.findElement(cartLink);
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid"))); // cart table body
    }


    public boolean hasPlaceOrder(){
        return !driver.findElements(By.xpath("//button[normalize-space(.)='Place Order']")).isEmpty();
    }

    public void deleteFirstRowIfPresent(){
        // Wait for cart/body
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_TABLE));

        List<WebElement> deletes = driver.findElements(DELETE_LINKS);
        if (deletes.isEmpty()) return;

        // Track the row we’re about to delete so we can wait for staleness
        WebElement row = deletes.get(0).findElement(By.xpath("./ancestor::tr"));
        deletes.get(0).click();

        // Wait for that row to be removed (staled or count decreased)
        wait.until(ExpectedConditions.stalenessOf(row));
        wait.until(ExpectedConditions.or(
                ExpectedConditions.numberOfElementsToBeLessThan(DELETE_LINKS, deletes.size()),
                ExpectedConditions.invisibilityOf(row)
        ));
    }

    public void placeOrderWith(String name, String card){
        driver.findElement(By.xpath("//button[text()='Place Order']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name"))).sendKeys(name);
        driver.findElement(By.id("card")).sendKeys(card);
        driver.findElement(By.xpath("//button[text()='Purchase']")).click();
    }

    public boolean sawPurchaseConfirmation(){
        return driver.getPageSource().contains("Id:") || driver.getPageSource().contains("Amount");
    }
}

