package drivers;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.github.bonigarcia.wdm.WebDriverManager;


public class DriverFactory {
	
	 private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	 public static WebDriver initializeDriver(String browser) {
	        PropertyConfigurator.configure("src/main/resources/log4j.properties");

	        WebDriver dr = null;

	        if (browser.equalsIgnoreCase("chrome")) {
	            WebDriverManager.chromedriver().setup();

	            ChromeOptions options = new ChromeOptions();
	            options.addArguments("--disable-dev-shm-usage", "--no-sandbox", "--disable-extensions");
	            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
	            options.setCapability("webSocketUrl", false);
	            options.setCapability("se:cdpEnabled", false);
	            options.addArguments("--disable-save-password-bubble", "--disable-infobars");

	            HashMap<String, Object> prefs = new HashMap<>();
	            prefs.put("credentials_enable_service", false);
	            prefs.put("profile.password_manager_enabled", false);
	            options.setExperimentalOption("prefs", prefs);

	            dr = new ChromeDriver(options);
	        } 
	        else if (browser.equalsIgnoreCase("firefox")) {
	            WebDriverManager.firefoxdriver().setup();

	            FirefoxOptions options = new FirefoxOptions();
	            options.addPreference("dom.webnotifications.enabled", false);
	            options.addPreference("signon.rememberSignons", false);
	            options.addPreference("signon.autofillForms", false);
	            options.addPreference("extensions.passwordmgr.rememberSignons", false);
	            options.setCapability("marionette", true);
	            dr = new FirefoxDriver(options);
	        } 
	        else {
	            throw new IllegalArgumentException("Invalid browser name: " + browser);
	        }

	        dr.manage().window().maximize();
	        dr.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

	        driver.set(dr);
	        return getDriver();
	    }

	    public static WebDriver getDriver() {
	        return driver.get();
	    }

	    public static void quitDriver() {
	        if (driver.get() != null) {
	            driver.get().quit();
	            driver.remove();
	        }
	    }
	
	

}
