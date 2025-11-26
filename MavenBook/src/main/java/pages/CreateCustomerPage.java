package pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Utilities;

public class CreateCustomerPage {
	private final WebDriver driver;
    private final WebDriverWait wait;
    public CreateCustomerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    //==================New customer Navigation=========================   
    private final By dashboardField=By.xpath("//a[text()='Dashboard']");
    private final By salesMenuField = By.xpath("//div[@title='sales']/a[contains(text(),'Sales')]");
    private final By customerMenuField = By.xpath("//span[text()='Customers']");
    private final By newCustomerField = By.xpath("//button/p[contains(text(),'new')]");
    
    //=================Customer Creations=============================== 
    private final By searchSalutationField=By.xpath("//label[contains(text(),'Salutation')]/following-sibling::input[@type='text']");
    private final By selectSalutationField=By.xpath("//ul/li[1]/span");    
    private final By firstNameField=By.id("first_name");
    private final By lastNameField=By.id("last_name");
    private final By companyNameField=By.id("company_name");
    private final By companyDisplayNameField=By.xpath("//input[@placeholder='Company Display Name']");
    private final By companyDispNameArabicField=By.id("company_display_name_ar");
    private final By primaryEmailField=By.id("email_1");
    private final By secondaryEmailField=By.id("email_2");
    private final By workPhoneField=By.id("phone_1");
    private final By mobileNumberField=By.id("phone_2");
    private final By creditLimitField=By.id("credit_limit");
    //=================Other Details========================================   
    private final By searchTaxTreatmentField=By.xpath("//label[contains(text(),'Tax Treatment')]/following-sibling::input[@type='text']");
    private final By selectTaxTreatmentField=By.xpath("//ul/li[4]/span"); //li[1]   
    private final By trnField=By.id("tax_number");
    private final By searchPlaceOfSupplyField=By.xpath("//label[contains(text(),'Place of Supply (Country)')]/following-sibling::input[@type='text']");
    private final By selectPlaceodSupplyField=By.id("//ul/li[1]/span");
    private final By searchCurrencyField=By.xpath("//label[contains(text(),'Currency')]/following-sibling::input[@type='text']");
    private final By selectCurrencyField=By.id("//ul/li[1]/span"); 
    private final By exchangeRateField=By.id("exchange_rate");
    private final By openingBalanceField=By.id("opening_balance");
    private final By searchTermsField=By.xpath("//label[contains(text(),'Terms')]/following-sibling::input[@type='text']");
    private final By selectTermsSupplyField=By.id("//ul/li[1]/span");   
    private final By searchPriceListField=By.xpath("//label[contains(text(),'Price List')]/following-sibling::input[@type='text']");
    private final By selectPriceListField=By.id("//ul/li[1]/span");   
    private final By websiteField=By.id("website");
    private final By facebookField=By.id("facebook");
    private final By twitterField=By.id("twitter");
    //===================More Details=========================================  
    private final By addmoreDetailsField=By.xpath("//p[text()='Add more Details']");
    private final By designationField=By.id("designation");
    private final By departmentField=By.id("department");  
    //===================Billing & Shipping Address===========================
    
    private final By addressTabField=By.xpath("//a[text()='Address']");
    private final By newBillingAddressField=By.xpath("//p[text()='BILLING ADDRESS']/following-sibling::div[1]/div/a[text()='Add New Address']");
    private final By newShippingAddressField=By.xpath("//p[text()='SHIPPING ADDRESS']/following-sibling::div[1]/div/a[text()='Add New Address']");
    private final By attentionField=By.id("attention");
    private final By addressL1Field=By.id("address_1");
    private final By searchCuntryField=By.xpath("//label[contains(text(),'Country')]/following-sibling::input[@type='text']");
    private final By selectCuntryField=By.id("//ul/li[1]/span");
    
    private final By searchStateField=By.xpath("//label[contains(text(),'State')]/following-sibling::input[@type='text']");
    private final By selectStateField=By.id("//ul/li[1]/span");
    
