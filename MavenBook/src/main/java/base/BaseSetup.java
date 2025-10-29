package base;
import utils.ConfigReader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import drivers.DriverFactory;

import java.time.Duration;

public class BaseSetup {

	protected WebDriver driver;
    protected WebDriverWait wait;

    @Parameters("browser")
    @BeforeMethod(alwaysRun = true)
    public void setup(@Optional("chrome") String browser) {
        driver = DriverFactory.initializeDriver(browser);
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        driver.get(ConfigReader.get("url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
	
	
	
}
