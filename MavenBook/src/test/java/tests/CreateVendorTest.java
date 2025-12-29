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
import pages.CreateVendorPage;
import pages.Login;
import utils.CustomerExcelReader;

public class CreateVendorTest extends BaseTest{
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
  public void createVendorTest() throws InterruptedException, IOException {
	  
	  String filePath = System.getProperty("user.dir") + "/src/test/resources/VendorData.xlsx";
	  List<Map<String, Object>> allVendors =
              CustomerExcelReader.getTestData(filePath, "Vendor", "Address", "Contact Person");
	  CreateVendorPage createVendorPage=new CreateVendorPage(driver);
	  for (Map<String, Object> vendorData : allVendors) {
		  
		  	String salutation = getValue(vendorData, "Salutation");
	        String firstName = getValue(vendorData, "First Name");
	        String lastName = getValue(vendorData, "Last Name");
	        String companyName = getValue(vendorData, "Company Name");
	        String companyDisplayName = getValue(vendorData, "Company Display Name");
	        String companyDispNameArabic = getValue(vendorData, "Company Disp Name(Arabic)");
	        String primaryEmail = getValue(vendorData, "Primary Email");
	        String secondaryEmail = getValue(vendorData, "Secondary Email");
	        String workPhone = getValue(vendorData, "Work Phone");
	        String mobileNumber = getValue(vendorData, "Mobile Number");
	        String creditLimit = getValue(vendorData, "Credit Limit");
	        String taxTreatment = getValue(vendorData, "Tax Treatment");
	        String trn = getValue(vendorData, "Tax Registration Number(TRN)");
	        String placeOfSupply = getValue(vendorData, "Place of Supply");
	        String currency = getValue(vendorData, "Currency");
	        String exchangeRate = getValue(vendorData, "Exchange Rate");
	        String openingBalance = getValue(vendorData, "Opening Balance");
	        String terms = getValue(vendorData, "Terms");
	        String priceList = getValue(vendorData, "Price List");
	        String website = getValue(vendorData, "Website");
	        String facebook = getValue(vendorData, "Facebook");
	        String twitter = getValue(vendorData, "Twitter");
	        String designation = getValue(vendorData, "Designation");
	        String department = getValue(vendorData, "Department");

	        // === SHEET 2: Customer Billing Address ===
	        String attention = getValue(vendorData, "Attention");
	        String addressLine1 = getValue(vendorData, "Address line");
	        String country = getValue(vendorData, "Country");
	        String state = getValue(vendorData, "State");
	        String addressLine2 = getValue(vendorData, "Address line 2");
	        String addressLine3 = getValue(vendorData, "Address line 3");
	        String city = getValue(vendorData, "City");
	        String landmark = getValue(vendorData, "Landmark");
	        String zipCode = getValue(vendorData, "Zip Code");
	        String addressPhone = getValue(vendorData, "Phone");
	        String fax = getValue(vendorData, "Fax");

	        // === SHEET 3: Contact Person ===
	        String contactSalutation = getValue(vendorData, "Contact Salutation");
	        String contactFirstName = getValue(vendorData, "Contact FName");
	        String contactLastName = getValue(vendorData, "Contact LName");
	        String contactEmail = getValue(vendorData, "Email Address");
	        String contactWorkPhone = getValue(vendorData, "Contact Work Phone");
	        String contactMobile = getValue(vendorData, "Mobile No");

	        // === Skip invalid data ===
	        if ((companyDisplayName == null || companyDisplayName.trim().isEmpty()) ||(taxTreatment == null || taxTreatment.trim().isEmpty())) {

	        	    throw new SkipException("Skipping: Missing Customer Display Name or Tax Treatment");
	        }
	   createVendorPage.navigateToNewVendor();
	   createVendorPage.vendorDetails(salutation, firstName, lastName, companyName, companyDisplayName, companyDispNameArabic,primaryEmail, secondaryEmail, workPhone, 
		mobileNumber, creditLimit, taxTreatment, trn,placeOfSupply, currency, exchangeRate, openingBalance, terms, priceList, website,facebook, twitter, designation, department);
	  
	   if (country != null && !country.trim().isEmpty() && state != null && !state.trim().isEmpty() && zipCode != null && !zipCode.trim().isEmpty()){
		createVendorPage.vendorBillingAddress(attention, addressLine1, country, state, addressLine2, addressLine3, city, landmark,zipCode, addressPhone, fax );
        createVendorPage.vendorShippingAddress(attention, addressLine1, country, state, addressLine2, addressLine3, city, landmark,zipCode, addressPhone, fax );
	   } 
	   if (contactFirstName!= null && !contactFirstName.trim().isEmpty() && contactEmail != null && !contactEmail.trim().isEmpty() &&( (
			   contactWorkPhone != null && !contactWorkPhone.trim().isEmpty()) || (contactMobile != null && !contactMobile.trim().isEmpty()))){
	   createVendorPage.vendorContactAddress(contactSalutation, contactFirstName, contactLastName, contactEmail, contactWorkPhone, contactMobile);
	   } 
	  createVendorPage.clickSaveButton();
	  SoftAssert soft=new SoftAssert();
	  soft.assertTrue(createVendorPage.verifyVendorCreated(companyDisplayName),
	              "Vendor not found or failed to create : " + companyDisplayName);
	  soft.assertAll();
	  }	  
  }       
	  private String getValue(Map<String, Object> data, String key) {
	            return data.getOrDefault(key, "").toString().trim();
	        }   
}