    private final By addressL2Field=By.id("address_2");
    private final By addressL2LabelField=By.xpath("//label[@title='Address line 2']");
    private final By addressL3Field=By.id("address_3");
    private final By addressL3LabelField=By.xpath("//label[@title='Address line 3']");
    private final By cityField=By.id("city");
    private final By landMarkField=By.id("landmark");
    private final By zipCodeField=By.id("zip_code");
    private final By billPhoneField=By.id("phone");
    private final By billFaxField=By.id("fax");
    private final By billingAddressSubmitBtnField=By.xpath("//button[@name='button' and text()='Submit']"); 
    //===================Contact Person=======================================  
    private final By contactPersonTabField=By.xpath("//a[text()='Contact Persons']");
    private final By searchContactSalutationField=By.xpath("//tbody/tr[1]/td/div/div/div[1]/div[text()='Salutation']/following-sibling::div/input[@type='text']");
    private final By salutationList=By.xpath("//div[contains(@id,'react-select') and contains(@id,'-option-')]");
    private final By contactFnameField=By.xpath("//tbody/tr[1]/td[2]/input[@type='text']");
    private final By contactLnameField=By.xpath("//tbody/tr[1]/td[3]/input[@type='text']");
    private final By contactEmailField=By.xpath("//tbody/tr[1]/td[4]/input[@type='email']");
    private final By contactWorkPhoneField=By.xpath("//tbody/tr[1]/td[5]/input[@type='text']");
    private final By contactMobileField=By.xpath("//tbody/tr[1]/td[6]/input[@type='text']");    
    //===================Save=================================================
    private final By saveField=By.name("default_submit");
    private final By createdCustomerNameField=By.xpath("//tbody/tr[1]/td[2]/div/div/p");
    
