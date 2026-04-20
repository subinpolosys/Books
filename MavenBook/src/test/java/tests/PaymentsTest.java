package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseTest;
import dataprovider.PaymentDataProvider;
import dataprovider.ReceiptDataProvider;
import drivers.DriverFactory;
import model.PaymentData;
import model.ReceiptData;
import pages.CreatePaymentsPage;
import pages.CreateReceiptPage;
import pages.Login;

public class PaymentsTest extends BaseTest {
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
    @Test(dataProvider = "paymentData",
          dataProviderClass = PaymentDataProvider.class)
    public void paymentTest(PaymentData data) throws Exception {
    	CreatePaymentsPage paymentPage = new  CreatePaymentsPage(driver);
    	paymentPage.navigateToNewPayment();   	
    	paymentPage.fillPaymentHeader(
    			data.vendorName,
    			data.paymentDate,
    			data.amountReceived,
    			data.paymentmode,
    			data.referenceNo,
    			data.depositAccount, 	
    			data.invoiceNo,
    			data.notes);
    	String paymentNo=paymentPage.paymentNumber();
    	paymentPage.save();
    	 SoftAssert softAssert = new SoftAssert();
         softAssert.assertTrue(
        		 paymentPage.verifyPaymentCreated(paymentNo),
                 "Payment creation failed: " + paymentNo
         );
         softAssert.assertAll();	   	
    }
}
