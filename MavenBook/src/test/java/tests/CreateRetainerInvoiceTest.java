package tests;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseSetup;
import base.BaseTest;
import pages.CreateRetainerInvoicePage;
import pages.Login;
import utils.ItemExcelReader;
public class CreateRetainerInvoiceTest extends BaseTest {
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
	   @DataProvider(name = "RetainerData")
	   public Object[][] getItemData() throws Exception {
	       String filePath = "src/test/resources/RetainerInvoiceData.xlsx";
	       String sheetName = "Retainer";
	       return ItemExcelReader.getTestData(filePath, sheetName);
	   }
	  @Test(dataProvider = "RetainerData")
	  public void createRetainerInvoiceTest (
			  String customerName,
	          String retainerData,
	          String referenceNo,
	          String description,
	          String amount,
	          String customerNote,
	          String termsAndcondition)throws InterruptedException{
		  if (customerName == null || customerName.trim().isEmpty()) {
		        throw new SkipException("Customer Name cannot be empty — skipping test case.");
		    }
		  if (description == null || description.trim().isEmpty()) {
		        throw new SkipException("Description cannot be empty — skipping test case.");
		    }
		  if (amount == null || amount.trim().isEmpty()) {
		        throw new SkipException("Amount cannot be empty — skipping test case.");
		    }
		  CreateRetainerInvoicePage createRetainerInvoicePage=new CreateRetainerInvoicePage(driver);
		  createRetainerInvoicePage.navigateToNewRetainerInvoice(); 
		  createRetainerInvoicePage.fillRetainerInvoice(customerName, retainerData, referenceNo, description, amount, customerNote, termsAndcondition);
		  String RIno=createRetainerInvoicePage.retainerInvoiceNumber();
		 // createRetainerInvoicePage.saveAsDraft();
		  SoftAssert soft=new SoftAssert();
          soft.assertTrue(createRetainerInvoicePage.verifyRetainerInvoiceCreated(RIno),
                  "Retainer Invoice not found or failed to create : " + RIno);
          soft.assertAll();
	  }  
}