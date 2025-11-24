package com.zaperr.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class BaseTest {
    public static WebDriver driver;
    public static WebDriverWait wait;
    public static ExtentReports extent;
    public static ExtentTest test;

    @BeforeSuite
    public void setupReport() {
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setReportName("Zaperr Selenium Automation Report");
        spark.config().setDocumentTitle("Zaperr Test Results");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Manikantha");
        extent.setSystemInfo("Environment", "QA");
    }

    @BeforeClass
    public void setUpBrowser() {
        if (driver == null) {
            System.setProperty("webdriver.chrome.driver", "C:\\chromedriver-win64 (1)\\chromedriver-win64\\chromedriver.exe");
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        }
    }

    public void login() throws InterruptedException {
        driver.get("http://192.168.1.31:3000");
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@autocomplete='email']")));
        emailField.clear();
        emailField.sendKeys("admin@gmail.com");

        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='password']")));
        passwordField.clear();
        passwordField.sendKeys("Admin@0426#user");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Login']")));
        loginButton.click();
        Thread.sleep(5000); // wait for dashboard
    }

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

    @AfterSuite
    public void tearDown() {
        if (extent != null)
            extent.flush();
        System.out.println("📄 Combined report generated at: test-output/ExtentReport.html");
        if (driver != null)
            driver.quit();
    }
}
