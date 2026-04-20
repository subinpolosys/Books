package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTest;
import dataprovider.InvoicesDataProvider;
import dataprovider.SalesReturnDataProvider;
import drivers.DriverFactory;
import model.InvoicesData;
import model.SalesReturnData;
import pages.CreateSalesInvoicePage;
import pages.CreateSalesReturnPage;
import pages.Login;

public class SalesReturnTest extends BaseTest{
	private Login login;
    @BeforeMethod
    public void loginToApp() throws InterruptedException {
        login = new Login(driver);
        login.login();
    }
  
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try { login.logout(); } catch (Exception ignored) {}
        DriverFactory.quitDriver();
    }
    @Test(dataProvider = "salesReturnData",
          dataProviderClass = SalesReturnDataProvider.class)
    public void salesReturnTest(SalesReturnData data) throws Exception {
    	CreateSalesReturnPage srPage =
                new  CreateSalesReturnPage(driver);
    	srPage.navigateToNewSalesReturn();
    	srPage.fillSalesReturnHeader(
        		data.customerName,
        		data.invoiceNo,
        		data.reason,
        		data.salesPerson,
        		data.returnDate,
        		data.refNo,        		
        		data.transactionType,         		         		       		
        		data.taxType, 
        		data.priceList);
    	
    	 String srNumber = srPage.salesReturnNumber();
         String[] itemNames = data.items.stream()
                 .map(i -> i.get("Item Name"))
                 .toArray(String[]::new);
         String[] quantities = data.items.stream()
                 .map(i -> i.get("Item Quantity"))
                 .toArray(String[]::new);
         String[] discountType = data.items.stream()
                 .map(i -> i.get("Discount Type"))
                 .toArray(String[]::new);
         String[] discount = data.items.stream()
                 .map(i -> i.get("Discount"))
                 .toArray(String[]::new);		
         srPage.addItems(itemNames, quantities, discountType, discount);
         srPage.addNotesAndTerms(data.customerNote, data.termsAndconditions);
         srPage.saveAsMethod(data.saveAs);
         SoftAssert softAssert = new SoftAssert();
         softAssert.assertTrue(
        		 srPage.verifySalesReturnCreated(srNumber),
                 "Sales Return creation failed: " + srNumber);
         softAssert.assertAll();
	
	
	
    }
}
