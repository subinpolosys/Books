package pages;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

public class Login {

    private WebDriver driver;
    private WebDriverWait wait;
    private boolean recaptchaSolved = false;

    private String loginUrl = ConfigReader.get("url");
    private String username = ConfigReader.get("username");
    private String password = ConfigReader.get("password");

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

    public void login() throws InterruptedException {
        int retries = 0;
        int maxRetries = 3;

        while (!recaptchaSolved && retries < maxRetries) {
            retries++;
            driver.get(loginUrl);
            Thread.sleep(500);
            List<WebElement> captchas = driver.findElements(By.xpath("//iframe[@title='reCAPTCHA']"));
            if (!captchas.isEmpty()) {
                recaptchaSolved = handleCaptcha();
            }
        }

        if (!recaptchaSolved) {
            throw new RuntimeException("CAPTCHA could not be solved after " + maxRetries + " attempts.");
        }

        driver.switchTo().defaultContent();
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        try 
		{
			WebDriverWait localWait = new WebDriverWait(driver, Duration.ofSeconds(15));
			localWait.until(ExpectedConditions.presenceOfElementLocated(verifyRecaptchafield));
			Thread.sleep(200);
			driver.findElement(loginButton).click();
		        //dr.switchTo().frame(iframe);
		}catch(Exception e) 
		{	
		}
        
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(continueButton)).click();
        } catch (Exception ignored) {}

        wait.until(ExpectedConditions.visibilityOfElementLocated(dashboardMenu));
        driver.findElement(dashboardMenu).click();
    }

    private boolean handleCaptcha() {
        try {
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//iframe[@title='reCAPTCHA']")));
            driver.switchTo().frame(iframe);
            WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='recaptcha-checkbox-border']")));
            checkbox.click();
            driver.switchTo().defaultContent();
            return true;
        } catch (Exception e) {
            driver.switchTo().defaultContent();
            return false;
        }
    }

    public void logout() {
        try {
            By profileBtn = By.xpath("//button[@id='headlessui-popover-button-:re:']/div");
            By signOut = By.xpath("//p[contains(text(),'Sign Out')]");
            if (!driver.findElements(profileBtn).isEmpty()) {
                driver.findElement(profileBtn).click();
                driver.findElement(signOut).click();
            }
        } catch (Exception e) {
            System.out.println("Exception during logout: " + e.getMessage());
        }
    }

    private String currentDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
