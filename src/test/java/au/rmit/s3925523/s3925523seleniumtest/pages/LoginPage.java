package au.rmit.s3925523.s3925523seleniumtest.pages;
/**
 * This Page Object class was inspired by the "Main" example from the
 * SeleniumSpiraTut10 tutorial code, adapted for modular Page-based design
 * within this project.
 */

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class LoginPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver){
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(8));
    }

    public void open(){
        driver.findElement(By.id("login2")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loginusername")));
    }

    public void login(String user, String pass){
        driver.findElement(By.id("loginusername")).clear();
        driver.findElement(By.id("loginusername")).sendKeys(user);
        driver.findElement(By.id("loginpassword")).clear();
        driver.findElement(By.id("loginpassword")).sendKeys(pass);
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
    }
}

