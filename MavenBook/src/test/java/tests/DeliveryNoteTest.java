package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTest;
import dataprovider.DeliveryNoteDataProvider;
import drivers.DriverFactory;
import model.DeliveryNoteData;
import pages.CreateDeliverynotePage;
import pages.Login;

public class DeliveryNoteTest extends BaseTest {
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
    @Test(dataProvider = "deliveryNoteData",
          dataProviderClass = DeliveryNoteDataProvider.class)
    public void deliveryNoteTest(DeliveryNoteData data) throws Exception {
   	 CreateDeliverynotePage dnPage =
                new  CreateDeliverynotePage(driver);
   	 
   	dnPage.navigateToNewDeliverynote();
   	dnPage.fillDeliveryNoteHeader(
   			data.customerName, 
   			data.referenceNo,
   			data.dnDate,
   			data.dnType,
   			data.taxType,
   			data.priceList);
    String DNNo=dnPage.delivernoteNumber();
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
   	
	            dnPage.addItems(itemNames, quantities,discountType,discount);
	            dnPage.addNotesAndTerms(
	                    "Dear " + data.customerName + ", " + DNNo + " has been created by Automation."+data.customerNote,
	                    "This is a system-generated document. Ensure accuracy before acceptance."+data.termsAndconditions
	            );
	            dnPage.saveAsMethod(data.saveAs);
	            SoftAssert soft=new SoftAssert();
	            soft.assertTrue(dnPage.verifyDeliverynoteCreated(DNNo),
	                    "Delivery Note not found or failed to create : " + DNNo);
	            soft.assertAll();
	        }
	    

}
