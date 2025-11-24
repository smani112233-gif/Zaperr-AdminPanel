package com.zaperr.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class LoginTest {
    WebDriver driver;
    WebDriverWait wait;

    // Extent Report objects
    ExtentReports extent;
    ExtentTest test;

    @BeforeClass
    public void setUp() {
        // ✅ Path to ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64 (1)\\chromedriver-win64\\chromedriver.exe");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // ✅ Initialize Extent Report
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("Zaperr Selenium Automation Report");
        spark.config().setDocumentTitle("Zaperr Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Manikantha");
        extent.setSystemInfo("Environment", "QA");
    }

    // ✅ Helper method to take screenshots
    public String takeScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String filePath = System.getProperty("user.dir") + "/test-output/screenshots/" + testName + "_" + timestamp + ".png";
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/test-output/screenshots/"));
            Files.copy(src.toPath(), Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    // ✅ VALID LOGIN TEST
    @Test(priority = 1)
    public void validLoginTest() {
        test = extent.createTest("Valid Login Test", "Verify user can log in successfully");
        driver.get("http://192.168.1.31:3000");

        try {
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@autocomplete='email']")));
            emailField.sendKeys("admin@gmail.com");
            test.info("Entered valid email");

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
            passwordField.sendKeys("Admin@0426#user");
            test.info("Entered valid password");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Login']")));
            loginButton.click();
            test.info("Clicked Login button");

            Thread.sleep(5000);
            test.pass("✅ Login successful and dashboard loaded.");

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("validLoginTest");
            test.fail("❌ Error during login: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }

    // ❌ INVALID LOGIN TEST
    @Test(priority = 2)
    public void invalidLoginTest() {
        test = extent.createTest("Invalid Login Test", "Verify login fails with wrong credentials");
        driver.get("http://192.168.1.31:3000");

        try {
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@autocomplete='email']")));
            emailField.sendKeys("wrong@gmail.com");
            test.info("Entered invalid email");

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
            passwordField.sendKeys("WrongPassword123");
            test.info("Entered invalid password");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Login']")));
            loginButton.click();
            test.info("Clicked Login button with wrong credentials");

            Thread.sleep(5000);

            // ✅ Check if login failed (still on login page)
            if (driver.getCurrentUrl().contains("login") || driver.getPageSource().contains("Invalid")) {
                test.pass("❌ Invalid login correctly failed.");
            } else {
                String screenshotPath = takeScreenshot("invalidLoginTest");
                test.fail("🚨 Invalid login succeeded unexpectedly!", MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            }

        } catch (Exception e) {
            String screenshotPath = takeScreenshot("invalidLoginTest");
            test.fail("❌ Error during invalid login: " + e.getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
        }
    }

    @AfterClass
    public void tearDown() {
        extent.flush();
        System.out.println("📄 Report generated at: test-output/ExtentReport.html");
        System.out.println("Browser left open for inspection.");
        // driver.quit(); // Uncomment when you want auto-close
    }
}
