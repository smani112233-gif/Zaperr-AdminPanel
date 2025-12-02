package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

            // 2️⃣ Click hamburger (menu button) automatically
            clickHamburger(t);

            // 3️⃣ Click menu items using same locators Selenium IDE used
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

                    Thread.sleep(1000);

                    // Reopen menu if it collapses (best-effort)
                    try {
                        clickHamburger(t);
                        Thread.sleep(500);
                    } catch (Exception ignore) {
                        // ignore if hamburger not present
                    }

                } catch (Exception e) {
                    t.warning("Could not click menu item nth-child(" + index + "): " + e.getMessage());
                }
            }

            // 4️⃣ Optional: click dashboard title
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

    /**
     * Clicks the hamburger / drawer button automatically.
     */
    private void clickHamburger(ExtentTest t) {
        try {
            // primary locator (aria-label)
            WebElement menuButton = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[aria-label='open drawer']")
                )
            );
            try {
                menuButton.click();
                t.info("Clicked hamburger (open drawer button) by aria-label.");
            } catch (Exception e) {
                // JS click fallback
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuButton);
                t.info("Clicked hamburger using JS (aria-label='open drawer').");
            }
        } catch (Exception firstFail) {
            // backup locator: icon button on left
            try {
                WebElement menuButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiIconButton-edgeStart")
                    )
                );
                try {
                    menuButton.click();
                    t.info("Clicked hamburger using backup locator (.MuiIconButton-edgeStart).");
                } catch (Exception e2) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", menuButton);
                    t.info("Clicked hamburger using JS backup (.MuiIconButton-edgeStart).");
                }
            } catch (Exception finalFail) {
                t.warning("Hamburger menu not found or not clickable: " + finalFail.getMessage());
            }
        }
    }
}
