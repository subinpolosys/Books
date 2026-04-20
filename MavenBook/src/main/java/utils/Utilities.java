package utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
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
// Old ------------------------------------------********************************************
//	 public static void selectCustomer(WebDriver driver,By customerDropdownField, String customerName) throws InterruptedException {
//		 
//		WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(100));
//		 int maxAttempts = 3;
//
//		    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
//		        try {
//		            // Click dropdown
//		            WebElement dropdown = wait.until(
//		                    ExpectedConditions.elementToBeClickable(customerDropdownField));		            
//		            dropdown.click();
//		            // Clear input
//		            dropdown.sendKeys(Keys.CONTROL + "a");
//		            dropdown.sendKeys(Keys.DELETE);
//		            // Type slowly to trigger backend search
//		            for (char c : customerName.toCharArray()) {
//		                dropdown.sendKeys(String.valueOf(c));
//		                Thread.sleep(100);
//		            }
//		            By optionsLocator = By.xpath("//li[@role='option']");
//		            // Wait until options appear
//		            wait.until(driv ->
//		                    driver.findElements(optionsLocator).size() > 0
//		            );
//
//		            List<WebElement> options = driver.findElements(optionsLocator);
//		            for (WebElement option : options) {
//		                if (option.getText().trim().equalsIgnoreCase(customerName)) {
//		                    wait.until(ExpectedConditions.elementToBeClickable(option));
//		                    option.click();
//		                    break;
//		                }
//		            }
//		            // 🔍 VERIFY customer is actually loaded
//		            if (isCustomerSelected(driver, customerDropdownField,customerName)) {
//		                System.out.println("Customer selected successfully: " + customerName);
//		                return;
//		            }
//
//		            System.out.println("Customer not loaded, retrying... Attempt: " + attempt);
//
//		        } catch (Exception e) {
//		            if (attempt == maxAttempts) {
//		                throw new RuntimeException(
//		                        "Failed to select customer after retries: " + customerName, e
//		                );
//		            }
//		        }
//		    }
//		}

	//------------------------New customer selection : 21 March 2026--------------------------
	public static void selectCustomer(WebDriver driver, By customerDropdownField, String customerName) {
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
	    int maxAttempts = 3;

	    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
	        try {
	            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(customerDropdownField));
	            
	            dropdown.click();

	            // Clear field properly
	            dropdown.sendKeys(Keys.CONTROL + "a");
	            dropdown.sendKeys(Keys.DELETE);

	            // Type full name (no need char-by-char unless required)
	            dropdown.sendKeys(customerName);

	            // Wait for dropdown options to be visible
	            By optionsLocator = By.xpath("//li[@role='option']");
	            wait.until(ExpectedConditions.visibilityOfElementLocated(optionsLocator));

	            // Wait until at least one matching option appears
	            wait.until(driver1 -> driver1.findElements(optionsLocator)
	                    .stream()
	                    .anyMatch(el -> el.getText().trim().equalsIgnoreCase(customerName))
	            );

	            List<WebElement> options = driver.findElements(optionsLocator);

	            for (WebElement option : options) {
	            	if (option.getText().trim().equalsIgnoreCase(customerName)) {
	                    wait.until(ExpectedConditions.elementToBeClickable(option));
	                    option.click();
	                    break;
	                }
	            }

	            // 🔥 IMPORTANT: wait until value is actually set
	            boolean selected = wait.until(driver1 -> isCustomerSelected(driver1, customerDropdownField, customerName));

	            if (selected) {
	                System.out.println("Customer selected successfully: " + customerName);
	                return;
	            }

	            System.out.println("Retrying customer selection... Attempt: " + attempt);

	        } catch (Exception e) {
	            if (attempt == maxAttempts) {
	                throw new RuntimeException("Failed to select customer: " + customerName, e);
	            }
	        }
	    }
	}
	
	
	
	 private static boolean isCustomerSelected(WebDriver driver,By customerDropdownField, String customerName) {
		    try {
		        WebElement dropdown = driver.findElement(customerDropdownField);

		        String value = dropdown.getAttribute("value");
		        if (value != null && value.trim().equalsIgnoreCase(customerName)) {
		            return true;
		        }

		        // Some UI frameworks don’t use value attribute
		        String text = dropdown.getText();
		        return text != null && text.trim().equalsIgnoreCase(customerName);

		    } catch (Exception e) {
		        return false;
		    }
		}
	 public static String getTextWithRetry(WebDriver driver,By locator) {
		 WebDriverWait wait= new WebDriverWait(driver, Duration.ofSeconds(20));
	        int attempts = 0;

	        while (attempts < 3) {
	            try {
	                WebElement element = wait.until(
	                        ExpectedConditions.visibilityOfElementLocated(locator)
	                );
	                return element.getText().trim();
	            } catch (StaleElementReferenceException e) {
	                attempts++;
	            }
	        }
	        throw new RuntimeException("Element still stale after retries: " + locator);
	    }

	        public static boolean isNotBlank(String value) {
		    return value != null && !value.trim().isEmpty();
		    }

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
	
	 public static void openUrlSafely(WebDriver driver, String url) {
	        try {
	            driver.get(url);
	        } catch (WebDriverException e) {
	            if (e.getMessage() != null &&
	                e.getMessage().contains("ERR_CONNECTION_REFUSED")) {

	                throw new ApplicationDownException(
	                    "Skipping test: Application is down (" + url + ")"
	                );
	            }
	            throw e;
	        }
	    }

