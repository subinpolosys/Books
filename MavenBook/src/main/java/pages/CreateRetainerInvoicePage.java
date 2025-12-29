package pages;

import java.time.Duration;
import java.util.Arrays;
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

public class CreateRetainerInvoicePage {

	private final WebDriver driver;
    private final WebDriverWait wait;
    public CreateRetainerInvoicePage(WebDriver driver) {
	 this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
	    // ──────────────── Navigation Elements ────────────────
    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
	    private final By salesMenuField = By.xpath("//div[@title='sales']/a[contains(text(),'Sales')]");
	    private final By retainerInvoiceMenuField = By.xpath("//div[@title='retainer invoice']/span[contains(text(),'Retainer Invoice')]");
	    private final By newRetainerInvoiceButtonField = By.xpath("//button/p[contains(text(),'new')]");

	    // ──────────────── Header / Customer Fields ────────────────
	    private final By customerDropdownField = By.xpath("//input[@placeholder='Select Customer Name']");
	    private final By firstCustomerOptionField =  By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");
	    private final By rIDateField= By.xpath("//label[@title='Retainer Invoice Date']//following-sibling::div/div/div/div/input");
	    private final By retainerInvoiceNumberfield=By.id("retainer_invoice_no");	    	    
	    private final By referenceNumberField=By.id("reference_number");
	    private final By descriptionField=By.xpath("//tbody/tr[1]/td[1]/div/textarea");
	    private final By amountField=By.xpath("//tbody/tr[1]/td[2]/div[1]/input[@type='number']");		
	    private final By customerNoteField=By.id("customer_notes");
	    private final By termsField=By.id("terms_and_conditions");  
	    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
	    private final By saveAsArrowBtnField=By.xpath("//div[@class=' bg-accent w-44 flex items-center justify-center rounded-[4px] h-9 relative text-white shadow-sm']/div/div/div[1]");
	    private final By saveAndApproveBtnField=By.xpath("//button[@name='s_approve']/div[text()='Save And Approve']"); 
	    private final By saveAndSubmitBtnField=By.xpath("//button[@name='s_submit']/div[text()='Save and Submit']");	   
	    private final By retainerInvoiceNoinListField = By.xpath("(//tr/td[3])[1]/div/div");
	
	    public void navigateToNewRetainerInvoice(){
	    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(salesMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(retainerInvoiceMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(newRetainerInvoiceButtonField)).click();
	        //Utilities.waitForPageLoad(driver);
	    }
	    public String retainerInvoiceNumber() {
	    	String RINO=wait.until(ExpectedConditions.visibilityOfElementLocated(retainerInvoiceNumberfield)).getAttribute("value");
	    	return RINO;
	    }
	    public void fillRetainerInvoice(String customerName, String RIDate,String referenceNo,String deascription,String amount,String cNote,String terms ) throws InterruptedException {
	    	//System.out.println(RIDate);
	    	if (customerName != null && !customerName  .trim().isEmpty()) {	
    			Utilities.selectCustomer(driver,customerDropdownField, customerName);
    	   }
	    	Thread.sleep(1000);
	    	 if (RIDate != null && !RIDate.trim().isEmpty()) {  
	    		 driver.findElement(rIDateField).sendKeys(RIDate);	
	    	 }
	    	 else {
	    	 }
	    	   Thread.sleep(200);
	    	if (referenceNo != null && !referenceNo .trim().isEmpty()) {  
	    		String ctime=Utilities.dateTime(); 
	        	referenceNo=referenceNo+":"+ctime.split(" ")[1];
	    		driver.findElement(referenceNumberField).sendKeys(referenceNo);
	    	}
	    	if ( deascription!= null && !deascription .trim().isEmpty()) {    
	    		driver.findElement(descriptionField).sendKeys(deascription);
	    	}   
	    	if ( amount!= null && !amount .trim().isEmpty()) {    
	    		driver.findElement(amountField).sendKeys(amount);
	    	}  
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement cnote  = driver.findElement(customerNoteField);
		    js.executeScript("arguments[0].scrollIntoView();",cnote); 
	    	
	    	if ( cNote!= null && !cNote .trim().isEmpty()) {      
	    		driver.findElement(customerNoteField).sendKeys(cNote);
	    	}
	    	if ( terms!= null && !terms .trim().isEmpty()) {  
	    		driver.findElement(termsField).sendKeys(terms);
	    	}
	    }
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
	    	//  List<By> requiredFields = Arrays.asList(rIDateField,amountField,descriptionField);
	    	//  wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
	        //  Thread.sleep(200);
	        //  for (By fieldLocator : requiredFields) {
	        //     WebElement ele = driver.findElement(fieldLocator);
	        //      String validationMsg = Utilities.getValidationMessage(driver, ele);
	        //      if (validationMsg != null && !validationMsg.isEmpty()) {
	        //      System.out.println("⚠ Validation triggered on field: " + fieldLocator + " → " + validationMsg);
	        //      if (fieldLocator.equals(rIDateField)) {
	        //      ele.clear();
	        //      ele.sendKeys(RIDate);
	        //      } else if (fieldLocator.equals(amountField)) {
	        //      ele.clear();
	        //      ele.sendKeys(amount); 
	        //      } else if (fieldLocator.equals(descriptionField)) {
	        //      ele.clear();
	        //      ele.sendKeys(deascription);
	        //      }
	        //      Thread.sleep(300);
	        //      Click Save again after fixing
	        //      wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
	        //      System.out.println("✔ Field corrected and Save clicked again");
	        //  }
	    
      public boolean verifyRetainerInvoiceCreated(String expectedRetainerInvoiceNo) {
	    	try {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(retainerInvoiceNoinListField));
	        String actualRetainerInvoiceNo = Utilities.getTextWithRetry(driver,retainerInvoiceNoinListField);
	        return actualRetainerInvoiceNo.equalsIgnoreCase(expectedRetainerInvoiceNo);
	    	}
	    	catch(TimeoutException e) {
	    		return false;
	    	} 
	    }
}
	


