package pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginTPage {


    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginTPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

 // ──────────────── Navigation Elements ────────────────
    private final By usernameField = By.xpath("//a[@class='text-sm']");
    
    
    
    
    public boolean verifyuser(String usn) {
    	System.out.println(usn);
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        String actualusn = driver.findElement(usernameField).getText();
        System.err.println(actualusn);
        return actualusn.contains(usn);
    }

}
