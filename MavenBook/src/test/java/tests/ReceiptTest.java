package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseTest;
import dataprovider.ReceiptDataProvider;
import drivers.DriverFactory;
import model.ReceiptData;
import pages.CreateReceiptPage;
import pages.Login;

public class ReceiptTest extends BaseTest {
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
    @Test(dataProvider = "receiptData",
          dataProviderClass = ReceiptDataProvider.class)
    public void receiptTest(ReceiptData data) throws Exception {
    	CreateReceiptPage receiptPage = new  CreateReceiptPage(driver);
    	receiptPage.navigateToNewReceipt();   	
    	receiptPage.fillReceiptHeader(
    			data.customerName,
    			data.receiptDate,
    			data.amountReceived,
    			data.paymentmode,
    			data.referenceNo,
    			data.depositAccount,
    			data.bankCharge,
    			data.invoiceNo,
    			data.notes);
    	String receiptNo=receiptPage.receiptNumber();
    	receiptPage.save();
    	 SoftAssert softAssert = new SoftAssert();
         softAssert.assertTrue(
        		 receiptPage.verifyReceiptCreated(receiptNo),
                 "Receipt creation failed: " + receiptNo
         );
         softAssert.assertAll();	   	
    }
}
