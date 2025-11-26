package utils;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utilities {
	
	
	
	public static void selectIfListed(WebDriver driver, By searchField, By listField, String value) 
		{
		    if (value != null && !value.trim().isEmpty()) {
		        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		        // Step 1: Click the input to open the dropdown
		        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(searchField));
		        //inputField.click();
		        inputField.clear();
		        inputField.sendKeys(value);

		        try {
		            // Step 2: Wait until the dropdown options are visible
		            List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
		                By.xpath("//ul[contains(@id,'headlessui-combobox-options')]//li")
		            ));

		            boolean found = false;
		            for (WebElement option : options) {
		                String optionText = option.getText().trim();
		               // System.out.println("Found option: " + optionText);

		                if (optionText.equalsIgnoreCase(value)) {
		                    option.click();
		                   // System.out.println("✅ Selected option: " + optionText);
		                    found = true;
		                    break;
		                }
		            }

		            if (!found) {
		                System.out.println("⚠️ Value not found in dropdown: " + value);
		            }

		        } catch (Exception e) {
		            System.out.println("❌ " + value + ": Not Found OR Dropdown not loaded properly.");
		            e.printStackTrace();
		        }
		    } else {
		        System.out.println("⚠️ Skipped selection: value is null or empty");
		    }
		}
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
	
	
}
