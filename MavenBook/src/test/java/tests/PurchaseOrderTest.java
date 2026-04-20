package tests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import base.BaseTest;
import dataprovider.PurchaseOrderDataProvider;
import drivers.DriverFactory;
import model.PurchaseOrderData;
import pages.CreatePurchaseOrderPage;
import pages.Login;
public class PurchaseOrderTest extends BaseTest{
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
    @Test(dataProvider = "purchaseOrderData",
          dataProviderClass = PurchaseOrderDataProvider.class)
    public void purchaseOrderTest(PurchaseOrderData data) throws Exception {
        CreatePurchaseOrderPage poPage =
                new CreatePurchaseOrderPage(driver);
        poPage.navigateToNewPurchaseOrder();
        int discLevel =  poPage.fillPurchaseOrderHeader(
                data.vendorName,
                data.referenceNo,
                data.poDate,
                data.paymentTerms,
                data.deliveryDate,
                data.taxType,
                data.priceList,
                data.discountLevel);
       // System.out.println(discLevel);
        String poNumber = poPage.purchaseOrderNumber();
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
        poPage.addItems(itemNames, quantities, discountType, discount,discLevel);
        if (discLevel == 0) {
        	//System.out.println("inside transaction level disc");
        	poPage.applyTransactionLevelDiscount(
                    data.discountAfterBeforeTax,
                    data.discountType,
                    data.discountValue,
                    data.discountAccount
            );
        }
        poPage.addNotesAndTerms(data.customerNote, data.terms);
        poPage.saveAsMethod(data.saveAs);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(
                poPage.verifyPurchaseOrderCreated(poNumber),
                "PO creation failed: " + poNumber
        );
        softAssert.assertAll();
    }
}
