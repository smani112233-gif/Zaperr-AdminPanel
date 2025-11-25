package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

public class DeliveryPartnersCreation extends BaseTest {

    @Test(priority = 7)
    public void createDeliveryPartnerWithRandomInfo() {
        test = extent.createTest("Delivery Partners - Create",
                "Create delivery partner with random info (locators may need tweaking)");

        try {
            // 1️⃣ Login
            login();
            test.info("Logged in successfully.");

            // 2️⃣ Go to Settings (assuming Delivery Partners under Settings)
            WebElement settingsMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(.,'Settings')]")
            ));
            settingsMenu.click();
            test.info("Opened Settings.");

            // 3️⃣ Open card related to Delivery / Partners (adjust this if needed)
            WebElement deliveryCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(.,'Delivery') or contains(.,'Partner')]")
            ));
            deliveryCard.click();
            test.info("Opened Delivery Partners section.");

            // 4️⃣ Generate random data
            String name  = "DP-" + (System.currentTimeMillis() % 100000);
            String phone = "9" + (System.currentTimeMillis() % 1000000000L);

            // ⚠️ TODO: Update these locators to your real input fields
            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//input)[1]")
            ));
            nameInput.clear();
            nameInput.sendKeys(name);

            WebElement phoneInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//input)[2]")
            ));
            phoneInput.clear();
            phoneInput.sendKeys(phone);

            // Click Save / Create
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Save') or contains(.,'Create')]")
            ));
            saveButton.click();

            test.pass("Delivery partner creation flow executed (please verify data; update locators if needed).");

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("deliveryPartnersCreation");
            test.fail("Delivery partners creation test failed (update locators): " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }
}
