package au.rmit.s3925523.s3925523seleniumtest;
import au.rmit.s3925523.s3925523seleniumtest.pages.SignUpPage;
import au.rmit.s3925523.s3925523seleniumtest.base.BaseTest;
import com.inflectra.spiratest.addons.junitextension.SpiraExtension;
import org.junit.jupiter.api.Test;

import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpiraExtension.class)
@SpiraTestConfiguration(
        url = "https://rmit.spiraservice.net",
        login = "s3925523",
        rssToken = "{59E2D88B-88B1-4FFB-9470-FB906D2DFA96}",
        projectId = 643,
        releaseId = 2368,
        testSetId = -1
)

class SpiraSmokeTest {
    @SpiraTestCase(testCaseId = 40074)
    @Test void ok() { assertTrue(true); }
}

