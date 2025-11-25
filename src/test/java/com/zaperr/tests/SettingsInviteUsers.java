package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.MediaEntityBuilder;

public class SettingsInviteUsers extends BaseTest {

    @Test(priority = 6)
    public void inviteUserWithRandomDetails() {
        test = extent.createTest("Settings - Invite User",
                "Invite user with random email & name, using locators from .side");

        try {
            // 1️⃣ Login
            login();
            test.info("Logged in successfully.");

            // 2️⃣ Open Settings from side menu
            WebElement settingsMenu = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(.,'Settings')]")
            ));
            settingsMenu.click();
            test.info("Opened Settings.");

            // 3️⃣ Open "Users & permission" card
            WebElement usersPermissionCard = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[contains(.,'Users & permission')]")
            ));
            usersPermissionCard.click();
            test.info("Opened Users & permission section.");

            // 4️⃣ Click "Invite User" button
            WebElement inviteUserBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Invite User')]")
            ));
            inviteUserBtn.click();
            test.info("Clicked Invite User button.");

            // 5️⃣ Generate random email & name (same idea as .side)
            String email = "user" + System.currentTimeMillis() + "@example.com";
            String name  = "Name" + (System.currentTimeMillis() % 10000);

            // 6️⃣ Fill Email (id :rf: from .side)
            WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id=':rf:']")
            ));
            emailInput.clear();
            emailInput.sendKeys(email);
            test.info("Entered random email: " + email);

            // 7️⃣ Fill Name (id :rg: from .side)
            WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id=':rg:']")
            ));
            nameInput.clear();
            nameInput.sendKeys(name);
            test.info("Entered random name: " + name);

            // 8️⃣ Toggle one permission checkbox (8th item from .side)
            WebElement permissionCheckbox = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector(".MuiFormControlLabel-root:nth-child(8) .PrivateSwitchBase-input")
            ));
            permissionCheckbox.click();
            test.info("Toggled permission checkbox.");

            // 9️⃣ Save
            WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(.,'Save')]")
            ));
            saveButton.click();
            test.pass("Invite user flow executed successfully.");

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("settingsInviteUsers");
            test.fail("Invite Users test failed: " + e.getMessage(),
                MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }
}
