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

public class HomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private static final String BASE_URL = "https://demoblaze.com/";

    public HomePage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }
    /** Force navigation to the homepage and wait for product grid. */
    public void goHome() {
        driver.navigate().to(BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("tbodyid")));
    }
    // put in a small BasePage OR duplicate in SignUpPage & HomePage for speed
    // put this in a small BasePage you already have, or at top of HomePage/SignUpPage class
    private void waitForNoModal(WebDriver driver, WebDriverWait wait) {
        By visibleModal = By.cssSelector(".modal.show");
        By backdrop    = By.cssSelector(".modal-backdrop");

        // try to close any visible modal gracefully
        if (!driver.findElements(visibleModal).isEmpty()) {
            WebElement modal = driver.findElement(visibleModal);
            // try Close or ×
            for (WebElement btn : modal.findElements(By.xpath(".//button[normalize-space()='Close' or @class='close' or normalize-space()='×']"))) {
                try { btn.click(); break; } catch (Exception ignored) {}
            }
        }
        // wait until both modal & backdrop are gone (ignore if already absent)
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(visibleModal)); } catch (Exception ignored) {}
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(backdrop)); } catch (Exception ignored) {}
    }


    public boolean hasCategories(){
        return driver.getPageSource().contains("CATEGORIES");
    }

    public void openSignUpModal(){ driver.findElement(By.id("signin2")).click(); }

    // HomePage.java
    public void openLoginModal(){
        waitForNoModal(driver, wait); // clear any other modal first
        By loginLink = By.id("login2");
        wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        try {
            driver.findElement(loginLink).click();
        } catch (ElementClickInterceptedException e) {
            waitForNoModal(driver, wait);
            driver.findElement(loginLink).click();
        }
        new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
    }



    public void goToCart(){ driver.findElement(By.id("cartur")).click(); }

    public void chooseCategory(String name){
        // If categories aren’t present, go home first
        if (driver.findElements(By.id("itemc")).isEmpty()) {
            goHome();
        }
        // There are multiple #itemc nodes — wait for presence of all, then click by text
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("itemc")));
        for (WebElement cat : driver.findElements(By.id("itemc"))) {
            if (cat.getText().equalsIgnoreCase(name)) {
                cat.click();
                return;
            }
        }
        throw new NoSuchElementException("Category not found: " + name);
    }
}

