package pages;

import java.math.BigDecimal;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Utilities;

public class CreateReceiptPage {
	 private final WebDriver driver;
	    private final WebDriverWait wait;

	    public CreateReceiptPage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    }
	 // ──────────────── Navigation Elements ────────────────
	    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
	    private final By accountMenuField = By.xpath("//a[contains(text(),'Accounts')]");
	    private final By receiptMenuField = By.xpath("//span[contains(text(),'Receipt')]");
	    private final By newReceiptButtonField = By.xpath("//button/p[contains(text(),'new')]");
	    
	 // ──────────────── Header / Customer Fields ────────────────
	   
	    private final By customerDropdownField = By.xpath("//input[@placeholder='Select Customer Name']");
	    private final By receiptDateField=By.xpath("//label[@title='Payment Date']//following-sibling::div/div/div/div/input"); 
	    private final By receiptNumberField=By.id("voucher_number");
	    private final By amountReceivedField=By.id("total_amount");
	    
	    private final By searchPaymentModeField=By.xpath("//label[contains(text(),'Payment Mode')]/following-sibling::input[@type='text']");	    
	    private final By selectPaymentModeField = By.xpath("//ul/li[1]/span");	    
	    private final By referenceNumberField =By.id("reference_number");	    
	    private final By searchDepositToAccountField=By.xpath("//label[contains(text(),'Deposit To Account')]/following-sibling::input[@type='text']");	    
	    //private final By selectDepositToAccountField = By.xpath("//ul/li[1]/span");	    
	    private final By bankChargesField =By.id("bank_charges");
	    
