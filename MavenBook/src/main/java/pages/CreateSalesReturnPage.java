package pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

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
    
    private final By salesReturnDateField=By.id("date");
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
    
    // ──────────────── Item Fields ────────────────
    private final By itemDetailsField = By.xpath("//table[@class=' w-full ']/thead/tr[1]/td[1]");
    private final By itemListField = By.xpath("//input[@placeholder='Type or click to add items']");
    private final String itemSelectField = "//ul[1]/li[%d]"; // dynamic

    // ──────────────── Notes and Terms ────────────────
    private final By customerNoteField = By.id("notes");
    private final By termsField = By.id("terms_and_conditions");

    // ──────────────── Action Buttons ────────────────
    private final By saveAsDraftButtonField = By.xpath("//button[contains(text(),'Save as Draft')]");
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
    public void fillSalesReturnHeader(String customerName, String invoNo, String reason,String salesPerson,String salesReturnDate,String referenceNo,String transactionType) throws InterruptedException {
    	//System.out.println(customerName+" : "+invoNo+" : "+salesPerson+" : "+salesReturnDate+" : "+reason+" : "+referenceNo+" : "+transactionType);
    	//System.out.println("Return Date:"+salesReturnDate);
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
        if (salesReturnDate != null && !salesReturnDate.trim().isEmpty()) {
        	WebElement srdate = driver.findElement(salesReturnDateField);
	        	wait.until(ExpectedConditions.attributeToBeNotEmpty(srdate, "value"));
	        	//String dateValue = driver.findElement(salesReturnDateField).getAttribute("value"); //
		    	//System.out.println("Date value: " + dateValue);  
	        	
		    	srdate.clear();
		    	srdate.sendKeys(salesReturnDate);
		    	//srdate.sendKeys(Keys.TAB);
        	//wait.until(ExpectedConditions.visibilityOfElementLocated(salesReturnDateField)).sendKeys(salesReturnDate);
        	//srdate.sendKeys(Keys.TAB);
    		
        	//String dateValue1 = driver.findElement(salesReturnDateField).getAttribute("value"); //
	    	//System.out.println("Date value: " + dateValue1);
	    	//===============
        	
    	}
        else {
        	Thread.sleep(200);
        	//WebElement srdate = driver.findElement(salesReturnDateField);
        	//wait.until(ExpectedConditions.attributeToBeNotEmpty(srdate, todayStr));
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
        	driver.findElement(customerNoteField).clear();
            driver.findElement(customerNoteField).sendKeys(customerNote);
        }
        if (terms != null && !terms.isEmpty()) {
        	driver.findElement(termsField).clear();
            driver.findElement(termsField).sendKeys(terms);
        }
    }
    /** Save the estimate as draft */
    public void saveAsDraft() {
        wait.until(ExpectedConditions.elementToBeClickable(saveAsDraftButtonField)).click();
    }
    /** Verify estimate saved by checking list */
    public boolean verifySalesReturnCreated(String expectedSalesReturnNo) {
    	//System.out.println("expected SR number : "+expectedSalesReturnNo);
        wait.until(ExpectedConditions.visibilityOfElementLocated(salesReturnNoinListField));
        String actualSalesReturnNo = driver.findElement(salesReturnNoinListField).getText();
        //System.out.println("Actual SR number :"+actualSalesReturnNo);
        return actualSalesReturnNo.equalsIgnoreCase(expectedSalesReturnNo);
    }
}
