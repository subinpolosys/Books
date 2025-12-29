package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import utils.Utilities;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
public class CreateEstimatePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    public CreateEstimatePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    // ──────────────── Navigation Elements ────────────────
    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
    private final By salesMenuField = By.xpath("//div[@title='sales']/a[contains(text(),'Sales')]");
    private final By estimateMenuField = By.xpath("//div[@title='estimates']/span[contains(text(),'Estimates')]");
    private final By newEstimateButtonField = By.xpath("//button/p[contains(text(),'new')]");

    // ──────────────── Header / Customer Fields ────────────────
    private final By customerDropdownField = By.xpath("//input[@placeholder='Select Customer Name']");
    private final By firstCustomerOptionField =  By.xpath("//li[@role='option'][1]/div/div/h1");
    private final By estimateNumberfield=By.id("sales_estimate_no");
    private final By referenceNumberField = By.id("reference");
    
    private final By estimateDateField=By.xpath("//label[@title='Estimate Date']//following-sibling::div/div/div/div/input");
    private final By estimateDateLabelField=By.xpath("//label[@title='Estimate Date']");
    private final By expirydateField=By.xpath("//label[@title='Expiry Date']//following-sibling::div/div/div/div/input");
    private final By expirydateLabelField=By.xpath("//label[@title='Expiry Date']");
    private final By subjectField = By.id("subject");
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
    private final By estimateNoinListField = By.xpath("(//tr/td[3])[1]/div/div");

    // ──────────────── Actions ────────────────

    /** Navigate to Create Estimate Page */
    public void navigateToNewEstimate() {
    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(salesMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(estimateMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(newEstimateButtonField)).click();
    }

    public String estimatenumber() {
    	String ESTNO=wait.until(ExpectedConditions.visibilityOfElementLocated(estimateNumberfield)).getAttribute("value");
    	return ESTNO;
    }
   
    public void fillEstimateHeader(String customerName, String referenceNo, String subject,String salesPerson,String estDate,String expiryDate,String taxType,String priceList) throws InterruptedException {
    	System.out.println(customerName+" : "+referenceNo+" : "+subject+" : "+salesPerson+" : "+estDate+" : "+expiryDate +" : "+taxType);
    	
    	if (customerName != null && !customerName  .trim().isEmpty()) {	
    			Utilities.selectCustomer(driver,customerDropdownField, customerName);
    	}
    	
        if (referenceNo != null && !referenceNo .trim().isEmpty()) {
        	String ctime=Utilities.dateTime(); 
        	referenceNo=referenceNo+":"+ctime.split(" ")[1];
        driver.findElement(referenceNumberField).sendKeys(referenceNo);
        }
        Thread.sleep(200);
        if (estDate != null && !estDate .trim().isEmpty()) {
        	
        	wait.until(ExpectedConditions.visibilityOfElementLocated(estimateDateField)).sendKeys(estDate);       
        	driver.findElement(expirydateLabelField).click();
        	
        }
        Thread.sleep(2000);
        if (expiryDate != null && !expiryDate.trim().isEmpty()) {
        	//System.out.println("Expiry DAte : "+expiryDate);
        	
        	wait.until(ExpectedConditions.visibilityOfElementLocated(expirydateField)).sendKeys(expiryDate);
        	driver.findElement(estimateDateLabelField).click();
        }
        Thread.sleep(2000);
        if (salesPerson != null && !salesPerson .trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchSalesPersonField, selectSalesPersonField,salesPerson);
    	}   
        Thread.sleep(200);
        if (priceList != null && !priceList .trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchPriceListField, selectPriceListField,priceList);
    	}
        driver.findElement(searchSalesPersonField).sendKeys(Keys.ESCAPE); 
        JavascriptExecutor sub = (JavascriptExecutor) driver;
        WebElement subj  = driver.findElement(subjectField);
	    sub.executeScript("arguments[0].scrollIntoView();",subj);  
	    driver.findElement(subjectField).click();
        driver.findElement(subjectField).sendKeys(subject);
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
        	System.out.println(itemNames[i]+" : "+itemQtys[i]+" : "+discType[i]+" : "+discount[i]);
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

            	//tbody/tr[1]/td[6]/div[1]/div/div/div/ul/li[1]
                

            } else {
                // One or both values missing → no discount
                System.out.println("Discount not applied (type or value missing)");
            }
            
            
        }
    }
    /** Add optional notes and terms */
    public void addNotesAndTerms(String customerNote, String terms) {
        if (customerNote != null && !customerNote.isEmpty()) {
            driver.findElement(customerNoteField).sendKeys(customerNote);
        }
        if (terms != null && !terms.isEmpty()) {
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
    public boolean verifyEstimateCreated(String expectedstimateNo) {
    	try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(estimateNoinListField));
//      String actualEstimateNo = driver.findElement(estimateNoinListField).getText();
        String actualEstimateNo = Utilities.getTextWithRetry(driver,estimateNoinListField);
        return actualEstimateNo.equalsIgnoreCase(expectedstimateNo);
    	}
    	catch(TimeoutException e) {
    		return false;
    	}
    }

}

