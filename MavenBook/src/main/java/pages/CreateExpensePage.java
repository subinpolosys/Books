package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.Utilities;

public class CreateExpensePage {
	 private final WebDriver driver;
	    private final WebDriverWait wait;

	    public CreateExpensePage(WebDriver driver) {
	        this.driver = driver;
	        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
	    }
	 // ──────────────── Navigation Elements ────────────────
	    private final By dashboardField=By.xpath("//a[text()='Dashboard']"); 
	    private final By purchaseMenuField = By.xpath("//a[contains(text(),'Purchase')]");
	    private final By expenseMenuField = By.xpath("//span[text()='Expenses']");
	    private final By newExpenseButtonField = By.xpath("//button/p[contains(text(),'new')]");
	    
	 // ──────────────── Header / Customer Fields ────────────────
	   
	    private final By vendorDropdownField = By.xpath("//input[@placeholder='Select Vendor']");
	    private final By expenseDateField=By.xpath("//label[@title='Date']//following-sibling::div/div/div/div/input"); 
	    private final By searchEmployeeField=By.xpath("//label[contains(text(),'Employee')]/following-sibling::input[@type='text']");	    
	    private final By selectEmployeeField = By.xpath("//ul/li[1]/span"); 
	    private final By searchExpenseAccountField=By.xpath("//label[text()='Expense Account']//following-sibling::input");
	    private final By selectExpenseAccountField=By.xpath("//ul/div/li[3]");

	    //private final By paymentNumberField=By.id("voucher_number");
	    private final By amountField=By.id("sub_total");
	    
	    private final By searchPaidThroughAccountField=By.xpath("//label[contains(text(),'Paid Through Account')]/following-sibling::input[@type='text']");	    
	    private final By selectPaidThroughAccountField = By.xpath("//ul/div/li[4]/span");	
	    
	    private final By searchCurrencyField=By.xpath("//label[contains(text(),'Currency')]/following-sibling::input[@type='text']");	    		  
	    private final By selectCurrencyField = By.xpath("//ul/li[1]/span[1]");
	    
	    private final By searchTaxtreatmentField=By.xpath("//label[contains(text(),'Tax Treatment')]/following-sibling::input[@type='text']");	    		  
	    private final By selectTaxtreatmentField = By.xpath("//ul/li[1]/span[1]");
	    
	    private final By searchTaxField=By.xpath("//label[contains(text(),'Tax')]/following-sibling::input[@type='text' and @placeholder='Select Tax']");	    		  
	    private final By selectTaxField = By.xpath("//ul/li[1]/span[1]");
	    
	    private final By selectInclusiveTaxField=By.xpath("//input[@id='inclusive']");
	    
	    private final By referenceNumberField =By.id("reference");	    
	   
	    private final By customerDropdownField = By.xpath("//input[@placeholder='Select customer']");
	    private final By firstCustomerOptionField =  By.xpath("//ul/li[1]");
	    
	    private final By billableField=By.xpath("//label[contains(text(),'Billable')]/following-sibling::input[@type='checkbox']");
	    private final By markupField=By.id("markup");
	    
	    private final By notesField=By.id("notes");
	    
	    private final By saveButton=By.name("default_submit");
	    
	    private final By saveAndNewButton=By.name("s_submit_and_new");
	    
//	    private final By paymentNoinField=By.xpath("//div[contains(text(),'Payment #')]//following-sibling::div");
//	    private final By paymentNoinListField=By.xpath("(//tr/td[3])[1]/div/div");
	    
