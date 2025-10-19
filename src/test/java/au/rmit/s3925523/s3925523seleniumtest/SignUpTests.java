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

import java.util.UUID;

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

public class SignUpTests extends BaseTest {

    @Test
    @SpiraTestCase(testCaseId = 38288)
    void userCanSignUp_withUniqueCredentials_showsSuccessAlert() {
        String u = "user_" + UUID.randomUUID().toString().substring(0,8);
        String p = "Password123!";
        String alert = new SignUpPage(driver).signUp(u, p);
        String msg = alert.toLowerCase();
        assertTrue(msg.contains("sign up") || msg.contains("successful") || msg.contains("user"),
                "Unexpected alert: " + alert);
    }

    @Test
    @SpiraTestCase(testCaseId = 38289)
    void signUp_withExistingUsername_showsDuplicateError() {
        String existing = "existing_user_demo"; // ensure you create once first
        String alert = new SignUpPage(driver).signUp(existing, "Password123!");
        String msg = alert.toLowerCase();
        assertTrue(msg.contains("exist") || msg.contains("already") || msg.contains("error"),
                "Expected duplicate error, got: " + alert);
    }
}