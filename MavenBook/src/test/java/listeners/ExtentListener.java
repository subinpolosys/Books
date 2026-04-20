package listeners;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;

import drivers.DriverFactory;
import reports.ExtentManager;
import utils.ScreenshotUtil;

public class ExtentListener implements ITestListener{
	@Override
	public void onTestStart(ITestResult result) {

	    String browser = result.getTestContext()
	                           .getCurrentXmlTest()
	                           .getParameter("browser");

	    long threadId = Thread.currentThread().getId();
	    String threadName = Thread.currentThread().getName();

	    ExtentTest extentTest =
	        ExtentManager.getExtent()
	                     .createTest(result.getMethod().getMethodName()
	                         + " | Browser: " + browser
	                         + " | ThreadId: " + threadId)
	                     .assignCategory(browser)
	                     .assignCategory("Thread-" + threadId);

	    // Log thread details inside report
	    extentTest.info("Thread Name: " + threadName);
	    extentTest.info("Thread ID  : " + threadId);

	    ExtentManager.setTest(extentTest);
	}


	    @Override
	    public void onTestSuccess(ITestResult result) {
	        ExtentManager.getTest().pass("Test passed");
	    }

	    @Override
	    public void onTestFailure(ITestResult result) {

	        WebDriver driver = DriverFactory.getDriver();

	        String testName = result.getMethod().getMethodName();

	        String screenshotPath =
	                ScreenshotUtil.captureScreenshot(driver, testName);

	        ExtentManager.getTest().fail(result.getThrowable());

	        ExtentManager.getTest().addScreenCaptureFromPath(screenshotPath);
	    }


	    @Override
	    public void onFinish(ITestContext context) {
	        ExtentManager.getExtent().flush();
	    }
}