//------ new
	 public static void selectHeadlessUIDropdownValue(
		        WebDriver driver,
		        By inputField,
		        String value
		) throws InterruptedException {

		    if (value == null || value.trim().isEmpty()) {
		        System.out.println("⚠️ Skipped selection: value is null or empty");
		        return;
		    }

		    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

		    // HeadlessUI dropdown container
		    By optionsContainer =By.xpath("//ul[contains(@id,'headlessui-combobox-options')]");

		    // ✅ Exact selectable option (ignores group labels)
		    By exactOption = By.xpath("//li[@role='option' and normalize-space()='" + value + "']");

		    // Step 1: Type text to filter
		    WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputField));
		    //input.click();
		    input.clear();
		    Thread.sleep(200);
		    input.sendKeys(value);

		    // Step 2: Wait for dropdown
		    wait.until(ExpectedConditions.visibilityOfElementLocated(optionsContainer));

		    // Step 3: Click exact option with retry (stale-safe)
		    for (int i = 0; i < 3; i++) {
		        try {
		            WebElement option = wait.until(ExpectedConditions.elementToBeClickable(exactOption));
		            option.click();
		            break;
		        } catch (StaleElementReferenceException e) {
		            System.out.println("🔄 Retrying due to stale element (" + (i + 1) + ")");
		        }
		    }

		    // Step 4: Verify value committed
		    String selectedValue = input.getAttribute("value");
		    if (!value.equalsIgnoreCase(selectedValue)) {
		        throw new RuntimeException(
		                "❌ Dropdown selection failed. Expected: " + value +
		                ", Actual: " + selectedValue
		        );
		    }

		    //System.out.println("✅ Selected dropdown value: " + value);
		}
