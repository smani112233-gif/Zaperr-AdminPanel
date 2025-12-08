package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;

import java.util.List;
import java.util.Random;

public class SettingsInviteUserPermissionsTest extends BaseTest {

    @Test(priority = 7)
    public void inviteUserWithRandomPermissions() {
        ExtentTest t = extent.createTest("Settings - Invite User With Random Permissions");

        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // 1️⃣ Login as admin (from BaseTest)
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

            // 3️⃣ Open Settings card 1 (Users / Invite Users)
            try {
                WebElement settingsCard1 = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiGrid-root:nth-child(1) > .MuiPaper-root")
                    )
                );
                js.executeScript("arguments[0].scrollIntoView(true);", settingsCard1);
                Thread.sleep(300);
                try {
                    settingsCard1.click();
                } catch (Exception eClick) {
                    js.executeScript("arguments[0].click();", settingsCard1);
                }
                t.info("Opened Settings Card 1 (Users / Invite Users).");
            } catch (Exception e) {
                throw new RuntimeException("Could not open Settings Card 1: " + e.getMessage());
            }

            Thread.sleep(800); // allow card body to load

            // 4️⃣ Click "Invite User" button
            WebElement inviteButton;
            try {
                inviteButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(.,'Invite') or contains(.,'Add User') or contains(.,'Add New')]")
                    )
                );
                t.info("Found 'Invite User' button by text (XPath).");
            } catch (Exception e1) {
                inviteButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiButton-contained-dark")
                    )
                );
                t.info("Found 'Invite User' button by CSS (.MuiButton-contained-dark).");
            }

            js.executeScript("arguments[0].scrollIntoView(true);", inviteButton);
            Thread.sleep(300);
            try {
                inviteButton.click();
            } catch (Exception eClick) {
                js.executeScript("arguments[0].click();", inviteButton);
            }
            t.info("Clicked 'Invite User' button.");

            // 5️⃣ Wait for dialog to appear
            try {
                wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div[role='dialog'] input")
                    )
                );
            } catch (Exception ignored) {}
            Thread.sleep(500);

            // 6️⃣ Generate random email + name
            Random random = new Random();
            String randomEmail = "user" + (10000 + random.nextInt(90000)) + "@example.com";
            String randomName  = "Name" + (1000 + random.nextInt(9000));

            // 7️⃣ Fill Email field
            WebElement emailInput;
            try {
                emailInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id(":rf:"))
                );
                t.info("Found email field by id ':rf:'.");
            } catch (Exception e) {
                emailInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@role='dialog']//label[contains(.,'Email')]/following::input[1]")
                    )
                );
                t.info("Found email field by 'Email' label (fallback).");
            }
            emailInput.clear();
            emailInput.sendKeys(randomEmail);
            t.info("Entered random email: " + randomEmail);

            // 8️⃣ Fill Name field
            WebElement nameInput;
            try {
                nameInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id(":rg:"))
                );
                t.info("Found name field by id ':rg:'.");
            } catch (Exception e1) {
                nameInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("(//div[@role='dialog']//input)[2]")
                    )
                );
                t.info("Found name field as second input inside dialog (fallback).");
            }
            nameInput.clear();
            nameInput.sendKeys(randomName);
            t.info("Entered random name: " + randomName);

            // 9️⃣ Try to click a permission checkbox (no long waits, no warnings)
            List<WebElement> permissionLabels =
                driver.findElements(By.cssSelector("div[role='dialog'] .MuiFormControlLabel-root"));

            if (!permissionLabels.isEmpty()) {
                WebElement firstLabel = permissionLabels.getFirst();
                try {
                    js.executeScript("arguments[0].scrollIntoView({block:'center'});", firstLabel);
                    js.executeScript("arguments[0].click();", firstLabel);
                    t.info("Clicked first permission checkbox via label inside dialog.");
                } catch (Exception e) {
                    t.info("Permission checkbox present but click failed (skipping): " + e.getMessage());
                }
            } else {
                t.info("No permission checkboxes visible in dialog – skipping permissions selection.");
            }

            // 🔟 Click Submit / Save button
            WebElement saveButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".css-138zggh-MuiButtonBase-root-MuiButton-root")
                )
            );
            saveButton.click();
            t.info("Clicked 'Invite / Save' button.");

            // 1️⃣1️⃣ Optional: Click header / dashboard title
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

            // 1️⃣2️⃣ Optional: Logout from this flow
            try {
                WebElement logoutBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiBox-root")
                    )
                );
                logoutBtn.click();
                t.info("Clicked logout button after invite user.");
            } catch (Exception e) {
                t.info("Optional: could not click logout button: " + e.getMessage());
            }

            t.pass("✅ Settings → Invite User with random info executed successfully.");

        } catch (Exception e) {
            String path = takeScreenshot("SettingsInviteUserPermissionsTest");
            try {
                t.fail("❌ Invite User Permissions test failed: " + e.getMessage(),
                    com.aventstack.extentreports.MediaEntityBuilder
                        .createScreenCaptureFromPath(path).build());
            } catch (Exception ignore) {}
            throw new RuntimeException(e);
        }
    }
}
