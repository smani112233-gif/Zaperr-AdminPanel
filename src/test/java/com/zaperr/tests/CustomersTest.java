package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class CustomersTest extends BaseTest {

    @Test(priority = 5)
    public void customersFilterAndLogout() {
        ExtentTest t = extent.createTest("Customers Page Test");

        try {
            // 1️⃣ Login
            login();
            t.info("Logged in successfully.");

            // 2️⃣ Open Sidebar Automatically
            openSidebar(t);

            // 3️⃣ Click Customers menu (4th item)
            WebElement customersMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiListItem-root:nth-child(4) .MuiTypography-root")
                )
            );
            customersMenu.click();
            t.info("Navigated to Customers page.");

            Thread.sleep(1000);

            // 4️⃣ Apply Filter(s) — matching the IDE script logic
            applyFilterOption(t, 2); // Example: apply filter option 2
            applyFilterOption(t, 3); // Example: apply filter option 3
            applyFilterOption(t, 1); // Example: apply filter option 1

            t.info("Customer filter testing executed.");

            // 5️⃣ Logout
            WebElement profileIcon = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiAvatar-root")
                )
            );
            profileIcon.click();
            t.info("Clicked profile/avatar icon.");

            WebElement logoutBtn = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiBox-root")
                )
            );
            logoutBtn.click();
            t.pass("Successfully logged out from Customers page.");

        } catch (Exception e) {
            String screenshot = takeScreenshot("CustomersTest");
            try {
                t.fail("❌ Customers Test Failed: " + e.getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshot).build());
            } catch (Exception ignore) {
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper: open sidebar menu if collapsed
     */
    private void openSidebar(ExtentTest t) {
        try {
            WebElement menuButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector("button[aria-label='open drawer']"))
            );
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuButton);
            Thread.sleep(700);
            t.info("Sidebar opened.");
        } catch (Exception e) {
            t.warning("Sidebar may already be open. Details: " + e.getMessage());
        }
    }

    /**
     * Filter dropdown helper based on "nth-child" recorded by IDE script
     */
    private void applyFilterOption(ExtentTest t, int option) throws InterruptedException {
        try {
            WebElement filterDropdown = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("[id*=':r']"))
            );
            filterDropdown.click();
            Thread.sleep(600);

            WebElement optionClick = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiMenuItem-root:nth-child(" + option + ")")
                )
            );
            optionClick.click();
            t.info("Applied filter → Option " + option);

            Thread.sleep(800);

        } catch (Exception e) {
            t.warning("Filter option " + option + " failed: " + e.getMessage());
        }
    }
}