//	    private final By invoiceNoField=By.xpath("//tbody/tr[1]/td[2]");
//	    private final By invoiceBalanceField=By.xpath("//tbody/tr[1]/td[4]");
//	    private final By invoicePaymentField=By.xpath("//tbody/tr[1]/td[5]/input");
	    
	    private final By internalNoteField=By.id("notes");
	    private final By saveButton=By.name("default_submit");
	    private final By excessPaymentPopupOKField=By.xpath("//div[contains(@id,'headlessui-dialog-panel')]/div[2]/button[1]");
	    private final By ReceiptNoinField=By.xpath("//div[contains(text(),'Payment #')]//following-sibling::div");
	    private final By ReceiptNoinListField=By.xpath("(//tr/td[3])[1]/div/div");
	    
	    /** Navigate to Create Estimate Page */
	    public void navigateToNewReceipt() {
	    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(accountMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(receiptMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(newReceiptButtonField)).click();
	    }
	    public String receiptNumber() {
	    	String RVNO=wait.until(ExpectedConditions.visibilityOfElementLocated(receiptNumberField)).getAttribute("value");
	    	return RVNO;
	    }
	    public void fillReceiptHeader(
	    		String customerName, 
	    		String receiptDate,
	    		String amountReceived,
	    		String paymentmode,
	    		String referenceNo,
	    		String depositAccount,
	    		String bankCharge,
	    		String invoiceNo,
	    		String notes) throws Exception {
//	    	System.out.println(customerName + " " + receiptDate + " " + amountReceived + " " +
//	                   paymentmode + " " + referenceNo + " " + depositAccount + " " +
//	                   bankCharge + " " + invoiceNo + " " + notes);
	    	Utilities.waitForPageToLoad(driver);
	    	if (Utilities.isNotEmpty(customerName)) {	
				Utilities.selectCustomer(driver,customerDropdownField, customerName);
	    	}
	    	if (Utilities.isNotEmpty(receiptDate)) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(receiptDateField));
	    		Utilities.selectDateByValue(driver,receiptDateField,receiptDate);
	    		Thread.sleep(100);
	    	}
	    	if (Utilities.isNotEmpty(amountReceived)) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(amountReceivedField)).sendKeys(amountReceived);
	    		//Thread.sleep(100);
	    	}
	    	if (Utilities.isNotEmpty(paymentmode)) {
	    		
	    		Utilities.selectIfListed(driver, searchPaymentModeField, selectPaymentModeField,paymentmode);
	    		Thread.sleep(100);
	    	}
	    	if (Utilities.isNotEmpty(referenceNo)) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(referenceNumberField)).sendKeys(referenceNo);
	    	}
	    	if (Utilities.isNotEmpty(depositAccount)) {
	    		Utilities.selectHeadlessUIDropdownValue(driver, searchDepositToAccountField, depositAccount);
	    		//Utilities.selectIfListed(driver, searchDepositToAccountField, selectDepositToAccountField,depositAccount);
	    		Thread.sleep(100);	
	    	}
	    	if (Utilities.isNotEmpty(bankCharge)) {
	    		//System.out.println("Bank charge"+bankCharge);
	    		if (Double.parseDouble(bankCharge) < Double.parseDouble(amountReceived)) {
	    			wait.until(ExpectedConditions.visibilityOfElementLocated(bankChargesField)).sendKeys(bankCharge);
	    		}
	    		else
	    		{
	    			wait.until(ExpectedConditions.visibilityOfElementLocated(bankChargesField)).sendKeys("0");
	    		}
	    	}
	    	if(Utilities.isNotEmpty(invoiceNo)) {
	    		//System.out.println("Passed Invoice number:"+invoiceNo+" count:"+invoiceNo.length());
	    		int noOfInvoices=0;
	    		String invoiceDueAmount="";
	    		String invno="";
	    		try {
	    			noOfInvoices = driver.findElements(By.xpath("//tbody/tr")).size();
	    			if(noOfInvoices==0) {
	    			System.out.println("No Due");
	    			}	    			
	    			for(int i=1;i<=noOfInvoices;i++) {	    				
	    				invno=driver.findElement(By.xpath("//tbody/tr["+i+"]/td[2]")).getText();	    				
	    				if(invno.equalsIgnoreCase(invoiceNo)) {
	    					//System.out.println("Located Invoice number:"+invno+" count:"+invno.length());
	    					invoiceDueAmount=driver.findElement(By.xpath("//tbody/tr["+i+"]/td[4]")).getText();
	    					BigDecimal dueAmount = new BigDecimal(invoiceDueAmount);
	    					BigDecimal amount = new BigDecimal(amountReceived);
	    					if(dueAmount.compareTo(amount)<0) {
	    						driver.findElement(By.xpath("//tbody/tr["+i+"]/td[5]/input")).clear();
	    						driver.findElement(By.xpath("//tbody/tr["+i+"]/td[5]/input")).sendKeys(invoiceDueAmount);
	    						break;
	    					}
	    					else {
	    						driver.findElement(By.xpath("//tbody/tr["+i+"]/td[5]/input")).clear();
	    						driver.findElement(By.xpath("//tbody/tr["+i+"]/td[5]/input")).sendKeys(amountReceived);
	    						break;
	    					}
	    				}	
	    				if(i==noOfInvoices) {
	    					System.out.println("Invoice not Found");
	    				}
	    			}	
	    		}catch(Exception e) {
	    				System.out.println("No unpaid invoices associated with this customer.");
	    		}
	    	}
	    	if(Utilities.isNotEmpty(notes)) {
	    		JavascriptExecutor js = (JavascriptExecutor) driver;
				WebElement intNotes  = driver.findElement(internalNoteField);
			    js.executeScript("arguments[0].scrollIntoView();",intNotes);
			    driver.findElement(internalNoteField).sendKeys(notes);
	    	}
	    }
	    public void save() {
	    	WebDriverWait popupwait = new WebDriverWait(driver, Duration.ofSeconds(10));
	    	//boolean excessPaymentPopupHandled=false;
	    	wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
	    	try {
	    		WebElement popup = popupwait.until(ExpectedConditions.visibilityOfElementLocated(
	    				excessPaymentPopupOKField));
				    popup.click();
				    //excessPaymentPopupHandled = true;
				    System.out.println("Excess Payment");
				    // Optional: wait for popup to disappear
				    popupwait.until(ExpectedConditions.invisibilityOf(popup));
	    	}catch (TimeoutException e) {
			    // No popup appeared, proceed normally
	    	 }
	    }
	    public boolean verifyReceiptCreated(String expectedReceiptNo) {
	    	try {
	        wait.until(ExpectedConditions.visibilityOfElementLocated(ReceiptNoinField));
	        String actualSalesOrderNo = Utilities.getTextWithRetry(driver,ReceiptNoinField);
	        return actualSalesOrderNo.equalsIgnoreCase(expectedReceiptNo);
	    	}
	    	catch(Exception a) {
	    		try {
	    	        wait.until(ExpectedConditions.visibilityOfElementLocated(ReceiptNoinListField));
	    	        String actualSalesOrderNo = Utilities.getTextWithRetry(driver,ReceiptNoinListField);
	    	        return actualSalesOrderNo.equalsIgnoreCase(expectedReceiptNo);
	    	    	}
	    		catch(TimeoutException e) {	    		
	    		return false;
	    		}
	    	}
	    }     
}
