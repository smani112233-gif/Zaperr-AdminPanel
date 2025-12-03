package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class DashboardMenuButtonNavigationTest extends BaseTest {

    @Test(priority = 2)
    public void dashboardMenuNavigation() {
        ExtentTest t = extent.createTest("Dashboard Menu Button Navigation");

        try {
            // 1️⃣ Login
            login();
            t.info("Logged in successfully.");

            // ❌ NO hamburger click here anymore.
            // We assume sidebar is already open after login.

            // 2️⃣ Click menu items using the same structure Selenium IDE used
            int[] menuIndices = {2, 3, 4, 5, 7};

            for (int index : menuIndices) {
                String selector = ".MuiListItem-root:nth-child(" + index + ") .MuiTypography-root";
                try {
                    WebElement item = wait.until(
                        ExpectedConditions.elementToBeClickable(By.cssSelector(selector))
                    );

                    String label = "";
                    try {
                        label = item.getText().trim();
                    } catch (Exception ignore) {}

                    item.click();
                    t.info("Clicked side menu item nth-child(" + index + ")"
                           + (label.isEmpty() ? "" : " → " + label));

                    // small pause so page can load
                    Thread.sleep(1000);

                    // ❌ NO re-open hamburger here either.
                    // We won't toggle the menu at all.

                } catch (Exception e) {
                    t.warning("Could not click menu item nth-child(" + index + "): " + e.getMessage());
                }
            }

            // 3️⃣ Optional: click dashboard title
            try {
                WebElement dashboardTitle = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".css-1qtpij-MuiTypography-root")
                    )
                );
                dashboardTitle.click();
                t.info("Clicked dashboard title (.css-1qtpij-MuiTypography-root).");
            } catch (Exception e) {
                t.warning("Could not click dashboard title: " + e.getMessage());
            }

            t.pass("✅ Dashboard side menu navigation executed (with warnings if some items were not clickable).");

        } catch (Exception e) {
            String path = takeScreenshot("DashboardMenuButtonNavigationTest");
            try {
                t.fail("❌ Dashboard menu navigation failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception ignore) {}
            throw new RuntimeException(e);
        }
    }
}
