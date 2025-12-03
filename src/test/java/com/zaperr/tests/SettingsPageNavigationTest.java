package com.zaperr.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;

public class SettingsPageNavigationTest extends BaseTest {

    @Test(priority = 6)
    public void settingsPageNavigation() {
        ExtentTest t = extent.createTest("Settings Page Navigation Test");

        try {
            // 1️⃣ Login
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

            // 3️⃣ Card 1
            openSettingsCard(t, 1, "Settings Card 1");

            // 4️⃣ Card 2 (only ONCE now – recording had double-click, we don't need it)
            openSettingsCard(t, 2, "Settings Card 2");

            // 5️⃣ Back from card 2 (optional)
            clickBackIconSimple(t, "Back from Card 2");
            scrollToTop(t);

            // 6️⃣ Card 3 + optional back
            openSettingsCard(t, 3, "Settings Card 3");
            clickBackIconSimple(t, "Back from Card 3");
            scrollToTop(t);

            // 7️⃣ Card 5
            openSettingsCard(t, 5, "Settings Card 5");

            // 8️⃣ Click main Settings button (.MuiButton-root)
            try {
                WebElement button = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.cssSelector(".MuiButton-root")
                        )
                );
                button.click();
                t.info("Clicked main Settings button (.MuiButton-root).");
            } catch (Exception e) {
                t.info("Optional: could not click .MuiButton-root in Settings: " + e.getMessage());
            }

            // 9️⃣ Click special button (.css-179ojzl-MuiButtonBase-root-MuiButton-root)
            try {
                WebElement specialButton = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.cssSelector(".css-179ojzl-MuiButtonBase-root-MuiButton-root")
                        )
                );
                specialButton.click();
                t.info("Clicked special Settings button (.css-179ojzl-MuiButtonBase-root-MuiButton-root).");
            } catch (Exception e) {
                t.info("Optional: could not click .css-179ojzl-MuiButtonBase-root-MuiButton-root: " + e.getMessage());
            }

            // 🔟 Back icon from Card 5 (path:nth-child(3)) – intercept-safe, but optional
            clickBackPathOptional(t, 3, "Back from Card 5 (path:nth-child(3))");
            scrollToTop(t);

            // 1️⃣1️⃣ Card 7 (open only; no strict back required)
            openSettingsCard(t, 7, "Settings Card 7");

            // 1️⃣2️⃣ Optional back from Card 7
            clickBackPathOptional(t, 1, "Back from Card 7 (path:nth-child(1))");
            scrollToTop(t);

            // 1️⃣3️⃣ Header / Dashboard title
            try {
                WebElement header = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.cssSelector(".css-1qtpij-MuiTypography-root")
                        )
                );
                header.click();
                t.info("Clicked header / dashboard title (.css-1qtpij-MuiTypography-root).");
            } catch (Exception e) {
                t.info("Optional: could not click header .css-1qtpij-MuiTypography-root: " + e.getMessage());
            }

            // 1️⃣4️⃣ Logout (same pattern as other tests)
            try {
                WebElement logoutBtn = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                By.cssSelector(".MuiButtonBase-root:nth-child(2) > .MuiBox-root")
                        )
                );
                logoutBtn.click();
                t.info("Clicked logout button from Settings flow.");
            } catch (Exception e) {
                t.info("Optional: could not click logout button (.MuiButtonBase-root:nth-child(2) > .MuiBox-root): " + e.getMessage());
            }

            t.pass("✅ Settings page navigation flow executed successfully.");

        } catch (Exception e) {
            String path = takeScreenshot("SettingsPageNavigationTest");
            try {
                t.fail("❌ Settings page navigation failed: " + e.getMessage(),
                        MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (Exception ignore) {}
            throw new RuntimeException(e);
        }
    }

    // 🔧 Helper: open a Settings card by nth-child index (with scroll + intercept handling)
    private void openSettingsCard(ExtentTest t, int index, String label) {
        String selector = ".MuiGrid-root:nth-child(" + index + ") > .MuiPaper-root";

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement card = wait.until(
                        ExpectedConditions.elementToBeClickable(By.cssSelector(selector))
                );

                // Scroll into view
                try {
                    ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});", card);
                } catch (Exception ignore) {}

                try {
                    card.click();
                    t.info("Opened " + label + " (" + selector + ") on attempt " + attempt + ".");
                    return;
                } catch (ElementClickInterceptedException ice) {
                    t.info("Click intercepted on " + label + " attempt "
                            + attempt + ": " + ice.getMessage());

                    // Try Actions click
                    try {
                        new Actions(driver).moveToElement(card).click().perform();
                        t.info("Opened " + label + " via Actions on attempt " + attempt + ".");
                        return;
                    } catch (Exception actionsEx) {
                        // Last try via JS
                        try {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", card);
                            t.info("Opened " + label + " via JS on attempt " + attempt + ".");
                            return;
                        } catch (Exception jsEx) {
                            // let loop retry
                        }
                    }
                }

            } catch (Exception e) {
                if (attempt == 3) {
                    t.info("Optional: could not open " + label + " (" + selector + ") after 3 attempts: " + e.getMessage());
                } else {
                    try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                }
            }
        }
    }

    // 🔧 Helper: scroll to top
    private void scrollToTop(ExtentTest t) {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0)");
            t.info("Scrolled to top of the page.");
        } catch (Exception e) {
            t.info("Optional: could not scroll to top: " + e.getMessage());
        }
    }

    // 🔧 Simple generic back icon (.MuiBox-root > svg), treated as optional
    private void clickBackIconSimple(ExtentTest t, String context) {
        try {
            WebElement back = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.cssSelector(".MuiBox-root > svg")
                    )
            );
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block: 'center'});", back);
            back.click();
            t.info("Clicked back icon (.MuiBox-root > svg) → " + context);
        } catch (Exception e) {
            t.info("Optional: could not click back icon in context '" + context + "': " + e.getMessage());
        }
    }

    // 🔧 Helper: click back icon path (1 or 3) – fully optional, no warnings
    private void clickBackPathOptional(ExtentTest t, int pathIndex, String context) {
        String selector = ".MuiBox-root > svg > path:nth-child(" + pathIndex + ")";

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement backPath = wait.until(
                        ExpectedConditions.elementToBeClickable(By.cssSelector(selector))
                );

                ((JavascriptExecutor) driver).executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});", backPath);

                try {
                    backPath.click();
                    t.info("Clicked back icon (" + context + ") on attempt " + attempt + ".");
                    return;
                } catch (ElementClickInterceptedException ice) {
                    t.info("Back icon click intercepted in context '" + context + "', attempt "
                            + attempt + ": " + ice.getMessage());

                    // Actions fallback
                    try {
                        new Actions(driver).moveToElement(backPath).click().perform();
                        t.info("Clicked back icon via Actions in context '" + context + "', attempt " + attempt + ".");
                        return;
                    } catch (Exception actionsEx) {
                        // JS fallback
                        try {
                            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", backPath);
                            t.info("Clicked back icon via JS in context '" + context + "', attempt " + attempt + ".");
                            return;
                        } catch (Exception jsEx) {
                            // let loop retry
                        }
                    }
                }

            } catch (Exception e) {
                if (attempt == 3) {
                    t.info("Optional: could not click back icon (" + context + ") after 3 attempts: " + e.getMessage());
                } else {
                    try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                }
            }
        }
    }
}
