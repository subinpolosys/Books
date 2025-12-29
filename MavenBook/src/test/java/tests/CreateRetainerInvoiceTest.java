package tests;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseSetup;
import base.BaseTest;
import pages.CreateRetainerInvoicePage;
import pages.CreateSalesOrderPage;
import pages.Login;
import utils.ExcelReader;
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
//	   @DataProvider(name = "RetainerData")
//	   public Object[][] getItemData() throws Exception {
//	       String filePath = "src/test/resources/RetainerInvoiceData.xlsx";
//	       String sheetName = "Retainer";
//	       return ItemExcelReader.getTestData(filePath, sheetName);
//	   }
	  @Test
	  public void createRetainerInvoiceTest () throws IOException, InterruptedException {
	        String filePath = System.getProperty("user.dir") + "/src/test/resources/RetainerInvoiceData.xlsx";
	        List<Map<String, Object>> allRetainerInvoice =
	                ExcelReader.getMasterDetailData(filePath,"Retainer","Sheet1");
	        CreateRetainerInvoicePage retainerInvoicePage = new CreateRetainerInvoicePage(driver);
	        for (Map<String, Object> retainer : allRetainerInvoice) {
			  String customerName  	  = ExcelReader.getValue(retainer, "Customer Name");
	          String retainerData  	  = ExcelReader.getValue(retainer, "Retainer Invoice Date");
	          String referenceNo  	  = ExcelReader.getValue(retainer, "Reference Number");
	          String description  	  = ExcelReader.getValue(retainer, "Description");
	          String amount       	  = ExcelReader.getValue(retainer, "Amount");
	          String customerNote  	  = ExcelReader.getValue(retainer, "Customer Notes");
	          String termsAndcondition= ExcelReader.getValue(retainer, "Terms And Conditions");
	          String saveAs			  = ExcelReader.getValue(retainer, "Save As");
	         
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
		  createRetainerInvoicePage.saveAsMethod(saveAs);
		  SoftAssert soft=new SoftAssert();
          soft.assertTrue(createRetainerInvoicePage.verifyRetainerInvoiceCreated(RIno),
                  "Retainer Invoice not found or failed to create : " + RIno);
          soft.assertAll();
	      }
	  }  
}