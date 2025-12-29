package pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Utilities;


public class CreateDeliverynotePage {

	 private final WebDriver driver;
	    private final WebDriverWait wait;

	    public CreateDeliverynotePage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    }

	    // ──────────────── Navigation Elements ────────────────
	    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
	    private final By salesMenuField = By.xpath("//div[@title='sales']/a[contains(text(),'Sales')]");
	    private final By deliveryNoteMenuField = By.xpath("//div[@title='delivery note']/span[contains(text(),'Delivery Note')]");
	    private final By newDeliverynoteButtonField = By.xpath("//button/p[contains(text(),'new')]");

	    // ──────────────── Header / Customer Fields ────────────────
	    private final By customerDropdownField = By.xpath("//input[@placeholder='Select Customer Name']");  //li[@role='option'][1]/div/div/h1
//	    private final By firstCustomerOptionField =  By.xpath("//li[@role='option'][1]/div/div/h1"); //div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1
	    private final By deliveryNoteNumberfield=By.id("delivery_challan_no");
	    private final By referenceNumberField = By.id("reference");
	    
	    private final By dNDateField=By.xpath("//label[@title='Date']//following-sibling::div/div/div/div/input");
	   // private final By expirydateField=By.id("expected_delivery_date");
	    
	   // private final By subjectField = By.id("subject");
	    private final By searchDNoteTypeField=By.xpath("//label[contains(text(),'Challan Type')]/following-sibling::input[@type='text']");	    
	    private final By selectDNoteTypeField = By.xpath("//ul/li[1]/span");
	    private final By searchPriceListField=By.xpath("//input[@placeholder='Price List' and @type='text']");	    
	    private final By selectPriceListField = By.xpath("//ul/li[1]"); 
	     
	    private final By searchTaxField=By.xpath("//input[@placeholder='Tax' and @type='text']");
	    private final By selectTaxField = By.xpath("//ul/li[1]");
	    
	    // ──────────────── Item Fields ────────────────
	    private final By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
	    private final By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
	    private final String itemSelectField = "//ul[1]/li[%d]"; // dynamic

	    // ──────────────── Notes and Terms ────────────────
	    private final By customerNoteField = By.id("notes");
	    private final By termsField = By.id("terms_and_conditions");

	    // ──────────────── Action Buttons ────────────────
	    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
