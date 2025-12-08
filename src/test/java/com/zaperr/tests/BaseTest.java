package com.zaperr.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected static ExtentReports extent;           // single report for suite

    // ---------- CONFIG ----------
    private final String CHROME_DRIVER_PATH = "C:\\chromedriver-win64 (1)\\chromedriver-win64\\chromedriver.exe";
    private final Duration DEFAULT_WAIT = Duration.ofSeconds(20);
    private final String BASE_URL = "https://9cr34k91-3000.inc1.devtunnels.ms/"; // keep your url

    // ---------- Suite-level report ----------
    @BeforeSuite(alwaysRun = true)
    public void setupReport() {
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("Zaperr Selenium Automation Report");
        spark.config().setDocumentTitle("Zaperr Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Manikantha");
        extent.setSystemInfo("Environment", "QA");

        System.out.println("➡ Extent report initialised: " + reportPath);
    }

    // ---------- Browser-per-test (reduces flaky state) ----------
    @BeforeMethod(alwaysRun = true)
    public void setUpBrowser() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, DEFAULT_WAIT);
    }

    // ---------- Tear down browser after each test ----------
    @AfterMethod(alwaysRun = true)
    public void quitBrowser() {
        try {
            if (driver != null) driver.quit();
        } catch (Exception ignore) {}
    }

    // ---------- Finalize report ----------
    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        if (extent != null) extent.flush();
        System.out.println("📄 Combined report generated at: test-output/ExtentReport.html");
    }

    // ---------- Login helper (use in each test as first step) ----------
    public void login() {
        driver.get(BASE_URL);
        // wait for email
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@autocomplete='email']")));
        WebElement emailField = driver.findElement(By.xpath("//input[@autocomplete='email']"));
        safeClearSend(emailField, "admin@gmail.com");

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
        safeClearSend(passwordField, "Admin@0426#user");

        // click login button reliably
        By loginBtnLocator = By.xpath("//button[normalize-space()='Login']");
        safeClick(loginBtnLocator, null, "Login button");

        // wait for dashboard load — use a stable dashboard indicator (adjust if needed)
        try {
            wait.until(ExpectedConditions.urlContains("/dashboard"));
        } catch (Exception e) {
            // fallback: wait for dashboard header presence
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".css-1qtpij-MuiTypography-root")));
            } catch (Exception ignore) {}
        }
    }

    // ---------- Screenshot helper ----------
    public String takeScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String dir = System.getProperty("user.dir") + "/test-output/screenshots/";
        String filePath = dir + testName + "_" + timestamp + ".png";
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.createDirectories(Path.of(dir));
            Files.copy(src.toPath(), Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    // ---------- Wait for common blocking overlays (toast/backdrop) ----------
    protected void waitForNoBlockingOverlay() {
        try {
            // common MUI backdrop class; add additional selectors if your app uses other overlays
            By[] overlays = new By[] {
                By.cssSelector(".MuiBackdrop-root"),
                By.cssSelector(".Toastify__toast-container"),
                By.cssSelector(".css-183mqmm"), // you referenced this earlier — keep as backup
            };

            for (By overlay : overlays) {
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(overlay));
                } catch (Exception ignore) {}
            }

            // small pause for CSS animations to finish
            Thread.sleep(200);
        } catch (InterruptedException ignore) {}
    }

    // ---------- Safe click with retries and JS fallback ----------
    /**
     * safeClick - tries to click an element reliably.
     * @param locator element locator
     * @param t ExtentTest (optional) to log into report; can be null
     * @param friendlyName text used in logs
     */
    protected void safeClick(By locator, ExtentTest t, String friendlyName) {
        String name = (friendlyName == null ? locator.toString() : friendlyName);
        try {
            waitForNoBlockingOverlay();

            WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));
            try {
                el.click();
                if (t != null) t.info("Clicked: " + name);
                else System.out.println("Clicked: " + name);
                return;
            } catch (ElementClickInterceptedException intercepted) {
                // try scroll + JS click
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
                    Thread.sleep(150);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    if (t != null) t.info("Clicked via JS after intercept: " + name);
                    else System.out.println("Clicked via JS after intercept: " + name);
                    return;
                } catch (Exception jsEx) {
                    // fallthrough to retry attempts
                }
            } catch (StaleElementReferenceException sere) {
                // fallthrough to retry attempts
            }

            // retry loop: re-find and try again
            int attempts = 3;
            for (int i = 0; i < attempts; i++) {
                try {
                    WebElement e2 = wait.until(ExpectedConditions.elementToBeClickable(locator));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e2);
                    Thread.sleep(120);
                    e2.click();
                    if (t != null) t.info("Clicked on retry " + (i+1) + ": " + name);
                    else System.out.println("Clicked on retry " + (i+1) + ": " + name);
                    return;
                } catch (ElementClickInterceptedException | StaleElementReferenceException ex) {
                    try {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", wait.until(ExpectedConditions.presenceOfElementLocated(locator)));
                        if (t != null) t.info("Clicked via JS on retry " + (i+1) + ": " + name);
                        else System.out.println("Clicked via JS on retry " + (i+1) + ": " + name);
                        return;
                    } catch (Exception ignored) {}
                } catch (Exception ignored) {}
            }

            // final fallback: JS click on presence
            try {
                WebElement present = driver.findElement(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", present);
                if (t != null) t.info("Final JS click fallback: " + name);
                else System.out.println("Final JS click fallback: " + name);
                return;
            } catch (Exception finalEx) {
                if (t != null) t.warning("Could not click: " + name + " — " + finalEx.getMessage());
                else System.out.println("Could not click: " + name + " — " + finalEx.getMessage());
                throw new RuntimeException(finalEx);
            }
        } catch (Exception e) {
            if (t != null) t.warning("safeClick error for " + name + ": " + e.getMessage());
            else System.out.println("safeClick error for " + name + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // JS-only safe click (use when click needs to ignore layering)
    protected void safeClickJS(By locator, ExtentTest t, String friendlyName) {
        try {
            WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", el);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
            if (t != null) t.info("safeClickJS: " + (friendlyName == null ? locator.toString() : friendlyName));
        } catch (Exception e) {
            if (t != null) t.warning("safeClickJS failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // ---------- Small helpers ----------
    protected void safeClearSend(WebElement el, String text) {
        try {
            waitForNoBlockingOverlay();
            el.clear();
            el.sendKeys(text);
        } catch (Exception e) {
            // try JS set value as fallback
            ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", el, text);
        }
    }

    protected WebElement safeFind(By locator) {
        waitForNoBlockingOverlay();
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
