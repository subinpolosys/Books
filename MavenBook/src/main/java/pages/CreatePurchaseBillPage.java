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

public class CreatePurchaseBillPage {
	private final WebDriver driver;
    private final WebDriverWait wait;
    public CreatePurchaseBillPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ──────────────── Navigation Elements ────────────────
  
    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
    private final By purchaseMenuField = By.xpath("//div[@title='purchase']/a[contains(text(),'Purchase')]");
    private final By purchaseBillMenuField = By.xpath("//div[@title='bills']/span[contains(text(),'Purchase Bills')]");
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
    
    
    // ──────────────── Item Fields ────────────────
    private final By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
    private final By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
    private final String itemSelectField = "//ul[1]/li[%d]"; // dynamic

    // ──────────────── Notes and Terms ────────────────
    private final By noteField = By.id("notes");
//    private final By termsField = By.id("terms_and_conditions");

    // ──────────────── Action Buttons ────────────────
    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
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
     * @throws InterruptedException */
    public void fillPurchaseBillHeader(String vendorName, String entryDate, String billDate,String expectedDeliveryDate,String referenceNumber,String paymentTerms) throws InterruptedException {
    	//System.out.println(vendorName+" : "+refNo+" : "+subject+" : "+salesPerson+" : "+billDate+" :  "+paymentTerms+" : "+transactionType);
    	String ctime=Utilities.dateTime(); 
    	referenceNumber=referenceNumber+":"+ctime.split(" ")[1];
    	//System.out.println(refNo);
    	if (vendorName != null && !vendorName  .trim().isEmpty()) {	
			Utilities.selectCustomer(driver,vendorDropdownField, vendorName);
    	}
       // driver.findElement(orderNumberField).sendKeys(orderNo);    
    	  if (entryDate != null && !entryDate.trim().isEmpty()) {
    		  wait.until(ExpectedConditions.visibilityOfElementLocated(entrydateField)).sendKeys(entryDate);
    	  }
        if (billDate != null && !billDate.trim().isEmpty()) {
        	//Thread.sleep(3000);
        	//Utilities.waitForPageLoad(driver);
    		wait.until(ExpectedConditions.visibilityOfElementLocated(billDateField)).sendKeys(billDate);
    	}
       
        if(referenceNumber!= null && !referenceNumber.trim().isEmpty()) {
        driver.findElement(referenceNumberField).sendKeys(referenceNumber);
        }
        
        if (paymentTerms!= null && !paymentTerms.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchPaymentTermsField, selectPaymentTermsField,paymentTerms);
    	}
         
        if (expectedDeliveryDate != null && !expectedDeliveryDate.trim().isEmpty()) {
//        	Thread.sleep(3000);
//        	Utilities.waitForPageLoad(driver);
        	wait.until(ExpectedConditions.visibilityOfElementLocated(deliveryDateField)).sendKeys(expectedDeliveryDate);
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
		Thread.sleep(500);	
        for (int i = 0; i < itemNames.length; i++) {
        	System.out.println("Item Name: "+itemNames[i]+" --- Item Qty : "+itemQtys[i]);
            wait.until(ExpectedConditions.elementToBeClickable(itemListField)).click();
            driver.findElement(itemListField).sendKeys(itemNames[i]);
            Thread.sleep(1000);
            // Select item dynamically
            By selectItem = By.xpath(String.format(itemSelectField, 1));
            wait.until(ExpectedConditions.elementToBeClickable(selectItem)).click();
            Thread.sleep(500);
            // Fill item quantity
            WebElement qtyField = driver.findElement(By.xpath("//tbody/tr[" + (i + 1) + "]/td[5]//input"));
            qtyField.clear();
            qtyField.sendKeys(itemQtys[i]);
            Thread.sleep(500);
        }
    }
    /** Add optional notes and terms */
    public void addNotesAndTerms(String note, String terms) {
        if (note != null && !note.isEmpty()) {
            driver.findElement(noteField).sendKeys(note);
        }   
    }
    /** Save the estimate as draft */
    public void saveAsDraft() {
        wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
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

