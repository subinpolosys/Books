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

import utils.DiscountUtils;
import utils.Utilities;
import utils.WaitUtils;

public class CreatePurchaseBillPage {
	private final WebDriver driver;
    private final WebDriverWait wait;
    public CreatePurchaseBillPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    // ──────────────── Navigation Elements ────────────────
  
    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
    private final By purchaseMenuField = By.xpath("//a[contains(text(),'Purchase')]");
    private final By purchaseBillMenuField = By.xpath("//span[contains(text(),'Purchase Bills')]");
    private final By newPurchaseBillButtonField = By.xpath("//button/p[contains(text(),'new')]");

    // ──────────────── Header / Customer Fields ────────────────
    private final By vendorDropdownField = By.xpath("//input[@placeholder='Select Vendor Name']");
    private final By firstVendorOptionField =  By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");
    private final By purchaseBillNumberfield=By.id("purchase_invoice_no");
    
    private final By entrydateField=By.xpath("//label[@title='Entry date']//following-sibling::div/div/div/div/input");
    private final By billDateField=By.xpath("//label[@title='Invoice Date']//following-sibling::div/div/div/div/input");

    private final By deliveryDateField=By.xpath("//label[@title='Expected Delivery Date']//following-sibling::div/div/div/div/input");
    private final By referenceNumberField = By.id("reference_number");
    private final By searchPaymentTermsField=By.xpath("//label[contains(text(),'Payment Terms')]/following-sibling::input[@type='text']");	    
    private final By selectPaymentTermsField = By.xpath("//ul/li[1]/span");
//    private final By searchTransactionTypeField=By.xpath("//label[contains(text(),'Transaction Type')]/following-sibling::input[@type='text']");	    
//    private final By selectTransactionTypeField = By.xpath("//ul/li[1]/span");
    private final By discountLevelTypeDropdownField = By.xpath("//a[text()='Discount Type']//parent::div/div/div/div/button");
    private final By selectItemLevelDiscountField = By.xpath("//a[text()='Discount Type']//parent::div/div/div/div/ul/li[1]");
	
	
    private final By searchPriceListField=By.xpath("//input[@placeholder='Price List' and @type='text']");	    
    private final By selectPriceListField = By.xpath("//ul/li[1]"); 
    private final By searchTaxField=By.xpath("//input[@placeholder='Tax' and @type='text']");
    private final By selectTaxField = By.xpath("//ul/li[1]");
    
    
    // ──────────────── Item Fields ────────────────
    private final By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
    private final By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
    private final String itemSelectField = "//ul[1]/li[%d]"; // dynamic

    // ──────────────── Notes and Terms ────────────────
    private final By noteField = By.id("notes");
//    private final By termsField = By.id("terms_and_conditions");

 // ──────────────── Transaction level Discount ────────────────
    private final By addDiscountLinkField=By.xpath("//a[text()=' Add Discount']");
    private final By transactionLevelDiscountField=By.xpath("//input[@tabindex=0 and @type='number']");
    private final By searchDiscountAccountField=By.xpath("//div[text()='Discount Account']//following-sibling::div/div/div/div/button/div/input");
    private final By selectDiscountAccountField=By.xpath("//div[text()='Discount Account']//following-sibling::div/div/div/ul/div/li[3]");

    // ──────────────── Action Buttons ────────────────
    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
    private final By saveAsArrowBtnField=By.xpath("//div[@class=' bg-accent w-44 flex items-center justify-center rounded-[4px] h-9 relative text-white shadow-sm']/div/div/div[1]");
    private final By saveAndApproveBtnField=By.xpath("//button[@name='s_open']/div[text()='Save And Approve']"); 
    private final By saveAndSubmitBtnField=By.xpath("//button[@name='s_submit']");
    
    private final By billNoinListField = By.xpath("(//tr/td[3])[1]/div/div");
    
    // ──────────────── Actions ────────────────

    /** Navigate to Create Estimate Page */
    public void navigateToNewPurchaseBill() {
    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(purchaseMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(purchaseBillMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(newPurchaseBillButtonField)).click();
    }

    public String purchaseBillNumber() {
    	String BiNO=wait.until(ExpectedConditions.visibilityOfElementLocated(purchaseBillNumberfield)).getAttribute("value");
    	//System.out.println("creat Bill :"+BiNO);
    	return BiNO;
    }
    /** Fill in header details (customer, ref no, subject) 
     * @throws Exception */
    public int fillPurchaseBillHeader(
    		String vendorName, 
    		String entryDate, 
    		String billDate,
    		String expectedDeliveryDate,
    		String referenceNumber,
    		String paymentTerms,
    		String taxType,
    		String priceList,
    		String discountLevel) throws Exception {
//    	System.out.println("\n Vendor : "+vendorName+"\n Ref No: "+referenceNumber+"\n Bill Date : "+
//    		billDate+"\n Payment Terms : "+paymentTerms+"\n Entry date  : "+entryDate+"\n Expected delivery date  : "+
//    		expectedDeliveryDate);
    	int discLevel = DiscountUtils.resolveDiscountLevel(discountLevel);
    	String ctime=Utilities.dateTime(); 
    	ctime=ctime.split(" ")[1];
    	referenceNumber=referenceNumber+":"+ctime;
    	//System.out.println(refNo);
    	if (Utilities.isNotEmpty(vendorName))
    	{	
			Utilities.selectCustomer(driver,vendorDropdownField, vendorName);
    	}
    	// WaitUtils.waitForUi(driver);
   
    	if (Utilities.isNotEmpty(entryDate)) {
    		 wait.until(ExpectedConditions.visibilityOfElementLocated(entrydateField));
    		 Utilities.selectDateByValue(driver,entrydateField ,entryDate);
    	  }
    	if (Utilities.isNotEmpty(billDate)) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(billDateField));
    		Utilities.selectDateByValue(driver,billDateField ,billDate);
    	}
       
