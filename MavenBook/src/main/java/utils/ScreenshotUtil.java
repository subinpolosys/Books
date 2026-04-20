package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String testName) {

    	String screenshotDir = System.getProperty("user.dir")
                + "/test-output/screenshots/";

        String fileName = testName + "_"
                + Thread.currentThread().getId() + ".png";

        String absolutePath = screenshotDir + fileName;

        try {
            Files.createDirectories(Paths.get(screenshotDir));

            File src = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.FILE);

            Files.copy(src.toPath(), Paths.get(absolutePath),
                    StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 🔥 RETURN RELATIVE PATH (this is the key)
        return "screenshots/" + fileName;
    }
}
