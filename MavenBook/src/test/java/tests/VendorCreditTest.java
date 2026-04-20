package tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import base.BaseTest;
import dataprovider.VendorCreditDataProvider;
import drivers.DriverFactory;
import model.VendorCreditData;
import pages.CreateVendorCreditPage;
import pages.Login;

public class VendorCreditTest extends BaseTest {
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
    @Test(dataProvider = "vendorCreditData",
          dataProviderClass = VendorCreditDataProvider.class)
    public void vendorCreditTest(VendorCreditData data) throws Exception {
    	CreateVendorCreditPage vcPage =
                new CreateVendorCreditPage(driver);
    	vcPage.navigateToNewVendorCredit();
    	vcPage.fillVendorCreditHeader(
                data.vendorName,
                data.billNumber,
                data.vcDate,
                data.orderNumber,               
                data.taxType,
                data.priceList);
        //System.out.println(discLevel);
        String vcNumber = vcPage.vendorCreditNumber();
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
        vcPage.addItems(itemNames, quantities, discountType, discount);
        vcPage.addNotes(data.customerNote);
        vcPage.saveAsMethod(data.saveAs);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(
        		vcPage.verifyVendorCreditCreated(vcNumber),
                "VC creation failed: " + vcNumber
        );
        softAssert.assertAll();   
    }
}
