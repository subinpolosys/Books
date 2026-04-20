package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTest;
import dataprovider.SalesOrderDataProvider;
import drivers.DriverFactory;
import model.SalesOrderData;
import pages.CreateSalesOrderPage;
import pages.Login;

public class SalesOrderTest extends BaseTest {
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
    @Test(dataProvider = "salesOrderData",
          dataProviderClass = SalesOrderDataProvider.class)
    public void salesOrderTest(SalesOrderData data) throws Exception {
    	 CreateSalesOrderPage soPage =
                 new  CreateSalesOrderPage(driver);
    	 soPage.navigateToNewSalesOrder();
    	 soPage.fillSalesOrderHeader(
         		data.customerName,
         		data.referenceNo,
         		data.salesOrdertDate,
         		data.expDeliveryDate,  
         		data.paymentTerms,
         		data.salesPerson,         		         		       		
         		data.taxType, 
         		data.priceList
         		);
    	 String soNumber = soPage.salesOrderNumber();
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
         soPage.addItems(itemNames, quantities, discountType, discount);
         soPage.addNotesAndTerms(data.customerNote, data.termsAndconditions);
         soPage.saveAsMethod(data.saveAs);
         SoftAssert softAssert = new SoftAssert();
         softAssert.assertTrue(
        		 soPage.verifySalesOrderCreated(soNumber),
                 "Sales order creation failed: " + soNumber
         );
         softAssert.assertAll();	
    }
}
