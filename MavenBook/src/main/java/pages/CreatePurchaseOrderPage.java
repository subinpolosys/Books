package pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Utilities;
public class CreatePurchaseOrderPage {
	 private final WebDriver driver;
	    private final WebDriverWait wait;

	    public CreatePurchaseOrderPage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    }
	    // ──────────────── Navigation Elements ────────────────
	    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
	    private final By purchaseMenuField = By.xpath("//div[@title='purchase']/a[contains(text(),'Purchase')]");
	    private final By purchaseOrderMenuField = By.xpath("//div[@title='purchase order']/span[contains(text(),'Purchase Order')]");
	    private final By newPurchaseOrderButtonField = By.xpath("//button/p[contains(text(),'new')]");

	    // ──────────────── Header / Customer Fields ────────────────
	    private final By vendorDropdownField = By.xpath("//input[@placeholder='Select Vendor Name']");
	    private final By firstVendorOptionField =  By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");
	    private final By purchaseOrderNumberfield=By.id("purchase_order_no");
	    private final By referenceNumberField = By.id("reference");
	    private final By deliverToOrgField=By.xpath("(//input[@type='radio'])[1]");
	    
	    private final By poDateField=By.xpath("//label[@title='purchase order date']//following-sibling::div/div/div/div/input"); 
	    private final By deliverDateField=By.xpath("//label[@title='Expected Delivery Date']//following-sibling::div/div/div/div/input"); 
	    private final By searchPaymentTermsField=By.xpath("//label[contains(text(),'Payment Terms')]/following-sibling::input[@type='text']");	    
	    private final By selectPaymentTermsField = By.xpath("//ul/li[1]/span");
	    
	    
	    
	    // private final By subjectField = By.id("subject");
//	    private final By searchSalesPersonField=By.xpath("//label[contains(text(),'Sales Person')]/following-sibling::input[@type='text']");	    
//	    private final By selectSalesPersonField = By.xpath("//ul/li[1]/span");
	    // ──────────────── Item Fields ────────────────
	    private final By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
	    private final By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
	    private final String itemSelectField = "//ul[1]/li[%d]"; // dynamic

	    // ──────────────── Notes and Terms ────────────────
	    private final By noteField = By.id("notes");
	    private final By termsField = By.id("terms_and_conditions");

	    // ──────────────── Action Buttons ────────────────
	    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
	    private final By purchaseOrderNoinListField = By.xpath("(//tr/td[3])[1]/div/div");

	    // ──────────────── Actions ────────────────

	    /** Navigate to Create Estimate Page */
	    public void navigateToNewPurchaseOrder() {
	    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(purchaseMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(purchaseOrderMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(newPurchaseOrderButtonField)).click();
	        Utilities.waitForPageLoad(driver);
	    }
	    public String purchaseOrderNumber() {
	    	String SONO=wait.until(ExpectedConditions.visibilityOfElementLocated(purchaseOrderNumberfield)).getAttribute("value");
	    	return SONO;
	    }
	    /** Fill in header details (customer, ref no, subject) 
	     * @throws InterruptedException */
	    public void fillPurchaseOrderHeader(String vendorName, String referenceNo, String poDate,String paymentTerms,String deliveryDate) throws InterruptedException {
	    	System.out.println(vendorName+" : "+referenceNo+" PO date : "+poDate+" Delivery Date : "+deliveryDate);
	    	
	    	if (vendorName != null && !vendorName  .trim().isEmpty()) {	
				Utilities.selectCustomer(driver,vendorDropdownField, vendorName);
	    	}
	    	 wait.until(ExpectedConditions.elementToBeSelected(deliverToOrgField));
	        if(referenceNo!= null && !referenceNo.trim().isEmpty()) {
	        	String ctime=Utilities.dateTime();
	        	referenceNo=referenceNo+":"+ctime.split(" ")[1];
	        	driver.findElement(referenceNumberField).sendKeys(referenceNo);
	        }
	        if (poDate != null && !poDate.trim().isEmpty()) {
//	        	Thread.sleep(3000);
//	        	Utilities.waitForPageLoad(driver);
//	        
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(poDateField)).sendKeys(poDate);
	    	}	
	        Thread.sleep(200);
	        if (deliveryDate != null && !deliveryDate.trim().isEmpty()) {
	        	//System.out.println("delivery  Date:"+deliveryDate);
//	        	Thread.sleep(2000);
//	        	Utilities.waitForPageLoad(driver);
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(deliverDateField)).sendKeys(deliveryDate);
	    	}
	        Thread.sleep(200);
	        if (paymentTerms!= null && !paymentTerms.trim().isEmpty()) {
	        	Utilities.selectIfListed(driver, searchPaymentTermsField, selectPaymentTermsField, paymentTerms);
	   
	    	}	
	         Thread.sleep(200);
	    }
	    /** Add multiple items dynamically 
	     * @throws InterruptedException */
	    public void addItems(String[] itemNames, String[] itemQtys) throws InterruptedException {	    	
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement itemdetail  = driver.findElement(itemDetailsField);
		    js.executeScript("arguments[0].scrollIntoView();",itemdetail);  
			driver.findElement(itemDetailsField).click();  //#####  
			Thread.sleep(100);	
	        for (int i = 0; i < itemNames.length; i++) {
	            wait.until(ExpectedConditions.elementToBeClickable(itemListField)).click();
	            driver.findElement(itemListField).sendKeys(itemNames[i]);
	            Thread.sleep(500);
	            // Select item dynamically
	            By selectItem = By.xpath(String.format(itemSelectField, 1));
	            Thread.sleep(200);
	            wait.until(ExpectedConditions.elementToBeClickable(selectItem)).click();
	            Thread.sleep(200);
	            // Fill item quantity
	            WebElement qtyField = driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[4]//input"));
	            qtyField.clear();
	            qtyField.sendKeys(itemQtys[i]);
	            //Thread.sleep(500);
	        }
	    }
	    /** Add optional notes and terms */
	    public void addNotesAndTerms(String customerNote, String terms) {
	        if (customerNote != null && !customerNote.isEmpty()) {
	            driver.findElement(noteField).sendKeys(customerNote);
	        }
	        if (terms != null && !terms.isEmpty()) {
	            driver.findElement(termsField).sendKeys(terms);
	        }
	    }
	    /** Save the estimate as draft */
	    public void saveAsDraft() {
	        wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
	    }
	    /** Verify estimate saved by checking list */
	    public boolean verifyPurchaseOrderCreated(String expectedPurchaseOrderNo) {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(purchaseOrderNoinListField));
	        String actualPurchaseOrderNo = Utilities.getTextWithRetry(driver,purchaseOrderNoinListField);
	        return actualPurchaseOrderNo.equalsIgnoreCase(expectedPurchaseOrderNo);
	    }
	}
