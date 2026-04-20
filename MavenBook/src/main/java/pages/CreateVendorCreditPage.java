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

public class CreateVendorCreditPage {
	private final WebDriver driver;
    private final WebDriverWait wait;
    public CreateVendorCreditPage(WebDriver driver) {
		this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    // ──────────────── Navigation Elements ────────────────
  
    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
    private final By purchaseMenuField = By.xpath("//a[contains(text(),'Purchase')]");
    private final By vendorCreditMenuField = By.xpath("//span[contains(text(),'Vendor Credits')]");
    private final By newVendorCreditButtonField = By.xpath("//button/p[contains(text(),'new')]");

    // ──────────────── Header / Customer Fields ────────────────
    private final By vendorDropdownField = By.xpath("//input[@placeholder='Select Vendor Name']");
    private final By firstVendorOptionField =  By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");  private final By salesReturnNumberField=By.id("sales_return_no");
    private final By vendorCreditNumberField=By.id("vendor_credit_no");
    
    private final By vendorCreditDateField=By.xpath("//label[@title='Vendor Credit Date']//following-sibling::div/div/div/div/input");
    private final By orderNumberField = By.id("order_number");
    
    private final By searchBillField=By.xpath("//label[contains(text(),'Bill')]/following-sibling::input[@type='text']");	    
    private final By selectBillField = By.xpath("//ul/li[1]/span");
    private final By taxField=By.xpath("//a[text()='Tax']");    
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
     * @throws Exception */
    public void fillVendorCreditHeader(
    		String vendorName, 
    		String billNo, 
    		String vendorCreditDate,
    		String orderNo,
    		String taxType,
    		String priceList) throws Exception {
    	//System.out.println(vendorName+" : "+billNo+" : "+vendorCreditDate+" : "+orderNo);
    	
    	if (Utilities.isNotEmpty(vendorName)) {	
			Utilities.selectCustomer(driver,vendorDropdownField, vendorName);
    	}
        if (Utilities.isNotEmpty(vendorCreditDate)) {
        	wait.until(ExpectedConditions.visibilityOfElementLocated(vendorCreditDateField));
   		 Utilities.selectDateByValue(driver,vendorCreditDateField ,vendorCreditDate);
        }
        else {
        	//Thread.sleep(200);
        	//WebElement srdate = driver.findElement(salesReturnDateField);
        	//wait.until(ExpectedConditions.attributeToBeNotEmpty(srdate, todayStr));
        }
        Thread.sleep(2000);
        if (Utilities.isNotEmpty(billNo)) {
    		Utilities.selectIfListed(driver, searchBillField, selectBillField,billNo);
    	}
        if(Utilities.isNotEmpty(orderNo)){
        	driver.findElement(orderNumberField).sendKeys(orderNo);
        }
       // Thread.sleep(2000);
  
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
         if(driver.findElement(vendorDropdownField).getAttribute("value").isEmpty()) {
	            Utilities.selectCustomer(driver, vendorDropdownField, vendorName);
	            System.out.println("Vendor first time not loaded");
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
            		
	            		WebElement discountField=driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "][1]/td[6]/div[1]/input"));
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
	                System.out.println("Discount not applied (type or value missing)");
	            }
        }
    }
    /** Add Transaction level discount */ 
   
    /** Add optional notes */
    public void addNotes(String customerNote) {
      JavascriptExecutor js = (JavascriptExecutor) driver;
      WebElement notes  = driver.findElement(noteField);
	    js.executeScript("arguments[0].scrollIntoView();",notes); 
        if (Utilities.isNotEmpty(customerNote)) {
        	driver.findElement(noteField).clear();
            driver.findElement(noteField).sendKeys(customerNote);
        }
        
    }
    /** Save the Vendor Credit  */
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
    public boolean verifyVendorCreditCreated(String expectedVendorCreditNo) {
    	int attempts = 0;
        while (attempts < 3) {
	    	try {
	    	//System.out.println("expected VC number : "+expectedVendorCreditNo);
	        wait.until(ExpectedConditions.visibilityOfElementLocated(vendorCreditNoinListField));
	        String actualVendorCreditNo = Utilities.getTextWithRetry(driver,vendorCreditNoinListField);
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