    	if (Utilities.isNotEmpty(referenceNumber))  {
    		driver.findElement(referenceNumberField).sendKeys(referenceNumber);
        }
    	if (Utilities.isNotEmpty(paymentTerms))  {
    		Utilities.selectIfListed(driver, searchPaymentTermsField, selectPaymentTermsField,paymentTerms);
    	}
    	if (Utilities.isNotEmpty(expectedDeliveryDate))  {
        	//Utilities.waitForPageLoad(driver);
        	wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryDateField));
        	Utilities.selectDateByValue(driver,deliveryDateField ,expectedDeliveryDate);
    	}
        WaitUtils.waitForUi(driver);
        
//         if (discountLevel == null || discountLevel.trim().isEmpty()|| "At transaction level".equalsIgnoreCase(discountLevel)) 
//	        {
//	        	//System.out.println("Transaction level discount");
//	        	discLevel=0; 	
//	        }
//	        else if ("At Line Item Level".equalsIgnoreCase(discountLevel)) {
         if(discLevel==1) {
	        	//System.out.println("at line item level select. "+discountLevel);
	       	        	
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(discountLevelTypeDropdownField)).click();
	        	wait.until(ExpectedConditions.invisibilityOfElementLocated(
	        	By.cssSelector(".loading, .spinner")));		        	
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(selectItemLevelDiscountField));
	        	wait.until(ExpectedConditions.visibilityOfElementLocated(selectItemLevelDiscountField)).click();			        		  
	     }
         if (Utilities.isNotEmpty(priceList))  {
	    		Utilities.selectIfListed(driver, searchPriceListField, selectPriceListField,priceList);
	     }
	     if (taxType == null || taxType.trim().isEmpty()|| "Exclusive".equalsIgnoreCase(taxType)){ 
	        
	        	//System.out.println("Default Tax Exclusive");
	     }
	     else if ("Inclusive".equalsIgnoreCase(taxType)) {
	    		Utilities.selectIfListed(driver, searchTaxField, selectTaxField,taxType);
	     }
	        WaitUtils.waitForUi(driver);
	      if(driver.findElement(vendorDropdownField).getAttribute("value").isEmpty()) {
	            Utilities.selectCustomer(driver, vendorDropdownField, vendorName);
	            System.out.println("Vendor first time not loaded");
	        }
	         return discLevel;
    }    
    /** Add multiple items dynamically  * @throws InterruptedException */  
    public void addItems(
    		String[] itemNames, 
    		String[] itemQtys,
    		String[] discType,
    		String[] discount,
    		int discLevel) throws InterruptedException {    	   	
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement itemdetail  = driver.findElement(itemDetailsField);
	    js.executeScript("arguments[0].scrollIntoView();",itemdetail);  
		driver.findElement(itemDetailsField).click();  //#####  
		Thread.sleep(500);	
        for (int i = 0; i < itemNames.length; i++) {
        	//System.out.println("Item Name: "+itemNames[i]+" --- Item Qty : "+itemQtys[i]+"-- disc Tpe"+discType[i]+"-- Discount --"+discount[i]);
            wait.until(ExpectedConditions.elementToBeClickable(itemListField)).click();
            driver.findElement(itemListField).sendKeys(itemNames[i]);
            Thread.sleep(500);
            // Select item dynamically
            By selectItem = By.xpath(String.format(itemSelectField, 1));
            wait.until(ExpectedConditions.elementToBeClickable(selectItem)).click();
            Thread.sleep(200);
            // Fill item quantity
            WebElement qtyField = driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[5]//input"));
            qtyField.clear();
            qtyField.sendKeys(itemQtys[i]);
            //WaitUtils.waitForUi(driver);
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
    public void addNotesAndTerms(String note, String terms) {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement notes  = driver.findElement(noteField);
  	    js.executeScript("arguments[0].scrollIntoView();",notes);
    	if (Utilities.isNotEmpty(note))  {
            driver.findElement(noteField).sendKeys(note);
        }   
    }
    /** Save the Purchase Bill */
    public void saveAsMethod(String saveAs) {
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
	   		// wait.until(ExpectedConditions.elementToBeClickable(saveAsArrowBtnField)).click();
	   		 wait.until(ExpectedConditions.elementToBeClickable(saveAndSubmitBtnField)).click();
	   	 }
     }
    /** Verify estimate saved by checking list */
    public boolean verifyPurchaseBillCreated(String expectedBillNo) {
    	int attempts = 0;
        while (attempts < 3) {
        	try {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(billNoinListField));
	        String actualBillNo = Utilities.getTextWithRetry(driver,billNoinListField);
	        //System.out.println("actual SI NO: "+actualInvoiceNo);
	        return actualBillNo.equalsIgnoreCase(expectedBillNo);
	       	}
	    	catch(TimeoutException e) {
	    		attempts++;
	    	}
        }
        throw new RuntimeException("Element remained stale after retries");
    }
}

