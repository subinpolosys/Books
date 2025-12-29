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

public class CreateSalesReturnPage {
	private final WebDriver driver;
    private final WebDriverWait wait;

    public CreateSalesReturnPage(WebDriver driver) {
		this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ──────────────── Navigation Elements ────────────────
  
    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
    private final By salesMenuField = By.xpath("//div[@title='sales']/a[contains(text(),'Sales')]");
    private final By salesReturnMenuField = By.xpath("//div[@title='sales return']/span[contains(text(),'Sales Return')]");
    private final By newSalesReturnButtonField = By.xpath("//button/p[contains(text(),'new')]");

    // ──────────────── Header / Customer Fields ────────────────
    private final By customerDropdownField = By.xpath("//input[@placeholder='Select Customer Name']");
    private final By firstCustomerOptionField =  By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");
    private final By salesReturnNumberField=By.id("sales_return_no");
    //private final By orderNumberField = By.id("order_number");
    
    private final By salesReturnDateField=By.xpath("//label[@title='Sales Return Date']//following-sibling::div/div/div/div/input");
    //private final By subjectField = By.id("subject");
    
    private final By searchInvoiceField=By.xpath("//label[contains(text(),'Invoices')]/following-sibling::input[@type='text']");	    
    private final By selectInvoiceField = By.xpath("//ul/li[1]/span");
    private final By searchSalesPersonLabelField=By.xpath("//label[contains(text(),'Sales Person')]");
    private final By searchSalesPersonField=By.xpath("//label[contains(text(),'Sales Person')]/following-sibling::input[@type='text']");	    
    private final By selectSalesPersonField = By.xpath("//ul/li[1]/span");
    
    //private final By supplyDateField=By.id("supply_date");
    
    //private final By searchPaymentTermsField=By.xpath("//label[contains(text(),'Payment Terms')]/following-sibling::input[@type='text']");	    
    //private final By selectPaymentTermsField = By.xpath("//ul/li[1]/span");
    private final By searchTransactionTypeField=By.xpath("//label[contains(text(),'Transaction Type')]/following-sibling::input[@type='text']");	    
    private final By selectTransactionTypeField = By.xpath("//ul/li[1]/span");
    private final By referenceField=By.id("reference");
    private final By referenceLabelField=By.xpath("//label[@title='Reference']");
    private final By reasonField=By.id("reason");
    private final By taxField=By.xpath("//a[text()='Tax']");
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
    private final By saveAsArrowBtnField=By.xpath("//div[@class=' bg-accent w-44 flex items-center justify-center rounded-[4px] h-9 relative text-white shadow-sm']/div/div/div[1]");
    private final By saveAndApproveBtnField=By.xpath("//button[@name='s_approve']/div[text()='Save And Approve']"); 
    private final By saveAndSubmitBtnField=By.xpath("//button[@name='s_submit']/div[text()='Save and Submit']");
    private final By salesReturnNoinListField = By.xpath("(//tr/td[3])[1]/div/div");

    // ──────────────── Actions ────────────────

    /** Navigate to Create Estimate Page 
     * @throws InterruptedException */
    public void navigateToNewSalesReturn(){
    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(salesMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(salesReturnMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(newSalesReturnButtonField)).click();
       
    }

    public String salesReturnNumber() {
    	String SRno=wait.until(ExpectedConditions.visibilityOfElementLocated(salesReturnNumberField)).getAttribute("value");
    	//System.out.println(SRno);
    	return SRno;
    	
    }
    /** Fill in header details (customer, ref no, subject) 
     * @throws InterruptedException */
    public void fillSalesReturnHeader(String customerName, String invoNo, String reason,String salesPerson,String salesReturnDate,String referenceNo,String transactionType,String taxType,String priceList) throws InterruptedException {
    	//System.out.println(customerName+" : "+invoNo+" : "+salesPerson+" : "+salesReturnDate+" : "+reason+" : "+referenceNo+" : "+transactionType);
    	//System.out.println("Return Date:"+salesReturnDate);
    	if (customerName != null && !customerName  .trim().isEmpty()) {	
			Utilities.selectCustomer(driver,customerDropdownField, customerName);
    	}
        if (salesReturnDate != null && !salesReturnDate.trim().isEmpty()) {
        	wait.until(ExpectedConditions.visibilityOfElementLocated(salesReturnDateField)).sendKeys(salesReturnDate);
    	}
        else {
        	Thread.sleep(200);
        	
        }
        Thread.sleep(2000);
        if (invoNo!= null && !invoNo.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchInvoiceField, selectInvoiceField,invoNo);
    	}
        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement tax  = driver.findElement(taxField);
	    js.executeScript("arguments[0].scrollIntoView();",tax); 
        if (salesPerson  != null && !salesPerson  .trim().isEmpty()) {
        	//wait.until(ExpectedConditions.attributeToBeNotEmpty(salesP, "value"));
    		Utilities.selectIfListed(driver, searchSalesPersonField, selectSalesPersonField,salesPerson);
    		driver.findElement(searchSalesPersonField).sendKeys(Keys.ESCAPE);
    	}
        
        Thread.sleep(100);
        if (transactionType!= null && !transactionType.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchTransactionTypeField, selectTransactionTypeField,transactionType);
    		driver.findElement(referenceLabelField).click();
    	}
        Thread.sleep(100);
        if(referenceNo!=null&&!referenceNo.trim().isEmpty()) {
        	String ctime=Utilities.dateTime(); 
        	referenceNo=referenceNo+":"+ctime.split(" ")[1];
        	driver.findElement(referenceField).sendKeys(referenceNo);
        }
        if(reason!=null&&!reason.trim().isEmpty()) {
        	driver.findElement(reasonField).sendKeys(reason);
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
         Thread.sleep(200);
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
        if (customerNote != null && !customerNote.isEmpty()) {
        	driver.findElement(customerNoteField).clear();
            driver.findElement(customerNoteField).sendKeys(customerNote);
        }
        if (terms != null && !terms.isEmpty()) {
        	driver.findElement(termsField).clear();
            driver.findElement(termsField).sendKeys(terms);
        }
    }
    /** Save the estimate as draft */
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
    public boolean verifySalesReturnCreated(String expectedSalesReturnNo) {
    	//System.out.println("expected SR number : "+expectedSalesReturnNo);
    	try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(salesReturnNoinListField));
        String actualSalesReturnNo = Utilities.getTextWithRetry(driver,salesReturnNoinListField);
        //System.out.println("Actual SR number :"+actualSalesReturnNo);
        return actualSalesReturnNo.equalsIgnoreCase(expectedSalesReturnNo);
    	}
    	catch(TimeoutException e) {
    		return false;
    	}
    }
}
