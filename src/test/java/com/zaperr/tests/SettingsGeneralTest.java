package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class SettingsGeneralTest extends BaseTest {

    @Test(priority = 8)
    public void settingsGeneralUpdate() {
        ExtentTest t = extent.createTest("Settings - General Configuration Test");

        try {
            // 1️⃣ Login (from BaseTest)
            login();
            t.info("Logged in successfully.");

            // 2️⃣ Open Settings from sidebar (7th menu item)
            WebElement settingsMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiListItem-root:nth-child(7) .MuiTypography-root")
                )
            );
            settingsMenu.click();
            t.info("Opened Settings page from sidebar.");
            Thread.sleep(800);

            // 3️⃣ Open General settings card
            WebElement generalCard;
            try {
                // Prefer card that contains text 'General'
                generalCard = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'MuiGrid-root')]//div[contains(@class,'MuiPaper-root')]//h6[contains(.,'General')]/ancestor::div[contains(@class,'MuiPaper-root')]")
                    )
                );
                t.info("Found General Settings card by title text.");
            } catch (Exception e1) {
                // Fallback: original Selenium IDE XPath
                generalCard = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@id='root']/div/div[2]/div[2]/div[2]/div/div/div[2]/div")
                    )
                );
                t.info("Found General Settings card using fallback XPath.");
            }

            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", generalCard);
            Thread.sleep(400);
            try {
                generalCard.click();
            } catch (Exception clickEx) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", generalCard);
            }
            t.info("Opened General settings page.");

            // 4️⃣ Type text in General field (id ':rc:')
            WebElement generalInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id(":rc:"))
            );
            generalInput.clear();
            generalInput.sendKeys("i am the best");
            t.info("Updated General settings text to: 'i am the best'.");

            // 5️⃣ Click Save / Update button (.MuiButton-contained:nth-child(2))
            WebElement saveButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiButton-contained:nth-child(2)")
                )
            );
            saveButton.click();
            t.info("Clicked Save/Update button in General settings.");

            // 6️⃣ Optional: click dashboard/header title
            try {
                WebElement header = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".css-1qtpij-MuiTypography-root")
                    )
                );
                header.click();
                t.info("Clicked header / dashboard title (.css-1qtpij-MuiTypography-root).");
            } catch (Exception e) {
                t.info("Optional: could not click header: " + e.getMessage());
            }

            // 7️⃣ Optional: logout
            try {
                WebElement logoutBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiBox-root")
                    )
                );
                logoutBtn.click();
                t.info("Clicked logout button from General settings flow.");
            } catch (Exception e) {
                t.info("Optional: could not click logout button: " + e.getMessage());
            }

            t.pass("✅ Settings → General configuration flow executed successfully.");

        } catch (Exception e) {
            String path = takeScreenshot("SettingsGeneralTest");
            try {
                t.fail("❌ Settings General test failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception ignore) { }
            throw new RuntimeException(e);
        }
    }
}
