/**
 *  This abstract base class provides the common setup and teardown logic for
 *  all Selenium WebDriver tests in this project. It configures the ChromeDriver,
 *  manages browser lifecycle, sets global timeouts, and navigates to the
 *  application under test (Demoblaze).
 *
 *  The class is extended by individual test classes to ensure consistent
 *  environment configuration across the suite.
 *
 * -----------------------------------------------------------------------------
 *  Key Features:
 *  • Centralized WebDriver setup and teardown using JUnit 5 lifecycle methods
 *    (@BeforeAll and @AfterAll)
 *  • Implicit wait configuration for element synchronization
 *  • Browser window maximization and base URL loading
 *  • Integrated SpiraTest reporting and configuration through annotations
 *
 * -----------------------------------------------------------------------------
 *  SpiraTest Integration:
 *  This class uses the SpiraTest JUnit extension to automatically log test
 *  execution results in SpiraTest. Configuration details (URL, login, project
 *  and release IDs, RSS token) are provided through annotations:
 *
 *      @ExtendWith(SpiraExtension.class)
 *      @SpiraTestConfiguration(...)
 * -----------------------------------------------------------------------------
 *  Author: Deena Yu-fawcett (s3925523)
 *  Course: ISYS1087 Software Testing (RMIT University)
 * -----------------------------------------------------------------------------
 */
package au.rmit.s3925523.s3925523seleniumtest.base;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import com.inflectra.spiratest.addons.junitextension.SpiraExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;


public abstract class BaseTest {
    protected static WebDriver driver;
    protected static final String BASE_URL = "https://demoblaze.com/";

    @BeforeAll
    static void start() {
        driver = new ChromeDriver();                  // Selenium Manager handles driver
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize(); //consistency
        driver.get(BASE_URL);
    }

    @AfterAll
    static void stop() {
        if (driver != null) driver.quit();
    }
}

