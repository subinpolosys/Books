package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTest;
import dataprovider.LandedcostPIDataProvider;
import dataprovider.PurchaseBillDataProvider;
import drivers.DriverFactory;
import model.LandedcostPIData;
import model.PurchaseBillData;
import pages.CreateLadedCostPIPage;
import pages.CreatePurchaseBillPage;
import pages.Login;

public class LandedcostPITest extends BaseTest {
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
    @Test(dataProvider = "LandedcostPIData",
          dataProviderClass = LandedcostPIDataProvider.class)
    public void LandedcostPITest(LandedcostPIData data) throws Exception {
    	CreateLadedCostPIPage lpiPage =
                new CreateLadedCostPIPage(driver);
    	lpiPage.navigateToNewPurchaseBill();
        int discLevel =  lpiPage.fillPurchaseBillHeader(
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
        String piNumber = lpiPage.purchaseBillNumber();
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
        String[] landed = data.items.stream()
                .map(i -> i.get("Landed"))
                .toArray(String[]::new);
       
        lpiPage.addItems(itemNames, quantities, discountType, discount,landed,discLevel);
        if (discLevel == 0) {
        	lpiPage.applyTransactionLevelDiscount(
                data.discountAfterBeforeTax,
                data.discountType,
                data.discountValue,
                data.discountAccount
            );
        }
        lpiPage.addNotesAndTerms(data.customerNote, data.terms);
        lpiPage.saveAsMethod(data.saveAs);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(
        		lpiPage.verifyPurchaseBillCreated(piNumber),
                "PO creation failed: " + piNumber
        );
        softAssert.assertAll();        
    }
}
