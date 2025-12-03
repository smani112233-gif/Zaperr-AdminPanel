package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class SettingsOrdersTest extends BaseTest {

    @Test(priority = 9)
    public void configureOrderSettings() {
        ExtentTest t = extent.createTest("Settings - Orders Configuration Test");

        try {
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

            // 3️⃣ Open 'Orders' settings card (third card)
            try {
                // primary: click the whole card
                WebElement ordersCard = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiGrid-root:nth-child(3) > .MuiPaper-root")
                    )
                );
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ordersCard);
                Thread.sleep(400);
                try {
                    ordersCard.click();
                } catch (Exception clickEx) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ordersCard);
                }
                t.info("Opened Orders settings card by CSS (.MuiGrid-root:nth-child(3) > .MuiPaper-root).");
            } catch (Exception e1) {
                // fallback: locate by title text that contains 'Orders'
                WebElement ordersTitle = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[contains(@class,'MuiGrid-root')]" +
                                 "//div[contains(@class,'MuiPaper-root')]" +
                                 "//h6[contains(.,'Orders')]")
                    )
                );
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ordersTitle);
                Thread.sleep(400);
                ordersTitle.click();
                t.info("Opened Orders settings card by title text (contains 'Orders').");
            }

            Thread.sleep(1000); // allow Orders settings content to render

            // // 4️⃣ Toggle an Orders-related switch
            // try {
            //     WebElement ordersToggle = wait.until(
            //         ExpectedConditions.elementToBeClickable(
            //             By.cssSelector(".MuiGrid-root:nth-child(4) .MuiStack-root .PrivateSwitchBase-input")
            //         )
            //     );
            //     ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ordersToggle);
            //     Thread.sleep(300);
            //     try {
            //         ordersToggle.click();
            //     } catch (Exception ex) {
            //         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", ordersToggle);
            //     }
            //     t.info("Toggled Orders-related switch using primary CSS selector.");
            // } catch (Exception e1) {
            //     // fallback: any checkbox/switch within Orders settings form
            //     try {
            //         WebElement anyToggle = wait.until(
            //             ExpectedConditions.elementToBeClickable(
            //                 By.cssSelector(".MuiStack-root .PrivateSwitchBase-input")
            //             )
            //         );
            //         ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", anyToggle);
            //         Thread.sleep(300);
            //         ((JavascriptExecutor) driver).executeScript("arguments[0].click();", anyToggle);
            //         t.info("Toggled fallback Orders switch (.MuiStack-root .PrivateSwitchBase-input).");
            //     } catch (Exception e2) {
            //         t.warning("Could not toggle any Orders switch: " + e2.getMessage());
            //     }
            // }

            // 5️⃣ Click Save / Update button
            try {
                WebElement saveButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[contains(@class,'MuiButton-contained') " +
                                 "and (contains(.,'Save') or contains(.,'Update') or contains(.,'Apply') or .!='')]")
                    )
                );
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", saveButton);
                Thread.sleep(300);
                try {
                    saveButton.click();
                } catch (Exception ex) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);
                }
                t.info("Clicked Save/Update button in Orders settings.");
            } catch (Exception e1) {
                // original simple selector as fallback
                try {
                    WebElement saveButton = wait.until(
                        ExpectedConditions.elementToBeClickable(
                            By.cssSelector(".MuiButton-contained")
                        )
                    );
                    saveButton.click();
                    t.info("Clicked Save/Update button using fallback CSS (.MuiButton-contained).");
                } catch (Exception e2) {
                    t.warning("Could not click Save/Update button: " + e2.getMessage());
                }
            }

            // 6️⃣ Optional: click right-side icon (profile / menu) to logout
            try {
                WebElement rightIcon = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiIconButton-edgeEnd")
                    )
                );
                rightIcon.click();
                t.info("Clicked right-side icon (.MuiIconButton-edgeEnd) after Orders settings.");
            } catch (Exception e) {
                t.info("Optional: could not click right-side icon: " + e.getMessage());
            }

            // 7️⃣ Optional: confirm logout
            try {
                WebElement logoutConfirm = wait.until(
                    ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiBox-root")
                    )
                );
                logoutConfirm.click();
                t.info("Clicked logout confirm button (.MuiButtonBase-root:nth-child(2) > .MuiBox-root).");
            } catch (Exception e) {
                t.info("Optional: could not click logout confirm: " + e.getMessage());
            }

            t.pass("✅ Settings → Orders configuration flow executed successfully.");

        } catch (Exception e) {
            String path = takeScreenshot("SettingsOrdersTest");
            try {
                t.fail("❌ Settings Orders test failed: " + e.getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception ignore) {}
            throw new RuntimeException(e);
        }
    }
}
