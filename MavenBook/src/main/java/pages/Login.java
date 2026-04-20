package pages;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import drivers.DriverFactory;
import utils.ConfigReader;
import utils.Utilities;
public class Login {
    private WebDriver driver;
    private WebDriverWait wait;
    private boolean recaptchaSolved = false;
    private String loginUrl = ConfigReader.get("url");
    private String username = ConfigReader.get("username");
    private String password = ConfigReader.get("password");
    private By logininLoginPagefield=By.xpath("//h5[text()='Login']");
    private By logininLandingPagefield=By.xpath("//a[@href='/login']/p[text()='Login']");
    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[contains(text(),'Login')]");
    private By continueButton = By.xpath("//button[contains(text(),'Continue')]");
    private By dashboardMenu = By.xpath("//div[@title='Dashboard']/a[contains(text(),'Dashboard')]");
    private By verifyRecaptchafield=By.xpath("//div[contains(text(),'Please verify reCAPTCHA')]");
    public Login(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }
    public void login() {
        //long start = System.currentTimeMillis();

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(6));
        int attempts = 0;

        while (attempts < 2) {           
            attempts++;

            Utilities.openUrlSafely(driver, loginUrl);

            try {
                // detect login page
                shortWait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));

                driver.findElement(usernameField).clear();
                driver.findElement(usernameField).sendKeys(username);

                driver.findElement(passwordField).clear();
                driver.findElement(passwordField).sendKeys(password);

                driver.findElement(loginButton).click();

                // optional continue
                try {
                    shortWait.until(ExpectedConditions.elementToBeClickable(continueButton)).click();
                } catch (Exception ignored) {}

                // wait for dashboard
                shortWait.until(ExpectedConditions.visibilityOfElementLocated(dashboardMenu));

                //System.out.println("Login success in attempt " + attempts);
                long end = System.currentTimeMillis();
               // System.out.println("Login time taken = " + (end - start) + " ms");
                return;   
            }
            catch (Exception e) {
                System.out.println("Login attempt failed: " + attempts);
            }
        }

        throw new RuntimeException("Login failed after " + attempts + " attempts");
    }

    public void logout() {
        try {
            WebDriver driver = DriverFactory.getDriver();

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            By profileBtn = By.xpath("//img[@alt='profile']");
            By signOut = By.xpath("//p[contains(text(),'Sign Out')]");
            By toast = By.xpath("//div[contains(@class,'Toastify__toast')]");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(toast));
            wait.until(ExpectedConditions.elementToBeClickable(profileBtn)).click();
            wait.until(ExpectedConditions.elementToBeClickable(signOut)).click();
        } catch (Exception e) {
            System.out.println("⚠️ Exception during logout: " + e.getMessage());
        }
    }

}
