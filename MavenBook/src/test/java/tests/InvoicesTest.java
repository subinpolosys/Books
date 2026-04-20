package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseTest;
import dataprovider.InvoicesDataProvider;
import drivers.DriverFactory;
import model.InvoicesData;
import pages.CreateSalesInvoicePage;
import pages.Login;

public class InvoicesTest extends BaseTest {
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
    @Test(dataProvider = "invoicesData",
          dataProviderClass = InvoicesDataProvider.class)
    public void invoicesTest(InvoicesData data) throws Exception {
    	 CreateSalesInvoicePage siPage =
                 new  CreateSalesInvoicePage(driver);
    	 siPage.navigateToNewSalesInvoice();
    	 siPage.fillSalesInvoiceHeader(
         		data.customerName,
         		data.orderNo,
         		data.subject,
         		data.salesPerson,
         		data.invoiceDate,
         		data.paymentTerms,
         		data.supplyDate, 
         		data.transactionType,         		         		       		
         		data.taxType, 
         		data.priceList);
    	 String siNumber = siPage.salesInvoiceNumber();
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
         siPage.addItems(itemNames, quantities, discountType, discount);
         siPage.addNotesAndTerms(data.customerNote, data.termsAndconditions);
         siPage.saveAsMethod(data.saveAs);
         SoftAssert softAssert = new SoftAssert();
         softAssert.assertTrue(
        		 siPage.verifySalesInvoiceCreated(siNumber),
                 "Invoice creation failed: " + siNumber);
         softAssert.assertAll();
	
    }
}
