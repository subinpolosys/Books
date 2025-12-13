package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

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
	    private final By rIDateField= By.id("date");
	    private final By retainerInvoiceNumberfield=By.id("retainer_invoice_no");	    	    
	    private final By referenceNumberField=By.id("reference_number");
	    private final By descriptionField=By.xpath("//tbody/tr[1]/td[1]/div/textarea");
	    private final By amountField=By.xpath("//tbody/tr[1]/td[2]/div[1]/input[@type='number']");		
	    private final By customerNoteField=By.id("customer_notes");
	    private final By termsField=By.id("terms_and_conditions");  
	    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
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
	    	System.out.println(RIDate);
	    	if (customerName != null && !customerName  .trim().isEmpty()) {
	    		try {
	    		wait.until(ExpectedConditions.elementToBeClickable(customerDropdownField)).click();
		        driver.findElement(customerDropdownField).sendKeys(customerName);
		        
		        List<WebElement> options = driver.findElements(firstCustomerOptionField); // adjust locator as needed
		        boolean found = false;
	            for (WebElement option : options) {
	                if (option.getText().equalsIgnoreCase(customerName)) {
	                    option.click();
	                    found = true;
	                    break;
	                }
	            }
	            if (!found) {
	                Assert.fail("Customer name '" + customerName + "' not found in the dropdown list.");
	            }	        
		       // wait.until(ExpectedConditions.elementToBeClickable(firstCustomerOptionField)).click();
	    		}catch(Exception e) {
	    			 Assert.fail("Error selecting customer: " + e.getMessage());
	    		}
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
	    	List<By> requiredFields = Arrays.asList(rIDateField,amountField,descriptionField);
	    	wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
	        Thread.sleep(200);
	        for (By fieldLocator : requiredFields) {
	            WebElement ele = driver.findElement(fieldLocator);
	            String validationMsg = Utilities.getValidationMessage(driver, ele);

	            if (validationMsg != null && !validationMsg.isEmpty()) {
	                System.out.println("⚠ Validation triggered on field: " + fieldLocator + " → " + validationMsg);
	                if (fieldLocator.equals(rIDateField)) {
	                    ele.clear();
	                    ele.sendKeys(RIDate);
	                } else if (fieldLocator.equals(amountField)) {
	                    ele.clear();
	                    ele.sendKeys(amount); 
	                } else if (fieldLocator.equals(descriptionField)) {
	                    ele.clear();
	                    ele.sendKeys(deascription);
	                }

	                Thread.sleep(300);

	                // Click Save again after fixing
	                wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
	                System.out.println("✔ Field corrected and Save clicked again");
	            }
	        }
	    }
	
	    public boolean verifyRetainerInvoiceCreated(String expectedRetainerInvoiceNo) {
	    	try {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(retainerInvoiceNoinListField));
	        String actualRetainerInvoiceNo = driver.findElement(retainerInvoiceNoinListField).getText();
	        return actualRetainerInvoiceNo.equalsIgnoreCase(expectedRetainerInvoiceNo);
	    	}
	    	catch(TimeoutException e) {
	    		return false;
	    	} 
	    }
}
	


