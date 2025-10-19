package au.rmit.s3925523.s3925523seleniumtest.pages;
/**
 * This Page Object class was inspired by the "Main" example from the
 * SeleniumSpiraTut10 tutorial code, adapted for modular Page-based design
 * within this project.
 */

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class SignUpPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By username = By.id("sign-username");
    private final By password = By.id("sign-password");
    private final By submit   = By.xpath("//button[text()='Sign up']");

    public SignUpPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
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


    // SignUpPage.java
    public void open(){
        waitForNoModal(driver, wait); // ensure no modal blocks the click
        By signUpLink = By.id("signin2");
        wait.until(ExpectedConditions.elementToBeClickable(signUpLink));
        try {
            driver.findElement(signUpLink).click();
        } catch (ElementClickInterceptedException e) {
            waitForNoModal(driver, wait); // if a modal/backdrop just popped, clear again
            driver.findElement(signUpLink).click();
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(username));
    }

    public String signUp(String user, String pass){
        open();
        driver.findElement(username).clear();
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(pass);
        driver.findElement(submit).click();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert a = driver.switchTo().alert();
        String txt = a.getText(); a.accept();

        // ensure the signup modal/backdrop are gone before next action
        waitForNoModal(driver, wait);
        return txt;
    }


}