package au.rmit.s3925523.s3925523seleniumtest;

import au.rmit.s3925523.s3925523seleniumtest.base.BaseTest;
import au.rmit.s3925523.s3925523seleniumtest.pages.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraExtension;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@ExtendWith(SpiraExtension.class)
@SpiraTestConfiguration(
        url = "https://rmit.spiraservice.net",
        login = "s3925523",
        rssToken = "{59E2D88B-88B1-4FFB-9470-FB906D2DFA96}",
        projectId = 643,
        releaseId = 2368,
        testSetId = 5288
)

public class BrowseCategoryTests extends BaseTest {

    @Test
    @SpiraTestCase(testCaseId = 38292)
    void phonesCategoryShowsProducts() {
        new HomePage(driver).chooseCategory("Phones");
        assertFalse(new BrowsePage(driver).products().isEmpty());
    }

    @Test
    @SpiraTestCase(testCaseId = 38293)
    void laptopsCategoryShowsProducts() {
        new HomePage(driver).chooseCategory("Laptops");
        assertFalse(new BrowsePage(driver).products().isEmpty());
    }

    @Test
    @SpiraTestCase(testCaseId = 38294)
    void monitorsCategoryShowsProducts() {
        new HomePage(driver).chooseCategory("Monitors");
        assertFalse(new BrowsePage(driver).products().isEmpty());
    }
    @Test
    @SpiraTestCase(testCaseId = 38295)
    void productDetailsAreVisible() {
        new HomePage(driver).chooseCategory("Laptops");
        new BrowsePage(driver).openFirstProduct();

        new WebDriverWait(driver, Duration.ofSeconds(8))
                .until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Add to cart")));

        assertTrue(driver.findElements(By.linkText("Add to cart")).size() > 0,
                "Expected 'Add to cart' on the product details page");
    }

}
