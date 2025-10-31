package base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import drivers.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseSetup {

    protected WebDriver driver;
    private static final Logger logger = LogManager.getLogger(BaseSetup.class);

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        String browser = System.getProperty("browser", "chrome");
        driver = DriverFactory.initializeDriver(browser);
        logger.info(browser + " driver initialized successfully.");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
        logger.info("Driver quit successfully.");
    }
}
