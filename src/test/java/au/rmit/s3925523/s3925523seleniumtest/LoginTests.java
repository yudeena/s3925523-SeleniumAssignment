package au.rmit.s3925523.s3925523seleniumtest;

import au.rmit.s3925523.s3925523seleniumtest.pages.*;

import com.inflectra.spiratest.addons.junitextension.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.UUID;

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

public class LoginTests {
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

    // put in a small BasePage OR duplicate in SignUpPage & HomePage for speed
    private void waitForNoModal(WebDriverWait wait, WebDriver driver) {
        // modal content invisible
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal.show"))); }
        catch (Exception ignored) {}
        // backdrop overlay gone
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".modal-backdrop"))); }
        catch (Exception ignored) {}
    }


    @Test
    @SpiraTestCase(testCaseId = 38290)
    void loginWithValidCredentials() {
        String u = "user_" + UUID.randomUUID().toString().substring(0,8);
        new SignUpPage(driver).signUp(u, "Password123!");

        HomePage home = new HomePage(driver);
        home.goHome();            // ensures clean page, no lingering modal/backdrop
        home.openLoginModal();

        new LoginPage(driver).login(u, "Password123!");

        new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("nameofuser")));

        assertTrue(driver.findElement(By.id("nameofuser"))
                .getText().toLowerCase().contains(u.substring(0,4)));
    }



    @Test
    @SpiraTestCase(testCaseId = 38291)
    void loginWithInvalidCredentials_showsError() {
        HomePage home = new HomePage(driver);
        home.openLoginModal();
        new LoginPage(driver).login("not_a_user", "wrongpass");
        new WebDriverWait(driver, Duration.ofSeconds(6)).until(ExpectedConditions.alertIsPresent()).accept();
        // If alert text is needed, capture and assert contains 'wrong'/'user'
        assertTrue(true); // basic presence of alert is success criteria
    }
}

