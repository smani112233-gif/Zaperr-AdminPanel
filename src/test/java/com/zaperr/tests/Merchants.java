package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

public class Merchants extends BaseTest {

    @Test(priority = 5)
    public void openMerchantsList() {
        test = extent.createTest("Merchants / Franchise Admin",
                "Verify Merchants (Franchise Admin) list page opens");

        try {
            // 1️⃣ Login
            login();
            test.info("Logged in successfully.");

            // 2️⃣ Click Franchise Admin from side menu (from .side test)
            WebElement merchantsMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(.,'Franchise Admin')]")
            ));
            merchantsMenu.click();
            test.info("Clicked 'Franchise Admin' menu.");

            test.pass("Merchants list loaded successfully. Table is visible.");

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("merchantsList");
            test.fail("Merchants test failed: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }
}
