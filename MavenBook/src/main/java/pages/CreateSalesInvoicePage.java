package pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utils.Utilities;

public class CreateSalesInvoicePage {
	private final WebDriver driver;
    private final WebDriverWait wait;

    public CreateSalesInvoicePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ──────────────── Navigation Elements ────────────────
  
    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
    private final By salesMenuField = By.xpath("//div[@title='sales']/a[contains(text(),'Sales')]");
    private final By salesInvoiceMenuField = By.xpath("//div[@title='invoices']/span[contains(text(),'Invoices')]");
    private final By newSalesInvoiceButtonField = By.xpath("//button/p[contains(text(),'new')]");

    // ──────────────── Header / Customer Fields ────────────────
    private final By customerDropdownField = By.xpath("//input[@placeholder='Select Customer Name']");
    private final By firstCustomerOptionField =  By.xpath("//div[@class='flex flex-col gap-4 px-10 py-5']/div/div/div/div/div/div/div/ul/li[1]/div/div/h1");
    private final By salesInvoiceNumberfield=By.id("sales_invoice_no");
    private final By orderNumberField = By.id("order_number");
    
    private final By invoiceDateField=By.id("date");
    private final By subjectField = By.id("subject");
    private final By searchSalesPersonField=By.xpath("//label[contains(text(),'Sales Person')]/following-sibling::input[@type='text']");	    
    private final By selectSalesPersonField = By.xpath("//ul/li[1]/span");
    
    private final By supplyDateField=By.id("supply_date");
    
    private final By searchPaymentTermsField=By.xpath("//label[contains(text(),'Payment Terms')]/following-sibling::input[@type='text']");	    
    private final By selectPaymentTermsField = By.xpath("//ul/li[1]/span");
    private final By searchTransactionTypeField=By.xpath("//label[contains(text(),'Transaction Type')]/following-sibling::input[@type='text']");	    
    private final By selectTransactionTypeField = By.xpath("//ul/li[1]/span");
    
    
    // ──────────────── Item Fields ────────────────
    private final By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
    private final By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
    private final String itemSelectField = "//ul[1]/li[%d]"; // dynamic

    // ──────────────── Notes and Terms ────────────────
    private final By customerNoteField = By.id("notes");
    private final By termsField = By.id("terms_and_conditions");

    // ──────────────── Action Buttons ────────────────
    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
    private final By invoiceNoinListField = By.xpath("(//tr/td[3])[1]/div/div");

    // ──────────────── Actions ────────────────

    /** Navigate to Create Estimate Page */
    public void navigateToNewSalesInvoice() {
    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(salesMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(salesInvoiceMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(newSalesInvoiceButtonField)).click();
    }

    public String salesInvoiceNumber() {
    	String SONO=wait.until(ExpectedConditions.visibilityOfElementLocated(salesInvoiceNumberfield)).getAttribute("value");
    	System.out.println("creat Invoice ID :"+SONO);
    	return SONO;
    }
    /** Fill in header details (customer, ref no, subject) 
     * @throws InterruptedException */
    public void fillSalesInvoiceHeader(String customerName, String orderNo, String subject,String salesPerson,String invDate,String paymentTerms,String supDate,String transactionType) throws InterruptedException {
    	//System.out.println(customerName+" : "+orderNo+" : "+subject+" : "+salesPerson+" : "+invDate+" : "+supDate+" : "+paymentTerms+" : "+transactionType);
    	Utilities.waitForPageToLoad(driver);
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

        driver.findElement(orderNumberField).sendKeys(orderNo);
        
        JavascriptExecutor sp1 = (JavascriptExecutor) driver;
        WebElement invD  = driver.findElement(invoiceDateField);
	    sp1.executeScript("arguments[0].scrollIntoView();",invD); 
        
        if (invDate != null && !invDate.trim().isEmpty()) {
        	//System.out.println("inv Date:");
        	Thread.sleep(3000);
        	Utilities.waitForPageLoad(driver);
    		wait.until(ExpectedConditions.visibilityOfElementLocated(invoiceDateField)).sendKeys(invDate);
    	}
        
        if (paymentTerms!= null && !paymentTerms.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchPaymentTermsField, selectPaymentTermsField,paymentTerms);
    	}
        
         
        if (supDate != null && !supDate.trim().isEmpty()) {
        	//System.out.println("sup Date:");
        	Thread.sleep(2000);
        	Utilities.waitForPageLoad(driver);
        	wait.until(ExpectedConditions.visibilityOfElementLocated(supplyDateField)).sendKeys(supDate);
    	}
        if (transactionType!= null && !transactionType.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchTransactionTypeField, selectTransactionTypeField,transactionType);
    	}
        
        JavascriptExecutor sp = (JavascriptExecutor) driver;
        WebElement salesP  = driver.findElement(searchSalesPersonField);
	    sp.executeScript("arguments[0].scrollIntoView();",salesP); 
        
        if (salesPerson  != null && !salesPerson  .trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchSalesPersonField, selectSalesPersonField,salesPerson);
    	}
        
        salesP.sendKeys(Keys.ESCAPE);
        if (subject != null && !subject .trim().isEmpty()) {
        driver.findElement(subjectField).click();
        driver.findElement(subjectField).sendKeys(subject);
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
    public void saveAsDraft() {
        wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
    }
    /** Verify estimate saved by checking list */
    public boolean verifySalesInvoiceCreated(String expectedInvoiceNo) {
    	try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(invoiceNoinListField));
        String actualInvoiceNo = driver.findElement(invoiceNoinListField).getText();
        //System.out.println("actual SI NO: "+actualInvoiceNo);
        return actualInvoiceNo.equalsIgnoreCase(expectedInvoiceNo);
    	}
    	catch(TimeoutException e) {
    		return false;
    	}
    }
}