//	    private final By saveAsArrowBtnField=By.xpath("//div[@class=' bg-accent w-44 flex items-center justify-center rounded-[4px] h-9 relative text-white shadow-sm']/div/div/div[1]");
//	    private final By saveAndApproveBtnField=By.xpath("//button[@name='s_approve']/div[text()='Save And Approve']"); 
//	    private final By saveAndSubmitBtnField=By.xpath("//button[@name='s_submit']/div[text()='Save and Submit']");
	    private final By deliveryNoteNoinListField = By.xpath("(//tr/td[4])[1]/div/div");

	    // ──────────────── Actions ────────────────

	    /** Navigate to Create Estimate Page */
	    public void navigateToNewDeliverynote() {
	    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(salesMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(deliveryNoteMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(newDeliverynoteButtonField)).click();
	    }
	    public String delivernoteNumber() {
	    	String DCNO=wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryNoteNumberfield)).getAttribute("value");
	    	return DCNO;
	    }
	    /** Fill in header details (customer, ref no, subject) 
	     * @throws InterruptedException */
	    public void fillEstimateHeader(String customerName, String referenceNo,String dNDate,String challanType,String taxType,String priceList) throws InterruptedException {
	    	System.out.println(customerName+" : "+referenceNo+" : Date : "+dNDate+" : "+challanType);

	    	if (customerName != null && !customerName  .trim().isEmpty()) {
	    			Utilities.selectCustomer(driver,customerDropdownField, customerName);
	    		}
 	 	
	        if (referenceNo != null && !referenceNo .trim().isEmpty()) {
	        	String ctime=utils.Utilities.dateTime(); 
	        	referenceNo=referenceNo+":"+ctime.split(" ")[1];
	        driver.findElement(referenceNumberField).sendKeys(referenceNo);
	        }
	        Thread.sleep(200);
	        if (dNDate != null && !dNDate.trim().isEmpty()) {
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(dNDateField)).sendKeys(dNDate);
	  	        }
	        else {
//	    		 
	    	 }
	        Thread.sleep(2000);	        
	        if (challanType != null && !challanType .trim().isEmpty()) {
	    		utils.Utilities.selectIfListed(driver, searchDNoteTypeField, selectDNoteTypeField, challanType);
	    	}   
	        if (priceList != null && !priceList .trim().isEmpty()) {
	    		Utilities.selectIfListed(driver, searchPriceListField, selectPriceListField,priceList);
	    	}
	        if (taxType == null || taxType.trim().isEmpty()|| "Exclusive".equalsIgnoreCase(taxType)) 
	        {
	        	System.out.println("Default Tax Exclusive");
	        }
	        else if ("Inclusive".equalsIgnoreCase(taxType)) {
	    		Utilities.selectIfListed(driver, searchTaxField, selectTaxField,taxType);
	    	}
	    }

	    /** Add multiple items dynamically 
	     * @throws InterruptedException */
	    public void addItems(String[] itemNames, String[] itemQtys,String[] discType,String[] discount) throws InterruptedException {
	    	
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
	            if (discType[i] != null && !discType[i].trim().isEmpty() && discount[i] != null && !discount[i].trim().isEmpty()) {
	            	if("%".equalsIgnoreCase(discType[i])) {
	            		
	            		WebElement discountField=driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[6]/div[1]/input"));
	            		discountField.clear();
	            		discountField.sendKeys(discount[i]);
	            	}
	            	else {
	            		By discDropdown=By.xpath(("//tbody/tr[" + (i + 1) + "]/td[6]/div[1]/div"));
	            		WebElement discontDropdownField=driver.findElement(discDropdown);
	            		wait.until(ExpectedConditions.elementToBeClickable(discDropdown));
	            		discontDropdownField.click();
	            		WebElement discountTypeAmountField=driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[6]/div[1]/div/div/div/ul/li[2]"));
	            		discountTypeAmountField.click();
	            		WebElement discountField=driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[6]/div[1]/input"));
	            		discountField.clear();
	            		System.out.println(discount[i]);
	            		discountField.sendKeys(discount[i]);
	            	}
	            } else {
	                // One or both values missing → no discount
	                System.out.println("Discount not applied (type or value missing)");
	            }
	        }
	    }
	    /** Add optional notes and terms */
	    public void addNotesAndTerms(String customerNote, String terms) {
//	    	String dateValue = driver.findElement(dNDateField).getAttribute("value"); //
//	    	System.out.println("Date value: " + dateValue);                          //                    
	        if (customerNote != null && !customerNote.isEmpty()) {
	            driver.findElement(customerNoteField).sendKeys(customerNote);
	        }
	        if (terms != null && !terms.isEmpty()) {
	            driver.findElement(termsField).sendKeys(terms);
	        }
	    }
	    /** Save the estimate as draft */
	    public void saveAsMethod(String saveAs)  {	
	    	if (saveAs == null || saveAs.trim().isEmpty()|| "SAVE AS DRAFT".equalsIgnoreCase(saveAs))   	        
	         {    	        
	        wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
	    	 }
	    }
	    /** Verify estimate saved by checking list */
	    public boolean verifyDeliverynoteCreated(String expectedDeliveryNoteNo) {
	    	int attempts = 0;
	        while (attempts < 3) {
		    	try {
		        wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryNoteNoinListField));
		        String actualDeliverynoteNo = Utilities.getTextWithRetry(driver,deliveryNoteNoinListField);
		        return actualDeliverynoteNo.equalsIgnoreCase(expectedDeliveryNoteNo);
		    	}
		    	catch(TimeoutException e) {
		    		attempts++;
		    	}
	        }
		    throw  new RuntimeException("Element remained stale after retries");
	    }
	}
