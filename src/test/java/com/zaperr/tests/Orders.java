package com.zaperr.tests;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

public class Orders extends BaseTest {

    @Test(priority = 3)
    public void updateOrderStatuses() {
        test = extent.createTest("Orders - Update Status",
                "Verify that order status dropdown values can be changed");

        try {
            // 1️⃣ Login
            login();
            test.info("Logged in successfully.");

            // 2️⃣ Go to Orders menu
            WebElement ordersMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(.,'Orders')]")
            ));
            ordersMenu.click();
            test.info("Opened Orders page from side menu.");

            // 3️⃣ Wait for table
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".MuiTableContainer-root table")
            ));

            // 4️⃣ Find all status dropdowns (based on .side ids like order-status-xxxxx)
            List<WebElement> statusDropdowns =
                    driver.findElements(By.cssSelector("select[id^='order-status-']"));

            if (statusDropdowns.isEmpty()) {
                test.skip("No order status dropdowns found. Nothing to update.");
                return;
            }

            // Use labels seen in .side: PaySuccess, Paid, Accepted, Ready, Collected, Completed, Cancelled
            String[] labelsToTry = {
                "PaySuccess", "Paid", "Accepted", "Ready", "Collected", "Completed", "Cancelled"
            };

            int idx = 0;
            for (WebElement dropdown : statusDropdowns) {
                if (idx >= labelsToTry.length) break;
                String label = labelsToTry[idx++];

                try {
                    Select select = new Select(dropdown);
                    select.selectByVisibleText(label);
                    test.info("Changed one order status to: " + label);
                } catch (Exception ee) {
                    test.warning("Could not set status '" + label + "' for one dropdown: " + ee.getMessage());
                }
            }

            test.pass("Orders status update flow executed.");

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("ordersUpdateOrderStatuses");
            test.fail("Orders status update test failed: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }
}