public static void selectDateByValue(WebDriver driver,By dateField, String dateValue) throws Exception {

		    // Parse incoming date value dd-MM-yyyy
		    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		    LocalDate target = LocalDate.parse(dateValue, formatter);

		    LocalDate today = LocalDate.now();

		    // Block past dates
		    if (target.isBefore(today)) {
		    	   System.out.println("Past date provided, skipping selection: " + dateValue);
		       // throw new Exception("Past date not allowed : " + dateValue);
		    	return;
		    }
		    // Open datepicker
		    driver.findElement(dateField).click();

		    // loop until target month-year is visible
		    while (true) {

		        // read currently displayed month & year
		        String monthYear = driver.findElement(
		                By.cssSelector(".react-datepicker__current-month")
		        ).getText(); // Example: January 2026

		        DateTimeFormatter headerFormat = DateTimeFormatter.ofPattern("MMMM yyyy");
		        YearMonth displayed = YearMonth.parse(monthYear, headerFormat);

		        YearMonth targetMonthYear = YearMonth.of(target.getYear(), target.getMonth());

		        if (displayed.equals(targetMonthYear)) {
		            break; // correct month year found
		        }

		        // if target is future -> click Next
		        if (displayed.isBefore(targetMonthYear)) {
		            driver.findElement(
		                    By.cssSelector("button.react-datepicker__navigation--next")
		            ).click();
		        }
		        // if needed you can handle prev also
		        else {
		            driver.findElement(
		                    By.cssSelector("button.react-datepicker__navigation--previous")
		            ).click();
		        }
		    }

		    // Now click the day
		    int day = target.getDayOfMonth();

		    driver.findElement(
		            By.xpath("//div[contains(@class,'react-datepicker__day') and text()='" + day + "']")
		    ).click();
		}
public static boolean isNotEmpty(String value) {
    return value != null && !value.trim().isEmpty();
}
public static void addTransactionLevelDiscount(WebDriver driver,
        WebDriverWait wait,
        By addDiscountLinkField,
        By transactionLevelDiscountField,
        String discountAfterBeforeTax,
        String discountType,
        String discountValue,
        String discountAccount) {

boolean isAfterTax = "After Tax".equalsIgnoreCase(discountAfterBeforeTax);
JavascriptExecutor js = (JavascriptExecutor) driver;

// STEP 1: Check and click Add Discount
try {
WebElement addDiscount = driver.findElement(addDiscountLinkField);
js.executeScript("arguments[0].scrollIntoView(true);", addDiscount);
wait.until(ExpectedConditions.elementToBeClickable(addDiscount));
addDiscount.click();
} catch (Exception e) {
System.out.println("Add Discount link not present. Skipping transaction discount.");
return;
}

// STEP 2: Handle After Tax (if applicable)
if (isAfterTax) {
try {
By applyBtn = By.xpath("//div/div[text()='Discount']//parent::div/div[text()='Apply ']");
wait.until(ExpectedConditions.elementToBeClickable(applyBtn));
driver.findElement(applyBtn).click();
} catch (Exception e) {
System.out.println("After Tax Apply option not available. Continuing...");
}
}

// STEP 3: Discount Type (Amount vs %)
if (!"%".equalsIgnoreCase(discountType)) {
selectAmountDiscountType(driver, wait);
}

// STEP 4: Enter Discount Value
try {
WebElement discountField = driver.findElement(transactionLevelDiscountField);
wait.until(ExpectedConditions.visibilityOf(discountField));
discountField.clear();
discountField.sendKeys(discountValue);
} catch (Exception e) {
System.out.println("Discount input field not available.");
}

// STEP 5: Optional Discount Account
/*
try {
Utilities.selectHeadlessUIDropdownValue(driver,
searchDiscountAccountField,
discountAccount);
} catch (Exception e) {
System.out.println("Discount account selection skipped.");
}
*/
}

// =========================
// Helper Method
// =========================
public static void selectAmountDiscountType(WebDriver driver,
    WebDriverWait wait) {

try {
By dropdown = By.xpath("(//button[@type='button' and starts-with(@id,'headlessui-listbox-button')])[2]");

wait.until(ExpectedConditions.elementToBeClickable(dropdown));
driver.findElement(dropdown).click();

By amountOption = By.xpath(
"//ul[@data-headlessui-state='open' and starts-with(@id,'headlessui-listbox-options')]/li[2]"
);

wait.until(ExpectedConditions.elementToBeClickable(amountOption));
driver.findElement(amountOption).click();

} catch (Exception e) {
System.out.println("Discount type dropdown not available.");
}
}
}
