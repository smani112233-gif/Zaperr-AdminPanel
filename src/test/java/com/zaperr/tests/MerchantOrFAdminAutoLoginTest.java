package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class MerchantOrFAdminAutoLoginTest extends BaseTest {

    @Test(priority = 3)
    public void merchantOrFAdminAutoLogin() {
        ExtentTest t = extent.createTest("Merchant / Franchise Admin Auto Login Test");

        try {
            // 1️⃣ Login as Super Admin
            login();
            t.info("Logged in as admin successfully.");

            // // 2️⃣ OPEN SIDEBAR MENU AUTOMATICALLY (no manual click needed)
            // openSidebar(t);

            // 3️⃣ Click Franchise Admin (side menu item 3)
            WebElement franchiseAdminMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiListItem-root:nth-child(3) .MuiTypography-root")
                )
            );
            String faLabel = franchiseAdminMenu.getText();
            franchiseAdminMenu.click();
            t.info("Opened side menu item 3 → " + faLabel);

            // 4️⃣ Click auto-login icon/button in 9th table row
            WebElement autoLoginBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiTableRow-root:nth-child(9) .css-4y8pkd")
                )
            );
            autoLoginBtn.click();
            t.info("Clicked auto-login action on 9th row in Franchise Admin table.");

            // 5️⃣ Scroll to top (as in original script)
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
            t.info("Scrolled to top of the page.");

            // 6️⃣ Click header / dashboard title
            try {
                WebElement header = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".css-1qtpij-MuiTypography-root")
                    )
                );
                header.click();
                t.info("Clicked header / dashboard title (.css-1qtpij-MuiTypography-root).");
            } catch (Exception e) {
                t.warning("Could not click header (.css-1qtpij-MuiTypography-root): " + e.getMessage());
            }

            // 7️⃣ Click right-side icon (stale-safe)
            clickRightIcon(t, "first");

            // 8️⃣ Click header again
            try {
                WebElement headerAgain = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".css-1qtpij-MuiTypography-root")
                    )
                );
                headerAgain.click();
                t.info("Clicked header again (.css-1qtpij-MuiTypography-root).");
            } catch (Exception e) {
                t.warning("Could not click header again (.css-1qtpij-MuiTypography-root): " + e.getMessage());
            }

            // 9️⃣ Click button 2 (>.MuiBox-root) – likely a confirm
            try {
                WebElement secondButtonBox = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiBox-root")
                    )
                );
                secondButtonBox.click();
                t.info("Clicked second button (>.MuiBox-root) – likely logout/confirm.");
            } catch (Exception e) {
                t.warning("Could not click .MuiButtonBase-root:nth-child(2) > .MuiBox-root: " + e.getMessage());
            }

            // 🔁 10️⃣ Click right-side icon again (stale-safe)
            clickRightIcon(t, "second");

            // 🔁 11️⃣ Final confirm button
            try {
                WebElement secondButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiButtonBase-root:nth-child(2)")
                    )
                );
                secondButton.click();
                t.info("Clicked second confirm button (.MuiButtonBase-root:nth-child(2)).");
            } catch (Exception e) {
                t.warning("Could not click final .MuiButtonBase-root:nth-child(2): " + e.getMessage());
            }

            t.pass("✅ Merchant / Franchise Admin auto-login flow executed (with stale-safe icon handling).");

        } catch (Exception e) {
            String path = takeScreenshot("MerchantOrFAdminAutoLoginTest");
            try {
                t.fail("❌ Merchant / Franchise Admin auto-login test failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception ignore) {}
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper to click the right-side icon (.MuiIconButton-edgeEnd) with retry on stale element.
     */
    private void clickRightIcon(ExtentTest t, String label) {
        By locator = By.cssSelector(".MuiIconButton-edgeEnd");

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement rightIcon = wait.until(
                    ExpectedConditions.elementToBeClickable(locator)
                );
                rightIcon.click();
                t.info("Clicked right-side icon (" + label + " attempt " + attempt + ") using locator: " + locator);
                return;
            } catch (StaleElementReferenceException sere) {
                t.warning("StaleElementReference on right-side icon (" + label + " attempt " + attempt + "), retrying...");
            } catch (Exception e) {
                t.warning("Could not click right-side icon (" + label + " attempt " + attempt + "): " + e.getMessage());
                return; // don't retry on other exceptions
            }
        }

        t.warning("Failed to click right-side icon (" + label + ") after 3 attempts.");
    }
}
