package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class LoginLogoutTest extends BaseTest {

    @Test(priority = 1)
    public void loginLogoutFlow() {
        ExtentTest t = extent.createTest("Login & Logout Test");

        try {
            // Step 1: Login using BaseTest method
            login();
            t.info("Logged in successfully.");

            // Step 2: Open Profile dropdown (avatar)
            WebElement profileIcon = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector(".MuiAvatar-root"))
            );
            profileIcon.click();
            t.info("Opened profile menu (avatar clicked).");

            // Step 3: Click Logout using Selenium IDE locator
            // from your original script:
            // driver.findElement(By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiBox-root")).click();
            WebElement logoutBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiBox-root")
                )
            );
            logoutBtn.click();
            t.info("Clicked logout button from profile menu.");

            // Step 4: Validate landing back on login page
            wait.until(ExpectedConditions.urlContains("login"));
            t.pass("✅ Logout successful and redirected to login screen.");

        } catch (Exception e) {
            String path = takeScreenshot("LoginLogoutTest");
            try {
                t.fail("❌ Login-Logout flow failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception ignore) {}
            throw new RuntimeException(e);
        }
    }
}