	    /** Navigate to Create Estimate Page */
	    public void navigateToNewExpense() {
	    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(purchaseMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(expenseMenuField)).click();
	        wait.until(ExpectedConditions.elementToBeClickable(newExpenseButtonField)).click();
	    }
//	    public String expenseNumber() {
//	    	String EXNO=wait.until(ExpectedConditions.visibilityOfElementLocated()).getAttribute("value");
//	    	return EXNO;
//	    }
	    public void fillExpenseFields(
	    		String amount,
		        String expenseDate,
		        String employee,
		        String expenseAccount,
		        String paidThroughAccount,
		        String currency,
		        String vendorName,
		        String taxTreatment,
		        String tax,
		        String inclusiveTax,
		        String referenceNumber,
		        String customer,
		        String billable,
		        String markup,
		        String notes) throws Exception {
//	    	System.out.println(
//	    			"  Amount: " + amount +	
//	    	        ", Expense Date: " + expenseDate +
//	    	        ", Employee: " + employee +
//	    	        ", Expense Account: " + expenseAccount +	    	        
//	    	        ", Paid Through Account: " + paidThroughAccount +
//	    	        ", Currency: " + currency +
//	    	        ", Vendor Name: " + vendorName +
//	    	        ", Tax Treatment: " + taxTreatment +
//	    	        ", Tax: " + tax +
//	    	        ", Inclusive Tax: " + inclusiveTax +
//	    	        ", Reference Number: " + referenceNumber +
//	    	        ", Customer: " + customer +
//	    	        ", Billable: " + billable+
//	    	        ", Markup: " + markup +
//	    	        ", Notes: " + notes );
	    	if (Utilities.isNotEmpty(expenseDate)) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(expenseDateField));
	    		Utilities.selectDateByValue(driver,expenseDateField,expenseDate);
	    		Thread.sleep(100);
	    	}
	    	if (Utilities.isNotEmpty(employee)) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(expenseDateField));
	    		Utilities.selectIfListed(driver, searchEmployeeField, selectEmployeeField,employee);
	        	Thread.sleep(100);
	    	}
	    	if (Utilities.isNotEmpty(expenseAccount)) {
	    		//Utilities.selectHeadlessUIDropdownValue(driver, searchExpenseAccountField, expenseAccount);
	    		Utilities.selectIfListed(driver, searchExpenseAccountField, selectExpenseAccountField,expenseAccount);
	    		Thread.sleep(100);	
	    	}
	    	if (Utilities.isNotEmpty(amount)) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(amountField)).sendKeys(amount);
	    		//Thread.sleep(100);
	    	}
	    	if (Utilities.isNotEmpty(paidThroughAccount)) {
	    		//Utilities.selectHeadlessUIDropdownValue(driver, searchPaidThroughAccountField, paidThroughAccount);
	    		Utilities.selectIfListed(driver, searchPaidThroughAccountField, selectPaidThroughAccountField,paidThroughAccount);
	    		Thread.sleep(100);	
	    	}
	    	if (Utilities.isNotEmpty(currency)) {
	    		//Utilities.selectHeadlessUIDropdownValue(driver, searchCurrencyField, currency);
	    		Utilities.selectIfListed(driver, searchCurrencyField, selectCurrencyField,currency);
	    		Thread.sleep(100);	
	    	}
	    	Utilities.waitForPageToLoad(driver);
	    	if (Utilities.isNotEmpty(vendorName)) {	
				Utilities.selectCustomer(driver,vendorDropdownField, vendorName);
	    	}
	    	if (Utilities.isNotEmpty(taxTreatment)&&!(Utilities.isNotEmpty(vendorName))) {
	    		//Utilities.selectHeadlessUIDropdownValue(driver, searchCurrencyField, taxTreatment);
	    		Utilities.selectIfListed(driver, searchTaxtreatmentField, selectTaxtreatmentField,taxTreatment);
	    		Thread.sleep(100);	
	    	}
	    	Boolean r=false;
	    	try {
	    		WebElement revrsecharge=driver.findElement(By.id("reverse_charge"));
	    		if(revrsecharge.isEnabled()) {
	    			revrsecharge.click();
	    			r=true;
	    		}	
	    	}
	    	catch(Exception e) {	
	    	}
	    	if(r==true) {
	    		Utilities.selectIfListed(driver, searchTaxField, selectTaxField,"Standard Rate (15%)");
	    		Thread.sleep(100);	    		
	    	}
	    	else {	    	
	    			if (Utilities.isNotEmpty(tax)) {    		
	    					Utilities.selectIfListed(driver, searchTaxField, selectTaxField,tax);
	    					Thread.sleep(100);	
	    			}
	    			if (Utilities.isNotEmpty(inclusiveTax)) {
	    				try {
	    						wait.until(ExpectedConditions.visibilityOfElementLocated(selectInclusiveTaxField));
	    						JavascriptExecutor js = (JavascriptExecutor) driver;
	    						WebElement incTax  = driver.findElement(selectInclusiveTaxField);
	    						js.executeScript("arguments[0].scrollIntoView();",incTax);	   
	    						incTax.click();
	    						Thread.sleep(100);
	    				}
	    				catch(Exception e) {
	    					System.out.println("Inclusive can not selectable.");
	    				}
	    			}
	    	}
	    	if (Utilities.isNotEmpty(referenceNumber)) {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(referenceNumberField));
	    		driver.findElement(referenceNumberField).sendKeys(referenceNumber);
	    		Thread.sleep(100);
	    	}
	    	if (Utilities.isNotEmpty(customer)) {	
	    		try {
	    			wait.until(ExpectedConditions.visibilityOfElementLocated(customerDropdownField));
		    		JavascriptExecutor js = (JavascriptExecutor) driver;
					WebElement customerSelection  = driver.findElement(customerDropdownField);
				    js.executeScript("arguments[0].scrollIntoView();",customerSelection);	  
    			Utilities.selectCustomer(driver,customerDropdownField, customer);
	    		}
	    		catch(Exception e) {
	    			System.out.println("Customer not found");
	    		}
	    		Thread.sleep(100);
	    	}
	    	if (Utilities.isNotEmpty(billable)&&Utilities.isNotEmpty(customer)) {	
	    		try {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(billableField));
	    		JavascriptExecutor js = (JavascriptExecutor) driver;
				WebElement billableExpense  = driver.findElement(billableField);
			    js.executeScript("arguments[0].scrollIntoView();",billableExpense);	   
			    billableExpense.click();
			    Thread.sleep(100);
	    		}
	    		catch(Exception e) {
	    			System.out.println("Customer not selected");
	    		}
	    	}
	    	if (Utilities.isNotEmpty(markup)&&Utilities.isNotEmpty(customer)&&Utilities.isNotEmpty(billable)) {	
	    		try {
	    		wait.until(ExpectedConditions.visibilityOfElementLocated(markupField));
	    		WebElement markUp=driver.findElement(markupField);
	    		new Actions(driver).doubleClick(markUp).perform();
	    		markUp.sendKeys(markup);
	    		Thread.sleep(100);
	    		}
	    		catch(Exception e) {
	    			System.out.println("Makup field not displayed.Customer may not selected OR Billable not selecetd.");	    			
	    		}
	    	}	    		
	    	if(Utilities.isNotEmpty(notes)) {
	    		JavascriptExecutor js = (JavascriptExecutor) driver;
				WebElement cnotes  = driver.findElement(notesField);
			    js.executeScript("arguments[0].scrollIntoView();",cnotes);			    
			    driver.findElement(notesField).sendKeys(notes+" : "+Utilities.dateTime());
			    Thread.sleep(100);
	    	}
	    	if(!Utilities.isNotEmpty(notes)) {
	    		JavascriptExecutor js = (JavascriptExecutor) driver;
				WebElement cnotes  = driver.findElement(notesField);
			    js.executeScript("arguments[0].scrollIntoView();",cnotes);			    
			    driver.findElement(notesField).sendKeys(Utilities.dateTime());
			    Thread.sleep(100);
	    	}
	    }
	    public void save() {	    	
	    	wait.until(ExpectedConditions.elementToBeClickable(saveButton)).click();
	    	
	    }
	    public String verifyExpenseCreated() {
	    	 WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

	    	    WebElement message = wait.until(
	    	            ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='alert']//div[last()]")));
	    	    String actualMessage = message.getText().trim();
	    	    return actualMessage;
	    }     
}