    public void navigateToNewCustomer() {
    	wait.until(ExpectedConditions.elementToBeClickable(dashboardField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(salesMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(customerMenuField)).click();
        wait.until(ExpectedConditions.elementToBeClickable(newCustomerField)).click();
    }

    public void customerDetails(String salutation, String firstName, String lastName, String companyName, String companyDisplayName,
    	    String companyDispNameArabic, String primaryEmail, String secondaryEmail, String workPhone,
    	    String mobileNumber, String creditLimit, String taxTreatment, String trn, String placeOfSupply,
    	    String currency, String exchangeRate, String openingBalance, String terms, String priceList,
    	    String website, String facebook, String twitter, String designation, String department) throws InterruptedException{ 
    	//System.out.println(salutation + ", " + firstName + ", " + lastName + ", " + companyName + ", " + companyDisplayName + ", " + companyDispNameArabic + ", " + primaryEmail + ", " + secondaryEmail + ", Work Phone:" + workPhone + ", " + mobileNumber + ", " + creditLimit + ", " + taxTreatment + ", " + trn + ", " + placeOfSupply + ", " + currency + ", " + exchangeRate + ", " + openingBalance + ", " + terms + ", " + priceList + ", " + website + ", " + facebook + ", " + twitter + ", " + designation + ", " + department);

    	if (salutation!= null && !salutation .trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchSalutationField, selectSalutationField,salutation);
    	}
    	if (firstName != null && !firstName.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField)).sendKeys(firstName);
    	}
    	if (lastName != null && !lastName.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameField)).sendKeys(lastName);
    	}
    	if (companyName != null && !companyName.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(companyNameField)).sendKeys(companyName);
    	}
    	if (companyDisplayName != null && !companyDisplayName.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(companyDisplayNameField)).sendKeys(companyDisplayName);
    	}
    	if (companyDispNameArabic != null && !companyDispNameArabic.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(companyDispNameArabicField)).sendKeys(companyDispNameArabic);
    	}
    	if (primaryEmail != null && !primaryEmail.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(primaryEmailField)).sendKeys(primaryEmail);
    	}
    	if (secondaryEmail != null && !secondaryEmail.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(secondaryEmailField)).sendKeys(secondaryEmail);
    	}
    	if (workPhone!= null && !workPhone.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(workPhoneField)).sendKeys(workPhone);
    	}
    	if (mobileNumber != null && !mobileNumber.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(mobileNumberField)).sendKeys(mobileNumber);
    	}
    	if (creditLimit != null && !creditLimit.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(creditLimitField)).sendKeys(creditLimit);
    	}
    	
    	Thread.sleep(300);
    	
    	if (taxTreatment!= null && !taxTreatment.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchTaxTreatmentField, selectTaxTreatmentField,taxTreatment);
    	}
    	Thread.sleep(20);
    	if (trn != null && !trn.trim().isEmpty()) { 
    		try {
    		wait.until(ExpectedConditions.visibilityOfElementLocated(trnField)).sendKeys(trn);
    		}catch(Exception e) {
        		
        	}
    	}
    	if (placeOfSupply!= null && !placeOfSupply.trim().isEmpty()&& !(taxTreatment.trim().equalsIgnoreCase("VAT Registered")) ) {
    		try {
    		wait.until(ExpectedConditions.elementToBeClickable(searchPlaceOfSupplyField));
    		Utilities.selectIfListed(driver, searchPlaceOfSupplyField, selectPlaceodSupplyField,placeOfSupply);
    		}catch(Exception e) {
    			  e.printStackTrace();
        	}
    	}
    	if (currency!= null && !currency.trim().isEmpty()) {
    		
    		try {
        		wait.until(ExpectedConditions.elementToBeClickable(searchCurrencyField));       		
        		Utilities.selectIfListed(driver, searchCurrencyField, selectCuntryField, currency);
    		}catch(Exception e) {
        		
        	}
    	}
    	driver.findElement(By.xpath("//a[text()='Other Details']")).click();
    	if (exchangeRate!= null && !exchangeRate.trim().isEmpty()&& !(currency.trim().equalsIgnoreCase("Saudi Riyal"))){ 
    		try {
        		wait.until(ExpectedConditions.elementToBeClickable(exchangeRateField));
        		wait.until(ExpectedConditions.visibilityOfElementLocated(exchangeRateField)).sendKeys(exchangeRate);
    		}catch(Exception e) {
    			  e.printStackTrace();
        	}
    	}
    	driver.findElement(By.xpath("//a[text()='Other Details']")).click();
    	if (openingBalance!= null && !openingBalance.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(openingBalanceField)).sendKeys(openingBalance);
    	}
    	if (terms!= null && !terms.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchTermsField, selectTermsSupplyField,terms);
    	}
    	//driver.findElement(By.xpath("//a[text()='Other Details']")).click();
    	if (priceList!= null && !priceList.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchPriceListField, selectPriceListField,priceList);
    	}
    	
    	JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement moreDetails  = driver.findElement(websiteField);
	    js.executeScript("arguments[0].scrollIntoView();",moreDetails); 
    	
    	
    	if (website!= null && !website.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(websiteField)).sendKeys(website);
    	}
    	
    	if (facebook!= null && !facebook.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(facebookField)).sendKeys(facebook);
    	}
    	if (twitter!= null && !twitter.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(twitterField)).sendKeys(twitter);
    	}
    	if (designation!= null && !designation.trim().isEmpty()) { 
    		driver.findElement(addmoreDetailsField).click();
    		 Thread.sleep(300);
    		
    		wait.until(ExpectedConditions.visibilityOfElementLocated(designationField)).sendKeys(designation);
    	}
    	if (department!= null && !department.trim().isEmpty()) { 
    		wait.until(ExpectedConditions.visibilityOfElementLocated(departmentField)).sendKeys(department);
    	}
    	    	   	
    }
    public void customerBillingAddress(String attention, String addressLine1, String country, String state, String addressLine2,
		    String addressLine3, String city, String landmark, String zipCode, String addressPhone,String fax) throws InterruptedException {
		    
    	System.out.println(" Zip: "+zipCode+" phone: "+addressPhone+" fax : "+fax);
    	wait.until(ExpectedConditions.elementToBeClickable(addressTabField)).click();
    	wait.until(ExpectedConditions.elementToBeClickable(newBillingAddressField)).click();
    	Thread.sleep(300);
    	if (attention!= null && !attention.trim().isEmpty()) {
    	wait.until(ExpectedConditions.presenceOfElementLocated(attentionField)).sendKeys(attention);
    	}
    	if (addressLine1!= null && !addressLine1.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(addressL1Field)).sendKeys(addressLine1);
        }   	
    	if (country!= null && !country.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchCuntryField, selectCuntryField,country);
    		driver.findElement(addressL3LabelField).click();	
    	}
    	Thread.sleep(200);
    	if (state!= null && !state.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchStateField, selectStateField,state);
    		driver.findElement(addressL2LabelField).click();
    	}
    	if (addressLine2!= null && !addressLine2.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(addressL2Field)).sendKeys(addressLine2);
        }
    	if (addressLine3!= null && !addressLine3.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(addressL3Field)).sendKeys(addressLine3);
        }
    	if (city!= null && !city.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(cityField)).sendKeys(city);
        }Thread.sleep(500);
    	if (landmark!= null && !landmark.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(landMarkField)).sendKeys(landmark);
        }Thread.sleep(500);
    	if (zipCode!= null && !zipCode.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(zipCodeField)).sendKeys(zipCode);
        }Thread.sleep(500);
    	if (addressPhone!= null && !addressPhone.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(billPhoneField)).sendKeys(addressPhone);
        }Thread.sleep(500);
    	if (fax!= null && !fax.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(billFaxField)).sendKeys(fax);
        }
    	
    	wait.until(ExpectedConditions.elementToBeClickable(billingAddressSubmitBtnField)).click();
    	
    }
    
    public void customerShippingAddress(String attention, String addressLine1, String country, String state, String addressLine2,
		    String addressLine3, String city, String landmark, String zipCode, String addressPhone,
		    String fax) throws InterruptedException {
    	
    	wait.until(ExpectedConditions.elementToBeClickable(addressTabField)).click();
    	wait.until(ExpectedConditions.elementToBeClickable(newShippingAddressField)).click();
    	Thread.sleep(300);
    	if (attention!= null && !attention.trim().isEmpty()) {
    	wait.until(ExpectedConditions.presenceOfElementLocated(attentionField)).sendKeys(attention);
    	}
    	if (addressLine1!= null && !addressLine1.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(addressL1Field)).sendKeys(addressLine1);
        }
    	
    	if (country!= null && !country.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchCuntryField, selectCuntryField,country);
    		driver.findElement(addressL3LabelField).click();
    	}
    	Thread.sleep(200);
    	if (state!= null && !state.trim().isEmpty()) {
    		Utilities.selectIfListed(driver, searchStateField, selectStateField,state);
    		driver.findElement(addressL2LabelField).click();
    	}
    	if (addressLine2!= null && !addressLine2.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(addressL2Field)).sendKeys(addressLine2);
        }
    	if (addressLine3!= null && !addressLine3.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(addressL3Field)).sendKeys(addressLine3);
        }
    	if (city!= null && !city.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(cityField)).sendKeys(city);
        }
    	if (landmark!= null && !landmark.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(landMarkField)).sendKeys(landmark);
        }
    	if (zipCode!= null && !zipCode.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(zipCodeField)).sendKeys(zipCode);
        }
    	if (addressPhone!= null && !addressPhone.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(billPhoneField)).sendKeys(addressPhone);
        }
    	if (fax!= null && !fax.trim().isEmpty()) {
        	wait.until(ExpectedConditions.presenceOfElementLocated(billFaxField)).sendKeys(fax);
        }
    	wait.until(ExpectedConditions.elementToBeClickable(billingAddressSubmitBtnField)).click();
    	Thread.sleep(200);
    }
    
    public void customerContactAddress(String contactSalutation, String contactFirstName,String contactLastName, String contactEmail, 
    		String contactWorkPhone, String contactMobile) throws InterruptedException{
    	System.out.println("Work Phone : "+contactWorkPhone+" Contact Mobile: "+ contactMobile);
    	Thread.sleep(20);
    	
    	JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement contactperson  = driver.findElement(contactPersonTabField);
	    js.executeScript("arguments[0].scrollIntoView();",contactperson); 
    	wait.until(ExpectedConditions.elementToBeClickable(contactPersonTabField)).click();
    	Thread.sleep(2500);

    	if (contactSalutation != null && !contactSalutation.trim().isEmpty()) {
    	    WebElement salutationInput = wait.until(ExpectedConditions.elementToBeClickable(searchContactSalutationField));
    	    salutationInput.click();
    	    salutationInput.sendKeys(contactSalutation);
    	    List<WebElement> salutationOptions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(salutationList));
    	    for (WebElement option : salutationOptions) {
    	        if (option.getText().equalsIgnoreCase(contactSalutation)) {
    	            option.click();
    	            break;
    	        }
    	    }
    	}
    	if (contactFirstName!= null && !contactFirstName.trim().isEmpty()) {
    	wait.until(ExpectedConditions.visibilityOfElementLocated(contactFnameField)).sendKeys(contactFirstName);
    	}
    	if (contactLastName!= null && !contactLastName.trim().isEmpty()) {
        	wait.until(ExpectedConditions.visibilityOfElementLocated(contactLnameField)).sendKeys(contactLastName);
        	}
    	if (contactEmail!= null && !contactEmail.trim().isEmpty()) {
        	wait.until(ExpectedConditions.visibilityOfElementLocated(contactEmailField)).sendKeys(contactEmail);
        	}
    	if (contactWorkPhone!= null && !contactWorkPhone.trim().isEmpty()) {
        	wait.until(ExpectedConditions.visibilityOfElementLocated(contactWorkPhoneField)).sendKeys(contactWorkPhone);
        	}
    	if (contactMobile!= null && !contactMobile.trim().isEmpty()) {
        	wait.until(ExpectedConditions.visibilityOfElementLocated(contactMobileField)).sendKeys(contactMobile);
        	}
    	    	
    }
    public void clickSaveButton() { 
        try {
        	wait.until(ExpectedConditions.elementToBeClickable(saveField)).click();
        	
        }catch(Exception e) {	
        }
    }
    
    public boolean verifyCustomerCreated(String expectedCustomerName) {
    	try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(createdCustomerNameField));
        String actualCustomerName = driver.findElement(createdCustomerNameField).getText();
        System.out.println("Created Customer : "+actualCustomerName);
        return actualCustomerName.contains(expectedCustomerName);
    	}catch(Exception e) {
    		return false;
    	}
    }	
    
}
