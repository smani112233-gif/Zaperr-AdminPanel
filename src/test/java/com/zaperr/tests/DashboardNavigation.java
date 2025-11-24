package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;
import com.aventstack.extentreports.MediaEntityBuilder;

public class DashboardNavigation extends BaseTest {

    @Test(priority = 1)
    public void dashboardNavigationTest() {
        test = extent.createTest("Dashboard Navigation", "Verify dashboard page loads correctly");

        try {
            login();
            test.info("✅ Logged in successfully.");

            // Example verification
            WebElement dashboardHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h3[contains(text(),'Dashboard')]")
            ));
            test.pass("Dashboard loaded successfully with header: " + dashboardHeader.getText());

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("dashboardNavigationTest");
            test.fail("❌ Dashboard navigation failed: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }
}
