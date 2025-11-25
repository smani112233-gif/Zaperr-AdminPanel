package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

public class OrderAssign extends BaseTest {

    @Test(priority = 4)
    public void assignOrderWithFreeOption() {
        test = extent.createTest("Orders - Assign Order (Free)",
                "Open order assign popup and assign 'Free' option");

        try {
            // 1️⃣ Login
            login();
            test.info("Logged in successfully.");

            // 2️⃣ Go to Orders
            WebElement ordersMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(.,'Orders')]")
            ));
            ordersMenu.click();
            test.info("Opened Orders page.");

            // 3️⃣ Wait for table
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".MuiTableContainer-root")
            ));

            // 4️⃣ Open actions menu for one order (row 7, col 11 in .side)
            WebElement actionIcon = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".MuiTableRow-root:nth-child(7) > .MuiTableCell-root:nth-child(11) svg")
            ));
            actionIcon.click();
            test.info("Opened order action popup.");

            // 5️⃣ Click "Free"
            WebElement freeOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Free')]")
            ));
            freeOption.click();
            test.info("Selected 'Free' option.");

            // 6️⃣ Click "Save"
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Save')]")
            ));
            saveButton.click();
            test.pass("Order assigned with 'Free' and saved successfully.");

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("orderAssignFree");
            test.fail("Order assignment test failed: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }
}
