package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

public class LoginTest extends BaseTest {

    // ✅ VALID LOGIN
    @Test(priority = 1)
    public void validLoginTest() {
        test = extent.createTest("Valid Login Test", "Verify user can log in successfully");

        try {
            login();  // from BaseTest
            test.pass("✅ Login successful and dashboard loaded.");

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("validLoginTest");
            test.fail("❌ Error during valid login: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }

    // ❌ INVALID LOGIN
    @Test(priority = 2)
    public void invalidLoginTest() {
        test = extent.createTest("Invalid Login Test", "Verify login fails with wrong credentials");

        try {
            driver.get("http://192.168.1.31:3000");

            WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@autocomplete='email']"))
            );
            emailField.clear();
            emailField.sendKeys("wrong@gmail.com");
            test.info("Entered invalid email");

            WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//input[@type='password']"))
            );
            passwordField.clear();
            passwordField.sendKeys("WrongPassword123");
            test.info("Entered invalid password");

            WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Login']"))
            );
            loginButton.click();
            test.info("Clicked Login with wrong credentials");

            Thread.sleep(4000);

            boolean stillOnLogin =
                    driver.getCurrentUrl().contains("login")
                 || driver.getPageSource().contains("Invalid")
                 || driver.getPageSource().contains("Login");

            if (stillOnLogin) {
                test.pass("❌ Invalid login correctly failed, user is not logged in.");
            } else {
                String screenshotPath = takeScreenshot("invalidLoginTest");
                test.fail("🚨 Invalid login succeeded unexpectedly!",
                    MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            }

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("invalidLoginTest");
            test.fail("❌ Error during invalid login: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }
}
