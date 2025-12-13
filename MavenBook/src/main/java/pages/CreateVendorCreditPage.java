package pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.Utilities;

public class CreateVendorCreditPage {
	private final WebDriver driver;
    private final WebDriverWait wait;
    public CreateVendorCreditPage(WebDriver driver) {
		this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ──────────────── Navigation Elements ────────────────
  
    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
    private final By purchaseMenuField = By.xpath("//div[@title='purchase']/a[contains(text(),'Purchase')]");
    private final By vendorCreditMenuField = By.xpath("//div[@title='vendor credits']/span[contains(text(),'Vendor Credits')]");
    private final By newVendorCreditButtonField = By.xpath("//button/p[contains(text(),'new')]");

    // ──────────────── Header / Customer Fields ────────────────
    private final By vendorDropdownField = By.xpath("//input[@placeholder='Select Vendor Name']");
    private final By firstVendorOptionField =  By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");  private final By salesReturnNumberField=By.id("sales_return_no");
    private final By vendorCreditNumberField=By.id("vendor_credit_no");
    
    private final By vendorCreditDateField=By.id("date");
    private final By orderNumberField = By.id("order_number");
    
    private final By searchBillField=By.xpath("//label[contains(text(),'Bill')]/following-sibling::input[@type='text']");	    
    private final By selectBillField = By.xpath("//ul/li[1]/span");

    private final By taxField=By.xpath("//a[text()='Tax']");
    
    // ──────────────── Item Fields ────────────────
    private final By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
    private final By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
    private final String itemSelectField = "//ul[1]/li[%d]"; // dynamic

    // ──────────────── Notes and Terms ────────────────
    private final By noteField = By.id("notes");
   

    // ──────────────── Action Buttons ────────────────
    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
    private final By vendorCreditNoinListField = By.xpath("(//tr/td[3])[1]/div/div");

    // ──────────────── Actions ────────────────

    /** Navigate to Create Estimate Page 
     * @throws InterruptedException */
    public void navigateToNewVendorCredit(){
    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(purchaseMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(vendorCreditMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(newVendorCreditButtonField)).click();
       
    }

    public String vendorCreditNumber() {
    	String VCno=wait.until(ExpectedConditions.visibilityOfElementLocated(vendorCreditNumberField)).getAttribute("value");
    	//System.out.println(SRno);
    	return VCno;
    	
    }
    /** Fill in header details (customer, ref no, subject) 
     * @throws InterruptedException */
    public void fillVendorCreditHeader(String vendorName, String billNo, String vendorCreditDate,String orderNo) throws InterruptedException {
    	System.out.println(vendorName+" : "+billNo+" : "+vendorCreditDate+" : "+orderNo);
    	
    	if (vendorName != null && !vendorName  .trim().isEmpty()) {
    		try {
    		wait.until(ExpectedConditions.elementToBeClickable(vendorDropdownField)).click();
	        driver.findElement(vendorDropdownField).sendKeys(vendorName);	        
	        List<WebElement> options = driver.findElements(firstVendorOptionField); // adjust locator as needed
	        boolean found = false;
            for (WebElement option : options) {
                if (option.getText().equalsIgnoreCase(vendorName)) {
                    option.click();
                    found = true;
                    break;
                }
            }
            if (!found) {
                Assert.fail("Customer name '" + vendorName + "' not found in the dropdown list.");
            }	        
	       // wait.until(ExpectedConditions.elementToBeClickable(firstCustomerOptionField)).click();
    		}catch(Exception e) {
    			 Assert.fail("Error selecting customer: " + e.getMessage());
    		}
    	}
        if (vendorCreditDate != null && !vendorCreditDate.trim().isEmpty()) {
        	WebElement srdate = driver.findElement(vendorCreditDateField);
	        	wait.until(ExpectedConditions.attributeToBeNotEmpty(srdate, "value"));
	        	        	
		    	srdate.clear();
		    	srdate.sendKeys(vendorCreditDate);
		    
    	}
        else {
        	Thread.sleep(200);
        	//WebElement srdate = driver.findElement(salesReturnDateField);
        	//wait.until(ExpectedConditions.attributeToBeNotEmpty(srdate, todayStr));
        }
        Thread.sleep(2000);
        if (billNo!= null && !billNo.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchBillField, selectBillField,billNo);
    	}
        Thread.sleep(2000);
        if(orderNo!=null && !orderNo.trim().isEmpty()){
        	driver.findElement(orderNumberField).sendKeys(orderNo);
        }
        Thread.sleep(2000);
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        WebElement tax  = driver.findElement(taxField);
//	    js.executeScript("arguments[0].scrollIntoView();",tax); 
        
    }

    /** Add multiple items dynamically 
     * @throws InterruptedException */
    public void addItems(String[] itemNames, String[] itemQtys) throws InterruptedException {    	
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement itemdetail  = driver.findElement(itemDetailsField);
	    js.executeScript("arguments[0].scrollIntoView();",itemdetail);  
		driver.findElement(itemDetailsField).click();  //#####  
		Thread.sleep(500);	
        for (int i = 0; i < itemNames.length; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(itemListField)).click();
            driver.findElement(itemListField).sendKeys(itemNames[i]);
            Thread.sleep(500);
            // Select item dynamically
            By selectItem = By.xpath(String.format(itemSelectField, 1));
            wait.until(ExpectedConditions.elementToBeClickable(selectItem)).click();
            Thread.sleep(500);
            // Fill item quantity
            WebElement qtyField = driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[4]//input"));
            qtyField.clear();
            qtyField.sendKeys(itemQtys[i]);
            Thread.sleep(500);
        }
    }
    /** Add optional notes */
    public void addNotes(String customerNote, String terms) {
      JavascriptExecutor js = (JavascriptExecutor) driver;
      WebElement notes  = driver.findElement(noteField);
	    js.executeScript("arguments[0].scrollIntoView();",notes); 
        if (customerNote != null && !customerNote.isEmpty()) {
        	driver.findElement(noteField).clear();
            driver.findElement(noteField).sendKeys(customerNote);
        }
        
    }
    /** Save the estimate as draft */
    public void saveAsDraft() {
        wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
    }
    /** Verify estimate saved by checking list */
    public boolean verifyVendorCreditCreated(String expectedVendorCreditNo) {
    	int attempts = 0;
        while (attempts < 3) {
	    	try {
	    	//System.out.println("expected VC number : "+expectedVendorCreditNo);
	        wait.until(ExpectedConditions.visibilityOfElementLocated(vendorCreditNoinListField));
	        String actualVendorCreditNo = driver.findElement(vendorCreditNoinListField).getText();
	        //System.out.println("Actual VC number :"+actualVendorCreditNo);
	        return actualVendorCreditNo.equalsIgnoreCase(expectedVendorCreditNo);
	    	}
	        catch(TimeoutException e) {
	    		attempts++;
	    	}
        }
    throw new RuntimeException("Element remained stale after retries");
    }
}
