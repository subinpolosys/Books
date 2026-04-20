package drivers;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import utils.ConfigReader;
public class DriverFactory {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    public static void initializeDriver(String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            tlDriver.set(new ChromeDriver());
        } else if (browser.equalsIgnoreCase("edge")) {
            tlDriver.set(new EdgeDriver());
        } else {
            throw new RuntimeException("Unsupported browser: " + browser);
        }
        getDriver().manage().window().maximize();
    }
    public static WebDriver getDriver() {
        return tlDriver.get();
    }
    public static void quitDriver() {
        if (tlDriver.get() != null) {
            tlDriver.get().quit();
            tlDriver.remove();
        }
    }
}


/*  //-----second
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver initializeDriver(String browser) {

        if (driver.get() == null) {

            if ("chrome".equalsIgnoreCase(browser)) {
                ChromeOptions options = new ChromeOptions();

                if (Boolean.parseBoolean(
                        System.getProperty("headless", "false"))) {
                    options.addArguments("--headless=new");
                    options.addArguments("--window-size=1920,1080");
                    options.addArguments("--disable-gpu");
                }

                driver.set(new ChromeDriver(options));
            }
            // add firefox/edge later if needed
        }
        return driver.get();
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
*/
//--------------------first--------------
//package drivers;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.edge.EdgeOptions;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.firefox.FirefoxOptions;
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//import java.time.Duration;
//import java.util.Collections;
//import java.util.HashMap;
//
//public class DriverFactory {
//
//    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
//    private static final Logger logger = LogManager.getLogger(DriverFactory.class);
//
//    public static WebDriver initializeDriver(String browser) {
//        logger.info("Initializing driver: " + browser);
//        WebDriver dr;
//
//        if (browser.equalsIgnoreCase("chrome")) {
//            WebDriverManager.chromedriver().setup();
//            ChromeOptions options = new ChromeOptions();
//            options.addArguments("--disable-dev-shm-usage", "--no-sandbox", "--disable-extensions",
//                    "--disable-infobars", "--disable-save-password-bubble");
//            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
//            HashMap<String, Object> prefs = new HashMap<>();
//            prefs.put("credentials_enable_service", false);
//            prefs.put("profile.password_manager_enabled", false);
//            options.setExperimentalOption("prefs", prefs);
//            dr = new ChromeDriver(options);
//
//        } else if (browser.equalsIgnoreCase("firefox")) {
//            WebDriverManager.firefoxdriver().setup();
//            FirefoxOptions options = new FirefoxOptions();
//            options.addPreference("dom.webnotifications.enabled", false);
//            options.addPreference("signon.rememberSignons", false);
//            options.addPreference("signon.autofillForms", false);
//            options.addPreference("extensions.passwordmgr.rememberSignons", false);
//            dr = new FirefoxDriver(options);
//
//        }else if (browser.equalsIgnoreCase("edge")) {
//
//        	System.setProperty("webdriver.edge.driver", "C:\\Drivers\\msedgedriver.exe");
//
//            EdgeOptions options = new EdgeOptions();
//
//            // ===== Stability / CI Safe =====
//            options.addArguments("--disable-dev-shm-usage");
//            options.addArguments("--no-sandbox");
//            options.addArguments("--disable-extensions");
//            options.addArguments("--disable-infobars");
//            options.addArguments("--disable-notifications");
//            options.addArguments("--disable-save-password-bubble");
//
//            // ===== Remove automation banner =====
//            options.setExperimentalOption(
//                    "excludeSwitches",
//                    Collections.singletonList("enable-automation")
//            );
//
//            // ===== Disable password manager =====
//            HashMap<String, Object> prefs = new HashMap<>();
//            prefs.put("credentials_enable_service", false);
//            prefs.put("profile.password_manager_enabled", false);
//            options.setExperimentalOption("prefs", prefs);
//
//            dr = new EdgeDriver(options);
//        }
//        else {
//            throw new IllegalArgumentException("Invalid browser name: " + browser);
//        }
//
//        dr.manage().window().maximize();
//        dr.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//        driver.set(dr);
//
//        return getDriver();
//    }
//
//    public static WebDriver getDriver() {
//        return driver.get();
//    }
//
//    public static void quitDriver() {
//        if (driver.get() != null) {
//            driver.get().quit();
//            driver.remove();
//        }
//    }
//}
