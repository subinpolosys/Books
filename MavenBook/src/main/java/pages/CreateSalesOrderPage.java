package pages;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Utilities;
import utils.WaitUtils;

public class CreateSalesOrderPage {
	 private final WebDriver driver;
	    private final WebDriverWait wait;

	    public CreateSalesOrderPage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    }
	    // ──────────────── Navigation Elements ────────────────
	    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
	    private final By salesMenuField = By.xpath("//a[contains(text(),'Sales')]");
	    private final By salesOrderMenuField = By.xpath("//span[contains(text(),'Sales Orders')]");
	    private final By newSalesOrderButtonField = By.xpath("//button/p[contains(text(),'new')]");

	    // ──────────────── Header / Customer Fields ────────────────
	    private final By customerDropdownField = By.xpath("//input[@placeholder='Select Customer Name']");
	    private final By firstCustomerOptionField =  By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");
	    private final By salesOrderNumberfield=By.id("sales_order_no");
	    private final By referenceNumberField =By.id("reference");
	    
	    private final By soDateField=By.xpath("//label[@title='Sale Order Date']//following-sibling::div/div/div/div/input"); 
	    private final By deliverDateField=By.xpath("//label[@title='Expected Delivery Date']//following-sibling::div/div/div/div/input"); 
	    private final By searchPaymentTermsField=By.xpath("//label[contains(text(),'Payment Terms')]/following-sibling::input[@type='text']");	    
	    private final By selectPaymentTermsField = By.xpath("//ul/li[1]/span");
	    
	    // private final By subjectField = By.id("subject");
	    private final By searchSalesPersonField=By.xpath("//label[contains(text(),'Sales Person')]/following-sibling::input[@type='text']");	    
	    private final By selectSalesPersonField = By.xpath("//ul/li[1]/span");
	    private final By searchPriceListField=By.xpath("//input[@placeholder='Price List' and @type='text']");	    
	    private final By selectPriceListField = By.xpath("//ul/li[1]"); 
	     
	    private final By searchTaxField=By.xpath("//input[@placeholder='Tax' and @type='text']");
	    private final By selectTaxField = By.xpath("//ul/li[1]");
	      
	    // ──────────────── Item Fields ────────────────
	    private final By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
	    private final By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
	    private final String itemSelectField = "//ul[1]/li[%d]"; // dynamic

	    // ──────────────── Notes and Terms ────────────────
	    private final By customerNoteField = By.id("customer notes");
	    private final By termsField = By.id("terms_and_conditions");

	    // ──────────────── Action Buttons ────────────────
	    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
	    private final By saveAsArrowBtnField=By.xpath("//div[@class=' bg-accent w-44 flex items-center justify-center rounded-[4px] h-9 relative text-white shadow-sm']/div/div/div[1]");
	    private final By saveAndApproveBtnField=By.xpath("//button[@name='s_approve']/div[text()='Save And Approve']"); 
	    private final By saveAndSubmitBtnField=By.xpath("//button[@name='s_submit']/div[text()='Save and Submit']");
	    private final By creditLimitProceedPopupField= By.xpath("//div[contains(@id,'headlessui-dialog-panel')]/div[2]/button[1]");
	    private final By salesOrderNoinListField = By.xpath("(//tr/td[3])[1]/div/div");

	    // ──────────────── Actions ────────────────

	    /** Navigate to Create Estimate Page */
	    public void navigateToNewSalesOrder() {
	    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(salesMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(salesOrderMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(newSalesOrderButtonField)).click();
	    }
	    public String salesOrderNumber() {
	    	String SONO=wait.until(ExpectedConditions.visibilityOfElementLocated(salesOrderNumberfield)).getAttribute("value");
	    	return SONO;
	    }
	    /** Fill in header details (customer, ref no, subject) 
	     * @throws InterruptedException */
	    //String customerName="";
	    public void fillSalesOrderHeader(
	    		String customerName, 
	    		String referenceNo,
	    		String soDate,
	    		String expDeliDate,
	    		String paymentTerms, 
	    		String salesPerson,
	    		String taxType,
	    		String priceList) throws InterruptedException {
	    	//customerName=customerName1;
	    	//System.out.println(customerName+" : "+referenceNo+" : "+soDate+" :"+expDeliDate+": "+salesPerson+":"+paymentTerms);
	    	Utilities.waitForPageToLoad(driver);
	    	if (Utilities.isNotEmpty(customerName)) {	
				Utilities.selectCustomer(driver,customerDropdownField, customerName);
	    	}
	    	
	    	if(Utilities.isNotEmpty(referenceNo)) {
	    		String ctime=Utilities.dateTime(); 
	        	referenceNo=referenceNo+":"+ctime.split(" ")[1];
	        driver.findElement(referenceNumberField).sendKeys(referenceNo);
	    	}
	        if (Utilities.isNotEmpty(soDate)) {
	        	//System.out.println("inv Date:");
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(soDateField)).sendKeys(soDate);
	    		Thread.sleep(100);
	    	}	        
	        if (Utilities.isNotEmpty(paymentTerms)) {
	    		Utilities.selectIfListed(driver, searchPaymentTermsField, selectPaymentTermsField,paymentTerms);
	    		Thread.sleep(100);
	    	}	         
	        if (Utilities.isNotEmpty(expDeliDate)) {
	        	//System.out.println("sup Date:");
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(deliverDateField)).sendKeys(expDeliDate);
	        	Thread.sleep(100);
	    	}
	        JavascriptExecutor sp = (JavascriptExecutor) driver;
	        WebElement salesP  = driver.findElement(searchSalesPersonField);
		    sp.executeScript("arguments[0].scrollIntoView();",salesP);  
	        if (Utilities.isNotEmpty(salesPerson)) {
	    		Utilities.selectIfListed(driver, searchSalesPersonField, selectSalesPersonField,salesPerson);
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
	         Thread.sleep(200);
	         if(driver.findElement(customerDropdownField).getAttribute("value").isEmpty()) {
	             Utilities.selectCustomer(driver, customerDropdownField, customerName);
	             System.out.println("Customer first time not loaded");
	         }
	    }
	    /** Add multiple items dynamically 
	     * @throws InterruptedException */
	    public void addItems(
	    		String[] itemNames, 
	    		String[] itemQtys,
	    		String[] discType,
	    		String[] discount) throws InterruptedException {	    	
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement itemdetail  = driver.findElement(itemDetailsField);
		    js.executeScript("arguments[0].scrollIntoView();",itemdetail);  
			driver.findElement(itemDetailsField).click();  //#####  
			//Thread.sleep(500);
			WaitUtils.waitForUi(driver);
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
	            		//System.out.println(discount[i]);
	            		discountField.sendKeys(discount[i]);
	            	}
	            } else {
	                // One or both values missing → no discount
	                //System.out.println("Discount not applied (type or value missing)");
	            }
	        }
	    }

	    /** Add optional notes and terms */
	    public void addNotesAndTerms(String customerNote, String terms) {
	        if (Utilities.isNotEmpty(customerNote)) {
	            driver.findElement(customerNoteField).sendKeys(customerNote);
	        }
	        if (Utilities.isNotEmpty(terms)) {
	            driver.findElement(termsField).sendKeys(terms);
	        }
	    }
	    /** Save the estimate as draft 
	     * @throws InterruptedException */
	    public void saveAsMethod(String saveAs) throws InterruptedException {
//	    	 if(driver.findElement(customerDropdownField).getAttribute("value").isEmpty()) {
//	             Utilities.selectCustomer(driver, customerDropdownField, customerName);
//	             System.out.println("Customer first time not loaded");
//	         }
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
	    	WebDriverWait popupwait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    	boolean creditLimitPopupHandled = false;
	    	boolean outOfStockPopupHandled = false;
	    	
			try {
			    WebElement popup = popupwait.until(ExpectedConditions.visibilityOfElementLocated(
			        creditLimitProceedPopupField));
			    popup.click();
			    creditLimitPopupHandled = true;
			    System.out.println("Customer's credit limit exceeded !!");
			    // Optional: wait for popup to disappear
			    popupwait.until(ExpectedConditions.invisibilityOf(popup));
			    // Add delay or check for some stable element after popup
			    // Example: wait for the form or a certain section to be visible
			    popupwait.until(ExpectedConditions.visibilityOfElementLocated(
			        By.xpath("//button[contains(text(),'Save as Draft')]"))); // or any stable element
			} catch (TimeoutException e) {
			    // No popup appeared, proceed normally
			}
	    	try {  //h3[text()='Out of Stock']
	    		WebElement popup1 = popupwait.until(ExpectedConditions.visibilityOfElementLocated(
				       creditLimitProceedPopupField));
				    popup1.click();
				    outOfStockPopupHandled  = true;
				    System.out.println("Product Stocked out !!");
				    popupwait.until(ExpectedConditions.invisibilityOf(popup1));				    
				    popupwait.until(ExpectedConditions.visibilityOfElementLocated(saveAsDraftButtonField));
				       	    		
	    	}catch (TimeoutException e) {
			    // No popup appeared, proceed normally
			}
	    	
	    }
	    /** Verify estimate saved by checking list */
	    public boolean verifySalesOrderCreated(String expectedSalesOrderNo) {
	    	try {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(salesOrderNoinListField));
	        String actualSalesOrderNo = Utilities.getTextWithRetry(driver,salesOrderNoinListField);
	        return actualSalesOrderNo.equalsIgnoreCase(expectedSalesOrderNo);
	    	}
	    	catch(TimeoutException e) {
	    		return false;
	    	}
	    }
	}
