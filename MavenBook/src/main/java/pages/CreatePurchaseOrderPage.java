package pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.DiscountUtils;
import utils.Utilities;
import utils.WaitUtils;
public class CreatePurchaseOrderPage {
	 private final WebDriver driver;
	    private final WebDriverWait wait;

	    public CreatePurchaseOrderPage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    }
	    // ──────────────── Navigation Elements ────────────────
	    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
	    private final By purchaseMenuField = By.xpath("//a[contains(text(),'Purchase')]");
	    private final By purchaseOrderMenuField = By.xpath("//span[contains(text(),'Purchase Order')]");
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
	    private final By discountLevelTypeDropdownField = By.xpath("//a[text()='Discount Type']//parent::div/div/div/div/button");
	    private final By selectItemLevelDiscountField = By.xpath("//a[text()='Discount Type']//parent::div/div/div/div/ul/li[1]");		    	
	    private final By searchPriceListField=By.xpath("//input[@placeholder='Price List' and @type='text']");	    
	    private final By selectPriceListField = By.xpath("//ul/li[1]"); 
	    private final By searchTaxField=By.xpath("//input[@placeholder='Tax' and @type='text']");
	    private final By selectTaxField = By.xpath("//ul/li[1]");
	    
	    
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
	    
	 // ──────────────── Transaction level Discount ────────────────
	    private final By addDiscountLinkField=By.xpath("//a[text()=' Add Discount']");
	    private final By transactionLevelDiscountField=By.xpath("//input[@tabindex=0 and @type='number']");
	    private final By searchDiscountAccountField=By.xpath("//div[text()='Discount Account']//following-sibling::div/div/div/div/button/div/input");
	    private final By selectDiscountAccountField=By.xpath("//div[text()='Discount Account']//following-sibling::div/div/div/ul/div/li[3]");

	    // ──────────────── Action Buttons ────────────────
	    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
	    
	    private final By saveAsArrowBtnField=By.xpath("//div[@class=' bg-accent w-44 flex items-center justify-center rounded-[4px] h-9 relative text-white shadow-sm']/div/div/div[1]");
	    private final By saveAndApproveBtnField=By.xpath("//button[@name='s_approve']/div[text()='Save And Approve']"); 
	    private final By saveAndSubmitBtnField=By.xpath("//button[@name='s_submit']/div[text()='Save and Submit']");
	    
	    private final By purchaseOrderNoinListField = By.xpath("(//tr/td[3])[1]/div/div");
	    //int discLevel;

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
	     * @throws Exception */
	    public int fillPurchaseOrderHeader(
	    		String vendorName,
	    		String referenceNo,
	    		String poDate,
	    		String paymentTerms,
	    		String deliveryDate,
	    		String taxType,
	    		String priceList,
	    		String discountLevel ) throws Exception {
	    	System.out.println(vendorName+" : "+referenceNo+" PO date : "+poDate+" Delivery Date : "+deliveryDate+" discount level : "+discountLevel);
	    	int discLevel = DiscountUtils.resolveDiscountLevel(discountLevel);
	    	
	    	if (Utilities.isNotEmpty(vendorName)){	
				Utilities.selectCustomer(driver,vendorDropdownField, vendorName);
	    	}
	    	 wait.until(ExpectedConditions.elementToBeSelected(deliverToOrgField));
	    	 if (Utilities.isNotEmpty(referenceNo)) {
	        	String ctime=Utilities.dateTime();
	        	ctime=ctime.split(" ")[1];
	        	referenceNo=referenceNo+":"+ctime;
	        	driver.findElement(referenceNumberField).sendKeys(referenceNo);
	        }
	        if (Utilities.isNotEmpty(poDate)) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(poDateField));
	    		Utilities.selectDateByValue(driver, poDateField, poDate);
	    	}	
	        WaitUtils.waitForUi(driver);
	        if (Utilities.isNotEmpty(deliveryDate)) {

	        	wait.until(ExpectedConditions.visibilityOfElementLocated(deliverDateField));
	        	Utilities.selectDateByValue(driver, deliverDateField, deliveryDate);
	        }
	        WaitUtils.waitForUi(driver);
	        if (Utilities.isNotEmpty(paymentTerms)) {
	        	Utilities.selectIfListed(driver, searchPaymentTermsField, selectPaymentTermsField, paymentTerms);
	    	}	
	        WaitUtils.waitForUi(driver);
	        
	         /*if (discountLevel == null || discountLevel.trim().isEmpty()|| "At Aransaction level".equalsIgnoreCase(discountLevel)) 
		        {
		        	//System.out.println("Transaction level discount");
		        	discLevel=0; 	
		        }
		        //else if ("At Line Item Level".equalsIgnoreCase(discountLevel))
		        */
		       if(discLevel==1) {
		        	//System.out.println("at line item level select. "+discountLevel);
		    	   JavascriptExecutor selectDiscount = (JavascriptExecutor) driver;
		 			 WebElement selDct  = driver.findElement(discountLevelTypeDropdownField);
		 			selectDiscount.executeScript("arguments[0].scrollIntoView();",selDct);
		        		        	
		        	wait.until(ExpectedConditions.visibilityOfElementLocated(discountLevelTypeDropdownField)).click();
		        	wait.until(ExpectedConditions.invisibilityOfElementLocated(
		        	By.cssSelector(".loading, .spinner")));		        	
		        	wait.until(ExpectedConditions.visibilityOfElementLocated(selectItemLevelDiscountField));
		        	wait.until(ExpectedConditions.visibilityOfElementLocated(selectItemLevelDiscountField)).click();			        		  
		    	}
	         
	         if (Utilities.isNotEmpty(priceList)) {
		    		Utilities.selectIfListed(driver, searchPriceListField, selectPriceListField,priceList);
		    	}
		        if (taxType == null || taxType.trim().isEmpty()|| "Exclusive".equalsIgnoreCase(taxType)) 
		        {
		        	//System.out.println("Default Tax Exclusive");
		        }
		        else if ("Inclusive".equalsIgnoreCase(taxType)) {
		    		Utilities.selectIfListed(driver, searchTaxField, selectTaxField,taxType);
		    	}
		        
		//         Thread.sleep(200);
		        WaitUtils.waitForUi(driver);
		        
		        if(driver.findElement(vendorDropdownField).getAttribute("value").isEmpty()) {
		            Utilities.selectCustomer(driver, vendorDropdownField, vendorName);
		            System.out.println("Vendor first time not loaded");
		        }
		        
		         return discLevel;
	    }
	    
	    
	    /** Add multiple items dynamically 
	     * @throws InterruptedException */
	    public void addItems(
	    		String[] itemNames, 
	    		String[] itemQtys,
	    		String[] discType,
	    		String[] discount,
	    		int discLevel) throws InterruptedException {	    	
//			JavascriptExecutor js = (JavascriptExecutor) driver;			
//			WebElement itemdetail  = driver.findElement(itemDetailsField);
//		    js.executeScript("arguments[0].scrollIntoView();",itemdetail);  
//			driver.findElement(itemDetailsField).click();  //#####  
	 
	    	WebElement itemdetail = wait.until(ExpectedConditions.visibilityOfElementLocated(itemDetailsField));

	    	// 👉 DO NOT CLICK — just focus
	    	JavascriptExecutor js = (JavascriptExecutor) driver;
	    	js.executeScript("arguments[0].focus();", itemdetail);
	    	
			Thread.sleep(500);	
	        for (int i = 0; i < itemNames.length; i++) {
//	            wait.until(ExpectedConditions.elementToBeClickable(itemListField)).click();
//	            driver.findElement(itemListField).sendKeys(itemNames[i]);
	        	WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(itemListField));

	        	js.executeScript("arguments[0].focus();", input);
	        	input.clear();
	        	input.sendKeys(itemNames[i]);
	            Thread.sleep(500);
	            // Select item dynamically
	            By selectItem = By.xpath(String.format(itemSelectField, 1));
	            Thread.sleep(200);
	            wait.until(ExpectedConditions.elementToBeClickable(selectItem)).click();
	            Thread.sleep(200);
	            // Fill item quantity
	            WebElement qtyField = driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[5]//input"));
	            qtyField.clear();
	            qtyField.sendKeys(itemQtys[i]);
	            //Thread.sleep(500);
	            if(discLevel!=0) {
	            	if (discType[i] != null && !discType[i].trim().isEmpty() && discount[i] != null && !discount[i].trim().isEmpty()) {
	            		if("%".equalsIgnoreCase(discType[i])) {
	            		
		            		WebElement discountField=driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "][1]/td[7]/div[1]/input"));
		            		discountField.clear();
		            		discountField.sendKeys(discount[i]);
	            		}
		            	else {
		            		By discDropdown = By.xpath("//tbody/tr[" + (i + 1) + "]/td[7]/div[1]/div");
		            		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(discDropdown));
		            		dropdown.click();
		            		// Wait for dropdown options globally
		            		By optionLocator = By.xpath("//ul/li[2]");
		            		WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
		            		option.click();
		            		// Now enter value
		            		By discountFieldLocator = By.xpath("(//tbody/tr[" + (i + 1) + "]/td[7]//input)[1]");
		            		WebElement discountField = wait.until(ExpectedConditions.visibilityOfElementLocated(discountFieldLocator));
		            		discountField.clear();
		            		discountField.sendKeys(discount[i]);
		            	}
		            } else {
		                // One or both values missing → no discount
		                //System.out.println("Discount not applied (type or value missing)");
		            }
	            }
	        }
	    }
	    /** Add Transaction level discount */ 
	    public void applyTransactionLevelDiscount(String discountAfterBeforeTax,
	            String discountType,
	            String discountValue,
	            String discountAccount) {

	    		Utilities.addTransactionLevelDiscount(
	    				driver,
	    				wait,
	    				addDiscountLinkField,
	    				transactionLevelDiscountField,
	    				discountAfterBeforeTax,
	    				discountType,
	    				discountValue,
	    				discountAccount);
	    }
	    /** Add optional notes and terms */
	    public void addNotesAndTerms(String customerNote, String terms) {
	        if (Utilities.isNotEmpty(customerNote)) {
	            driver.findElement(noteField).sendKeys(customerNote);
	        }
	        if (Utilities.isNotEmpty(terms)) {
	            driver.findElement(termsField).sendKeys(terms);
	        }
	    }
	    /** Save the Purchase Order*/
	    public void saveAsMethod(String saveAs) {
	    	//System.out.println("SAve as : "+saveAs);
	    	if (saveAs == null || saveAs.trim().isEmpty()|| "SAVE AS DRAFT".equalsIgnoreCase(saveAs))   	        
	         {    	        
	        wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
	    	 }
	    	 //System.out.println("Estimate date field value: " + driver.findElement(estimateDateField).getAttribute("value"));	    	 
	    	 else if(("SAVE AND APPROVE".equalsIgnoreCase(saveAs))) {	    		 
	    		 wait.until(ExpectedConditions.elementToBeClickable(saveAsArrowBtnField)).click();
	    		 wait.until(ExpectedConditions.elementToBeClickable(saveAndApproveBtnField)).click();
	    	 }
	    	 else if("SAVE AND SUBMIT".equalsIgnoreCase(saveAs)) {
	    		 wait.until(ExpectedConditions.elementToBeClickable(saveAsArrowBtnField)).click();
	    		 wait.until(ExpectedConditions.elementToBeClickable(saveAndSubmitBtnField)).click();
	    	 }
	    }
	    /** Verify estimate saved by checking list */
	    public boolean verifyPurchaseOrderCreated(String expectedPurchaseOrderNo) {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(purchaseOrderNoinListField));
	        String actualPurchaseOrderNo = Utilities.getTextWithRetry(driver,purchaseOrderNoinListField);
	        return actualPurchaseOrderNo.equalsIgnoreCase(expectedPurchaseOrderNo);
	    }
	}
