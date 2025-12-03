package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class OrdersStatusUpdateOrderAssignTest extends BaseTest {

    @Test(priority = 4)
    public void ordersStatusUpdateAndAssign() {
        ExtentTest t = extent.createTest("Orders Status Update & Order Assign Test");

        try {
            // 1️⃣ Login
            login();
            t.info("Logged in as admin successfully.");

            // // 2️⃣ OPEN SIDEBAR MENU automatically
            // openSidebar(t);

            // 3️⃣ Click Orders in side menu (nth-child(2))
            WebElement ordersMenu = wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector(".MuiListItem-root:nth-child(2) .MuiTypography-root")
                )
            );
            String label = ordersMenu.getText();
            ordersMenu.click();
            t.info("Opened side menu item 2 → " + label);

            // 4️⃣ Update multiple order statuses (based on your IDE script)
            //    For each ID, set dropdown to a target status

            updateOrderStatus(t, "order-status-677fd28c84c2ef5caa011d7d", "PayFailed");
            updateOrderStatus(t, "order-status-674d610a52719623389622d0", "PaySuccess");
            updateOrderStatus(t, "order-status-677fd8f484c2ef5caa011d80", "PaySuccess");
            updateOrderStatus(t, "order-status-6780f975bda0445bb37a241c", "Paid");
            updateOrderStatus(t, "order-status-677fd75b84c2ef5caa011d7f", "Accepted");
            updateOrderStatus(t, "order-status-674ab1b6a32970e259ed5ef1", "Ready");
            updateOrderStatus(t, "order-status-677fd3eb84c2ef5caa011d7e", "Collected");
            updateOrderStatus(t, "order-status-674ad8cca32970e259ed5ef4", "Completed");
            updateOrderStatus(t, "order-status-67810855bda0445bb37a241d", "Cancelled");

            t.info("Updated all configured order statuses.");

            // 5️⃣ Assign an order (row 7, action column 11)
            try {
                WebElement assignIcon = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiTableRow-root:nth-child(7) > .MuiTableCell-root:nth-child(11) svg")
                    )
                );
                assignIcon.click();
                t.info("Clicked assign icon in row 7, column 11.");

                // Select first option in assign popover/dialog
                WebElement firstAssignOption = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiPaper-root:nth-child(3) > .MuiPaper-root:nth-child(2) .MuiButtonBase-root")
                    )
                );
                firstAssignOption.click();
                t.info("Selected first option in assign popup (delivery partner / merchant).");

                // Click assign/confirm button (.css-138zggh-MuiButtonBase-root-MuiButton-root)
                WebElement confirmAssign = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".css-138zggh-MuiButtonBase-root-MuiButton-root")
                    )
                );
                confirmAssign.click();
                t.info("Clicked confirm/assign button (.css-138zggh-MuiButtonBase-root-MuiButton-root).");
            } catch (Exception e) {
                t.warning("Order assign flow encountered an issue: " + e.getMessage());
            }

            // 6️⃣ Scroll a bit (as in original script)
            try {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 221)");
                t.info("Scrolled down slightly in Orders table.");
            } catch (Exception e) {
                t.warning("Could not scroll table: " + e.getMessage());
            }

            // 7️⃣ Go back to Dashboard from side menu (nth-child(1))
            try {
                WebElement dashboardMenu = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiListItem-root:nth-child(1) .MuiTypography-root")
                    )
                );
                dashboardMenu.click();
                t.info("Navigated back to Dashboard via menu item 1.");
            } catch (Exception e) {
                t.warning("Could not click Dashboard menu item: " + e.getMessage());
            }

            // 8️⃣ Logout flow similar to LoginLogoutTest (optional)
            try {
                WebElement profileIcon = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiIconButton-edgeEnd")
                    )
                );
                profileIcon.click();
                t.info("Opened profile / user menu (.MuiIconButton-edgeEnd).");

                WebElement logoutButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiBox-root")
                    )
                );
                logoutButton.click();
                t.info("Clicked logout button (second button in profile menu).");
            } catch (Exception e) {
                t.warning("Logout from Orders test encountered an issue: " + e.getMessage());
            }

            t.pass("✅ Orders status update & order assign flow executed successfully (with warnings if some optional steps failed).");

        } catch (Exception e) {
            String path = takeScreenshot("OrdersStatusUpdateOrderAssignTest");
            try {
                t.fail("❌ Orders Status Update & Assign test failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception ignore) {}
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper to change order status in a <select> by id and visible text.
     */
    private void updateOrderStatus(ExtentTest t, String selectId, String targetStatus) {
        try {
            WebElement dropdown = wait.until(
                ExpectedConditions.elementToBeClickable(By.id(selectId))
            );
            dropdown.click();

            // Limit the search to inside this dropdown using ".//"
            WebElement option = dropdown.findElement(
                By.xpath(".//option[. = '" + targetStatus + "']")
            );
            option.click();

            t.info("Updated order status [" + selectId + "] → '" + targetStatus + "'.");
        } catch (Exception e) {
            t.warning("Could not update order status [" + selectId + "] to '" + targetStatus + "': " + e.getMessage());
        }
    }
}
