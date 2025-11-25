package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

public class CreateCoupons extends BaseTest {

    @Test(priority = 8)
    public void createCouponWithRandomInfo() {
        test = extent.createTest("Promotions - Create Coupon",
                "Create coupon with random info (locators may need tweaking)");

        try {
            // 1️⃣ Login
            login();
            test.info("Logged in successfully.");

            // 2️⃣ Open Promotions page
            WebElement promotionsMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(.,'Promotions')]")
            ));
            promotionsMenu.click();
            test.info("Opened Promotions page.");

            // 3️⃣ Click "Add New" button (like in .side for some modules)
            WebElement addNewBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Add New')]")
            ));
            addNewBtn.click();
            test.info("Clicked 'Add New'.");

            // 4️⃣ Random coupon data
            String code  = "AUTO" + (System.currentTimeMillis() % 100000);
            String title = "Automation Coupon";

            //  TODO: Update these locators based on actual coupon form
            WebElement codeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//input)[1]")
            ));
            codeInput.clear();
            codeInput.sendKeys(code);

            WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//input)[2]")
            ));
            titleInput.clear();
            titleInput.sendKeys(title);

            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Save') or contains(.,'Create')]")
            ));
            saveButton.click();

            test.pass("Coupon creation flow executed (verify in UI, adjust locators as needed).");

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("createCoupons");
            test.fail("Create coupon test failed (update locators): " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }
}
