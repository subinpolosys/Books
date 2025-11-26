package base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import drivers.DriverFactory;
import utils.ConfigReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseSetup {

    protected WebDriver driver;
    private static final Logger logger = LogManager.getLogger(BaseSetup.class);

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = System.getProperty("browser", ConfigReader.get("browser"));
        driver = DriverFactory.initializeDriver(browser);
        logger.info(browser + " driver initialized successfully.");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
        logger.info("Driver quit successfully.");
    }
    
    public void injectNetworkTracker(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "if (!window.pendingRequests) {" +
            "  window.pendingRequests = 0;" +
            "  (function(open) {" +
            "    XMLHttpRequest.prototype.open = function() { window.pendingRequests++; this.addEventListener('loadend', function() { window.pendingRequests--; }); open.apply(this, arguments); };" +
            "  })(XMLHttpRequest.prototype.open);" +
            "  (function(fetch) {" +
            "    window.fetch = function() { window.pendingRequests++; return fetch.apply(this, arguments).finally(() => window.pendingRequests--); };" +
            "  })(window.fetch);" +
            "}"
        );
       // System.out.println("✅ Injected network tracker for AJAX/fetch monitoring.");
    }
    
    
    
    
}
