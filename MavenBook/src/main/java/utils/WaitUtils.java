package utils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import org.openqa.selenium.JavascriptExecutor;
public class WaitUtils {

    private WaitUtils() {
        // prevent instantiation
    }

    public static void waitForUi(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));
    }
}
