package tests;

import javax.swing.text.Utilities;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseTest;
import dataprovider.PurchaseBillDataProvider;
import drivers.DriverFactory;
import model.PurchaseBillData;
import pages.CreatePurchaseBillPage;
import pages.Login;

public class PurchaseBillTest extends BaseTest{
	private Login login;
	//private long start;
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
    @Test(dataProvider = "purchaseBillData",
          dataProviderClass = PurchaseBillDataProvider.class)
    public void purchaseBillTest(PurchaseBillData data) throws Exception {
        CreatePurchaseBillPage piPage =
                new CreatePurchaseBillPage(driver);
        piPage.navigateToNewPurchaseBill();
        int discLevel =  piPage.fillPurchaseBillHeader(
                data.vendorName,
                data.entryDate,
                data.piDate,
                data.deliveryDate,
                data.referenceNo,               
                data.paymentTerms,              
                data.taxType,
                data.priceList,
                data.discountLevel);
        //System.out.println(discLevel);
        String piNumber = piPage.purchaseBillNumber();
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
       
        piPage.addItems(itemNames, quantities, discountType, discount,discLevel);
        if (discLevel == 0) {
            piPage.applyTransactionLevelDiscount(
                data.discountAfterBeforeTax,
                data.discountType,
                data.discountValue,
                data.discountAccount
            );
        }
        piPage.addNotesAndTerms(data.customerNote, data.terms);
        piPage.saveAsMethod(data.saveAs);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(
        		piPage.verifyPurchaseBillCreated(piNumber),
                "PO creation failed: " + piNumber
        );
        softAssert.assertAll();        
    }
}
