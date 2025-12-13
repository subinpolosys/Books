package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utilities {
	
	public static void selectIfListed(WebDriver driver, By searchField, By listField, String value) {

	    if (value == null || value.trim().isEmpty()) {
	        System.out.println("⚠️ Skipped selection: value is null or empty");
	        return;
	    }

	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));

	    try {
	        // Step 1: Type in the search field (opens dropdown)
	        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(searchField));
	        inputField.clear();
	        inputField.sendKeys(value);

	        // Locator for all li options
	        By optionsLocator = By.xpath("//ul[contains(@id,'headlessui-combobox-options')]//li");

	        // Step 2: Wait until dropdown list is visible
	        wait.until(ExpectedConditions.visibilityOfElementLocated(optionsLocator));

	        // Step 3: Select element by text using a fresh locator (no stale element ever)
	        By optionByExactText = By.xpath(
	                "//ul[contains(@id,'headlessui-combobox-options')]//li[normalize-space()='" + value + "']"
	        );

	        WebElement option = wait.until(
	                ExpectedConditions.refreshed(
	                        ExpectedConditions.elementToBeClickable(optionByExactText)
	                )
	        );

	        option.click();
	        //System.out.println("✅ Selected: " + value);

	    } catch (TimeoutException te) {
	        System.out.println("❌ Timeout: '" + value + "' not found in dropdown!");
	    } catch (Exception e) {
	        System.out.println("❌ Error selecting '" + value + "'");
	        e.printStackTrace();
	    }
	}

	
//	public static void selectIfListed(WebDriver driver, By searchField, By listField, String value) 
//		{
//		    if (value != null && !value.trim().isEmpty()) {
//		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//
//		        // Step 1: Click the input to open the dropdown
//		        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(searchField));
//		        //inputField.click();
//		        inputField.clear();
//		        inputField.sendKeys(value);
//
//		        try {
//		            // Step 2: Wait until the dropdown options are visible
//		            List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
//		                By.xpath("//ul[contains(@id,'headlessui-combobox-options')]//li")
//		            ));
//
//		            boolean found = false;
//		            for (WebElement option : options) {
//		                String optionText = option.getText().trim();
//		               // System.out.println("Found option: " + optionText);
//
//		                if (optionText.equalsIgnoreCase(value)) {
//		                    option.click();
//		                   // System.out.println("✅ Selected option: " + optionText);
//		                    found = true;
//		                    break;
//		                }
//		            }
//
//		            if (!found) {
//		                System.out.println("⚠️ Value not found in dropdown: " + value);
//		            }
//
//		        } catch (Exception e) {
//		            System.out.println("❌ " + value + ": Not Found OR Dropdown not loaded properly.");
//		            e.printStackTrace();
//		        }
//		    } else {
//		        System.out.println("⚠️ Skipped selection: value is null or empty");
//		    }
//		}
/*____________________________OLD____________________*/
	public static void selectValuFromDropdown(WebDriver driver, By searchField, By listField, String value) {
		String datanotfoundField="//ul/div[1]";
	    if (value != null && !value.trim().isEmpty()) {
	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	        wait.until(ExpectedConditions.visibilityOfElementLocated(searchField)).clear();
	        driver.findElement(searchField).sendKeys(value);
	        try {
	            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
	            List<WebElement> options = shortWait
	                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(listField));
                   // System.out.println(options);
	            if (!options.isEmpty()) {
	                options.get(0).click();
	                System.out.println("Dropdown :"+options.get(0)); 
	            } else {
	                System.out.println("Value not found in dropdown: " + value);
	            }    
	        } catch (Exception e) {
	            System.out.println(value +": Not Found OR Dropdown not loaded or failed ");
	        }
	    } else {
	        System.out.println("Skipped selection: value is null or empty");
	    }
	}
/*________________________________________________*/
	
	

	public static void waitForPageToLoad(WebDriver driver) {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

	    // Wait for document.readyState == 'complete'
	    ExpectedCondition<Boolean> jsLoad = d ->
	            ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete");

	    // Wait for jQuery AJAX requests to finish (if jQuery is used)
	    ExpectedCondition<Boolean> jQueryLoad = d -> {
	        try {
	            return (Long) ((JavascriptExecutor) d)
	                    .executeScript("return jQuery.active") == 0;
	        } catch (Exception e) {
	            return true; // jQuery not present
	        }
	    };

	    // Wait for fetch or XHR network calls to finish (React/Angular)
	    ExpectedCondition<Boolean> fetchLoad = d -> {
	        try {
	            Object result = ((JavascriptExecutor) d).executeScript(
	                    "return (window.pendingRequests || 0) === 0;"
	            );
	            return Boolean.TRUE.equals(result);
	        } catch (Exception e) {
	            return true;
	        }
	    };

	    wait.until(jsLoad);
	    wait.until(jQueryLoad);
	    wait.until(fetchLoad);

	    //System.out.println("✅ Page and all async data fully loaded.");
	}

//===========================Wait for page load ==========================================================
	public static void waitForPageLoad(WebDriver driver) {
	    new WebDriverWait(driver, Duration.ofSeconds(200)).until(
	        webDriver -> ((JavascriptExecutor) webDriver)
	                .executeScript("return document.readyState").equals("complete"));
	}
	
//==============================Wait for JS============================================================
	
	public static void waitForJS(WebDriver driver) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    wait.until(d -> (Boolean) ((JavascriptExecutor) d)
	            .executeScript("return window.jQuery != null && jQuery.active == 0"));
	}
	
//============================Wait to DOM stable =================================================	
	
	public static void waitForDomStable(WebDriver driver) {
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    for (int i = 0; i < 10; i++) {
	        Long count1 = (Long) js.executeScript("return document.getElementsByTagName('*').length");
	        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
	        Long count2 = (Long) js.executeScript("return document.getElementsByTagName('*').length");

	        if (count1.equals(count2)) {
	            return; // DOM has stopped changing
	        }
	    }
	}
	
//=====================Wait to stable the Elements========================================	
	
	public static void waitUntilElementStable(WebDriver driver, By locator) {
	    JavascriptExecutor js = (JavascriptExecutor) driver;

	    WebElement element = driver.findElement(locator);

	    for (int i = 0; i < 10; i++) {
	        Point loc1 = element.getLocation();
	        Dimension size1 = element.getSize();

	        try { Thread.sleep(300); } catch (Exception ignored) {}

	        element = driver.findElement(locator); // re-fetch element
	        Point loc2 = element.getLocation();
	        Dimension size2 = element.getSize();

	        if (loc1.equals(loc2) && size1.equals(size2)) {
	            return; // element is stable
	        }
	    }
	}
//===========================================================================================	
	
	public static void injectNetworkTracker(WebDriver driver) {
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
	
//-----------------------------------
	public static String dateTime() {
		String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		//System.out.println(dateTime);
		return dateTime;

	}
	
//============ Validation message=============
	public static String getValidationMessage(WebDriver driver, WebElement element) {
	    JavascriptExecutor js = (JavascriptExecutor) driver;
	    return (String) js.executeScript("return arguments[0].validationMessage;", element);
	}
	
	

}
