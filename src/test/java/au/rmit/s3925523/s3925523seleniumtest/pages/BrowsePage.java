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

public class BrowsePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final By FIRST_PRODUCT_LINK = By.cssSelector("#tbodyid .card-title a");

    public BrowsePage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    public List<WebElement> products(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".card-title")));
        return driver.findElements(By.cssSelector(".card-title"));
    }

    // pages/BrowsePage.java
    public void openFirstProduct(){
        // Wait for products to be present
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(FIRST_PRODUCT_LINK));

        // Retry a few times in case the grid re-renders
        for (int i = 0; i < 3; i++) {
            try {
                WebElement link = wait.until(ExpectedConditions.elementToBeClickable(FIRST_PRODUCT_LINK));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", link);
                link.click();
                return;
            } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
                // re-loop: the DOM likely rebuilt; re-find and try again
            }
        }
        throw new TimeoutException("Could not click first product (stale/click intercepted repeatedly).");
    }
}

