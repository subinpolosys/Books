package tests;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseSetup;
import base.BaseTest;
import pages.CreateCustomerPage;
import pages.Login;
import utils.CustomerExcelReader;

public class CreateCustomerTest extends BaseTest{
	private Login loginPage;
    @BeforeMethod
    public void beforeMethod() throws InterruptedException {
        loginPage = new Login(driver);
        loginPage.login();
    }
   @AfterMethod
    public void afterMethod() {
       loginPage.logout();
   }
  @Test
  public void createCustomerTest() throws IOException, InterruptedException {
	  String filePath = System.getProperty("user.dir") + "/src/test/resources/CustomerData.xlsx";
	  List<Map<String, Object>> allCustomers =
              CustomerExcelReader.getTestData(filePath, "Customer", "Address", "Contact Person");
	  CreateCustomerPage createCustomerPage=new CreateCustomerPage(driver);
	  for (Map<String, Object> customerData : allCustomers) {
		  
		  	String salutation = getValue(customerData, "Salutation");
	        String firstName = getValue(customerData, "First Name");
	        String lastName = getValue(customerData, "Last Name");
	        String companyName = getValue(customerData, "Company Name");
	        String companyDisplayName = getValue(customerData, "Company Display Name");
	        String companyDispNameArabic = getValue(customerData, "Company Disp Name(Arabic)");
	        String primaryEmail = getValue(customerData, "Primary Email");
	        String secondaryEmail = getValue(customerData, "Secondary Email");
	        String workPhone = getValue(customerData, "Work Phone");
	        String mobileNumber = getValue(customerData, "Mobile Number");
	        String creditLimit = getValue(customerData, "Credit Limit");
	        String taxTreatment = getValue(customerData, "Tax Treatment");
	        String trn = getValue(customerData, "Tax Registration Number(TRN)");
	        String placeOfSupply = getValue(customerData, "Place of Supply");
	        String currency = getValue(customerData, "Currency");
	        String exchangeRate = getValue(customerData, "Exchange Rate");
	        String openingBalance = getValue(customerData, "Opening Balance");
	        String terms = getValue(customerData, "Terms");
	        String priceList = getValue(customerData, "Price List");
	        String website = getValue(customerData, "Website");
	        String facebook = getValue(customerData, "Facebook");
	        String twitter = getValue(customerData, "Twitter");
	        String designation = getValue(customerData, "Designation");
	        String department = getValue(customerData, "Department");

	        // === SHEET 2: Customer Billing Address ===
	        String attention = getValue(customerData, "Attention");
	        String addressLine1 = getValue(customerData, "Address line");
	        String country = getValue(customerData, "Country");
	        String state = getValue(customerData, "State");
	        String addressLine2 = getValue(customerData, "Address line 2");
	        String addressLine3 = getValue(customerData, "Address line 3");
	        String city = getValue(customerData, "City");
	        String landmark = getValue(customerData, "Landmark");
	        String zipCode = getValue(customerData, "Zip Code");
	        String addressPhone = getValue(customerData, "Phone");
	        String fax = getValue(customerData, "Fax");

	        // === SHEET 3: Contact Person ===
	        String contactSalutation = getValue(customerData, "Contact Salutation");
	        String contactFirstName = getValue(customerData, "Contact FName");
	        String contactLastName = getValue(customerData, "Contact LName");
	        String contactEmail = getValue(customerData, "Email Address");
	        String contactWorkPhone = getValue(customerData, "Contact Work Phone");
	        String contactMobile = getValue(customerData, "Mobile No");

	        // === Skip invalid data ===
	        if ((companyDisplayName == null || companyDisplayName.trim().isEmpty()) ||(taxTreatment == null || taxTreatment.trim().isEmpty())) {
	        	    throw new SkipException("Skipping: Missing Customer Display Name or Tax Treatment");
	        }
	   createCustomerPage.navigateToNewCustomer();
	   createCustomerPage.customerDetails(salutation, firstName, lastName, companyName, companyDisplayName, companyDispNameArabic,primaryEmail, secondaryEmail, workPhone, 
		mobileNumber, creditLimit, taxTreatment, trn,placeOfSupply, currency, exchangeRate, openingBalance, terms, priceList, website,facebook, twitter, designation, department);
	  
	   if (country != null && !country.trim().isEmpty() && state != null && !state.trim().isEmpty() && zipCode != null && !zipCode.trim().isEmpty()){
		createCustomerPage.customerBillingAddress(attention, addressLine1, country, state, addressLine2, addressLine3, city, landmark,zipCode, addressPhone, fax );
        createCustomerPage.customerShippingAddress(attention, addressLine1, country, state, addressLine2, addressLine3, city, landmark,zipCode, addressPhone, fax );
	   } 
	   if (contactFirstName!= null && !contactFirstName.trim().isEmpty() && contactEmail != null && !contactEmail.trim().isEmpty() &&( (
			   contactWorkPhone != null && !contactWorkPhone.trim().isEmpty()) || (contactMobile != null && !contactMobile.trim().isEmpty()))){
	   createCustomerPage.customerContactAddress(contactSalutation, contactFirstName, contactLastName, contactEmail, contactWorkPhone, contactMobile);
	   } 
	  createCustomerPage.clickSaveButton();
	  SoftAssert soft=new SoftAssert();
	  soft.assertTrue(createCustomerPage.verifyCustomerCreated(companyDisplayName),
	              "Customer not found or failed to create : " + companyDisplayName);
	  soft.assertAll();   
	  } 
  }       
	  private String getValue(Map<String, Object> data, String key) {
	            return data.getOrDefault(key, "").toString().trim();
	        }   
}
	        
	        